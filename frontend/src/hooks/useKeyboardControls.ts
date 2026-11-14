import { useEffect, useState, type RefObject } from "react";

export function useKeyboardControls(listLength: number, listRef: RefObject<HTMLUListElement | null>, searchbarRef: RefObject<HTMLInputElement | null>) {

    const [highlightedIndex, setHighlightedIndex] = useState(0);

    useEffect(() => {
        function navigateList(event: KeyboardEvent): void { 
            if (event.ctrlKey || event.altKey || event.metaKey) return; // The app acknowledging ctrl, alt, or windows key presses would feel pretty janky.
        
            if (document.activeElement === searchbarRef.current) {
                        
                if (event.key === 'ArrowUp' || event.key === 'ArrowRight' || event.key === 'ArrowLeft'  || event.key === 'Enter') {
                    event.preventDefault();
                    searchbarRef.current?.blur();
                } 
                
                return;
            }
        
            switch (event.key) {
                case 'ArrowRight':
                    event.preventDefault();
                    setHighlightedIndex(idx => Math.max(0, Math.min(idx + 1, listLength - 1)));
                    break;
                        
                case 'ArrowLeft':
                    event.preventDefault();
                    setHighlightedIndex(idx => Math.max(idx - 1, 0));
                    break;
        
                case 'ArrowDown':
                    event.preventDefault();
                    searchbarRef.current?.focus();
                    break;
        
                case 'Enter':
                    event.preventDefault();
        
                    const highlightedCell: Element | undefined = listRef.current?.children[highlightedIndex];
                    highlightedCell?.querySelector('button')?.click();
                    break;
        
                case 'Backspace': // Pressing backspace on a disabled cell caused the page to close, at least on FireFox.
                    event.preventDefault();
                    searchbarRef.current?.focus();
                    break;
        
                default:
                    if (event.key.length === 1) searchbarRef.current?.focus();
            }
        }

        window.addEventListener('keydown', navigateList);
        return () => window.removeEventListener('keydown', navigateList);
    }, [listLength, highlightedIndex]);

    return { highlightedIndex, setHighlightedIndex }
}