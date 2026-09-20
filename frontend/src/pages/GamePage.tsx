import ItemList from '../components/ItemList/ItemList';
import FeedbackGrid from '../components/FeedbackGrid/FeedbackGrid';
import useItems from '../hooks/useItems';
import useGuess from '../hooks/useGuess'
import useGame from '../hooks/useGame'

function GamePage() {

  const { items, isItemListLoading } = useItems();
  const { guessItem, isGuessLoading } = useGuess();
  const { updateGameCache, game, isGameLoading } = useGame();

  async function handleCellClick(itemId: number): Promise<void> {
    if (isGameLoading || game == null || game.guessedIds.includes(itemId)) {
      return;
    }

    const guess = await guessItem(itemId)
 
    if (guess != null) {
      updateGameCache(itemId, guess);
    }
  }

  return (
  <>
  <ItemList items={items} guessedIds={game?.guessedIds ?? []} isLoading={isGuessLoading && isItemListLoading} onCellClick={handleCellClick}/>
  <FeedbackGrid guessHistory={game?.guessHistory ?? []}/>
  </>
  )
}

export default GamePage