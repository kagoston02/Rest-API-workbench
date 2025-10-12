package musicapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/music")
public class MusicManager {
    private static final Logger logger = LoggerFactory.getLogger(MusicManager.class);

    //initialize
    private final List<Music> musicList = new ArrayList<>(Arrays.asList(
            new Music("Hypnotyzing", "R3HAB", System.currentTimeMillis() + 1),
            new Music("Tokyo drift remix", "Callmearco", System.currentTimeMillis() + 1)
    ));


    //CREATE - POST
    //Insert new items
    // /music/{"artist": , "title": }
    @PostMapping
    public ResponseEntity<Music> insertMusic(@RequestBody Music music) {
        // Basic validation
        if (music.getArtist() == null || music.getArtist().trim().isEmpty() || music.getTitle() == null || music.getTitle().trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        // Simulate ID generation
        music.setId(System.currentTimeMillis() + 3);

        // Add to in-memory list
        musicList.add(music);

        // Return CREATED with the created resource
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(music);
    }

    //READ GET
    //list out items
    @GetMapping
    public ResponseEntity<?> QueryMusic() {
        return Optional.of(musicList)
                .filter(list -> !list.isEmpty())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty list."));
    }
    //request a specific id with /music/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> GetMusicById(@PathVariable long id) {
        Optional<Music> optMusic = musicList.stream().filter(music -> music.getId() == id).findFirst();

        return optMusic.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>("ID: " + id + " music cannot be found", HttpStatus.NOT_FOUND));
    }


    //UPDATE PUT
    //update items
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMusic(@PathVariable Long id, @RequestBody Music updatedMusic) {
        logger.info("Updating music with id: " + id);
        try {
            Music music = findById(id);
            if (updatedMusic.getArtist() == null || updatedMusic.getArtist().trim().isEmpty()
                    || updatedMusic.getTitle() == null || updatedMusic.getTitle().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Artist and title must not be empty.");
            }

            //updating attributes
            music.setArtist(updatedMusic.getArtist());
            music.setTitle(updatedMusic.getTitle());

            return ResponseEntity.ok("ID: " + id + " music updated");
        }catch (NotFoundException e) {
            logger.error("Failed to update music with  ID {}: {}", id, e.getCause());
        }
    }






    //DELETE
    //delete items
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMusic(@PathVariable Long id) {
        Optional<Music> optionalMuisc = musicList.stream().filter(music -> music.getId().equals(id)).findFirst();

        if (optionalMuisc.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Music with ID " + id + " was not found.");
        }

        musicList.remove(optionalMuisc.get());
        return ResponseEntity.noContent().build();
    }

    //find function
    private Music findById(long id) {
        return musicList.stream().filter(music -> music.getId() == id).findFirst().orElseThrow(
                () -> new NotFoundException("Music not found")
        );
    }



}