package ru.shr.restaurant_voting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.shr.restaurant_voting.model.Vote;
import ru.shr.restaurant_voting.repository.UserRepository;
import ru.shr.restaurant_voting.repository.VoteRepository;
import ru.shr.restaurant_voting.web.user.ProfileController;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.shr.restaurant_voting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteController {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/votes")
    public List<Vote> getAllUserVotes(@AuthenticationPrincipal AuthUser authUser, @RequestParam(required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate) {
        log.info("getAllUserVotes");
        if (voteDate == null) {
            return voteRepository.findAllByUserId(authUser.id());
        } else {
            return List.of(voteRepository.findByUserIdAndVoteDate(authUser.id(), voteDate));
        }
    }

    @PostMapping(value = "/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createVote(@Valid @RequestBody Vote vote, @AuthenticationPrincipal AuthUser authUser) {
        if (!vote.getVoteDate().isEqual(LocalDate.now())) {
            throw (new IllegalArgumentException("You can't vote for past"));
        }

        Vote pastVote = voteRepository.findByUserIdAndVoteDate(authUser.id(), vote.getVoteDate());
        if (pastVote != null) {
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime deadlineTime = LocalDateTime.parse(LocalDate.now() + "T11:00:00");
            if (today.isAfter(deadlineTime)) {
                throw (new IllegalArgumentException("You have voted today already. Vote can't be changed after 11:00"));
            }
            else {
                log.info("delete {}", pastVote);
                voteRepository.deleteExisted(pastVote.id());
            }
        }

        log.info("create {}", vote);
        checkNew(vote);
        vote.setUser(userRepository.getById(authUser.id()));
        Vote created = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ProfileController.REST_URL + "/votes/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
