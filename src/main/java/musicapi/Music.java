package musicapi;

import jakarta.validation.constraints.NotEmpty;
import musicapi.validation.OnCreateOrUpdate;

public class Music {
    private String title;
    @NotEmpty(message = "field cannot be left empty.", groups = OnCreateOrUpdate.class)
    private String artist;
    @NotEmpty(message = "filed cannot be left empty.", groups = OnCreateOrUpdate.class)
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



