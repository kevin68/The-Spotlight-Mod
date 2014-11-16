package fr.mcnanotech.kevin_68.thespotlightmod.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLightSlotConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;

public class GuiHandler implements IGuiHandler
{

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileentity = world.getTileEntity(x, y, z);
        if(tileentity instanceof TileEntitySpotLight)
        {
            return new ContainerSpotLightSlotConfig((TileEntitySpotLight)tileentity, player.inventory, world);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileentity = world.getTileEntity(x, y, z);
        if(tileentity instanceof TileEntitySpotLight)
        {
            return new GuiSpotLight(player.inventory, (TileEntitySpotLight)tileentity, world);
        }
        return null;
    }
}