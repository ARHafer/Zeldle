import type { Game } from "../types/Game";
import type { ProblemDetail } from "../types/ProblemDetail";

export async function initializeGame(): Promise<Game> {

    const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

    const response = await fetch(`${BACKEND_URL}/game`, {
        method: 'GET',
        credentials: 'include'
    });

    if (!response.ok) {
            const error: ProblemDetail = await response.json();
            console.error(error.title + ": " + error.detail);
        }

    return response.json();
}