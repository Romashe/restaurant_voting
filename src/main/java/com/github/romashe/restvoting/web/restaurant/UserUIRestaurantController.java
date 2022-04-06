package com.github.romashe.restvoting.web.restaurant;

import com.github.romashe.restvoting.repository.RestaurantRepository;
import com.github.romashe.restvoting.repository.VoteRepository;
import com.github.romashe.restvoting.to.RestaurantTo;
import com.github.romashe.restvoting.util.RestaurantUtil;
import com.github.romashe.restvoting.web.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserUIRestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;

    @GetMapping("ratings")
    @Transactional
    public String todayRestaurantsRatings (Model model, @AuthenticationPrincipal AuthUser authUser){
        log.info("getAllRestaurantWithMenuByTodayWithRating for Today");
        List<RestaurantTo> restaurantToList = RestaurantUtil.convertListTo(restaurantRepository.getAllWithMenu(), voteRepository.findAllTodayVotes());
        model.addAttribute("userName", authUser.getUsername());
        model.addAttribute("title", "Today Restaurants Ratings");
        model.addAttribute("restaurants", restaurantToList);
        return "todayRestaurantsRatings";
    }
}
