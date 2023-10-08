/*
 * Constraints
    o Appointments may only be scheduled on the hour.
    o Appointments can be scheduled as early as 8 am UTC and as late as 4 pm UTC.
    o Appointments may only be scheduled on weekdays during the months of November and December 2021.
    o Appointments can be scheduled on holidays.
    o For a given doctor, you may only have one appointment scheduled per hour (though different doctors may have appointments at the same time).
    o For a given patient, each appointment must be separated by at least one week. For example, if Bob Smith has an appointment on 11/17 you may schedule another appointment on or before 11/10 or on or after 11/24.
    o Appointments for new patients may only be scheduled for 3 pm and 4 pm.
 *
 * API Key
 * I would put this in an env file but didn't want to waste time figuring it out with the time constraint
 * 475da121-0bd1-4c1f-8f62-39cbd8cd1846
 * 
 * Notes
 * Since new patients can only be scheduled for 3 or 4 pm, returning patients should try an not schedule during these times to leave those spots open
 * Probably easiest to do a greedy search and place them on the schedule as early as possible (on their preferred times)
 * I spent too much time getting things set up with the API that I didn't have much time to implement how to actaully solve
 * the problem of scheduling people.
 * 
 * After 3 hours things I would implement given more time
 * 
 * Doctors need a method to pick a time on a given date that they are open,
 * since the set is sorted on the schedule we find the next appointment after the day starts and choose a time before that after 8,
 * if not get the next one
 * 
 * We need a method on the patient to check if there are any appointments +- a week around a datetime,
 * so we can exclude them, just go to that -week date and find all appointments within the range , sorted set
 * 
 * For new patients we would just manually check the 3 and 4 pm time slots for doctors on preferred days and then days around them
 * 
 */

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Scheduler {
    // not great storing this here but I don't want to waste time figuring out how to do .env
    static final String KEY = "475da121-0bd1-4c1f-8f62-39cbd8cd1846";
    static final String BASE_URL = "http://scheduling-interview-2021-265534043.us-west-2.elb.amazonaws.com";
    static final int NUM_DOCTORS = 3;
    
    static DoctorSchedule[] doctorSchedules = new DoctorSchedule[NUM_DOCTORS];
    static List<PatientSchedule> patientSchedules = new ArrayList<PatientSchedule>();

    public static void main(String[] args) {
        System.out.println("Scheduler");

        // initialize the doctor list
        for (int i = 0; i < NUM_DOCTORS; i++) {
            doctorSchedules[i] = new DoctorSchedule(i + 1);
        }

        // initialize the http client
        HttpClient client = HttpClient.newHttpClient();

        // start the test system to restart the API
        if (!start(client)) {
            System.out.println("Scheduler failed to start.");
            return;
        }
        System.out.println("Scheduler started.");


        // get the intitial schedule
        AppointmentInfo[] appointments = getInitialAppointmentInfos(client);
        if (appointments == null) {
            System.out.println("Failed to get initial appointments.");
            return;
        }
        System.out.println("Scheduler got intitial schedule.");

        // loop through the initials and set the schedule
        for (AppointmentInfo info : appointments) {
            // We can assume that things are set up correctly in the initial so we don't have to check for duplicates
            // add to the doctor schedule
            doctorSchedules[info.getDoctorId() - 1].setAppointment(info.getAppointmentTime());

            // add to the patient schedule
            getPatientSchedule(info.getPersonId()).setAppointment(info.getAppointmentTime());
        }

        // loop until there are no more requests to handle
        while (true) {
            AppointmentRequest request = getAppointmentRequest(client);
            // if no request (or error) break from the loop
            if (request == null) {
                break;
            }
            // the schema said that the preferred days and doctors are nullable but
            // in the interest of time I checked the requests and they are all filled
            // so I would normally check this but to get something done I'm just going
            // to assume they exists and then go from there. Less cases

            // we will check the time and date for the doctor and see if they are available
            // this really only schedules an appointment if the doctor and preferred times match up
            // this doesn't solve the problem but was all I could get in within the timeframe
            for (int doc : request.getPreferredDocs()) {
                boolean foundTime = false;

                for (String dateTime : request.getPreferredDays()) {
                    if (doctorSchedules[doc - 1].setAppointment(dateTime)) {
                        AppointmentInfoRequest newRequest = new AppointmentInfoRequest(
                            doc,
                            request.getPersonId(),
                            dateTime,
                            request.getIsNew(),
                            request.getRequestId()
                        );
                        postAppointmentInfoRequest(client, newRequest);

                        foundTime = true;
                        break;
                    }
                }
                if (foundTime) {
                    break;
                }
            }

        }

        // stop the system after each run.
        if (!stop(client)) {
            System.out.println("Scheduler failed to stop.");
            return;
        }
        System.out.println("Scheduler stopped.");
    }

    // Restart the API service, return a boolean if the start was successful.
    private static boolean start(HttpClient client) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(BASE_URL + "/api/Scheduling/Start?token=" + KEY))
            .POST(BodyPublishers.ofString(""))
            .build();
    
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return true;
            } else if (response.statusCode() == 401) {
                System.out.println("The API key provided on Start was invalid.");
            }
        } catch (Exception e) {
            // continue the flow to return false
            System.out.println(e);
        }
        return false;
    }

    // Restart the API service, return a boolean if the start was successful.
    private static boolean stop(HttpClient client) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(BASE_URL + "/api/Scheduling/Stop?token=" + KEY))
            .POST(BodyPublishers.ofString(""))
            .build();
    
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return true;
            } else if (response.statusCode() == 401) {
                System.out.println("The API key provided on End was invalid.");
            }
        } catch (Exception e) {
            // continue the flow to return false
            System.out.println(e);
        }
        return false;
    }

    // get the initial schedule
    private static AppointmentInfo[] getInitialAppointmentInfos(HttpClient client) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(BASE_URL + "/api/Scheduling/Schedule?token=" + KEY))
            .GET()
            .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                AppointmentInfo[] initialSchedule = gson.fromJson(response.body(), AppointmentInfo[].class);
    
                return initialSchedule;
            } else if (response.statusCode() == 401) {
                System.out.println("The API key provided on Initial Appointment Infos was invalid.");
            } else if (response.statusCode() == 405) {
                System.out.println("The schedule has already been retrieved for this 'run'. You must hit the Start endpoint to reset the system.");
            }


        } catch (Exception e) {
            // continue the flow
            System.out.println(e);
        }
        return null;
    }

    // get an appointment request
    private static AppointmentRequest getAppointmentRequest(HttpClient client) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(BASE_URL + "/api/Scheduling/AppointmentRequest?token=" + KEY))
            .GET()
            .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                AppointmentRequest appointmentRequest = gson.fromJson(response.body(), AppointmentRequest.class);

                return appointmentRequest;
            } else if (response.statusCode() == 204) {
                return null;
            } else if (response.statusCode() == 401) {
                System.out.println("The API key provided on Initial Appointment Infos was invalid.");
            } else if (response.statusCode() == 405) {
                System.out.println("The schedule has already been retrieved for this 'run'. You must hit the Start endpoint to reset the system.");
            }

        } catch (Exception e) {
            // continue the flow
            System.out.println(e);
        }
        return null;
    }

    // post an appointment info request
    private static boolean postAppointmentInfoRequest(HttpClient client, AppointmentInfoRequest appt) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(appt);

        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(BASE_URL + "/api/Scheduling/Schedule?token=" + KEY))
            .POST(BodyPublishers.ofString(jsonBody))
            .build();
            
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return true;
            } else if (response.statusCode() == 405) {
                System.out.println("You have already called the stop endpoint for this run.");
            } else if (response.statusCode() == 405) {
                System.out.println("The schedule was unable to accomodate your requested appointment.");
            }

        } catch (Exception e) {
            // continue the flow
            System.out.println(e);
        }
        return false;
    }

    // find or create the patient schedule if non existent
    private static PatientSchedule getPatientSchedule(int patientId) {
        for (PatientSchedule schedule : patientSchedules) {
            if (schedule.getPatientId() == patientId) {
                return schedule;
            }
        }

        // not found, create
        return new PatientSchedule(patientId);
    }




}
