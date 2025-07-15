package com.huanle.capabilities;

import com.huanle.ModAttributes;
import com.huanle.ModEnchantments;
import com.huanle.enchantments.ManaMasteryEnchantment;
import com.huanle.network.ManaUpdatePacket;
import com.huanle.network.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.network.PacketDistributor;

public class ManaUtils {

    public static IMana getMana(Player player) {
        return player.getCapability(ManaCapability.MANA_CAPABILITY).orElse(null);
    }

    public static boolean hasMana(Player player, float amount) {
        IMana mana = getMana(player);
        return mana != null && mana.hasMana(amount);
    }

    public static boolean consumeMana(Player player, float amount) {
        IMana mana = getMana(player);
        if (mana != null && mana.consumeMana(amount)) {
            syncManaToClient(player);
            return true;
        }
        return false;
    }

    public static boolean consumeManaWithEnchantment(Player player, float amount, ItemStack itemStack) {
        int manaMasteryLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.MANA_MASTERY.get(), itemStack);

        float reducedAmount = Math.max(0.0f, amount - ManaMasteryEnchantment.getManaReduction(manaMasteryLevel));

        if (reducedAmount <= 0.0f) {
            return true;
        }
        
        return consumeMana(player, reducedAmount);
    }

    public static void restoreMana(Player player, float amount) {
        IMana mana = getMana(player);
        if (mana != null) {
            mana.restoreMana(amount);
            syncManaToClient(player);
        }
    }

    public static void setMana(Player player, float amount) {
        IMana mana = getMana(player);
        if (mana != null) {
            mana.setMana(amount);
            syncManaToClient(player);
        }
    }

    public static float getCurrentMana(Player player) {
        IMana mana = getMana(player);
        return mana != null ? mana.getMana() : 0;
    }

    public static float getMaxMana(Player player) {
        IMana mana = getMana(player);
        return mana != null ? mana.getMaxMana() : 0;
    }

    public static float getMaxManaWithAttributes(Player player) {
        IMana mana = getMana(player);
        if (mana != null) {
            float baseMana = mana.getMaxMana();
            double attributeMana = player.getAttributeValue(ModAttributes.MAX_MANA.get());
            return (float) attributeMana;
        }
        return 0;
    }

    public static void setMaxMana(Player player, float amount) {
        IMana mana = getMana(player);
        if (mana != null) {
            mana.setMaxMana(amount);
            syncManaToClient(player);
        }
    }

    public static float getManaRegenRate(Player player) {
        IMana mana = getMana(player);
        return mana != null ? mana.getManaRegenRate() : 0;
    }

    public static void setManaRegenRate(Player player, float rate) {
        IMana mana = getMana(player);
        if (mana != null) {
            mana.setManaRegenRate(rate);
            syncManaToClient(player);
        }
    }

    public static float getManaPercentage(Player player) {
        IMana mana = getMana(player);
        if (mana != null && mana.getMaxMana() > 0) {
            return mana.getMana() / mana.getMaxMana();
        }
        return 0;
    }

    public static void syncManaToClient(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            IMana mana = getMana(player);
            if (mana != null) {
                NetworkHandler.sendToPlayer(
                    new ManaUpdatePacket(mana.getMana(), mana.getMaxMana(), mana.getManaRegenRate()),
                    serverPlayer
                );
            }
        }
        else if (player.level().isClientSide) {
            IMana mana = getMana(player);
            if (mana != null) {
            }
        }
    }

    public static boolean isManaFull(Player player) {
        IMana mana = getMana(player);
        return mana != null && mana.getMana() >= mana.getMaxMana();
    }

    public static boolean isManaEmpty(Player player) {
        IMana mana = getMana(player);
        return mana == null || mana.getMana() <= 0;
    }
}