import { useEffect, useState } from 'react';
import placeholder from '../../assets/placeholder.png';
import type { Item } from '../../types/Item';

export default function ItemListCell(props: { item: Item; guessedIds: number[]; isLoading: boolean; onCellClick: (itemId: number) => void }) {

    const [imageSrc, setImageSrc] = useState(placeholder);
    const isGuessed: boolean = props.guessedIds.includes(props.item.id);

    useEffect(() => {
        const image = new Image();

        image.src = `/item_images/${props.item.id}.png`;
        image.onload = () => { setImageSrc(image.src); };
        image.onerror = () => { setImageSrc(placeholder); };
    }, [props.item.id]);

    return (
        <button className='item-list-cell' 
        onClick={() => props.onCellClick(props.item.id)}
        disabled={props.isLoading || isGuessed}>

            <img src={imageSrc}
            onError={(event) => {
                event.currentTarget.onerror = null;
                event.currentTarget.src = placeholder;
                }}/>

            <span>{props.item.name}</span>

        </button>
    )
}