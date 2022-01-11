package ru.shr.restaurant_voting.util;

import lombok.experimental.UtilityClass;
import ru.shr.restaurant_voting.model.Vote;
import ru.shr.restaurant_voting.to.VoteTo;

import java.util.List;

@UtilityClass
public class VoteUtil {
    public static List<VoteTo> convertListTo (List<Vote> votes){
       return  votes.stream().map(VoteUtil::createTo).toList();
    }
    private static VoteTo createTo(Vote vote){
        return new VoteTo(vote.getId(), vote.getUser().getName(),vote.getVoteDate());
    }
}
