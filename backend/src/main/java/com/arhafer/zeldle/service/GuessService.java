package com.arhafer.zeldle.service;

import com.arhafer.zeldle.dto.GuessRequest;
import com.arhafer.zeldle.dto.ItemPropertyResponse;
import com.arhafer.zeldle.entity.Item;
import com.arhafer.zeldle.dto.FeedbackResponse;
import com.arhafer.zeldle.constant.Result;
import com.arhafer.zeldle.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
public class GuessService {

    private final ItemRepository itemRepo;

    public GuessService(ItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    public FeedbackResponse submitGuess(GuessRequest guess) {
        Item guessedItem = itemRepo.findById(guess.id()).orElseThrow();
        Item targetItem = itemRepo.findById(1).orElseThrow(); // !!!PLACEHOLDER FOR TESTING!!!

        return evaluateGuess(guessedItem, targetItem);
    }

    private static FeedbackResponse evaluateGuess(Item guessed, Item target) {
        boolean correct = guessed.getId() == target.getId();

        return new FeedbackResponse(correct,
                compareProperty(guessed.getName(), target.getName()),
                compareProperty(guessed.getGame(), target.getGame()),
                compareReleaseOrder(guessed.getGame(), target.getGame()),
                compareProperty(guessed.getPurpose(), target.getPurpose()),
                compareProperty(guessed.getConsumption(), target.getConsumption()),
                compareProperty(guessed.getAcquisition(), target.getAcquisition()),
                compareProperty(guessed.getRange(), target.getRange()),
                compareProperty(guessed.getEnemyInteraction(), target.getEnemyInteraction()),
                compareProperty(guessed.getControlMode(), target.getControlMode()));
    }

    private static Result compareProperty(Object guessed, Object target) {
        if (guessed.equals(target)) {
            return Result.CORRECT;
        } else {
            return Result.INCORRECT;
        }
    }

    private static Result compareReleaseOrder(Item.Game guessed, Item.Game target) {
        if (guessed == target) {
            return Result.EQUAL;
        } else if (guessed.getReleaseOrder() < target.getReleaseOrder()) {
            return Result.LOWER;
        } else {
            return Result.HIGHER;
        }
    }

    public ItemPropertyResponse getItemProperties(int id) {
        Item item = itemRepo.findById(id).orElseThrow();

        return new ItemPropertyResponse(
                item.getName(),
                item.getGame().getFullName(),
                item.getPurpose(),
                item.getConsumption(),
                item.getAcquisition(),
                item.getRange(),
                item.getEnemyInteraction(),
                item.getControlMode());
    }
}
