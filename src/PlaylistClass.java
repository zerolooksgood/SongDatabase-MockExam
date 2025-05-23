import java.util.List;
import java.util.Scanner;

public class PlaylistClass {

    private static Scanner scanner = new Scanner(System.in);
    private static DbClass dbConnection = new DbClass();

    public static void playlistMenu(int userId) {
        while (true) {
            System.out.println("Choose an action:\n(B) Back\n(1) Create new playlist\n(2) Edit playlist\n(3) Delete playlist\n(4) View my playlists");
            String choice = Utility.validateChoice(new String[] {"b", "1", "2", "3", "4"});
            switch (choice) {
                case "b":
                    return;
                case "1":
                    createNewPlaylist(userId);
                    break;
                case "2":
                    boolean run = true;
                    while (run) {
                        System.out.println("Choose an action:\n(B) Back\n(1) Rename playlist\n(2) Add song to playlist\n(3) Remove song from playlist");
                        switch (Utility.validateChoice(new String[] {"b", "1", "2", "3"})) {
                            case "b":
                                run = false;
                                break;
                            case "1":
                                updatePlaylistName(userId);
                                break;
                            case "2":
                                addSongToPlaylist(userId);
                                break;
                            case "3":
                                removeSongFromPlaylist(userId);
                                break;
                        }
                    }
                    break;
                case "3":
                    deletePlaylist(userId);
                    break;
                case "4":
                    List<PlaylistObject> playlists = dbConnection.getAllPlaylists(userId);
                    if (!playlists.isEmpty()) {
                        System.out.println("Which playlist would you like to view?");
                        PlaylistObject playlist = choosePlaylist(playlists);
                        System.out.println(playlist.getName() + ":");
                        SongClass.printSongsList(dbConnection.getPlayList(playlist.getPlaylistId()));
                    } else {
                        System.out.println("Couldn't find any playlists");
                    }
                    System.out.println("Input any key to proceed");
                    scanner.nextLine();
                    break;
            }
        }
    }

    public static void removeSongFromPlaylist(int userId) {
        System.out.println("Which playlist would you like to remove a song from?");
        PlaylistObject playlist = choosePlaylist(dbConnection.getAllPlaylists(userId));

        System.out.println("Which song would you like to remove?");
        SongClass song = SongClass.chooseSong(dbConnection.getPlayList(playlist.getPlaylistId()));
        dbConnection.removeSongFromPlaylist(playlist.getPlaylistId(), song.getSongId());
    }

    public static void addSongToPlaylist(int userId) {
        System.out.println("Which song would you like to add?");
        SongClass song = SongClass.chooseSong(dbConnection.getAllSongs());
        System.out.println("Which playlist would you like to add the song to?");
        PlaylistObject playlist = choosePlaylist(dbConnection.getAllPlaylists(userId));
        dbConnection.addSongToPlaylist(playlist.getPlaylistId(), song.getSongId());
    }

    public static void createNewPlaylist(int userId) {
        System.out.println("What would you like to name the new playlist?");
        String name = scanner.nextLine().trim();
        dbConnection.createNewPlaylist(name, userId);
    }

    public static void updatePlaylistName(int userId) {
        System.out.println("Please choose a playlist to rename:");
        PlaylistObject playlist = choosePlaylist(dbConnection.getAllPlaylists(userId));
        System.out.println("What is the new name for the playlist?");
        String newName = scanner.nextLine().trim();
        dbConnection.updatePlaylistName(newName, playlist.getPlaylistId());
    }

    public static void deletePlaylist(int userId) {
        System.out.println("Please choose the playlist you would like to delete:");
        PlaylistObject playlist = choosePlaylist(dbConnection.getAllPlaylists(userId));
        System.out.println("Are you sure that you want to delete this playlist? [Y/N]");
        if (scanner.nextLine().trim().toLowerCase().equals("y")) {
            dbConnection.deletePlaylist(playlist.getPlaylistId());
        } else {
            System.out.println("Aborted deletion");
        }
    }

    public static void displayPlaylists(List<PlaylistObject> playlists) {
        if (!playlists.isEmpty()) {
            for (int i = 1; i < playlists.size() + 1; i++) {
                System.out.println("(" + i + ") " + playlists.get(i - 1).getName() + " | " + playlists.get(i - 1).getPlaylistId());
            }
        } else {
            System.out.println("Couldn't find any playlists");
        }
    }

    public static PlaylistObject choosePlaylist(List<PlaylistObject> playlists) {
        if (!playlists.isEmpty()) {
            displayPlaylists(playlists);
            while (true) {
                System.out.println("Please choose an option from the list");
                try {
                    return playlists.get(Integer.parseInt(scanner.nextLine().trim()) - 1);
                } catch (Exception e) {

                }
            }
        } else {
            System.out.println("Couldn't find any playlists");
            return null;
        }
    }
}
