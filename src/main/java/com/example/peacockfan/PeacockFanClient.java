package com.example.peacockfan;

import com.example.peacockfan.item.PeacockFeatherFanItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.player.LocalPlayer;

public class PeacockFanClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            LocalPlayer player = client.player;
            if (player == null) return;

            if (player.onGround()) {
                PeacockFeatherFanItem.resetFanUsed();
            } else if (PeacockFeatherFanItem.isFallProtected()) {
                // 착지할 때까지 낙하 거리 계속 0으로 유지
                player.resetFallDistance();
            }
        });
    }
}