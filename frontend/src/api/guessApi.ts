import type { FeedbackResponse } from "../types/FeedbackResponse";

export async function submitGuess(itemId: number): Promise<FeedbackResponse> {

    const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;
    
    const response = await fetch(`${BACKEND_URL}/guess`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: itemId })
    });

    if (!response.ok) {
        throw new Error('Failed to submit guess.');
    }

    return response.json();
}