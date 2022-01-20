package com.github.romashe.restvoting.util;

import com.github.romashe.restvoting.model.Vote;
import com.github.romashe.restvoting.to.VoteTo;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class VoteUtil {
    public static List<VoteTo> convertListTo(List<Vote> votes) {
        return votes.stream().map(VoteUtil::createTo).toList();
    }

    private static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getRestaurant().id());
    }
}
