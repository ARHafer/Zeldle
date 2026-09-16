import type { Feedback } from "./Feedback";
import type { GameState } from "./GameState";

export interface Guess {
    gameState: GameState
    feedback: Feedback
}