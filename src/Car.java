public class Car {
    private String plate;
    private String ownerName;
    private CarType carType;

    public Car(String plate, String ownerName, CarType carType) {
        this.plate = plate;
        this.ownerName = ownerName;
        this.carType = carType;
    }

    public String getPlate() {
        return plate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public CarType getCarType() {
        return carType;
    }
}
