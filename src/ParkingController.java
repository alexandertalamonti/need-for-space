import java.time.LocalDateTime;
import java.util.*;

public class ParkingController {
    private List<Car> cars;
    private List<ParkingSpot> spots;
    private List<Ticket> tickets;
    private List<TowEvent> tows;
    private FileManager fileManager;

    public ParkingController(FileManager fileManager) {
        this.fileManager = fileManager;
        this.cars = fileManager.loadCars();
        this.spots = fileManager.loadSpots();
        this.tickets = fileManager.loadTickets();
        this.tows = fileManager.loadTows();
    }

    public void saveAll() {
        fileManager.saveCars(cars);
        fileManager.saveSpots(spots);
        fileManager.saveTickets(tickets);
        fileManager.saveTows(tows);
    }

    // car logic 
    public Car getCar(String plate) {
        for (Car car : cars) {
            if (car.getPlate().equalsIgnoreCase(plate)) {
                return car;
            }
        }
        return null;
    }


    public void registerCar(String plate, String owner, CarType type) {
        cars.add(new Car(plate, owner, type));
        saveAll();
    }

    //  spot logic
    public ParkingSpot getSpot(String spotId) {
        for (ParkingSpot spot : spots) {
            if (spot.getSpotId().equalsIgnoreCase(spotId)) {
                return spot;
            }
        }
        return null;
    }

    public List<ParkingSpot> getAllSpots() {
        return spots;
    }

    // parking logic
    public boolean parkCar(String plate, String spotId) {
        ParkingSpot spot = getSpot(spotId);
        Car car = getCar(plate);
    
        if (spot == null || car == null || !spot.isAvailable()) {
            return false;
        }
    
        spot.assignCar(plate);
        saveAll();
        return true;
    }
    
    public boolean unparkCar(ParkingSpot spot) {
        String plate = spot.getAssignedPlate();

        spot.clearSpot();
        resolveTicketsFor(plate);
        saveAll();
        return true;
    }
    
    

    // ticket logic
    public void issueTicket(ParkingSpot spot, String reason) {
        String plate = spot.getAssignedPlate();
        String dateTime = LocalDateTime.now().toString();
        Ticket ticket = new Ticket(plate, reason, dateTime, spot.getSpotId());
        tickets.add(ticket);
        saveAll();
    }
    
    
    public List<Ticket> getTicketsForCar(String plate) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getPlate().equalsIgnoreCase(plate)) {
                result.add(t);
            }
        }
        return result;
    }
    
    public List<Ticket> getActiveTicketsForCar(String plate) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getPlate().equalsIgnoreCase(plate) && !t.isResolved()) {
                result.add(t);
            }
        }
        return result;
    }

    // tow logic
    public void towCar(ParkingSpot spot, String reason) {
        String plate = spot.getAssignedPlate();
        String timestamp = LocalDateTime.now().toString();
        TowEvent tow = new TowEvent(plate, spot.getSpotId(), timestamp, reason);
        tows.add(tow);
    
        resolveTicketsFor(plate);
        spot.clearSpot();
        saveAll();
    }    
    
    
    public List<Ticket> getAllViolations() {
        return tickets;
    }
    
    public List<TowEvent> getTowHistory() {
        return tows;
    }

    //  a helper 
    private void resolveTicketsFor(String plate) {
        for (Ticket t : tickets) {
            if (t.getPlate().equalsIgnoreCase(plate) && !t.isResolved()) {
                t.resolveTicket();
            }
        }
    }

}
