public class ParkingSpot {
    private String spotId;
    private SpotType spotType;
    private boolean isAvailable;
    private String assignedPlate;

    public ParkingSpot(String spotId, SpotType spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
        this.isAvailable = true;
        this.assignedPlate = null;
    }

    public String getSpotId() {
        return spotId;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getAssignedPlate() {
        return assignedPlate;
    }

    public void assignCar(String plate) {
        this.assignedPlate = plate;
        this.isAvailable = false;
    }

    public void clearSpot() {
        this.assignedPlate = null;
        this.isAvailable = true;
    }
}
