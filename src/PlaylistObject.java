import java.util.List;

public class PlaylistObject {
    private int playlistId;
    private String name;
    private List<SongClass> songs;

    public PlaylistObject(int playlistId, String name) {
        this.playlistId = playlistId;
        this.name = name;
    }

    public PlaylistObject(int playlistId, String name, List<SongClass> songs) {
        this.playlistId = playlistId;
        this.name = name;
        this.songs = songs;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SongClass> getSongs() {
        return songs;
    }

    public void setSongs(List<SongClass> songs) {
        this.songs = songs;
    }
}
