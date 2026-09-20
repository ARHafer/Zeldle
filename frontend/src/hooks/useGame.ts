import { useEffect, useState } from "react";
import { initializeGame } from "../api/gameApi";
import { loadCachedGame, cacheGame } from "../cache/gameCache"
import type { Game } from "../types/Game";
import type { Guess } from "../types/Guess";

export default function useGame() {

    const [game, setGame] = useState<Game | null>(loadCachedGame);
    const [isGameLoading, setIsLoading] = useState(true); // To prevent players from guesses before the actual game is loaded.

    function updateGameCache(guessedId: number, guess: Guess): void {
        setGame(currentGame => {

            if (currentGame == null) {
                return currentGame;
            }

            const updatedGame: Game = { ...currentGame,
                guessedIds: [...currentGame.guessedIds, guessedId],
                guessHistory: [...currentGame.guessHistory, guess.feedback]
            };

            cacheGame(updatedGame);
            return updatedGame;
        });
    }

    useEffect(() => {
            initializeGame().then(game => {
                setGame(game);
                cacheGame(game);
                setIsLoading(false);
            })
        }, []);

    return { updateGameCache, game, isGameLoading }
}