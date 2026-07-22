export type Result = 'CORRECT' | 'INCORRECT' | 'HIGHER' | 'LOWER' | 'EQUAL'

export interface Feedback {

    correct: boolean;
    name: Result;
    game: Result;
    gameReleaseDate: Result;
    purpose: Result;
    consumption: Result;
    acquisition: Result;
    range: Result;
    enemyInteraction: Result;
    controlMode: Result; 
}