package com.arhafer.zeldle.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "items")
public class Item {

    @Id
    private Integer id;
    private String name;
    private Game game;
    private Purpose purpose;
    private Consumption consumption;
    private Acquisition acquisition;
    private Range range;
    private EnemyInteraction enemyInteraction;
    private ControlMode controlMode;

    public Integer getId() { return id; }
    public String getName() { return name; }
    public Game getGame() { return game; }
    public Purpose getPurpose() { return purpose; }
    public Consumption getConsumption() { return consumption; }
    public Acquisition getAcquisition() { return acquisition; }
    public Range getRange() { return range; }

    @JsonProperty("enemy_interaction")
    public EnemyInteraction getEnemyInteraction() { return enemyInteraction; }

    @JsonProperty("control_mode")
    public ControlMode getControlMode() { return controlMode; }

    public void setId(Integer id) { this.id = id; }
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

        OOT("Ocarina of Time"),
        MM("Majora's Mask"),
        WW("The Wind Waker"),
        TP("Twilight Princess"),
        SS("Skyward Sword");

        private final String fullName;

        Game(String fullName) {
            this.fullName = fullName;
        }

        public String getFullName() { return fullName; }
    }

    public enum Purpose { COMBAT, INTERACTION, TRAVERSAL }
    public enum Consumption { MATERIAL, MAGIC, NONE }
    public enum Acquisition { OVERWORLD, DUNGEON, PURCHASE }
    public enum Range { SELF, CLOSE, RANGED }
    public enum EnemyInteraction { DAMAGE, DISRUPT, NONE }
    public enum ControlMode { IMMEDIATE, AIMED, PERSISTENT }

}
