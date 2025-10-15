package musicapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Validator;
import musicapi.validation.OnCreateOrUpdate;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/music")
@Validated
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
        logger.info("Inserting music {}", music);
        try{
            // Basic validation
            if (music.getArtist() == null || music.getArtist().trim().isEmpty() || music.getTitle() == null || music.getTitle().trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(null);
            }


            validateAndThrow(music, OnCreateOrUpdate.class);
            // Simulate ID generation
            music.setId(System.currentTimeMillis() + 3);
            // Add to in-memory list
            musicList.add(music);
            // Return CREATED with the created resource
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(music);
        }catch (Exception e){
            logger.error("Error inserting music {}: {}",music, e.getMessage());
            throw e;
        }
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

            validateAndThrow(updatedMusic, OnCreateOrUpdate.class);
            //updating attributes
            music.setArtist(updatedMusic.getArtist());
            music.setTitle(updatedMusic.getTitle());

            return ResponseEntity.ok("ID: " + id + " music updated");
        }catch (NotFoundException e) {
            logger.error("Failed to update music with  ID {}: {}", id, e.getCause());
            throw e;
        }
    }






    //DELETE
    //delete items
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMusic(@PathVariable Long id) {
        logger.info("Deleting music with ID {} ", id);
        try {
            Optional<Music> optionalMuisc = musicList.stream().filter(music -> music.getId().equals(id)).findFirst();

            if (optionalMuisc.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Music with ID " + id + " was not found.");
            }


            musicList.remove(optionalMuisc.get());
            return ResponseEntity.noContent().build();
        }catch (NotFoundException e) {
            logger.error("Failed to delete music with  ID {}: {}", id, e.getCause());
            throw e;
        }
    }

    //find function
    private Music findById(long id) {
        return musicList.stream().filter(music -> music.getId() == id).findFirst().orElseThrow(
                () -> new NotFoundException("Music not found")
        );
    }


    private void validateAndThrow(Object object, Class<?>... groups) {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(object, groups);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    }



}