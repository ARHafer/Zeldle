import { useState } from "react";
import { submitGuess } from "../api/guessApi";
import type { Guess } from "../types/Guess";

export default function useGuess() {

    const [isLoading, setIsLoading] = useState(false); // Users cannot guess again until the promise is fulfilled and the guess registers.
    const [error, setError] = useState<string | null>(null);

    async function guessItem(itemId: number): Promise<Guess | null> {
        setIsLoading(true);
        setError(null);

        try {
            const guess = await submitGuess(itemId);

            return guess;
        } catch (error: unknown) {
            
            if (error instanceof Error) {
                setError(error.message)
                console.log(error.message)
            } else {
                setError('An unknown error occurred attempting to submit the guess. Seriously, I have no idea what happened.')
                console.error(error)
            }

            return null;
        } finally {
            setIsLoading(false)
        }
    }

    return { guessItem, isLoading, error } // Returning the error for UI display later.
}