import type { Guess } from "../types/Guess";
import type { ProblemDetail } from "../types/ProblemDetail";

export async function submitGuess(itemId: number): Promise<Guess> {

    const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;
    
    const response = await fetch(`${BACKEND_URL}/guess`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ itemId: itemId }),
        credentials: 'include'
    });

    if (!response.ok) {
        const error: ProblemDetail = await response.json();
        throw new Error(error.title + ": " + error.detail);
    }

    return response.json();
}