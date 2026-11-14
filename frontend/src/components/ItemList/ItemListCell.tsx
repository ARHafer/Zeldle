import { memo } from 'react';
import type { Item } from '../../types/Item';

import './ItemList.css';

function ItemListCell(props: { item: Item; guessedIds: number[]; isGuessLoading: boolean; isLoadedFromServer: boolean; onCellClick: (itemId: number) => void }) {

    const isGuessed: boolean = props.guessedIds.includes(props.item.id);

    return (
        <button className='item-list-cell' 
        onClick={() => props.onCellClick(props.item.id)}
        disabled={props.isGuessLoading || !props.isLoadedFromServer || isGuessed}>

            <img src={`/item_images/${props.item.id}.png`}
            alt={props.item.name}
            loading='eager'
            decoding='async'/>

            <span>{props.item.name}</span>

        </button>
    )
}

export default memo(ItemListCell) // Another small optimization. Every single cell doesn't need to re-render every time the list does (which is admittedly a lot).