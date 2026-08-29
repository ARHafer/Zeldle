package com.arhafer.zeldle;

import com.arhafer.zeldle.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ZeldleApplicationTests {

	public static Item createItem(int id) {
		Item item = new Item();
		item.setId(id);
		item.setName("Test Item of Testing (For Testing)");
		item.setGame(Item.Game.OOT);
		item.setPurpose(Item.Purpose.COMBAT);
		item.setConsumption(Item.Consumption.MATERIAL);
		item.setAcquisition(Item.Acquisition.OVERWORLD);
		item.setRange(Item.Range.SELF);
		item.setEnemyInteraction(Item.EnemyInteraction.DAMAGE);
		item.setControlMode(Item.ControlMode.IMMEDIATE);
		return item;
	}

	@Test
	void contextLoads() {
	}

}
