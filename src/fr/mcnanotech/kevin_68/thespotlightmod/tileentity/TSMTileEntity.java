package fr.mcnanotech.kevin_68.thespotlightmod.tileentity;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class TSMTileEntity
{
	public static void registerTiles()
	{
		GameRegistry.registerTileEntity(TileEntitySpotLight.class, "TheSpotLightMod_SpotLight");
		GameRegistry.registerTileEntity(TileEntityReflector.class, "TheSpotLightMod_Reflector");
	}
}