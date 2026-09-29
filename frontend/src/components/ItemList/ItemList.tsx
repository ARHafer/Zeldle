import { useState } from 'react';
import ItemListCell from './ItemListCell';
import type { Item } from '../../types/Item';

import './ItemList.css';

export default function ItemList(props: { items: Item[]; guessedIds: number[]; isLoading: boolean; onCellClick: (itemId: number) => void; } ) {

    const [search, setSearch] = useState('');
    const filteredItems = props.items.filter(item => item.name.toLowerCase().includes(search.toLowerCase()));

    if (props.isLoading && props.items.length == 0) {
        return (
            <div className='item-list-container'>

            <ul className='item-list'>
                <p>Loading item list, please wait...</p>
            </ul>

            <div className='item-list-searchbar-container'>
                <input className='item-list-searchbar'
                type='text'
                placeholder='Loading...'
                value={search}
                onChange={(event) => setSearch(event.target.value)}></input>
            </div>

        </div>
        )
    
    } else if (props.items.length == 0) {
        return (
            <div className='item-list-container'>

            <ul className='item-list'>
                <p>An error occured; No items were found!</p>
            </ul>

            <div className='item-list-searchbar-container'>
                <input className='item-list-searchbar'
                type='text'
                placeholder='Error!'
                value={search}
                onChange={(event) => setSearch(event.target.value)}></input>
            </div>

        </div>
        )
    
    }

    return (
        <div className='item-list-container'>

            <ul className='item-list'>
                {filteredItems.map(item => (
                    <li key={item.id}> 
                    <ItemListCell item={item} guessedIds={props.guessedIds} isLoading={props.isLoading} onCellClick={props.onCellClick}/> 
                    </li>))} 
            </ul>

            <div className='item-list-searchbar-container'>
                <input className='item-list-searchbar'
                type='text'
                placeholder='Search item list...'
                value={search}
                onChange={(event) => setSearch(event.target.value)}></input>
            </div>

        </div>
    )
}