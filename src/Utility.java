import java.util.Scanner;

public class Utility {
    private static Scanner scanner = new Scanner(System.in);

    public static String validateChoice(String[] choices) {
        String input;
        while (true) {
            System.out.println("Please select an option: ");
            input = scanner.nextLine().trim().toLowerCase();
            for (String i : choices) {
                if (i.toLowerCase().equals(input)) {
                    return input;
                }
            }
            System.out.println("Invalid option.");
        }
    }
}
