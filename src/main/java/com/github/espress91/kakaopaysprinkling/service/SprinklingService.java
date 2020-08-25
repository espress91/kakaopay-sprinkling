package com.github.espress91.kakaopaysprinkling.service;

import com.github.espress91.kakaopaysprinkling.data.model.ReceivedSprinkling;
import com.github.espress91.kakaopaysprinkling.data.model.Room;
import com.github.espress91.kakaopaysprinkling.data.model.Sprinkling;
import com.github.espress91.kakaopaysprinkling.data.model.User;
import com.github.espress91.kakaopaysprinkling.repository.ReceivedSprinklingRepository;
import com.github.espress91.kakaopaysprinkling.repository.SprinklingRepository;
import com.github.espress91.kakaopaysprinkling.repository.UserRepository;
import com.github.espress91.kakaopaysprinkling.util.RandomUtils;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SprinklingService {
    private final SprinklingRepository sprinklingRepository;

    private final ReceivedSprinklingRepository receivedSprinklingRepository;

    private final UserRepository userRepository;

    public Optional<Sprinkling> getSprinkling(Long sprinklingId) {
        return sprinklingRepository.findById(sprinklingId);
    }

    public Optional<Sprinkling> getSprinkling(String token) {
        return sprinklingRepository.findByToken(token);
    }

    public SprinklingService(SprinklingRepository sprinklingRepository, ReceivedSprinklingRepository receivedSprinklingRepository, UserRepository userRepository) {
        this.sprinklingRepository = sprinklingRepository;
        this.receivedSprinklingRepository = receivedSprinklingRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void shutDownSprinkling(Sprinkling sprinkling) {
        if(sprinkling.getBalance() != 0) {
            User user = sprinkling.getOwner();
            user.setBudget(user.getBudget() + sprinkling.getBalance());
            sprinkling.setBalance(0);

            userRepository.save(user);
            sprinklingRepository.save(sprinkling);
        }
    }


    @Transactional
    public Sprinkling createSprinkling(Sprinkling sprinkling, User user, Room room) {
        setOwnerBudget(sprinkling, user);
        sprinkling = setSprinkling(sprinkling, user, room);
        setReceivedSprinklings(sprinkling);

        return sprinkling;
    }

    private void setReceivedSprinklings(Sprinkling sprinkling) {
        List<ReceivedSprinkling> receivedSprinklings = divideBudget(sprinkling);
        receivedSprinklings.forEach(receivedSprinkling -> {
            receivedSprinklingRepository.save(receivedSprinkling);
        });
    }

    @NotNull
    public Sprinkling setSprinkling(Sprinkling sprinkling, User user, Room room) {
        sprinkling.setToken(RandomUtils.getInstance().generateToken());
        sprinkling.setBalance(sprinkling.getBudget());
        sprinkling.setOwner(user);
        sprinkling.setRoom(room);

        sprinkling = sprinklingRepository.save(sprinkling);
        return sprinkling;
    }

    public void setOwnerBudget(Sprinkling sprinkling, User user) {
        user.setBudget(user.getBudget() - sprinkling.getBudget());
        userRepository.save(user);
    }

    public List<ReceivedSprinkling> divideBudget(Sprinkling sprinkling) {
        int divideNum = sprinkling.getDivideNum();
        int budget = sprinkling.getBudget();

        List<ReceivedSprinkling> receivedSprinklings = new ArrayList<>();
        for(int i = 0; i < divideNum; i++) {
            ReceivedSprinkling receivedSprinkling = new ReceivedSprinkling();
            receivedSprinkling.setBudget(1);
            receivedSprinkling.addSprinkling(sprinkling);
            receivedSprinklings.add(receivedSprinkling);
            budget--;
        }

        return divide(receivedSprinklings, budget);

    }

    private List<ReceivedSprinkling> divide(List<ReceivedSprinkling> receivedSprinklings, int budget) {
        if (budget < 2) {
            int index = RandomUtils.getInstance().getRandom(receivedSprinklings.size());
            receivedSprinklings.get(index).setBudget(receivedSprinklings.get(index).getBudget() + budget);
            return receivedSprinklings;
        }
        int dividen = RandomUtils.getInstance().getRandom(budget);

        int index = RandomUtils.getInstance().getRandom(receivedSprinklings.size());
        receivedSprinklings.get(index).setBudget(receivedSprinklings.get(index).getBudget() + dividen);

        budget = budget - dividen;
        return divide(receivedSprinklings, budget);
    }
}
