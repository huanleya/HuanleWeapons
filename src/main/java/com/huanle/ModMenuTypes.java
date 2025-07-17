package com.huanle;

import com.huanle.menus.GrinderMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 菜单类型注册类
 */
public class ModMenuTypes {
    
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, HuanleMod.MOD_ID);
    
    public static final RegistryObject<MenuType<GrinderMenu>> GRINDER_MENU =
            registerMenuType("grinder_menu", GrinderMenu::new);
    
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(
            String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
    
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}