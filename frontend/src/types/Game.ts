import type { Feedback } from "./Feedback"
import type { GameState } from "./GameState"

export interface Game {
    date: string
    gameState: GameState
    guessedIds: number[]
    guessHistory: Feedback[]
}