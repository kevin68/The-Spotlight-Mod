package fr.mcnanotech.kevin_68.thespotlightmod.tileentity;

import cpw.mods.fml.common.registry.GameRegistry;

public class TSMTileEntity
{
    public static void registerTiles()
    {
        GameRegistry.registerTileEntity(TileEntitySpotLight.class, "NanotechMod_SpotLight");
    }
}