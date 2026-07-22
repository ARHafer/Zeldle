import { useEffect, useState } from 'react';
import { getItems } from '../api/itemApi';
import type { Item } from '../types/Item';
import ItemList from '../components/ItemList/ItemList';
import useGuess from '../hooks/useGuess'
import FeedbackGrid from '../components/FeedbackGrid/FeedbackGrid';
import type { GuessData } from '../types/GuessData';

function GamePage() {

  const [items, setItems] = useState<Item[]>([]);
  const { guess, isLoading, error } = useGuess();
  const [guessedIds, setGuessedIds] = useState<Set<number>>(new Set()); // Previously chosen items gray out and becoming unchooseable.
  const [guessHistory, setGuessHistory] = useState<GuessData[]>([])

  async function handleCellClick(itemId: number) {
    if (guessedIds.has(itemId)) {
      return;
    }

    const guessData = await guess(itemId)
 
    if (guessData != null) {
      setGuessedIds(new Set([...guessedIds, itemId]));
      setGuessHistory([...guessHistory, guessData])
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