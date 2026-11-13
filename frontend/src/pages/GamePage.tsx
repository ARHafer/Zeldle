import ItemList from '../components/ItemList/ItemList';
import FeedbackGrid from '../components/FeedbackGrid/FeedbackGrid';
import useItems from '../hooks/useItems';
import useGuess from '../hooks/useGuess'
import useGame from '../hooks/useGame'

import './GamePage.css';

/* 
 * (The ever growing) TODO: 
 * Fix CSS scaling and border issues in both components (ugh...).
 * Improve error handling.
 * Add pop-ups for game win / loss.
 * Add health meter.
 * Add contact / bug-reporting functionality.
 * Finish FeedbackGrid styling (game release arrows & animations).
 * Beta styling & sound effects for rest of site.
 * Fix guess ordering.
 * Final debugging and code clean-up.
 * Redesign mobile UI.
 * Final revision for beta styling.
 * Beta release!!!
 */

function GamePage() {

  const { items, isItemListLoadedFromServer } = useItems();
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
  <main className='game-page'>

    <ItemList items={items} guessedIds={game?.guessedIds ?? []} isGuessLoading={isGuessLoading} isLoadedFromServer={isItemListLoadedFromServer} onCellClick={handleCellClick}/>
    <FeedbackGrid guessHistory={game?.guessHistory ?? []}/>

  </main>
  )
}

export default GamePage