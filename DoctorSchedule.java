// what are questions we will ask the doctor about the schedule?
//
// Are you free at this time (each session is only ever an hour long and on the hour)?
// Give me the closest opening to the requested time (possible duplicate of the previous question)
// Do I really care what the appointment is or just that one exists?

import java.util.SortedSet;
import java.util.TreeSet;

public class DoctorSchedule {
    private SortedSet<String> scheduledTimes;
    private int doctorId;

    DoctorSchedule(int doctorId) {
        this.doctorId = doctorId;
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

    public String toString() {
        return "Doctor: " + doctorId + "\n" +
        scheduledTimes.toString();
    }


}
