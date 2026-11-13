import { getItemImage } from '../../util/Images';
import type { Item } from '../../types/Item';

import './ItemList.css';

export default function ItemListCell(props: { item: Item; guessedIds: number[]; isGuessLoading: boolean; isLoadedFromServer: boolean; onCellClick: (itemId: number) => void }) {

    const isGuessed: boolean = props.guessedIds.includes(props.item.id);

    return (
        <button className='item-list-cell' 
        onClick={() => props.onCellClick(props.item.id)}
        disabled={props.isGuessLoading || !props.isLoadedFromServer || isGuessed}>

            <img src={getItemImage(props.item.id)}/>
            <span>{props.item.name}</span>

        </button>
    )
}