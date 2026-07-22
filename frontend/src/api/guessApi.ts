import type { Feedback } from "../types/Feedback";
import type { ItemProperty } from "../types/ItemProperty";

export async function submitGuess(itemId: number): Promise<Feedback> {

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

export async function getItemProperties(itemId: number): Promise<ItemProperty> {

    const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;
    const response = await fetch(`${BACKEND_URL}/guess/item/${itemId}`);

    if (!response.ok) {
        throw new Error('Failed to retrieve item properties.');
    }

    return response.json();
}