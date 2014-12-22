package fr.mcnanotech.kevin_68.thespotlightmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import fr.mcnanotech.kevin_68.thespotlightmod.client.render.tileentity.TileEntityReflectorRender;
import fr.mcnanotech.kevin_68.thespotlightmod.client.render.tileentity.TileEntitySpotLightRender;
import fr.mcnanotech.kevin_68.thespotlightmod.items.TSMItems;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntityReflector;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;

public class ClientProxy extends CommonProxy
{
	@Override
	public void register()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpotLight.class, new TileEntitySpotLightRender());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReflector.class, new TileEntityReflectorRender());

		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(TSMItems.configSaver, 0, new ModelResourceLocation(TheSpotLightMod.MODID + ":tsm_configsaver", "inventory"));
	}
}