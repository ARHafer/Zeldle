import type { Game } from "../types/Game";

    const STORAGE_KEY: string = 'zeldle_gameCache';

    export function loadCachedGame(): Game | null {
        const cache: string | null = localStorage.getItem(STORAGE_KEY);

        if (cache == null) {
            return null;
        }
        
        const cachedGame: Game = JSON.parse(cache) as Game;

        if (cachedGame.date !== getCurrentDate()) {
            localStorage.removeItem(STORAGE_KEY);
            return null;
        }

        return cachedGame;
    }

    export function cacheGame(game: Game): void {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(game));
    }

    // This is formatted identically to a Java LocalDate, which will allow it to be compared to the date returned from the /game endpoint.
    function getCurrentDate(): string {
        return new Intl.DateTimeFormat("en-CA", {
            timeZone: "America/New_York",
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
        }).format(new Date());
    }