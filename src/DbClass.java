import com.sun.net.httpserver.Authenticator;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class DbClass {
    private String url;
    private String username;
    private String password;

    public DbClass() {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream("resources/db.properties")) {
            props.load(fis);

            this.url = props.getProperty("db.url");
            this.username = props.getProperty("db.username");
            this.password = props.getProperty("db.password");

        } catch (Exception e) {}
    }

    public int verifyUserLogin(String user, String pass) {
        String sql = "SELECT user_id FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user);
            stmt.setString(2, pass);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
            return -1;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public boolean checkUsernameAvailable(String user) {
        String sql = "SELECT user_id FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return false;
            }
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void createUser(String user, String pass) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user);
            stmt.setString(2, pass);

            stmt.executeUpdate();
            System.out.println("Successfully created new user");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void placeholder() {
        String sql = "";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void createNewPlaylist(String name, int userID) {
        String sql = "INSERT INTO playlists (user_id, name) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            stmt.setString(2, name);

            stmt.executeUpdate();
            System.out.println("Successfully created new playlist");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePlaylistName(String newName, int playlistId) {
        String sql = "UPDATE playlists SET name = ? WHERE playlist_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newName);
            stmt.setInt(2, playlistId);

            stmt.executeUpdate();
            System.out.println("Successfully updated playlist");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSongToPlaylist(int playlistId, int songId) {
        String sql = "INSERT INTO playlist_entries (playlist_id, song_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);

            stmt.executeUpdate();
            System.out.println("Successfully added song to playlist");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeSongFromPlaylist(int playlistId, int songId) {
        String sql = "DELETE FROM playlist_entries WHERE playlist_id = ? AND song_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);

            stmt.executeUpdate();
            System.out.println("Successfully removed song from playlist");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deletePlaylist(int playlistId) {
        String sql = "DELETE FROM playlists WHERE playlist_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);

            stmt.executeUpdate();
            System.out.println("Successfully deleted playlist");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<PlaylistObject> getAllPlaylists(int userId) {
        List<PlaylistObject> playlists = new ArrayList<>();
        String sql = "SELECT * FROM playlists WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int playlistId = rs.getInt("playlist_id");
                String playlistName = rs.getString("name");

                playlists.add(new PlaylistObject(playlistId, playlistName));
            }

            return playlists;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<SongClass> getPlayList(int playlistId) {
        String sql = "SELECT song_id FROM playlist_entries WHERE playlist_id = ?";
        List<SongClass> songs = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                songs.add(getSongById(rs.getInt("song_id")));
            }
            return songs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public void addSong(SongClass song) {
        String sql = "INSERT INTO songs (artist_id, name, genre, tags) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, song.getArtistId());
            stmt.setString(2, song.getName());
            stmt.setArray(3, conn.createArrayOf("text", song.getGenre().toArray()));
            stmt.setArray(4, conn.createArrayOf("text", song.getTags().toArray()));

            stmt.executeUpdate();
            System.out.println("Successfully added song");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateSong(SongClass song) {
        String sql = "UPDATE songs SET name = ?, genre = ?, tags = ? WHERE song_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, song.getName());
            stmt.setArray(2, conn.createArrayOf("text", song.getGenre().toArray()));
            stmt.setArray(3, conn.createArrayOf("text", song.getTags().toArray()));
            stmt.setInt(4, song.getSongId());

            stmt.executeUpdate();
            System.out.println("Successfully updated song");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteSong(SongClass song) {
        String sql = "DELETE FROM songs WHERE song_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, song.getSongId());

            stmt.executeUpdate();
            System.out.println("Successfully deleted song");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public SongClass getSongById(int id) {
        String sql = "SELECT * FROM songs WHERE song_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int songId = rs.getInt("song_id");
                int artistId = rs.getInt("artist_id");
                String name = rs.getString("name");

                Array genreArray = rs.getArray("genre");
                List<String> genre = genreArray != null ? Arrays.asList((String[]) genreArray.getArray()) : new ArrayList<>();

                Array tagsArray = rs.getArray("tags");
                List<String> tags = tagsArray != null ? Arrays.asList((String[]) tagsArray.getArray()) : new ArrayList<>();

                return new SongClass(songId, artistId, name, genre, tags);
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<SongClass> getSongsByName(String keyword) {
        List<SongClass> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE name ILIKE ?";
        keyword = "%" + keyword + "%";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, keyword);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int songId = rs.getInt("song_id");
                int artistId = rs.getInt("artist_id");
                String name = rs.getString("name");

                Array genreArray = rs.getArray("genre");
                List<String> genre = genreArray != null ? Arrays.asList((String[]) genreArray.getArray()) : new ArrayList<>();

                Array tagsArray = rs.getArray("tags");
                List<String> tags = tagsArray != null ? Arrays.asList((String[]) tagsArray.getArray()) : new ArrayList<>();

                songs.add(new SongClass(songId, artistId, name, genre, tags));
            }

            return songs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<SongClass> getSongsByArtist(int id) {
        List<SongClass> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE artist_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int songId = rs.getInt("song_id");
                int artistId = rs.getInt("artist_id");
                String name = rs.getString("name");

                Array genreArray = rs.getArray("genre");
                List<String> genre = genreArray != null ? Arrays.asList((String[]) genreArray.getArray()) : new ArrayList<>();

                Array tagsArray = rs.getArray("tags");
                List<String> tags = tagsArray != null ? Arrays.asList((String[]) tagsArray.getArray()) : new ArrayList<>();

                songs.add(new SongClass(songId, artistId, name, genre, tags));
            }

            return songs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<SongClass> getSongsByGenre(String keyword) {
        List<SongClass> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE genre ILIKE ?";
        keyword = "%" + keyword + "%";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, keyword);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int songId = rs.getInt("song_id");
                int artistId = rs.getInt("artist_id");
                String name = rs.getString("name");

                Array genreArray = rs.getArray("genre");
                List<String> genre = genreArray != null ? Arrays.asList((String[]) genreArray.getArray()) : new ArrayList<>();

                Array tagsArray = rs.getArray("tags");
                List<String> tags = tagsArray != null ? Arrays.asList((String[]) tagsArray.getArray()) : new ArrayList<>();

                songs.add(new SongClass(songId, artistId, name, genre, tags));
            }

            return songs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<SongClass> getSongsByTags(String keyword) {
        List<SongClass> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE tags ILIKE ?";
        keyword = "%" + keyword + "%";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, keyword);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int songId = rs.getInt("song_id");
                int artistId = rs.getInt("artist_id");
                String name = rs.getString("name");

                Array genreArray = rs.getArray("genre");
                List<String> genre = genreArray != null ? Arrays.asList((String[]) genreArray.getArray()) : new ArrayList<>();

                Array tagsArray = rs.getArray("tags");
                List<String> tags = tagsArray != null ? Arrays.asList((String[]) tagsArray.getArray()) : new ArrayList<>();

                songs.add(new SongClass(songId, artistId, name, genre, tags));
            }

            return songs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<SongClass> getAllSongs() {
        List<SongClass> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int songId = rs.getInt("song_id");
                int artistId = rs.getInt("artist_id");
                String name = rs.getString("name");

                Array genreArray = rs.getArray("genre");
                List<String> genre = genreArray != null ? Arrays.asList((String[]) genreArray.getArray()) : new ArrayList<>();

                Array tagsArray = rs.getArray("tags");
                List<String> tags = tagsArray != null ? Arrays.asList((String[]) tagsArray.getArray()) : new ArrayList<>();

                songs.add(new SongClass(songId, artistId, name, genre, tags));
            }

            return songs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public void addArtist(ArtistClass artist) {
        String sql = "INSERT INTO artists (name, description) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getDescription());

            stmt.executeUpdate();
            System.out.println("Successfully added artist");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateArtist(ArtistClass artist) {
        String sql = "UPDATE artists SET name = ?, description = ? WHERE artist_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getDescription());
            stmt.setInt(3, artist.getArtistId());

            stmt.executeUpdate();
            System.out.println("Successfully update artist");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteArtist(ArtistClass artist) {
        String sql = "DELETE FROM artists WHERE artist_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, artist.getArtistId());

            stmt.executeUpdate();
            System.out.println("Successfully deleted artist");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArtistClass getArtistById(int id) {
        String sql = "SELECT * FROM artists WHERE artist_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String description = rs.getString("description");

                    return new ArtistClass(id, name, description);
                }
            }

            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ArtistClass> getArtistByName(String keyword) {
        List<ArtistClass> artists = new ArrayList<>();
        keyword = "%" + keyword + "%";
        String sql = "SELECT * FROM artists WHERE name ILIKE ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, keyword);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int artistId = rs.getInt("artist_id");
                String name = rs.getString("name");
                String description = rs.getString("description");

                artists.add(new ArtistClass(artistId, name, description));
            }

            return artists;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ArtistClass> getAllArtists() {
        List<ArtistClass> artists = new ArrayList<>();
        String sql = "SELECT * FROM artists";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int artistId = rs.getInt("artist_id");
                String name = rs.getString("name");
                String description = rs.getString("description");

                artists.add(new ArtistClass(artistId, name, description));
            }

            return artists;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
