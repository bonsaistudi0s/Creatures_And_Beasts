package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.menus.CinderFurnaceMenu;
import com.helliongames.hellionsapi.registration.holders.MenuDataHolder;
import com.helliongames.hellionsapi.registration.registries.HellionsAPIMenuRegistry;

public class CNBMenuModule {

    public static final HellionsAPIMenuRegistry MENU_TYPES = new HellionsAPIMenuRegistry(CreaturesAndBeastsConstants.MOD_ID);

    public static final MenuDataHolder<CinderFurnaceMenu> CINDER_FURNACE_MENU = MENU_TYPES.register(
            "cinder_furnace_container",
            MenuDataHolder.of(CinderFurnaceMenu::new)
    );

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
