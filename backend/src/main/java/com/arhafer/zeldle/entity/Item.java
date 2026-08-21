package com.arhafer.zeldle.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "items")
public class Item {

    @Id
    private int id;
    private String name;
    private Game game;
    private Purpose purpose;
    private Consumption consumption;
    private Acquisition acquisition;
    private Range range;
    private EnemyInteraction enemyInteraction;
    private ControlMode controlMode;

    public int getId() { return id; }
    public String getName() { return name; }
    public Game getGame() { return game; }
    public Purpose getPurpose() { return purpose; }
    public Consumption getConsumption() { return consumption; }
    public Acquisition getAcquisition() { return acquisition; }
    public Range getRange() { return range; }
    public EnemyInteraction getEnemyInteraction() { return enemyInteraction; }
    public ControlMode getControlMode() { return controlMode; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setGame(Game game) { this.game = game; }
    public void setPurpose(Purpose purpose) { this.purpose = purpose; }
    public void setConsumption(Consumption consumption) { this.consumption = consumption; }
    public void setAcquisition(Acquisition acquisition) {  this.acquisition = acquisition; }
    public void setRange(Range range) { this.range = range; }
    public void setEnemyInteraction(EnemyInteraction enemyInteraction) { this.enemyInteraction = enemyInteraction; }
    public void setControlMode(ControlMode controlMode) { this.controlMode = controlMode; }

    // Enums //
    public enum Game {

        OOT("Ocarina of Time", 1),
        MM("Majora's Mask", 2),
        WW("The Wind Waker", 3),
        TP("Twilight Princess", 4),
        SS("Skyward Sword", 5);

        private final String fullName;
        private final int releaseOrder;

        Game(String fullName, int releaseOrder) {
            this.fullName = fullName;
            this.releaseOrder = releaseOrder;
        }

        public String getFullName() { return fullName; }
        public int getReleaseOrder() { return releaseOrder; }
    }

    public enum Purpose { COMBAT, INTERACTION, TRAVERSAL }
    public enum Consumption { MATERIAL, MAGIC, NONE }
    public enum Acquisition { OVERWORLD, DUNGEON, PURCHASE }
    public enum Range { SELF, CLOSE, RANGED }
    public enum EnemyInteraction { DAMAGE, DISRUPT, NONE }
    public enum ControlMode { IMMEDIATE, TARGETED, PERSISTENT }

}
