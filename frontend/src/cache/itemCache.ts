import type { Item } from "../types/Item";

    const STORAGE_KEY: string = 'zeldle_itemCache';

    export function loadCachedItems(): Item[] {
        const cache: string | null = localStorage.getItem(STORAGE_KEY);

        if (cache == null) {
            return [];
        }
        
        const cachedItems: Item[] = JSON.parse(cache) as Item[];
        return cachedItems;
    }

    export function cacheItems(items: Item[]): void {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(items));
    }