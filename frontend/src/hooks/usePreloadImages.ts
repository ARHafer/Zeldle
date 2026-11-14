import { useEffect } from 'react';
import placeholder from '../assets/placeholder.png'
import type { Item } from '../types/Item';;

export function usePreloadItemImages(items: Item[]): void { // Preload the item images, which should get rid of the noticeable pop-in.

    useEffect(() => {
        items.forEach(item => {
            const image = new Image();

            image.src = `/item_images/${item.id}.png` 
            image.onerror = () => { image.src = placeholder };
        });
    }, [items])

}