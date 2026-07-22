type Purpose = 'COMBAT' | 'INTERACTION' | 'TRAVERSAL'
type Consumption = 'MATERIAL' | 'MAGIC' | 'NONE'
type Acquisition = 'OVERWORLD' | 'DUNGEON' | 'PURCHASE'
type Range = 'SELF' | 'CLOSE' | 'RANGED'
type EnemyInteraction = 'DAMAGE' | 'DISRUPT' | 'NONE'
type ControlMode = 'IMMEDIATE' | 'TARGETED' | 'PERSISTENT'

export interface ItemProperty {
    
    name: string
    game: string
    purpose: Purpose
    consumption: Consumption
    acquisition: Acquisition
    range: Range
    enemyInteraction: EnemyInteraction
    controlMode: ControlMode
}