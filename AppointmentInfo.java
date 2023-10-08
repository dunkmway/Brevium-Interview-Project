public class AppointmentInfo {
    // {
    //     "doctorId": 2,
    //     "personId": 1,
    //     "appointmentTime": "2021-11-08T08:00:00Z",
    //     "isNewPatientAppointment": false
    //   }

    private int doctorId;
    private int personId;
    private String appointmentTime;
    private boolean isNewPatientAppointment;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public boolean getIsNewPatientAppointment() {
        return isNewPatientAppointment;
    }

    public void setIsNewPatientAppointment(boolean isNewPatientAppointment) {
        this.isNewPatientAppointment = isNewPatientAppointment;
    }

    public String toString() {
        return "doctorId: " + doctorId + "\n" +
        "personId: " + personId + "\n" +
        "appointmentTime: " + appointmentTime + "\n" +
        "isNewPatientAppointment: " + isNewPatientAppointment;
    }
}
