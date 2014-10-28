package fr.mcnanotech.kevin_68.thespotlightmod;

import fr.mcnanotech.kevin_68.thespotlightmod.blocks.TSMBlocks;
import fr.mcnanotech.kevin_68.thespotlightmod.client.render.tileentity.TileEntitySpotLightRender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.minecraftforgefrance.ffmtlibs.FFMTClientRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void register()
    {
        FFMTClientRegistry.bindTESRWithInventoryRender(TSMBlocks.spotlight, 0, TileEntitySpotLight.class, new TileEntitySpotLightRender());
    }
}