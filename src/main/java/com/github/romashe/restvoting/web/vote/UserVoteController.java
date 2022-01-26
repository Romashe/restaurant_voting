package com.github.romashe.restvoting.web.vote;

import com.github.romashe.restvoting.error.IllegalRequestDataException;
import com.github.romashe.restvoting.model.Restaurant;
import com.github.romashe.restvoting.model.Vote;
import com.github.romashe.restvoting.repository.RestaurantRepository;
import com.github.romashe.restvoting.repository.VoteRepository;
import com.github.romashe.restvoting.to.VoteTo;
import com.github.romashe.restvoting.util.validation.ValidationUtil;
import com.github.romashe.restvoting.web.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.github.romashe.restvoting.util.validation.ValidationUtil.DEADLINE_TIME;
import static com.github.romashe.restvoting.web.vote.UserVoteController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
@CacheConfig(cacheNames = "restaurants")
public class UserVoteController {
    public static final String REST_URL = "/api/votes";
    final private RestaurantRepository restaurantRepository;
    final private VoteRepository voteRepository;

    @GetMapping()
    @Cacheable
    @Operation(summary = "Get all current user votes")
    public List<Vote> getAllUserVotes(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAllUserVotes");
        return voteRepository.findAllByUserId(authUser.id());
    }

    @GetMapping("/by-date")
    @Cacheable
    @Operation(summary = "Get current user vote by voteDate")
    public ResponseEntity<Vote> getUserVoteByDate(@AuthenticationPrincipal AuthUser authUser,
                                                  @RequestParam
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate) {
        log.info("getUserVoteByDate {}", voteDate);
        return ResponseEntity.of(voteRepository.findByUserIdAndVoteDate(authUser.id(), voteDate));
    }

    @GetMapping("/{id}")
    @Cacheable
    @Operation(summary = "Get current user vote by Id")
    public ResponseEntity<Vote> getUserVoteById(@AuthenticationPrincipal AuthUser authUser,
                                                @PathVariable int id) {
        log.info("getUserVoteById {}", id);
        return ResponseEntity.of(voteRepository.findByUserIdAndVoteId(authUser.id(), id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(allEntries = true)
    @Operation(summary = "You can vote right here", description = "Voting is only possible for today's date")
    public ResponseEntity<Vote> createVote(@Valid @RequestBody VoteTo voteTo,
                                           @AuthenticationPrincipal AuthUser authUser) {
        log.info("create {}", voteTo);
        Optional<Vote> pastVote = voteRepository.findByUserIdAndVoteDate(authUser.id(), LocalDate.now());
        if (pastVote.isPresent()) {
            throw (new IllegalRequestDataException("You have voted today already. Use PUT to change your mind"));
        }
        Restaurant votedRestaurant = restaurantRepository.getById(voteTo.getRestaurantId());
        Vote vote = new Vote(LocalDate.now(), votedRestaurant, authUser.getUser());
        ValidationUtil.checkNew(vote);
        Vote created = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "You can change your vote right here", description = "Vote can't be changed after 11:00")
    public void updateVote(@Valid @RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update Vote {}", voteTo);
        Optional<Vote> pastVote = voteRepository.findByUserIdAndVoteDate(authUser.id(), LocalDate.now());
        if (pastVote.isPresent()) {
            if (LocalDateTime.now().isAfter(LocalDateTime.parse(LocalDate.now() + DEADLINE_TIME))) {
                throw (new IllegalRequestDataException(
                        "You have voted today already. Vote can't be changed after " + DEADLINE_TIME));
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
