package musicapi;

public class Music {
    private String title;
    private String artist;
    private Long id;

    public Music(String title, String artist, Long id) {
        this.title = title;
        this.artist = artist;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setId(Long id) {
        this.id = id;
    }
}



