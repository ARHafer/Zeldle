import { useState } from "react";
import { getItemProperties, submitGuess } from "../api/guessApi";
import type { GuessData } from "../types/GuessData";

export default function useGuess() {

    const [isLoading, setIsLoading] = useState(false); // Users cannot guess again until the promise is fulfilled and the guess registers.
    const [error, setError] = useState<string | null>(null);

    async function guess(itemId: number): Promise<GuessData | null> {
        setIsLoading(true);
        setError(null);

        try {
            const feedback = await submitGuess(itemId);
            const properties = await getItemProperties(itemId);

            const data = { feedback, properties }

            return data; // No longer returns state, as up-to-date data needs to be returned without re-rendering.
        } catch (error: unknown) {
            
            if (error instanceof Error) {
                setError(error.message)
            } else {
                setError('An error occurred.')
            }

            return null;
        } finally {
            setIsLoading(false)
        }
    }

    return { guess, isLoading, error }
}