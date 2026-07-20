import type { Item } from "../types/Item";

export async function getItems(): Promise<Item[]> {

    const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;
    const response = await fetch(`${BACKEND_URL}/items`);

    if (!response.ok) {
        throw new Error('Failed to fetch items.');
    }

    return response.json();
}