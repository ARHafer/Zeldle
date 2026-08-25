package com.arhafer.zeldle.service; // Not technically a service, but a service HELPER, so I feel it's appropriate.

import com.arhafer.zeldle.constant.Result;
import com.arhafer.zeldle.dto.Feedback;
import com.arhafer.zeldle.dto.ItemProperties;
import com.arhafer.zeldle.dto.Results;
import com.arhafer.zeldle.entity.Item;
import org.springframework.stereotype.Component;

@Component
public class GuessEvaluator {

    public Feedback evaluateGuess(Item guessedItem, Item targetItem) {
        Results results = getResults(guessedItem, targetItem);
        ItemProperties properties = getItemProperties(guessedItem);

        return new Feedback(results, properties);
    }

    private static Results getResults(Item guessed, Item target) {
        return new Results(
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
            return Result.TARGET_HIGHER;
        } else {
            return Result.TARGET_LOWER;
        }
    }

    private static ItemProperties getItemProperties(Item item) {
        return new ItemProperties(
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
