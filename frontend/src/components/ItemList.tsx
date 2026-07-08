import type { Item } from '../types/Item';

export default function ItemList(props: { items: Item[] }) {

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
                    <li key={item.id}>{item.name}</li>
                ))}
            </ul>
        </div>
    )
}