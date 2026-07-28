import { useEffect, useState } from "react";
import type { GuessData } from "../types/GuessData";
import { getTodaysGame } from "../api/gameApi";
import type { Game } from "../types/Game";

export default function useStorage() {

    const [guessedIds, setGuessedIds] = useState<Set<number>>(new Set());
    const [guessHistory, setGuessHistory] = useState<GuessData[]>([]);
    const [game, setGame] = useState<Game | null>(null);

    useEffect(() => {
            getTodaysGame().then(game => {
                setGame(game);

                const key = `zeldle_${game.gameDate}`;
                const storedData = localStorage.getItem(key);

                if (storedData != null) {
                    const parsedData = JSON.parse(storedData);

                    setGuessedIds(new Set(parsedData.guessedIds));
                    setGuessHistory(parsedData.guessHistory);
                }
            })
        }, [])

    async function storeGuess(id: number, guessData: GuessData) {
        if (game == null) {
            return; // Shouldn't ever happen, but TypeScript won't stop yelling at me.
        }

        const key = `zeldle_${game.gameDate}`;
        const data = {
            guessedIds: [...guessedIds, id], // Stored as an array here, since JSON.stringify doesn't recognize sets.
            guessHistory: [...guessHistory, guessData]
        };

        localStorage.setItem(key, JSON.stringify(data));

        setGuessedIds(new Set(data.guessedIds));
        setGuessHistory(data.guessHistory);
    }

    return { storeGuess, guessedIds, guessHistory }
}