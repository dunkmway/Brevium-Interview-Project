import java.util.Arrays;

public class AppointmentRequest {
    // {
    //     "requestId": 0,
    //     "personId": 0,
    //     "preferredDays": [
    //       "2023-10-07T22:37:24.617Z"
    //     ],
    //     "preferredDocs": [
    //       0
    //     ],
    //     "isNew": true
    //   }

    private int requestId;
    private int personId;
    private String[] preferredDays;
    private int[] preferredDocs;
    private boolean isNew;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestID(int requestId) {
        this.requestId = requestId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonID(int personId) {
        this.personId = personId;
    }
    public String[] getPreferredDays() {
        return preferredDays;
    }

    public void setPreferredDays(String[] preferredDays) {
        this.preferredDays = preferredDays;
    }
    public int[] getPreferredDocs() {
        return preferredDocs;
    }

    public void setRequestID(int[] preferredDocs) {
        this.preferredDocs = preferredDocs;
    }
    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String toString() {
        return "requestId: " + requestId + "\n" +
        "personId: " + personId + "\n" +
        "preferredDays: " + Arrays.toString(preferredDays) + "\n" +
        "preferredDocs: " + Arrays.toString(preferredDocs) + "\n" +
        "isNew: " + isNew;
    }
}
