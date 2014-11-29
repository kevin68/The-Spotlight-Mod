package fr.mcnanotech.kevin_68.thespotlightmod.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;

public class TSMItems
{
    public static Item configSaver;

    public static void initItems()
    {
        configSaver = new ItemConfigSaver().setUnlocalizedName("thespotlightmod.configsaver").setCreativeTab(TheSpotLightMod.tab).setMaxStackSize(1);
        GameRegistry.registerItem(configSaver, "tsm_configsaver", TheSpotLightMod.MODID);
    }
}