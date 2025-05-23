import jdk.jshell.execution.Util;

import java.util.List;
import java.util.Scanner;

public class ArtistClass {
    private int artistId;
    private String name;
    private String description;

    private static Scanner scanner = new Scanner(System.in);
    private static DbClass dbConnection = new DbClass();

    public static void printArtistsList(List<ArtistClass> artists) {
        if (!artists.isEmpty()) {
            for (int i = 1; i < artists.size() + 1; i++) {
                System.out.println("(" + i + ") " + artists.get(i - 1).getName() + " | Id: " + artists.get(i - 1).getArtistId());
            }
        } else {
            System.out.println("Couldn't find any artists");
        }
    }

    public void printArtist() {
        try {
            System.out.println("Id: " + this.artistId + "\nName: " + this.name + "\nDescription: " + this.description);
        } catch (Exception e) {
            System.out.println("This artist contains incomplete values");
        }
    }

    public ArtistClass() {}

    public ArtistClass(int artistId, String name, String description) {
        this.artistId = artistId;
        this.name = name;
        this.description = description;
    }

    public int getArtistId() {
        return artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static void addArtist() {
        ArtistClass newArtist = new ArtistClass();

        System.out.println("What is the name of the artist you would like to add?");
        newArtist.setName(scanner.nextLine());

        System.out.println("How would you like to describe the artist?");
        newArtist.setDescription(scanner.nextLine());

        dbConnection.addArtist(newArtist);
    }

    public static void updateArtist(ArtistClass artist) {
        while (true) {
            System.out.println("Choose and action:\n(B) Back\n(1) Edit name\n(2) Edit description\n(S) Save changes");
            String choice = Utility.validateChoice(new String[] {"b", "1", "2", "s"});
            switch (choice) {
                case "b":
                    return;
                case "1":
                    System.out.println("What is the new name?");
                    artist.setName(scanner.nextLine());
                    break;
                case "2":
                    System.out.println("What is the new description?");
                    artist.setDescription(scanner.nextLine());
                    break;
                case "s":
                    dbConnection.updateArtist(artist);
                    return;
            }
        }
    }

    public static void deleteArtist(ArtistClass artist) {
        System.out.println("Are you sure that you would like to delete this artist?\nThis will also delete all their songs.\n(Y) Yes\n(N) No");
        String choice = Utility.validateChoice(new String[] {"y", "n"});
        if (choice.equals("y")) {
            dbConnection.deleteArtist(artist);
        }

    }

    public static void searchForArtists() {
        while (true) {
            System.out.println("Please choose an option:\n(B) Back\n(1) Search by id\n(2) Search by name\n(3) View all artists");
            String choice = Utility.validateChoice(new String[] {"b", "1", "2", "3"});
            switch (choice) {
                case "b":
                    return;
                case "1":
                    System.out.println("What is the id you want to search for?");
                    while (true) {
                        try {
                            int input = Integer.parseInt(scanner.nextLine().trim());
                            ArtistClass artist = dbConnection.getArtistById(input);
                            if (artist != null) {
                                artist.printArtist();
                                break;
                            } else {
                                System.out.println("Found no artists with the provided id");
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("Please provide a whole number");
                        }
                    }
                    break;
                case "2":
                    System.out.println("What name would you like to search for?");
                    printArtistsList(dbConnection.getArtistByName(scanner.nextLine().trim()));
                    break;
                case "3":
                    printArtistsList(dbConnection.getAllArtists());
                    break;
            }
            System.out.println("\nInput (B) to go back to menu or press enter to search again");
            if (scanner.nextLine().toLowerCase().equals("b")) {
                return;
            }
        }
    }

    public static ArtistClass chooseAnArtist(List<ArtistClass> artists) {
        printArtistsList(artists);
        System.out.println("Which artist would you like to choose?");
        while (true) {
            try {
                return artists.get(Integer.parseInt(scanner.nextLine().trim()) - 1);
            } catch (Exception e) {
                System.out.println("Please choose an option from the list");
            }
        }
    }

    public static void artistMenu() {
        while (true) {
            System.out.println("Please choose an action:\n(B) Back\n(1) Create new artist\n(2) Edit existing artist\n(3) Delete existing artist\n(4) Search for artist");
            String input = Utility.validateChoice(new String[] {"b", "1", "2", "3", "4"});
            switch (input) {
                case "b":
                    return;
                case "1":
                    addArtist();
                    break;
                case "2":
                    updateArtist(chooseAnArtist(dbConnection.getAllArtists()));
                    break;
                case "3":
                    deleteArtist(chooseAnArtist(dbConnection.getAllArtists()));
                    break;
                case "4":
                    searchForArtists();
                    break;
            }
        }
    }
}
