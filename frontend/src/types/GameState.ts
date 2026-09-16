type GameStatus = 'WON' | 'LOST' | 'IN_PROGRESS'

export interface GameState {
    guessesRemaining: number
    gameStatus: GameStatus
}