import { useEffect, useMemo, useRef, useState } from 'react';
import { useKeyboardControls } from '../../hooks/useKeyboardControls';
import ItemListCell from './ItemListCell';
import type { Item } from '../../types/Item';

import './ItemList.css';

const SCROLL_LENGTH = 280; // In pixels. Probably gonna have to tweak this a lot, so I made it a constant.

export default function ItemList(props: { items: Item[]; guessedIds: number[]; isGuessLoading: boolean; isLoadedFromServer: boolean; onCellClick: (itemId: number) => void; } ) {

    // Searching //
    const [search, setSearch] = useState('');

    // Negligible for performance I know, but I don't like the thought of an effect running every single render for no reason.
    const filteredItems: Item[] = useMemo(() => props.items.filter(item => item.name.toLowerCase().includes(search.toLowerCase())), [props.items, search]);
   
    
    // Scrolling //
    const listRef = useRef<HTMLUListElement>(null);
    const [atListStart, setAtListStart] = useState(true);
    const [atListEnd, setAtListEnd] = useState(false);

    function scrollList(direction: ('left' | 'right')): void {
        const list = listRef.current;
        if (!list) return;

        const scrollLength: number = direction === 'left' ? -SCROLL_LENGTH : SCROLL_LENGTH; // Because you can't just scroll right, apparently?

        list.scrollBy({
            left: scrollLength,
            behavior: 'smooth'
        });
    }

    useEffect(() => { // For disabling scroll buttons if the list cannot be scrolled any further.
        const list = listRef.current;
        if (!list) return;

        function updateScrollBoundaries(): void {
            if (!list) return;

            const distanceFromStart = list.scrollLeft;
            const distanceFromEnd = (list.scrollWidth - list.clientWidth) - distanceFromStart;

            setAtListStart(distanceFromStart <= 0);
            setAtListEnd(distanceFromEnd <= 0);
        }

        const observer = new ResizeObserver(updateScrollBoundaries);
        observer.observe(list); // Updates the scroll boundaries if the window is resized.

        updateScrollBoundaries();
        list.addEventListener('scroll', updateScrollBoundaries, { passive: true });

        return() => { 
            list.removeEventListener('scroll', updateScrollBoundaries);
            observer.disconnect();
        }
    }, [filteredItems.length]);


    // Keyboard Controls //
    const searchbarRef = useRef<HTMLInputElement>(null);
    const [searchbarFocused, setSearchbarFocused] = useState(false);
    const [keyboardMode, setKeyboardMode] = useState(false);
    const { highlightedIndex, setHighlightedIndex } = useKeyboardControls(filteredItems.length, listRef, searchbarRef);

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
        const keyPressed = () => setKeyboardMode(true);
        const mouseMoved = () => setKeyboardMode(false);

        window.addEventListener('keydown', keyPressed);
        window.addEventListener('mousemove', mouseMoved);

        return() => {
            window.removeEventListener('keydown', keyPressed);
            window.removeEventListener('mousemove', mouseMoved);
        }
    }, []);


    // Display List //
    const isListEmpty: boolean = props.items.length === 0;
    const listStatus = getListStatus(isListEmpty, props.isLoadedFromServer);
    const searchDisabled: boolean = listStatus !== 'READY';
    const showErrorMessage: boolean = listStatus === 'LOADING_EMPTY' || listStatus === 'ERROR';

    return (
        <div className='item-list-wrapper'>

            <div className='item-list-container'>

                <ul className={`item-list${keyboardMode && !searchbarFocused ? ' keyboard-mode' : ''}`}
                ref={listRef}>

                    {showErrorMessage ? (

                    <li className='item-list-error-text'>
                        {listStatus === 'LOADING_EMPTY' ? 'Now loading, if this is taking a while, please refresh the page or try again later.'
                        : "An error occurred, and the item list couldn't be loaded. Please try again later."}
                    </li>

                    ) : (

                    filteredItems.map((item, idx) => (
                        <li key={item.id}
                        className={keyboardMode && !searchbarFocused && idx === highlightedIndex ? 'highlighted' : ''}> 
                        <ItemListCell item={item} guessedIds={props.guessedIds} isGuessLoading={props.isGuessLoading} isLoadedFromServer={props.isLoadedFromServer} onCellClick={props.onCellClick}/>
                        </li>))
                    )}

                </ul>

            </div>

            <div className='item-list-navigation'>

                <button className='item-list-scroll-button'
                onClick={() => scrollList('left')}
                disabled={showErrorMessage || atListStart}>←</button>

                    <div className={`item-list-searchbar-container${searchDisabled ? ' no-hover' : ''}`}>

                        <input className='item-list-searchbar'
                        ref={searchbarRef}
                        type='text'
                        placeholder={getSearchbarPlaceholder(listStatus)}
                        value={search}
                        disabled={searchDisabled}
                        onChange={(event) => setSearch(event.target.value)}
                        onFocus={() => setSearchbarFocused(true)}
                        onBlur={() => setSearchbarFocused(false)}></input>

                    </div>

                  <button className='item-list-scroll-button' 
                onClick={() => scrollList('right')}
                disabled={showErrorMessage || atListEnd}>→</button>

            </div>

        </div>
    )
}

type ListStatus = 'READY' | 'LOADING_FULL' | 'LOADING_EMPTY' | 'ERROR'

function getListStatus(listEmpty: boolean, loadedFromServer: boolean): ListStatus {
    if (listEmpty && !loadedFromServer) return 'LOADING_EMPTY'; // If game isn't loaded and cache is empty, display loading message.
    if (!listEmpty && !loadedFromServer) return 'LOADING_FULL'; // If game isn't loaded but cache is populated, display list with disabled searchbar, cells, & no hover animations.
    if (listEmpty && loadedFromServer) return 'ERROR'; // If the list is loaded from the server but no items are found, give an error message.
    else return 'READY';
}

function getSearchbarPlaceholder(status: ListStatus): string {
    if (status === 'LOADING_EMPTY' || status === 'LOADING_FULL') return 'Loading, please wait...';
    if (status === 'ERROR') return 'An error occurred!';
    else return 'Search item list...';
}