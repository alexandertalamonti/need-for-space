import java.util.*;

public class MainCLI {
    private final ParkingController controller;
    private final Scanner scanner;
    private Car currentCar;

    public MainCLI(ParkingController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\n===== NEED FOR SPACE =====");
            System.out.println("1. Driver");
            System.out.println("2. LotBoss");
            System.out.println("3. Quit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                driverEntryMenu();
            } else if (choice.equals("2")) {
                lotBossMenu();
            } else if (choice.equals("3")) {
                controller.saveAll();
                System.out.println("👋 Goodbye!");
                return;
            } else {
                System.out.println("❌ Invalid choice.");
            }
        }
    }

    // the driver flow 

    private void driverEntryMenu() {
        while (true) {
            System.out.println("\n-- DRIVER ACCESS --");
            System.out.println("1. Returning Driver");
            System.out.println("2. Register Car");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Enter your license plate: ");
                String plate = scanner.nextLine().trim().toUpperCase();

                if (plate.isEmpty()) {
                    System.out.println("❌ Plate cannot be empty.");
                    continue;
                }

                Car car = controller.getCar(plate);
                if (car != null) {
                    currentCar = car;
                    driverMainMenu();
                    return;
                } else {
                    System.out.println("❌ Car not found.");
                }

            } else if (choice.equals("2")) {
                System.out.print("Plate: ");
                String plate = scanner.nextLine().trim().toUpperCase();
                if (plate.isEmpty()) {
                    System.out.println("❌ Plate cannot be empty.");
                    continue;
                }

                if (controller.getCar(plate) != null) {
                    System.out.println("❌ A car with this plate already exists.");
                    continue;
                }

                System.out.print("Owner Name: ");
                String owner = scanner.nextLine().trim();
                if (owner.isEmpty()) {
                    System.out.println("❌ Owner name cannot be empty.");
                    continue;
                }

                System.out.println("Choose Car Type:");
                System.out.println("1. COMPACT");
                System.out.println("2. STANDARD");
                System.out.println("3. LARGE");
                System.out.println("4. EV");
                System.out.print("Choice: ");
                String typeChoice = scanner.nextLine().trim();

                CarType type = null;
                if (typeChoice.equals("1")) {
                    type = CarType.COMPACT;
                } else if (typeChoice.equals("2")) {
                    type = CarType.STANDARD;
                } else if (typeChoice.equals("3")) {
                    type = CarType.LARGE;
                } else if (typeChoice.equals("4")) {
                    type = CarType.EV;
                } else {
                    System.out.println("❌ Invalid type selected.");
                    continue;
                }

                controller.registerCar(plate, owner, type);
                currentCar = controller.getCar(plate);
                System.out.println("✅ Car registered.");
                driverMainMenu();
                return;

            } else if (choice.equals("3")) {
                return;

            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private void driverMainMenu() {
        while (true) {
            System.out.println("\n-- DRIVER MENU (" + currentCar.getPlate() + ") --");
            System.out.println("1. View Parking Lot");
            System.out.println("2. View Ticket History");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                displayLotGrid(true);

            } else if (choice.equals("2")) {
                System.out.println("\n📋 TICKETS:");
                List<Ticket> tickets = controller.getTicketsForCar(currentCar.getPlate());
                for (Ticket t : tickets) {
                    System.out.println(t);
                }

                System.out.println("\n📦 TOWS:");
                List<TowEvent> allTows = controller.getTowHistory();
                for (TowEvent tow : allTows) {
                    if (tow.getPlate().equalsIgnoreCase(currentCar.getPlate())) {
                        System.out.println(tow);
                    }
                }

                System.out.println("\nPress Enter to return.");
                scanner.nextLine();

            } else if (choice.equals("3")) {
                return;

            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private boolean isCarParked(String plate) {
        List<ParkingSpot> allSpots = controller.getAllSpots();
        for (ParkingSpot spot : allSpots) {
            if (plate.equalsIgnoreCase(spot.getAssignedPlate())) {
                return true;
            }
        }
        return false;
    }

    private void handleDriverSpotMenu(ParkingSpot spot) {
        System.out.println("\nSpot ID: " + spot.getSpotId());
        System.out.println("Type: " + spot.getSpotType());
        System.out.println("Status: " + (spot.isAvailable() ? "Vacant" : "Occupied"));
        System.out.println();

        if (!spot.isAvailable()) {
            Car car = controller.getCar(spot.getAssignedPlate());
            System.out.println("Car Plate: " + car.getPlate());
            System.out.println("Owner: " + car.getOwnerName());
            System.out.println("Type: " + car.getCarType());

            List<Ticket> active = controller.getActiveTicketsForCar(car.getPlate());
            if (!active.isEmpty()) {
                System.out.println("Active Tickets:");
                for (Ticket t : active) {
                    System.out.println(t);
                }
            }

            if (car.getPlate().equals(currentCar.getPlate())) {
                System.out.print("Unpark car? (y/n): ");
                String decision = scanner.nextLine().trim();
                if (decision.equalsIgnoreCase("y")) {
                    controller.unparkCar(spot);
                    controller.saveAll();
                    System.out.println("✅ Unparked.");
                }
            } else {
                System.out.println("Press Enter to return.");
                scanner.nextLine();
            }

        } else if (!isCarParked(currentCar.getPlate())) {
            System.out.print("Park here? (y/n): ");
            String decision = scanner.nextLine().trim();
            if (decision.equalsIgnoreCase("y")) {
                controller.parkCar(currentCar.getPlate(), spot.getSpotId());
                controller.saveAll();
                System.out.println("✅ Parked.");
            }

        } else {
            System.out.println("Your car is already parked.");
            System.out.println("Press Enter to return.");
            scanner.nextLine();
        }
    }

    // the lotboss flow

    private void lotBossMenu() {
        while (true) {
            System.out.println("\n-- LOTBOSS MENU --");
            System.out.println("1. View Parking Lot");
            System.out.println("2. View All Violations");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                displayLotGrid(false);

            } else if (choice.equals("2")) {
                System.out.println("\n📋 ALL TICKETS:");
                List<Ticket> allTickets = controller.getAllViolations();
                for (Ticket t : allTickets) {
                    System.out.println(t);
                }

                System.out.println("\n📦 ALL TOWS:");
                List<TowEvent> allTows = controller.getTowHistory();
                for (TowEvent tow : allTows) {
                    System.out.println(tow);
                }

                System.out.println("\nPress Enter to return.");
                scanner.nextLine();

            } else if (choice.equals("3")) {
                return;

            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private void handleLotBossSpotMenu(ParkingSpot spot) {
        System.out.println("\nSpot ID: " + spot.getSpotId());
        System.out.println("Type: " + spot.getSpotType());
        System.out.println("Status: " + (spot.isAvailable() ? "Vacant" : "Occupied"));
        System.out.println();
        if (!spot.isAvailable()) {
            Car car = controller.getCar(spot.getAssignedPlate());
            System.out.println("Car Plate: " + car.getPlate());
            System.out.println("Owner: " + car.getOwnerName());
            System.out.println("Type: " + car.getCarType());

            List<Ticket> all = controller.getTicketsForCar(car.getPlate());
            if (!all.isEmpty()) {
                System.out.println("Tickets:");
                for (Ticket t : all) {
                    System.out.println(t);
                }
            }

            System.out.println("1. Tow Car");
            System.out.println("2. Issue Ticket");
            System.out.println("b. Back");
            System.out.print("Choose: ");
            String action = scanner.nextLine();

            if (action.equals("1")) {
                System.out.print("Enter reason for tow: ");
                String reason = scanner.nextLine().trim();
                if (reason.isEmpty()) {
                    System.out.println("❌ Reason cannot be empty.");
                } else {
                    controller.towCar(spot, reason);
                    System.out.println("✅ Car towed.");
                }

            } else if (action.equals("2")) {
                System.out.print("Enter reason for ticket: ");
                String reason = scanner.nextLine().trim();
                if (reason.isEmpty()) {
                    System.out.println("❌ Reason cannot be empty.");
                } else {
                    controller.issueTicket(spot, reason);
                    System.out.println("✅ Ticket issued.");
                }
            }

        } else {
            System.out.println("Press Enter to return.");
            scanner.nextLine();
        }
    }

    // grid display

    private void displayLotGrid(boolean isDriver) {
        System.out.println("\nKey: ⬜ Standard | 🟪 Compact | 🟧 Large | 🟩 EV | 🚗 Occupied\n");
    
        Map<SpotType, String> emoji = Map.of(
            SpotType.STANDARD, "⬜",
            SpotType.COMPACT, "🟪",
            SpotType.LARGE, "🟧",
            SpotType.EV, "🟩"
        );
    
        // col nums
        System.out.println("     1     2     3     4  ");
        System.out.println("  +-----+-----+-----+-----+");
    
        for (char row = 'A'; row <= 'D'; row++) {
            // -> where the emoji rows are
            System.out.print(row + " |");
            for (int col = 1; col <= 4; col++) {
                String id = row + String.valueOf(col);
                ParkingSpot spot = controller.getSpot(id);
                String symbol;
                if (spot == null) {
                    symbol = "❓";
                } else if (!spot.isAvailable()) {
                    symbol = "🚗";
                } else {
                    symbol = emoji.getOrDefault(spot.getSpotType(), "❓");
                }
                System.out.print("  " + symbol + " |");
            }
            System.out.println();
            System.out.println("  +-----+-----+-----+-----+");
        }
    
        // selecting the spot
        while (true) {
            System.out.print("\nEnter spot ID (A1-D4) or 'b' to go back: ");
            String input = scanner.nextLine().toUpperCase();
    
            if (input.equalsIgnoreCase("B")) {
                return;
            }
    
            if (!input.matches("^[A-D][1-4]$")) {
                System.out.println("❌ Invalid spot ID. Format should be like B3.");
                continue;
            }
    
            ParkingSpot spot = controller.getSpot(input);
            if (spot == null) {
                System.out.println("❌ Spot not found.");
            } else {
                if (isDriver) {
                    handleDriverSpotMenu(spot);
                } else {
                    handleLotBossSpotMenu(spot);
                }
            }
        }
    }
}
