package com.huanle;


import com.huanle.capabilities.ManaEventHandler;
import com.huanle.commands.ManaCommand;
import com.huanle.events.LifeRingEventHandler;
import com.huanle.network.NetworkHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

@Mod(HuanleMod.MOD_ID)
public class HuanleMod {
    public static final String MOD_ID = "huanle";

    public HuanleMod() {

        FMLModContainer modContainer = (FMLModContainer) ModList.get().getModContainerById(MOD_ID).orElseThrow();
        IEventBus modEventBus = modContainer.getEventBus();


        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModEntities.register(modEventBus);
        ModEnchantments.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModPotions.POTIONS.register(modEventBus);
        ModWorldGeneration.register(modEventBus);
        ModAttributes.register(modEventBus);
        modEventBus.addListener(ModBrewingRecipes::register);
        MinecraftForge.EVENT_BUS.register(ManaEventHandler.class);
        MinecraftForge.EVENT_BUS.register(LifeRingEventHandler.class);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::modifyEntityAttributes);

        net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(net.minecraftforge.api.distmarker.Dist.CLIENT, () -> () -> {
            try {
                Class.forName("com.huanle.client.ClientConfig").getMethod("register").invoke(null);
            } catch (Exception e) {

            }
        });

    }
    
    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.register();
            ModEntities.registerSpawnPlacements();
        });
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        ManaCommand.register(event.getServer().getCommands().getDispatcher());
    }
    
    private void modifyEntityAttributes(EntityAttributeModificationEvent event) {
         event.add(EntityType.PLAYER, ModAttributes.MAX_MANA.get());
     }
}