import { useEffect, useState } from 'react';
import { getItems } from '../api/itemApi';
import type { Item } from '../types/Item';
import ItemList from '../components/ItemList/ItemList';
import useGuess from '../hooks/useGuess'
import FeedbackGrid from '../components/FeedbackGrid/FeedbackGrid';
import useStorage from '../hooks/useStorage';

function GamePage() {

  const [items, setItems] = useState<Item[]>([]);
  const { guessItem, isLoading } = useGuess();
  const { storeGuess, guessedIds, guessHistory } = useStorage();

  async function handleCellClick(itemId: number) {
    if (guessedIds.has(itemId)) {
      return;
    }

    const guess = await guessItem(itemId)
 
    if (guess != null) {
      storeGuess(itemId, guess);
    }
  }

  useEffect(() => {
    getItems().then(setItems);
  }, [])

  return (
  <><ItemList items={items} guessedIds={guessedIds} isLoading={isLoading} onCellClick={handleCellClick} />
  <FeedbackGrid guessHistory={guessHistory}/></>
  )
}

export default GamePage