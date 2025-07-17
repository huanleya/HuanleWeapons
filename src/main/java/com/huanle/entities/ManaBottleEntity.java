package com.huanle.entities;

import com.huanle.ModEntities;
import com.huanle.capabilities.ManaCapability;
import com.huanle.capabilities.IMana;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class ManaBottleEntity extends ThrowableItemProjectile {
    
    public ManaBottleEntity(EntityType<? extends ManaBottleEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    public ManaBottleEntity(Level level, LivingEntity shooter) {
        super(ModEntities.MANA_BOTTLE.get(), shooter, level);
    }
    
    public ManaBottleEntity(Level level, double x, double y, double z) {
        super(ModEntities.MANA_BOTTLE.get(), x, y, z, level);
    }
    
    @Override
    protected Item getDefaultItem() {
        return Items.EXPERIENCE_BOTTLE;
    }
    
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof Player player) {
            restoreMana(player);
        }
        this.discard();
    }
    
    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        
        if (!this.level().isClientSide) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 1.0F, 1.0F);
            if (this.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 20; i++) {
                    double offsetX = (this.random.nextDouble() - 0.5) * 2.0;
                    double offsetY = this.random.nextDouble() * 2.0;
                    double offsetZ = (this.random.nextDouble() - 0.5) * 2.0;
                    
                    serverLevel.sendParticles(ParticleTypes.ENCHANT,
                            this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                            1, 0.0, 0.0, 0.0, 0.1);
                }
            }
            this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(5.0))
                    .forEach(this::restoreMana);
        }
        
        this.discard();
    }
    
    private void restoreMana(Player player) {
        player.getCapability(ManaCapability.MANA_CAPABILITY).ifPresent(mana -> {
            float currentMana = mana.getMana();
            float maxMana = mana.getMaxMana();
            float restoreAmount = 10.0f;

            if (currentMana >= maxMana - 0.01f) {
                return;
            }

            float actualRestore = Math.min(restoreAmount, maxMana - currentMana);
            mana.restoreMana(actualRestore);
        });
    }
    
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}