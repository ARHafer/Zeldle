import { useEffect, useState } from "react";
import { getItems } from "../api/itemApi";
import { loadCachedItems, cacheItems } from "../cache/itemCache"
import type { Item } from "../types/Item";

export default function useItems() {

    const [items, setItems] = useState<Item[]>(loadCachedItems);
    const [isItemListLoadedFromServer, setIsLoaded] = useState(false);

    useEffect(() => {
            getItems().then(items => {
                setItems(items);
                cacheItems(items);
                setIsLoaded(true);
            })
        }, []);

    return { items, isItemListLoadedFromServer }
}