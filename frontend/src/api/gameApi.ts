import type { Game } from "../types/Game";

export async function getTodaysGame(): Promise<Game> {

    const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;
    const response = await fetch(`${BACKEND_URL}/game`);

    if (!response.ok) {
        throw new Error('Failed to fetch game.');
    }

    return response.json();
}