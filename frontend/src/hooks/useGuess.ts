import { useState } from "react";
import type { FeedbackResponse } from "../types/FeedbackResponse";
import { submitGuess } from "../api/guessApi";

export default function useGuess() {

    const [result, setResult] = useState<FeedbackResponse | null>(null);
    const [isLoading, setIsLoading] = useState(false); // Users cannot guess again until the promise is fulfilled and the guess registers.
    const [error, setError] = useState<string | null>(null);

    async function guess(itemId: number) {
        setIsLoading(true);
        setError(null);

        try {
            const data = await submitGuess(itemId);
            setResult(data);
        } catch (error: unknown) {
            
            if (error instanceof Error) {
                setError(error.message)
            } else {
                setError('An error occurred.')
            }

        } finally {
            setIsLoading(false)
        }
    }

    return { guess, result, isLoading, error }
}