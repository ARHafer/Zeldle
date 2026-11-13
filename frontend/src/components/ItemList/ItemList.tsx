import { useEffect, useMemo, useRef, useState } from 'react';
import { useKeyboardControls } from '../../hooks/useKeyboardControls';
import ItemListCell from './ItemListCell';
import type { Item } from '../../types/Item';

import './ItemList.css';

const SCROLL_LENGTH = 280; // In pixels. Probably gonna have to tweak this a lot, so I made it a constant.

export default function ItemList(props: { items: Item[]; guessedIds: number[]; isGuessLoading: boolean; isLoadedFromServer: boolean; onCellClick: (itemId: number) => void; } ) {

    // Searching //
    const [search, setSearch] = useState('');

    // I know that the item array isn't big enough for useMemo to have any noticible impact on performance, but I don't like the thought of an effect running every render for no reason.
    const filteredItems = useMemo(() => props.items.filter(item => item.name.toLowerCase().includes(search.toLowerCase())), [props.items, search]);
    
    // Scrolling //
    const listRef = useRef<HTMLUListElement>(null);
    const [atListStart, setAtListStart] = useState(true);
    const [atListEnd, setAtListEnd] = useState(false);

    function updateScrollBoundaries(): void { // For disabling scroll buttons if the list cannot be scrolled any further.
        const list = listRef.current;
        if (!list) return;

        const distanceFromStart = list.scrollLeft;
        const distanceFromEnd = (list.scrollWidth - list.clientWidth) - distanceFromStart;

        setAtListStart(distanceFromStart <= (SCROLL_LENGTH / 2));
        setAtListEnd(distanceFromEnd <= (SCROLL_LENGTH / 2));
    }

    function scrollList(direction: ('left' | 'right')): void {
        const list = listRef.current;
        if (!list) return;

        const scrollLength: number = direction === 'left' ? -SCROLL_LENGTH : SCROLL_LENGTH; // Because you can't just scroll right, apparently?

        list.scrollBy({
            left: scrollLength,
            behavior: 'smooth'
        });
    }

    useEffect(() => {
        const list = listRef.current;
        if (!list) return;

        updateScrollBoundaries();

        list.addEventListener('scroll', updateScrollBoundaries, { passive: true });
        return() => list.removeEventListener('scroll', updateScrollBoundaries);
    }, [filteredItems.length]);

    // Keyboard Controls //
    const searchbarRef = useRef<HTMLInputElement>(null);
    const [searchbarFocused, setSearchbarFocused] = useState(false);
    const [keyboardMode, setKeyboardMode] = useState(false);
    const { highlightedIndex, setHighlightedIndex } = useKeyboardControls(filteredItems, listRef, searchbarRef);
    

    useEffect(() => {
        listRef.current?.children[highlightedIndex]?.scrollIntoView({ 
            inline: 'center',
            block: 'nearest',
            behavior: 'smooth'
         })
    }, [highlightedIndex]);

    useEffect(() => { // Reset the highlighted cell if the user searches. (More error prevention more than it is QoL)
        setHighlightedIndex(0);
    }, [search]);

    useEffect(() => { // Multiple cells can't be highlighted with both the keyboard and mouse.
        window.addEventListener('keydown', () => setKeyboardMode(true));
        window.addEventListener('mousemove', () => setKeyboardMode(false));

        return() => {
            window.removeEventListener('keydown', () => setKeyboardMode(true));
            window.removeEventListener('mousemove', () => setKeyboardMode(false));
        }
    }, []);
    

    // If game isn't loaded and cache is empty, display loading message. //
    if (!props.isLoadedFromServer && props.items.length === 0) {
        return (
            <div className='item-list-wrapper'>

                <div className='item-list-container'>

                    <ul className='item-list'>
                        <span className='item-list-error-text'>Now loading, if this is taking a while, please refresh the page or try again later.</span>
                    </ul>

                </div>

                <div className='item-list-navigation'>

                    <button className='item-list-scroll-button'
                    onClick={() => scrollList('left')}
                    disabled>←</button>

                    <div className='item-list-searchbar-container no-hover'>

                        <input className='item-list-searchbar'
                        ref={searchbarRef}
                        type='text'
                        placeholder='Loading, please wait...'
                        value={search}
                        onChange={(event) => setSearch(event.target.value)}
                        disabled>
                        </input>

                    </div>

                    <button className='item-list-scroll-button' 
                    onClick={() => scrollList('right')}
                    disabled>→</button>

                </div>

            </div>
        )
    
    // If game isn't loaded but cache is populated, display list with disabled searchbar, cells, & no hover animations. //
    } else if (!props.isLoadedFromServer) {
        return (
            <div className='item-list-wrapper'>

                <div className='item-list-container'>

                    <ul className={`item-list${keyboardMode && !searchbarFocused ? ' keyboard-mode' : ''}`} 
                    ref={listRef}>
                        {filteredItems.map((item, idx) => (
                            <li key={item.id}
                            className={keyboardMode && !searchbarFocused && idx === highlightedIndex ? 'highlighted' : ''}> 
                            <ItemListCell item={item} guessedIds={props.guessedIds} isGuessLoading={props.isGuessLoading} isLoadedFromServer={props.isLoadedFromServer} onCellClick={props.onCellClick}/> 
                            </li>))}
                    </ul>

                </div>

                <div className='item-list-navigation'>

                    <button className='item-list-scroll-button'
                    onClick={() => scrollList('left')}
                    disabled={atListStart}>←</button>

                    <div className='item-list-searchbar-container'>

                        <input className='item-list-searchbar'
                        ref={searchbarRef}
                        type='text'
                        placeholder='Loading, please wait...'
                        value={search}
                        onChange={(event) => setSearch(event.target.value)}
                        onFocus={() => setSearchbarFocused(true)}
                        onBlur={() => setSearchbarFocused(false)}>
                        </input>

                    </div>

                    <button className='item-list-scroll-button' 
                    onClick={() => scrollList('right')}
                    disabled={atListEnd}>→</button>

                </div>

            </div>
        )
    
    // If the list is loaded from the server but no items are found, give an error message. //
    } else if (props.isLoadedFromServer && props.items.length === 0) {
        return (
            <div className='item-list-wrapper'>

                <div className='item-list-container'>

                    <ul className='item-list'>
                        <span className='error-text'>The item list could not be loaded. Please try again later.</span>
                    </ul>

                </div>

                <div className='item-list-navigation'>

                    <button className='item-list-scroll-button'
                    onClick={() => scrollList('left')}
                    disabled>←</button>

                    <div className='item-list-searchbar-container no-hover'>

                        <input className='item-list-searchbar'
                        ref={searchbarRef}
                        type='text'
                        placeholder='Error!'
                        value={search}
                        onChange={(event) => setSearch(event.target.value)}
                        disabled></input>

                    </div>

                    <button className='item-list-scroll-button' 
                    onClick={() => scrollList('right')}
                    disabled>→</button>

                </div>

            </div>
        ) 

    // No errors //
    } else {
        return (
            <div className='item-list-wrapper'>

                <div className='item-list-container'>

                    <ul className={`item-list${keyboardMode && !searchbarFocused ? ' keyboard-mode' : ''}`}
                    ref={listRef}>
                        {filteredItems.map((item, idx) => (
                            <li key={item.id}
                            className={keyboardMode && !searchbarFocused && idx === highlightedIndex ? 'highlighted' : ''}> 
                            <ItemListCell item={item} guessedIds={props.guessedIds} isGuessLoading={props.isGuessLoading} isLoadedFromServer={props.isLoadedFromServer} onCellClick={props.onCellClick}/> 
                            </li>))}
                    </ul>

                </div>

                <div className='item-list-navigation'>

                    <button className='item-list-scroll-button'
                    onClick={() => scrollList('left')}
                    disabled={atListStart}>←</button>

                    <div className='item-list-searchbar-container'>

                        <input className='item-list-searchbar'
                        ref={searchbarRef}
                        type='text'
                        placeholder='Search item list...'
                        value={search}
                        onChange={(event) => setSearch(event.target.value)}
                        onFocus={() => setSearchbarFocused(true)}
                        onBlur={() => setSearchbarFocused(false)}></input>

                    </div>

                    <button className='item-list-scroll-button' 
                    onClick={() => scrollList('right')}
                    disabled={atListEnd}>→</button>

                </div>

            </div>
        )
    }
}

/*
 * Hello, welcome to the Rant Zone™.
 * Actual error I got: "The '^' operator is not allowed for boolean types. Consider using '!==' instead."
 * XOR isn't allowed for booleans... WHEN ELSE WOULD IT BE ALLOWED? Is it literally exclusive to binary? Like, does it return an integer '1' or '0' only?
 * Look, I can understand that '!==' might be a little more readable, but outright disallowing it?!
 * The funny thing is, the program still works with the '^'. VSCode just highlights it red and says its disallowed. Weird.
 */