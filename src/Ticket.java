public class Ticket {
    private String plate;
    private String reason;
    private String dateTime;
    private String spotId;
    private boolean resolved;

    public Ticket(String plate, String reason, String dateTime, String spotId) {
        this.plate = plate;
        this.reason = reason;
        this.dateTime = dateTime;
        this.spotId = spotId;
        this.resolved = false;
    }

    public String getPlate() {
        return plate;
    }

    public String getReason() {
        return reason;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getSpotId() {
        return spotId;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void resolveTicket() {
        this.resolved = true;
    }

    @Override
    public String toString() {
        return "Ticket [Plate: " + plate + 
               ", Spot: " + spotId + 
               ", Date/Time: " + dateTime + 
               ", Reason: " + reason + 
               ", Status: " + (resolved ? "Resolved" : "Pending") + "]";
    }
}