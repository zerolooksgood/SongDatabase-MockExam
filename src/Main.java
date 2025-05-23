import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int userId = UserClass.userMenu();
        if (userId == -1) {
            return;
        }

        while (true) {
            System.out.println("Choose an action:\n(E) Exit\n(1) Artists\n(2) Songs\n(3) Playlists");
            String choice = Utility.validateChoice(new String[] {"e", "1", "2", "3"});
            switch (choice) {
                case "e":
                    return;
                case "1":
                    ArtistClass.artistMenu();
                    break;
                case "2":
                    SongClass.songMenu();
                    break;
                case "3":
                    PlaylistClass.playlistMenu(userId);
                    break;
            }
        }
    }
}