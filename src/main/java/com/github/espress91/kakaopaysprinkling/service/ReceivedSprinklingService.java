package com.github.espress91.kakaopaysprinkling.service;

import com.github.espress91.kakaopaysprinkling.data.model.ReceivedSprinkling;
import com.github.espress91.kakaopaysprinkling.data.model.Sprinkling;
import com.github.espress91.kakaopaysprinkling.data.model.User;
import com.github.espress91.kakaopaysprinkling.repository.ReceivedSprinklingRepository;
import com.github.espress91.kakaopaysprinkling.repository.SprinklingRepository;
import com.github.espress91.kakaopaysprinkling.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ReceivedSprinklingService {
    private final SprinklingRepository sprinklingRepository;

    private final ReceivedSprinklingRepository receivedSprinklingRepository;

    private final UserRepository userRepository;

    public ReceivedSprinklingService(SprinklingRepository sprinklingRepository, ReceivedSprinklingRepository receivedSprinklingRepository, UserRepository userRepository) {
        this.sprinklingRepository = sprinklingRepository;
        this.receivedSprinklingRepository = receivedSprinklingRepository;
        this.userRepository = userRepository;
    }

    public boolean checkReceivedUser(List<ReceivedSprinkling> receivedSprinklings, Long userId) {
        List<ReceivedSprinkling> receivedSprinklingList =
                receivedSprinklings.stream().filter(receivedSprinkling -> receivedSprinkling.getUser() != null).filter(receivedSprinkling ->
                        receivedSprinkling.getUser().getId() == userId).collect(Collectors.toList());

        if(receivedSprinklingList.size() == 0) return false;

        return true;
    }

    @Transactional
    public ReceivedSprinkling receiveSprinkling(Sprinkling sprinkling, User user) {
        for(ReceivedSprinkling receivedSprinkling : sprinkling.getReceivedSprinklings()) {
            if(receivedSprinkling.getUser() == null) {
                receivedSprinkling.setUser(user);
                sprinkling.setBalance(sprinkling.getBalance() - receivedSprinkling.getBudget());
                user.setBudget(user.getBudget() + receivedSprinkling.getBudget());

                userRepository.save(user);
                sprinklingRepository.save(sprinkling);
                return receivedSprinklingRepository.save(receivedSprinkling);
            }
        }
        return null;
    }
}
