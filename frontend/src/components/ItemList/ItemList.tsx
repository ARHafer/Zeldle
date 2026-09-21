import { useState } from 'react';
import ItemListCell from './ItemListCell';
import type { Item } from '../../types/Item';

export default function ItemList(props: { items: Item[]; guessedIds: number[]; isLoading: boolean; onCellClick: (itemId: number) => void; } ) {

    const [search, setSearch] = useState('');
    const filteredItems = props.items.filter(item => item.name.toLowerCase().includes(search.toLowerCase()));

    if (props.isLoading && props.items.length == 0) {
        return (
            <ul className='item-list'>
                <p>Loading, please wait... <br/>
                    If this is taking awhile, refresh the page.
                </p>
            </ul>
        )
    
    } else if (props.items.length == 0) {
        return (
            <ul className='item-list'>
                <p>No items were found! If you're seeing this, please submit a bug report!</p>
            </ul>
        )
    
    }

    return (
        <div className='item-list-container'>
            <input className='item-list-searchbar'
            type='text'
            placeholder='Search item list...'
            value={search}
            onChange={(event) => setSearch(event.target.value)}></input>

            <ul className='item-list'>
                {filteredItems.map(item => (
                    <li key={item.id}> 
                    <ItemListCell item={item} guessedIds={props.guessedIds} isLoading={props.isLoading} onCellClick={props.onCellClick}/> 
                    </li>))} 
            </ul>
        </div>
    )
}