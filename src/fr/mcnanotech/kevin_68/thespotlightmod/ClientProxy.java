package fr.mcnanotech.kevin_68.thespotlightmod;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import fr.mcnanotech.kevin_68.thespotlightmod.client.TileEntitySpotLightRender;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerModel()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TheSpotLightMod.spotlight), 0, new ModelResourceLocation(TheSpotLightMod.MODID + ":tsm_spotlight", "inventory"));
        ModelLoader.setCustomModelResourceLocation(TheSpotLightMod.configSaver, 0, new ModelResourceLocation(TheSpotLightMod.MODID + ":tsm_configsaver", "inventory"));
        ModelLoader.setCustomModelResourceLocation(TheSpotLightMod.configSaver_full, 0, new ModelResourceLocation(TheSpotLightMod.MODID + ":tsm_configsaver_full", "inventory"));
    }

    @Override
    public void registerRender()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpotLight.class, new TileEntitySpotLightRender());
    }
}