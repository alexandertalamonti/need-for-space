public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        ParkingController controller = new ParkingController(fileManager);
        MainCLI cli = new MainCLI(controller);
        cli.run();
    }
}
