import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SongClass {
    private int songId;
    private int artistId;
    private String name;
    private List<String> genre;
    private List<String> tags;

    private static DbClass dbConnection = new DbClass();
    private static Scanner scanner = new Scanner(System.in);

    public static void printSongsList(List<SongClass> songs) {
        if (!songs.isEmpty()) {
            for (int i = 1; i < songs.size() + 1; i++) {
                System.out.println("(" + i +") " + songs.get(i - 1).getName() + " | Id: " + songs.get(i - 1).getSongId());
            }
        } else {
            System.out.println("Couldn't find any songs");
        }
    }

    public void printSong() {
        try {
            System.out.println("Id: " + this.songId + "\nName: " + this.name + "\nArtist: " + dbConnection.getArtistById(this.artistId));
            System.out.print("Genres: ");
            for (String i : genre) {
                System.out.print(i + ", ");
            }
            System.out.print("\nTags: ");
            for (String i : tags) {
                System.out.print(i + ", ");
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("This song contains incomplete values");
        }
    }

    public SongClass() {}

    public SongClass(int songId, int artistId, String name, List<String> genre, List<String> tags) {
        this.songId = songId;
        this.artistId = artistId;
        this.name = name;
        this.genre = deepCopy(genre);
        this.tags = deepCopy(tags);
    }

    public int getSongId() {
        return songId;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenre() {
        return deepCopy(genre);
    }

    public void setGenre(List<String> genre) {
        this.genre = deepCopy(genre);
    }

    public List<String> getTags() {
        return deepCopy(tags);
    }

    public void setTags(List<String> tags) {
        this.tags = deepCopy(tags);
    }

    private static List<String> deepCopy(List<String> input) {
        List<String> output = new ArrayList<>();
        for (String i : input) {
            output.add(i);
        }
        return output;
    }

    public static void addNewSong() {
        SongClass song = new SongClass();

        System.out.println("What is the name of the song?");
        song.setName(scanner.nextLine());

        System.out.println("Please choose what artist made the song");
        ArtistClass artist = ArtistClass.chooseAnArtist(dbConnection.getAllArtists());
        song.setArtistId(artist.getArtistId());

        System.out.println("What genre(s) does the song belong to?\nPlease input as a comma divided (1 ,2 ,3 ,)");
        song.setGenre(List.of(scanner.nextLine().replaceAll("\\s", "").split(",")));

        System.out.println("What tags(s) does the song belong to?\nPlease input as a comma divided (1 ,2 ,3 ,)");
        song.setTags(List.of(scanner.nextLine().replaceAll("\\s", "").split(",")));

        dbConnection.addSong(song);
    }

    public static void updateSong(SongClass song) {
        while (true) {
            System.out.println("Choose and option:\n(B) Back\n(1) Update name\n(2) Change artist\n(3) Update genre\n(4) Update tags\n(S) Save");
            String choice = Utility.validateChoice(new String[] {"b", "1", "2", "3", "4", "s"});
            switch (choice) {
                case "b":
                    return;
                case "1":
                    System.out.println("What is the new name for the song?");
                    song.setName(scanner.nextLine());
                    break;
                case "2":
                    System.out.println("Who is the new artist");
                    song.setArtistId(ArtistClass.chooseAnArtist(dbConnection.getAllArtists()).getArtistId());
                    break;
                case "3":
                    System.out.println("What genre(s) does the song belong to?\nPlease input as a comma divided (1 ,2 ,3 ,)");
                    song.setGenre(List.of(scanner.nextLine().replaceAll("\\s", "").split(",")));
                    break;
                case "4":
                    System.out.println("What tags(s) does the song belong to?\nPlease input as a comma divided (1 ,2 ,3 ,)");
                    song.setTags(List.of(scanner.nextLine().replaceAll("\\s", "").split(",")));
                    break;
                case "s":
                    dbConnection.updateSong(song);
                    return;
            }
        }
    }

    public static void deleteSong(SongClass song) {
        System.out.println("Are you sure that you would like to delete this song? [Y/N]");
        if (scanner.nextLine().toLowerCase().equals("y")) {
            dbConnection.deleteSong(song);
        }
    }

    public static void searchForSongs() {
        while (true) {
            System.out.println("Please choose an option:\n(B) Back\n(1) Search by id\n(2) Search by name\n(3) search by artist\n(4) Search by genre\n(5) Search by tags\n(6) View all songs");
            String choice = Utility.validateChoice(new String[] {"b", "1", "2", "3", "4", "5", "6"});
            switch (choice) {
                case "b":
                    return;
                case "1":
                    System.out.println("What id would you like to search with?");
                    while (true) {
                        try {
                            dbConnection.getSongById(Integer.parseInt(scanner.nextLine().trim())).printSong();
                            break;
                        } catch (Exception e) {
                            System.out.println("Please provide a while number");
                        }
                    }
                    break;
                case "2":
                    System.out.println("What artist would you like to search for?");
                    printSongsList(dbConnection.getSongsByArtist(ArtistClass.chooseAnArtist(dbConnection.getAllArtists()).getArtistId()));
                    break;
                case "3":
                    System.out.println("What name would you like to search for?");
                    printSongsList(dbConnection.getSongsByName(scanner.nextLine()));
                    break;
                case "4":
                    System.out.println("What genre would you like to search for?");
                    printSongsList(dbConnection.getSongsByGenre(scanner.nextLine()));
                    break;
                case "5":
                    System.out.println("What tags would you like to search for?");
                    printSongsList(dbConnection.getSongsByTags(scanner.nextLine()));
                    break;
                case "6":
                    printSongsList(dbConnection.getAllSongs());
                    break;
            }
            System.out.println("\nInput (B) to go back to menu or press enter to search again");
            if (scanner.nextLine().toLowerCase().equals("b")) {
                return;
            }
        }
    }

    public static void songMenu() {
        while (true) {
            System.out.println("Please choose an action:\n(B) Back\n(1) Add a song\n(2) Update a song\n(3) Delete a song\n(4) Search for songs");
            String choice = Utility.validateChoice(new String[] {"b", "1", "2", "3", "4"});
            switch (choice) {
                case "b":
                    return;
                case "1":
                    addNewSong();
                    break;
                case "2":
                    System.out.println("Which song would you like to update?");
                    updateSong(chooseSong(dbConnection.getAllSongs()));
                    break;
                case "3":
                    System.out.println("Which song would you like to delete?");
                    deleteSong(chooseSong(dbConnection.getAllSongs()));
                    break;
                case "4":
                    searchForSongs();
                    break;
            }
        }
    }

    public static SongClass chooseSong(List<SongClass> songs) {
        printSongsList(songs);
        System.out.println("Which song would you like to choose");
        while (true) {
            try {
                return songs.get(Integer.parseInt(scanner.nextLine().trim()) - 1);
            } catch (Exception e) {
                System.out.println("Please choose an option from the list");
            }
        }
    }
}
