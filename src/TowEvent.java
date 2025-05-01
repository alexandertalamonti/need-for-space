public class TowEvent {
    private String plate;
    private String spotId;
    private String dateTime;
    private String reason;

    public TowEvent(String plate, String spotId, String dateTime, String reason) {
        this.plate = plate;
        this.spotId = spotId;
        this.dateTime = dateTime;
        this.reason = reason;
    }

    public String getPlate() {
        return plate;
    }

    public String getSpotId() {
        return spotId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getReason() {
        return reason;
    }
}
