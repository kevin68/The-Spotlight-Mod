package fr.mcnanotech.kevin_68.thespotlightmod;

import cpw.mods.fml.client.registry.ClientRegistry;
import fr.mcnanotech.kevin_68.thespotlightmod.client.render.tileentity.TileEntitySpotLightRender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;

public class ClientProxy extends CommonProxy
{
    @Override
    public void register()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpotLight.class, new TileEntitySpotLightRender());
    }
}