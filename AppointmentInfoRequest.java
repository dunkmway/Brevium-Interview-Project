public class AppointmentInfoRequest {
    // {
    //     "doctorId": 1,
    //     "personId": 0,
    //     "appointmentTime": "2023-10-07T23:15:01.627Z",
    //     "isNewPatientAppointment": true,
    //     "requestId": 0
    //   }

    private int doctorId;
    private int personId;
    private String appointmentTime;
    private boolean isNewPatientAppointment;
    private int requestId;

    AppointmentInfoRequest(int doctorId, int personId, String appointmentTime, boolean isNewPatientAppointment, int requestId) {
        this.doctorId = doctorId;
        this.personId = personId;
        this.appointmentTime = appointmentTime;
        this.isNewPatientAppointment = isNewPatientAppointment;
        this.requestId = requestId;
    }

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

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
