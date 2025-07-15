package com.huanle.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.monster.Endermite;


public class NoHurtEnderPearl extends ThrownEnderpearl {

    public NoHurtEnderPearl(Level level, LivingEntity shooter) {
        super(level, shooter);
    }
    

    @Override
    protected void onHit(HitResult result) {

        if (this.level().isClientSide) {
            return;
        }

        LivingEntity owner = (LivingEntity) this.getOwner();
        if (owner instanceof ServerPlayer && this.random.nextFloat() < 0.05F && 
                this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            Endermite endermite = EntityType.ENDERMITE.create(this.level());
            if (endermite != null) {
                endermite.moveTo(owner.getX(), owner.getY(), owner.getZ(), owner.getYRot(), owner.getXRot());
                this.level().addFreshEntity(endermite);
            }
        }

        this.teleportThrower();

        this.discard();
    }
    

    @Override
    protected void onHitBlock(BlockHitResult result) {

    }
    

    @Override
    protected void onHitEntity(EntityHitResult result) {

    }
    

    private void teleportThrower() {
        LivingEntity thrower = (LivingEntity) this.getOwner();

        if (thrower instanceof ServerPlayer serverPlayer) {

            if (serverPlayer.isPassenger()) {
                serverPlayer.dismountTo(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ());
            }
            

            double destX = this.getX();
            double destY = this.getY();
            double destZ = this.getZ();
            

            serverPlayer.teleportTo(destX, destY, destZ);
            

            serverPlayer.fallDistance = 0.0F;
            

             this.level().playSound(null, destX, destY, destZ, 
                     net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT, 
                     net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
         }
     }
}
