package com.github.romashe.restvoting.web.vote;

import com.github.romashe.restvoting.error.IllegalRequestDataException;
import com.github.romashe.restvoting.model.Restaurant;
import com.github.romashe.restvoting.model.Vote;
import com.github.romashe.restvoting.repository.RestaurantRepository;
import com.github.romashe.restvoting.repository.VoteRepository;
import com.github.romashe.restvoting.to.VoteTo;
import com.github.romashe.restvoting.web.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.romashe.restvoting.util.validation.ValidationUtil.DEADLINE_TIME;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserVoteUIController {
    public static final String REST_URL = "ratings/votes";
    final private RestaurantRepository restaurantRepository;
    final private VoteRepository voteRepository;

    @PostMapping(value = REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(value = {"ratings"}, allEntries = true)
    public ResponseEntity<Vote> createVote(@Valid @RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("create {}", voteTo);
        if (authUser.getUser().getId() != null) {
            Optional<Vote> pastVote = voteRepository.findByUserIdAndVoteDate(authUser.id(), LocalDate.now());
            if (pastVote.isPresent()) {
                throw (new IllegalRequestDataException("You have voted today already. Use PUT to change your mind"));
            }

            Restaurant votedRestaurant = restaurantRepository.getById(voteTo.getRestaurantId());
            Vote vote = new Vote(LocalDate.now(), votedRestaurant, authUser.getUser(), votedRestaurant.getId());
            Vote created = voteRepository.save(vote);
            return ResponseEntity.ok(created);
        } else {
            throw (new IllegalRequestDataException("Please Relogin"));
        }

    }

    @PutMapping(value = REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"ratings"}, allEntries = true)
    public void updateVote(@Valid @RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update Vote {}", voteTo);
        Optional<Vote> pastVote = voteRepository.findByUserIdAndVoteDate(authUser.id(), LocalDate.now());
        if (pastVote.isPresent()) {
            if (LocalDateTime.now().isAfter(LocalDateTime.of(LocalDate.now(), DEADLINE_TIME))) {
                throw (new IllegalRequestDataException("You have voted today already. Vote can't be changed after " + DEADLINE_TIME));
            } else {
                Restaurant votedRestaurant = restaurantRepository.getById(voteTo.getRestaurantId());
                Vote updatedVote = pastVote.get();
                updatedVote.setRestaurant(votedRestaurant);
                voteRepository.save(updatedVote);
            }
        } else {
            throw (new IllegalRequestDataException("You didn't vote today. Use POST to vote"));
        }
    }
}