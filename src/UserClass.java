import jdk.jshell.execution.Util;

import java.util.Scanner;

public class UserClass {
    private static Scanner scanner = new Scanner(System.in);
    private static DbClass dbConnection = new DbClass();


    private static int userLogin() {
        while (true) {
            System.out.println("Username:");
            String username = scanner.nextLine().trim();

            System.out.println("Password:");
            String password = scanner.nextLine().trim();

            int userId = dbConnection.verifyUserLogin(username, password);
            if (userId == -1) {
                System.out.println("Invalid username or password");
            } else {
                System.out.println("Successfully logged in");
                return userId;
            }
        }
    }

    private static void createUser() {
        String username;
        while (true) {
            System.out.println("Username:");
            username = scanner.nextLine().trim();
            if (verifyNoWhiteSpaces(username)) {
                if (dbConnection.checkUsernameAvailable(username)) {
                    break;
                } else {
                    System.out.println("Username already taken");
                }
            } else {
                System.out.println("Username can't contain spaces");
            }
        }

        String password;
        while (true) {
            System.out.println("Password:");
            password = scanner.nextLine().trim();
            System.out.println("Repeat password:");
            if (scanner.nextLine().trim().equals(password)) {
                if (verifyNoWhiteSpaces(password)) {
                    break;
                } else {
                    System.out.println("Password can't contain spaces");
                }
            } else {
                System.out.println("Passwords don't match");
            }
        }

        dbConnection.createUser(username, password);
    }

    public static int userMenu() {
        while (true) {
            System.out.println("Choose an action:\n(E) Exit\n(1) Log in\n(2) Create user");
            String choice = Utility.validateChoice(new String[] {"e", "1", "2"});
            switch (choice) {
                case "e":
                    return -1;
                case "1":
                    return userLogin();
                case "2":
                    createUser();
                    break;
            }
        }
    }

    private static boolean verifyNoWhiteSpaces(String string) {
        if (string.contains(" ")) {
            return false;
        }
        return true;
    }
}
