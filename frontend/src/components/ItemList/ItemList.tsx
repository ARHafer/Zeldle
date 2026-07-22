import type { Item } from '../../types/Item';
import ItemListCell from './ItemListCell';

export default function ItemList(props: { items: Item[]; guessedIds: Set<number>; isLoading: boolean; onCellClick: (itemId: number) => void } ) {

    if (props.items.length == 0) {
        return (
            <div>
                <h2>Item List</h2>
                <p>The item database is either empty or communication with it has failed. Somebody should probably fix that.</p>
            </div>
        )
    }

    return (
        <div>
            <h2>Item List</h2>

            <ul>
                {props.items.map(item => (
                    <li key={item.id}><ItemListCell item={item} guessedIds={props.guessedIds} isLoading={props.isLoading} onCellClick={props.onCellClick}/></li>
                ))}
            </ul>
        </div>
    )
}