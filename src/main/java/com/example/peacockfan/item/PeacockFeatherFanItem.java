package com.example.peacockfan.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PeacockFeatherFanItem extends Item {

    private static final float LOOK_DOWN_ANGLE = 60.0F;

    private static boolean fanUsed = false;
    private static boolean fallProtected = false;

    public static void resetFanUsed() {
        fanUsed = false;
        fallProtected = false;
    }

    public static boolean isFallProtected() {
        return fallProtected;
    }

    public PeacockFeatherFanItem(Properties properties) {
        super(properties);
    }

    private boolean lookingDown(Player player) {
        return player.getXRot() > LOOK_DOWN_ANGLE;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean aimDown = lookingDown(player);

        level.playSound(null, player.blockPosition(),
                SoundEvents.PHANTOM_FLAP, SoundSource.PLAYERS,
                1.0F + level.getRandom().nextFloat(),
                level.getRandom().nextFloat() * 0.7F + 0.3F);

        if (!level.isClientSide()) {
            if (!aimDown) {
                doFan(level, player);
            }
            return InteractionResult.SUCCESS;
        }

        // --- 클라이언트 ---
        boolean canJump = aimDown && !player.isSwimming() && !fanUsed;

        if (player.isFallFlying()) {
            Vec3 look = player.getLookAngle();
            Vec3 movement = player.getDeltaMovement();
            player.setDeltaMovement(movement.add(
                    look.x() * 0.1D + (look.x() * 2.0D - movement.x()) * 0.5D,
                    (look.y() * 0.1D + (look.y() * 2.0D - movement.y()) * 0.5D) + 1.25D,
                    look.z() * 0.1D + (look.z() * 2.0D - movement.z()) * 0.5D));
            fallProtected = true;
        }

        if (canJump) {
            player.setDeltaMovement(new Vec3(
                    player.getDeltaMovement().x() * 1.05F,
                    1.5F,
                    player.getDeltaMovement().z() * 1.05F));
            fanUsed = true;
            fallProtected = true;
        }

        // 공중에서 쓰면 언제든 낙하 데미지 방어
        if (!player.onGround()) {
            fallProtected = true;
        }

        return InteractionResult.SUCCESS;
    }

    private AABB getEffectAABB(Player player) {
        double range = 3.0D;
        double radius = 2.0D;
        Vec3 srcVec = new Vec3(player.getX(),
                player.getY() + player.getEyeHeight(),
                player.getZ());
        Vec3 lookVec = player.getLookAngle().scale(range);
        Vec3 destVec = srcVec.add(lookVec.x(), lookVec.y(), lookVec.z());

        return new AABB(
                destVec.x() - radius, destVec.y() - radius, destVec.z() - radius,
                destVec.x() + radius, destVec.y() + radius, destVec.z() + radius);
    }

    private void doFan(Level level, Player player) {
        AABB fanBox = getEffectAABB(player);
        Vec3 moveVec = player.getLookAngle().scale(2);

        for (Entity entity : level.getEntitiesOfClass(Entity.class, fanBox)) {
            if (entity == player) continue;

            if (entity.isPushable() || entity instanceof ItemEntity || entity instanceof Projectile) {
                entity.setDeltaMovement(moveVec.x(), moveVec.y(), moveVec.z());
                entity.hurtMarked = true;
            }
        }
    }
}