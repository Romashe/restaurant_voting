package com.github.romashe.restvoting.web.restaurant;

import com.github.romashe.restvoting.error.IllegalRequestDataException;
import com.github.romashe.restvoting.model.MenuItem;
import com.github.romashe.restvoting.model.Restaurant;
import com.github.romashe.restvoting.model.User;
import com.github.romashe.restvoting.model.Vote;
import com.github.romashe.restvoting.repository.MenuItemRepository;
import com.github.romashe.restvoting.repository.RestaurantRepository;
import com.github.romashe.restvoting.repository.UserRepository;
import com.github.romashe.restvoting.repository.VoteRepository;
import com.github.romashe.restvoting.to.RestaurantTo;
import com.github.romashe.restvoting.to.VoteTo;
import com.github.romashe.restvoting.util.RestaurantUtil;
import com.github.romashe.restvoting.util.VoteUtil;
import com.github.romashe.restvoting.util.validation.ValidationUtil;
import com.github.romashe.restvoting.web.AuthUser;
import com.github.romashe.restvoting.web.user.ProfileController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.romashe.restvoting.web.restaurant.UserRestaurantController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class UserRestaurantController {

    public static final String REST_URL = "/api/restaurants";
    final private RestaurantRepository restaurantRepository;
    final private VoteRepository voteRepository;
    final private MenuItemRepository menuItemRepository;
    final private UserRepository userRepository;

    @GetMapping()
    @Operation(summary = "Main operation for get restaurants, it's menu and vote count by Requested Date",
            description = "Only restaurants that have a menu for the Requested Date are displayed")
    public List<RestaurantTo> getAllRestaurantWithMenuByDate(@Parameter(description = "Default Value = Today")
                                                             @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}")
                                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("getAllRestaurantWithMenu for MenuDate {}", menuDate);
        return RestaurantUtil.convertListTo(restaurantRepository.getAllWithMenu(menuDate), menuDate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable int id) {
        log.info("get Restaurant {}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    @GetMapping("/{id}/menu")
    public List<MenuItem> getRestaurantMenu(@PathVariable int id,
                                            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("get Restaurant with id={}, Menu for date {}", id, menuDate);
        return menuItemRepository.findByRestaurantIdFilteredByItemDate(id, menuDate);
    }

    @GetMapping("/{id}/votes")
    public List<VoteTo> getRestaurantVotes(@PathVariable int id,
                                           @Parameter(description = "Default Value = Today")
                                           @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate) {
        log.info("get Restaurant with id={}, Vote for date {}", id, voteDate);
        return VoteUtil.convertListTo(voteRepository.findVotesByDateAndRestId(id, voteDate));
    }

    @PostMapping(value = "/{id}/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "You can vote right here", description = "Voting is only possible for today's date")
    public ResponseEntity<Vote> createVote(@Valid @RequestBody VoteTo voteTo,
                                           @AuthenticationPrincipal AuthUser authUser,
                                           @PathVariable int id) {
        if (!voteTo.getVoteDate().isEqual(LocalDate.now())) {
            throw (new IllegalRequestDataException("You can't vote for past or future"));
        }

        Vote pastVote = voteRepository.findByUserIdAndVoteDate(authUser.id(), voteTo.getVoteDate());
        if (pastVote != null) {
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime deadlineTime = LocalDateTime.parse(LocalDate.now() + "T11:00:00");
//            deadlineTime = deadlineTime.plusMonths(1); //for testing
            if (today.isAfter(deadlineTime)) {
                throw (new IllegalRequestDataException("You have voted today already. Vote can't be changed after 11:00"));
            } else {
                log.info("delete {}", pastVote);
                voteRepository.deleteExisted(pastVote.id());
            }
        }

        Restaurant votedRestaurant = restaurantRepository.findById(id).orElseThrow(() -> new IllegalRequestDataException("Restaurant not found"));
        User user = userRepository.findById(authUser.id()).orElseThrow(() -> new IllegalRequestDataException("User not found"));
        Vote vote = new Vote(voteTo.getVoteDate(), votedRestaurant, user);
        log.info("create {}", vote);
        ValidationUtil.checkNew(vote);
        Vote created = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ProfileController.REST_URL + "/votes/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
