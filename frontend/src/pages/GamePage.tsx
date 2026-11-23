import ItemList from '../components/ItemList/ItemList';
import FeedbackGrid from '../components/FeedbackGrid/FeedbackGrid';
import useItems from '../hooks/useItems';
import useGuess from '../hooks/useGuess'
import useGame from '../hooks/useGame'
import { usePreloadItemImages } from '../hooks/usePreloadImages.ts';

import './GamePage.css';

/* 
 * (The [still] ever growing) TODO: 
 * ADD FRONTEND DESIGN AND FEATURES TO THE PLANNING DOC SO I DON'T KEEP UPDATING THIS AS I THINK OF THINGS ;-;
 * Make feedback grid rows appear as user guesses.
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

  usePreloadItemImages(items);

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

    <FeedbackGrid guessHistory={game?.guessHistory ?? []}/>
    <ItemList items={items} guessedIds={game?.guessedIds ?? []} isGuessLoading={isGuessLoading} isLoadedFromServer={isItemListLoadedFromServer} onCellClick={handleCellClick}/>

  </main>
  )
}

export default GamePage