// what are questions we will ask the patient about the schedule?
//
// Are you free at this time (each session is only ever an hour long and on the hour)?
// ALso keep in mind when answering this question that appointment's cannot be within a week of each other
// In the spirit of this last question, Do you have any lessons within this range (week-, week+)
// 

import java.util.SortedSet;
import java.util.TreeSet;

public class PatientSchedule {
    private SortedSet<String> scheduledTimes;
    private int patientId;

    PatientSchedule(int patientId) {
        this.patientId = patientId;
        this.scheduledTimes = new TreeSet<String>();
    }

    public boolean setAppointment(String dateTime) {
        // already has appointment
        if (scheduledTimes.contains(dateTime)) {
            return false;
        }

        scheduledTimes.add(dateTime);
        return true;
    }

    public boolean hasAppointment(String dateTime) {
        return scheduledTimes.contains(dateTime);
    }

    public int getPatientId() {
        return patientId;
    }

    public String toString() {
        return "Patient: " + patientId + "\n" +
        scheduledTimes.toString();
    }
}
