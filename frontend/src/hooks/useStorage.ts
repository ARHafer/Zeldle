import { useEffect, useState } from "react";
import type { Feedback } from "../types/Feedback";
import { initializeGame } from "../api/gameApi";
import type { Game } from "../types/Game";
import type { Guess } from "../types/Guess";

export default function useStorage() {

    const [guessedIds, setGuessedIds] = useState<Set<number>>(new Set());
    const [guessHistory, setGuessHistory] = useState<Feedback[]>([]);
    const [game, setGame] = useState<Game | null>(null);

    useEffect(() => {
            initializeGame().then(game => {
                setGame(game);

                const key = `zeldle_${game.date}`;
                const storedData = localStorage.getItem(key);

                if (storedData != null) {
                    const parsedData = JSON.parse(storedData);

                    setGuessedIds(new Set(parsedData.guessedIds));
                    setGuessHistory(parsedData.guessHistory);
                }
            })
        }, [])

    async function storeGuess(id: number, guess: Guess) {
        if (game == null) {
            return;
        }

        const key = `zeldle_${game.date}`;
        const data = {
            guessedIds: [...guessedIds, id],
            guessHistory: [...guessHistory, guess.feedback]
        };

        localStorage.setItem(key, JSON.stringify(data));

        setGuessedIds(new Set(data.guessedIds));
        setGuessHistory(data.guessHistory);
    }

    return { storeGuess, guessedIds, guessHistory }
}