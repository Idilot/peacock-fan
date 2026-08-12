package com.example.peacockfan;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.peacockfan.item.ModItems;
import com.example.peacockfan.item.PeacockFeatherFanItem;

public class PeacockFan implements ModInitializer {
	public static final String MOD_ID = "peacock-fan";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		ModItems.initialize();

		// 인벤토리에 부채가 있으면 낙하 데미지 방지
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				if (player.onGround()) continue;

				boolean hasFan = false;
				for (ItemStack stack : player.getInventory()) {
					if (stack.getItem() instanceof PeacockFeatherFanItem) {
						hasFan = true;
						break;
					}
				}

				if (hasFan) {
					player.resetFallDistance();
				}
			}
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}