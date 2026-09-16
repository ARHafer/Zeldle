export type Result = 'CORRECT' | 'INCORRECT' | 'TARGET_HIGHER' | 'TARGET_LOWER' | 'EQUAL'

export interface Results {
    correct: boolean
    name: Result
    game: Result
    gameReleaseDate: Result
    purpose: Result
    consumption: Result
    acquisition: Result
    range: Result
    enemyInteraction: Result
    controlMode: Result
}