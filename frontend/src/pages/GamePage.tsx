import { useEffect, useState } from 'react';
import { getItems } from '../api/itemApi';
import type { Item } from '../types/Item';
import ItemList from '../components/ItemList';
import useGuess from '../hooks/useGuess'

function GamePage() {

  const [items, setItems] = useState<Item[]>([]);
  const { guess, result, isLoading, error } = useGuess();
  const [guessedIds, setGuessedIds] = useState<Set<number>>(new Set()); // Previously chosen items gray out and becoming unchooseable.


  function handleCellClick(itemId: number) {
    if (guessedIds.has(itemId)) {
      return;
    }

    guess(itemId);
    setGuessedIds(new Set([...guessedIds, itemId]));
  }

  useEffect(() => {
    getItems().then(setItems);
  }, [])

  return (
  <ItemList items={items} guessedIds={guessedIds} isLoading={isLoading} onCellClick={handleCellClick}/>
  )
}

export default GamePage