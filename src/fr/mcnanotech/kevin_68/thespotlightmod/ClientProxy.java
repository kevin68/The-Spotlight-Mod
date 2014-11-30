package fr.mcnanotech.kevin_68.thespotlightmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import fr.mcnanotech.kevin_68.thespotlightmod.blocks.TSMBlocks;
import fr.mcnanotech.kevin_68.thespotlightmod.client.render.tileentity.TileEntitySpotLightRender;
import fr.mcnanotech.kevin_68.thespotlightmod.items.TSMItems;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;

public class ClientProxy extends CommonProxy
{
    @Override
    public void register()
    {//TODO
//        FFMTClientRegistry.bindTESRWithInventoryRender(TSMBlocks.spotlight, 0, TileEntitySpotLight.class, new TileEntitySpotLightRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpotLight.class, new TileEntitySpotLightRender());
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(TSMItems.configSaver, 0, new ModelResourceLocation(TheSpotLightMod.MODID + ":tsm_configsaver", "inventory"));
//        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(TSMBlocks.spotlight), 0, new ModelResourceLocation(TheSpotLightMod.MODID + ":tsm_spotlight", "inventory"));
    }
}