package musicapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/m")
public class MusicManager {

    //initialize
    private final List<Music> musicList = new ArrayList<>(Arrays.asList(
            new Music("Hypnotyzing", "R3HAB", System.currentTimeMillis() + 1),
            new Music("Tokyo drift remix", "Callmearco", System.currentTimeMillis() + 1)
    ));

    //list out items
    @GetMapping
    public ResponseEntity<?> QueryMusic() {
        return Optional.of(musicList)
                .filter(list -> !list.isEmpty())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty list."));
    }

}