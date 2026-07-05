export async function getStatus() {

    const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;
    const response = await fetch(`${BACKEND_URL}/status`);

    if (!response.ok) {
        throw new Error('Failed to fetch status.');
    }

    return response.json();
}