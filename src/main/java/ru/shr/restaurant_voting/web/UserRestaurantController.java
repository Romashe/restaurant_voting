package ru.shr.restaurant_voting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.shr.restaurant_voting.error.IllegalRequestDataException;
import ru.shr.restaurant_voting.model.MenuItem;
import ru.shr.restaurant_voting.model.Restaurant;
import ru.shr.restaurant_voting.model.Vote;
import ru.shr.restaurant_voting.repository.MenuItemRepository;
import ru.shr.restaurant_voting.repository.RestaurantRepository;
import ru.shr.restaurant_voting.repository.UserRepository;
import ru.shr.restaurant_voting.repository.VoteRepository;
import ru.shr.restaurant_voting.to.RestaurantTo;
import ru.shr.restaurant_voting.to.VoteTo;
import ru.shr.restaurant_voting.util.RestaurantUtil;
import ru.shr.restaurant_voting.util.VoteUtil;
import ru.shr.restaurant_voting.web.user.ProfileController;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.shr.restaurant_voting.util.validation.ValidationUtil.checkNew;
import static ru.shr.restaurant_voting.web.UserRestaurantController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserRestaurantController {

    public static final String REST_URL = "/api/restaurants";

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public List<RestaurantTo> getAll(@RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}")
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
                                           @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}")
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate) {
        log.info("get Restaurant with id={}, Vote for date {}", id, voteDate);
        return VoteUtil.convertListTo(voteRepository.findVotesByDateAndRestId(id, voteDate));
    }

    @PostMapping(value = "/{id}/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createVote(@Valid @RequestBody Vote vote,
                                           @AuthenticationPrincipal AuthUser authUser,
                                           @PathVariable int id) {
        if (!vote.getVoteDate().isEqual(LocalDate.now())) {
            throw (new IllegalRequestDataException("You can't vote for past or future"));
        }

        Vote pastVote = voteRepository.findByUserIdAndVoteDate(authUser.id(), vote.getVoteDate());
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

        log.info("create {}", vote);
        checkNew(vote);
        vote.setUser(userRepository.getById(authUser.id()));
        vote.setRestaurant(restaurantRepository.getById(id));
        Vote created = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ProfileController.REST_URL + "/votes/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
