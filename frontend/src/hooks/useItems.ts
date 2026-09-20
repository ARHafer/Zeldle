import { useEffect, useState } from "react";
import { getItems } from "../api/itemApi";
import { loadCachedItems, cacheItems } from "../cache/itemCache"
import type { Item } from "../types/Item";

export default function useItems() {

    const [items, setItems] = useState<Item[]>(loadCachedItems);
    const [isItemListLoading, setIsLoading] = useState(true);

    useEffect(() => {
            getItems().then(items => {
                setItems(items);
                cacheItems(items);
                setIsLoading(false);
            })
        }, []);

    return { items, isItemListLoading }
}