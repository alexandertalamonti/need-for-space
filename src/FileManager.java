import java.io.*;
import java.util.*;

public class FileManager {
    private static final String CAR_FILE = "data/cars.txt";
    private static final String SPOT_FILE = "data/spots.txt";
    private static final String TICKET_FILE = "data/tickets.txt";

    // ==== CARS ====

    public List<Car> loadCars() {
        List<Car> cars = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CAR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String plate = parts[0];
                    String owner = parts[1];
                    CarType type = CarType.valueOf(parts[2]);
                    cars.add(new Car(plate, owner, type));
                }
            }
        } catch (IOException e) {
            System.out.println("No car data found. Starting with empty list.");
        }
        return cars;
    }

    public void saveCars(List<Car> cars) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CAR_FILE))) {
            for (Car car : cars) {
                writer.println(car.getPlate() + ";" + car.getOwnerName() + ";" + car.getCarType());
            }
        } catch (IOException e) {
            System.out.println("Failed to save cars.");
        }
    }

    // ==== SPOTS ====

    public List<ParkingSpot> loadSpots() {
        List<ParkingSpot> spots = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SPOT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    String id = parts[0];
                    SpotType type = SpotType.valueOf(parts[1]);
                    ParkingSpot spot = new ParkingSpot(id, type);
                    if (parts.length == 3 && !parts[2].equals("null")) {
                        spot.assignCar(parts[2]);
                    }
                    spots.add(spot);
                }
            }
        } catch (IOException e) {
            System.out.println("No spot data found. Generating default lot.");
            return generateDefaultSpots();
        }
        return spots;
    }

    public void saveSpots(List<ParkingSpot> spots) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SPOT_FILE))) {
            for (ParkingSpot spot : spots) {
                String assigned = spot.isAvailable() ? "null" : spot.getAssignedPlate();
                writer.println(spot.getSpotId() + ";" + spot.getSpotType() + ";" + assigned);
            }
        } catch (IOException e) {
            System.out.println("Failed to save spots.");
        }
    }

    public List<ParkingSpot> generateDefaultSpots() {
        List<ParkingSpot> spots = new ArrayList<>();
        spots.add(new ParkingSpot("A1", SpotType.COMPACT));
        spots.add(new ParkingSpot("A2", SpotType.COMPACT));
        spots.add(new ParkingSpot("B1", SpotType.STANDARD));
        spots.add(new ParkingSpot("B2", SpotType.STANDARD));
        spots.add(new ParkingSpot("C1", SpotType.LARGE));
        spots.add(new ParkingSpot("C2", SpotType.LARGE));
        spots.add(new ParkingSpot("D1", SpotType.EV));
        spots.add(new ParkingSpot("D2", SpotType.EV));
        return spots;
    }

    // ==== TICKETS ====

    public List<Ticket> loadTickets() {
        List<Ticket> tickets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TICKET_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String plate = parts[0];
                    String reason = parts[1];
                    String dateTime = parts[2];
                    String spotId = parts[3];
                    boolean resolved = Boolean.parseBoolean(parts[4]);
                    Ticket ticket = new Ticket(plate, reason, dateTime, spotId);
                    if (resolved) ticket.markResolved();
                    tickets.add(ticket);
                }
            }
        } catch (IOException e) {
            System.out.println("No ticket data found. Starting with empty list.");
        }
        return tickets;
    }

    public void saveTickets(List<Ticket> tickets) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TICKET_FILE))) {
            for (Ticket ticket : tickets) {
                writer.println(ticket.getPlate() + ";" +
                               ticket.getReason() + ";" +
                               ticket.getDateTime() + ";" +
                               ticket.getSpotId() + ";" +
                               ticket.isResolved());
            }
        } catch (IOException e) {
            System.out.println("Failed to save tickets.");
        }
    }
}
