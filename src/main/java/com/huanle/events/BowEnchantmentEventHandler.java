package com.huanle.events;

import com.huanle.HuanleMod;
import com.huanle.ModEnchantments;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = HuanleMod.MOD_ID)
public class BowEnchantmentEventHandler {


    @SubscribeEvent
    public static void onItemUseStart(LivingEntityUseItemEvent.Start event) {
        ItemStack stack = event.getItem();
        

        if (stack.getItem() instanceof BowItem) {

            int originalDuration = event.getDuration();
            int newDuration = originalDuration;
            

            int quickDrawLevel = stack.getEnchantmentLevel(ModEnchantments.QUICK_DRAW.get());
            
            if (quickDrawLevel > 0) {

                int reduction = quickDrawLevel * 6;
                

                newDuration = newDuration - reduction;
            }

            newDuration = Math.max(5, newDuration);

            event.setDuration(newDuration);
        }
    }
}
