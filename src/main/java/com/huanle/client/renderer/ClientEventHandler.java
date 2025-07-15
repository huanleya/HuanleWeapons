package com.huanle.client.renderer;

import com.huanle.HuanleMod;
import com.huanle.ModItems;
import com.huanle.ModEntities;
import com.huanle.ModEnchantments;
import com.huanle.items.weapons.EnderSword;
import com.huanle.client.renderer.entity.ZombieMinerRenderer;
import com.huanle.client.renderer.entity.SkeletonMinerRenderer;
import com.huanle.client.renderer.entity.FireballRenderer;
import com.huanle.entity.client.MysteriousMerchantRenderer;
import com.huanle.network.NetworkHandler;
import com.huanle.network.OpenEnderChestMessage;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.model.EntityModel;
import com.huanle.client.renderer.IceLayer;
import com.huanle.client.renderer.ManaBottleRenderer;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    

    private static float calculateDrawProgress(ItemStack stack, LivingEntity entity, int timeUsed) {

        int quickDrawLevel = stack.getEnchantmentLevel(ModEnchantments.QUICK_DRAW.get());
        int quickDrawBonus = quickDrawLevel * 6;
        

        int adjustedTimeUsed = timeUsed + quickDrawBonus;
        

        float standardDrawTime = 20.0F;
        

        float drawProgress = Math.min(adjustedTimeUsed / standardDrawTime, 1.0F);

        return drawProgress;
    }


    @Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientRuntimeEventHandler {

        @SubscribeEvent
        public static void onFOVUpdate(ComputeFovModifierEvent event) {

        }
        

        @SubscribeEvent
        public static void onClientTick(net.minecraftforge.event.TickEvent.ClientTickEvent event) {

            if (event.phase != net.minecraftforge.event.TickEvent.Phase.END) return;
            

            net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
            Player player = minecraft.player;
            

            if (player == null || minecraft.level == null) return;
            

            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offHandItem = player.getOffhandItem();
            
            boolean holdingEnderSword = mainHandItem.getItem() instanceof EnderSword || 
                                       offHandItem.getItem() instanceof EnderSword;
            
            if (holdingEnderSword) {

                if (player.level().getRandom().nextFloat() < 0.5f) {

                    double offsetX = (player.level().getRandom().nextDouble() - 0.5) * 0.5;
                    double offsetY = (player.level().getRandom().nextDouble() - 0.5) * 0.5;
                    double offsetZ = (player.level().getRandom().nextDouble() - 0.5) * 0.5;
                    

                    double x = player.getX() + offsetX;
                    double y = player.getY() + 1.0 + offsetY;
                    double z = player.getZ() + offsetZ;
                    

                    player.level().addParticle(
                        net.minecraft.core.particles.ParticleTypes.PORTAL,
                        x, y, z,
                        offsetX, offsetY, offsetZ
                    );
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelLayers.FOREST_ARMOR_INNER, () -> {
            MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0f);
            return LayerDefinition.create(meshdefinition, 64, 32);
        });
        event.registerLayerDefinition(ModelLayers.FOREST_ARMOR_OUTER, () -> {
            MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0f);
            return LayerDefinition.create(meshdefinition, 64, 32);
        });
    }

    private static Field field_EntityRenderersEvent$AddLayers_renderers = null;

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void attachRenderLayers(EntityRenderersEvent.AddLayers event) {
        if (field_EntityRenderersEvent$AddLayers_renderers == null) {
            try {
                field_EntityRenderersEvent$AddLayers_renderers = EntityRenderersEvent.AddLayers.class.getDeclaredField("renderers");
                field_EntityRenderersEvent$AddLayers_renderers.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (field_EntityRenderersEvent$AddLayers_renderers != null) {
            event.getSkins().forEach(renderer -> {
                LivingEntityRenderer<Player, EntityModel<Player>> skin = event.getSkin(renderer);
                attachIceRenderLayer(Objects.requireNonNull(skin));
            });
            try {
                ((Map<EntityType<?>, EntityRenderer<?>>) field_EntityRenderersEvent$AddLayers_renderers.get(event)).values().stream().
                        filter(LivingEntityRenderer.class::isInstance).map(LivingEntityRenderer.class::cast).forEach(ClientEventHandler::attachIceRenderLayer);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static <T extends LivingEntity, M extends EntityModel<T>> void attachIceRenderLayer(LivingEntityRenderer<T, M> renderer) {
        renderer.addLayer(new IceLayer<>(renderer));
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        event.enqueueWork(() -> {

            System.out.println("Registering ThrowingAxeRenderer for entity: " + ModEntities.THROWING_AXE.getId());
            EntityRenderers.register(ModEntities.THROWING_AXE.get(), 
                (context) -> new ThrowingAxeRenderer(context));
            

            EntityRenderers.register(ModEntities.FLAME_ARROW.get(), 
            (context) -> new FlameArrowRenderer(context));
            
            EntityRenderers.register(ModEntities.MANA_BOTTLE.get(), 
            (context) -> new ManaBottleRenderer(context));
            

            EntityRenderers.register(ModEntities.MOONLIGHT_ARC.get(),
            (context) -> new MoonlightArcRenderer(context));
            
            // Register miner entity renderers
            EntityRenderers.register(ModEntities.ZOMBIE_MINER.get(),
            (context) -> new ZombieMinerRenderer(context));
            
            EntityRenderers.register(ModEntities.SKELETON_MINER.get(),
            (context) -> new SkeletonMinerRenderer(context));
            
            // Register mysterious merchant renderer
            EntityRenderers.register(ModEntities.MYSTERIOUS_MERCHANT.get(),
            (context) -> new MysteriousMerchantRenderer(context));
            
            // Register fireball renderer
            EntityRenderers.register(ModEntities.FIREBALL.get(),
            (context) -> new FireballRenderer(context));


            net.minecraft.client.renderer.item.ItemProperties.register(ModItems.FLAME_BOW.get(), 
                ResourceLocation.parse("minecraft:pulling"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            

            net.minecraft.client.renderer.item.ItemProperties.register(net.minecraft.world.item.Items.BOW, 
                ResourceLocation.parse("minecraft:pulling"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            
            net.minecraft.client.renderer.item.ItemProperties.register(net.minecraft.world.item.Items.BOW, 
                ResourceLocation.parse("minecraft:pull"), (stack, level, entity, seed) -> {
                    if (entity == null) {
                        return 0.0F;
                    }
                    if (entity.getUseItem() != stack) {
                        return 0.0F;
                    }
                    

                    int useDuration = stack.getUseDuration();
                    int timeLeft = entity.getUseItemRemainingTicks();
                    int timeUsed = useDuration - timeLeft;
                    

                    return calculateDrawProgress(stack, entity, timeUsed);
                });
            
            net.minecraft.client.renderer.item.ItemProperties.register(ModItems.FLAME_BOW.get(), 
                ResourceLocation.parse("minecraft:pull"), (stack, level, entity, seed) -> {
                    if (entity == null) {
                        return 0.0F;
                    }
                    if (entity.getUseItem() != stack) {
                        return 0.0F;
                    }
                    

                    int useDuration = stack.getUseDuration();
                    int timeLeft = entity.getUseItemRemainingTicks();
                    int timeUsed = useDuration - timeLeft;


                    return calculateDrawProgress(stack, entity, timeUsed);
            });
        });
    }
}
