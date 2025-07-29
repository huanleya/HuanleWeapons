package com.huanle;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, HuanleMod.MOD_ID);

    /**
     * 魔力值上限属性
     */
    public static final RegistryObject<Attribute> MAX_MANA = ATTRIBUTES.register("max_mana",
            () -> new RangedAttribute("attribute.name.huanle.max_mana", 100.0D, 0.0D, 10000.0D).setSyncable(true));

    /**
     * 受伤减免属性
     */
    public static final RegistryObject<Attribute> DAMAGE_REDUCTION = ATTRIBUTES.register("damage_reduction",
            () -> new RangedAttribute("attribute.name.huanle.damage_reduction", 0.0D, 0.0D, 1.0D).setSyncable(true));

    /**
     * 抢夺等级属性
     */
    public static final RegistryObject<Attribute> LOOTING_LEVEL = ATTRIBUTES.register("looting_level",
            () -> new RangedAttribute("attribute.name.huanle.looting_level", 0.0D, 0.0D, 10.0D).setSyncable(true));

    /**
     * 时运等级属性
     */
    public static final RegistryObject<Attribute> FORTUNE_LEVEL = ATTRIBUTES.register("fortune_level",
            () -> new RangedAttribute("attribute.name.huanle.fortune_level", 0.0D, 0.0D, 10.0D).setSyncable(true));

    /**
     * 挖掘速度属性
     */
    public static final RegistryObject<Attribute> MINING_SPEED = ATTRIBUTES.register("mining_speed",
            () -> new RangedAttribute("attribute.name.huanle.mining_speed", 0.0D, 0.0D, 10.0D).setSyncable(true));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}