package fr.mcnanotech.kevin_68.thespotlightmod;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import fr.mcnanotech.kevin_68.thespotlightmod.blocks.TSMBlocks;
import fr.mcnanotech.kevin_68.thespotlightmod.client.render.tileentity.TileEntitySpotLightRender;
import fr.mcnanotech.kevin_68.thespotlightmod.items.TSMItems;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TSMBlocks.spotlight), 0, new ModelResourceLocation(TheSpotLightMod.MODID + ":tsm_spotlight", "inventory"));
		ModelLoader.setCustomModelResourceLocation(TSMItems.configSaver, 0, new ModelResourceLocation(TheSpotLightMod.MODID + ":tsm_configsaver", "inventory"));
	}
	
	@Override
	public void registerRender()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpotLight.class, new TileEntitySpotLightRender());
	}
}