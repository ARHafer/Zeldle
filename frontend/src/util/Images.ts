import { useEffect, useState } from 'react';
import placeholder from '../assets/placeholder.png';

export function getItemImage(id: number) {
    const [imageSrc, setImageSrc] = useState(placeholder);

    useEffect(() => {
        const image = new Image();

        image.src = `/item_images/${id}.png`;
        image.onload = () => { setImageSrc(image.src); };
        image.onerror = () => { setImageSrc(placeholder); };
    }, [id]);

    return(imageSrc);
}

export function getPropertyImage(property: String) {
    const [imageSrc, setImageSrc] = useState(placeholder);

    useEffect(() => {
        const image = new Image();

        image.src = `../assets/property_images/${property}.png`;
        image.onload = () => { setImageSrc(image.src); };
        image.onerror = () => { setImageSrc(placeholder); };
    }, [property]);

    return(imageSrc);
}