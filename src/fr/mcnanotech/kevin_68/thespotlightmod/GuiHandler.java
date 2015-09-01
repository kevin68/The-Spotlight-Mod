package fr.mcnanotech.kevin_68.thespotlightmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightTextures;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLightConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLightTextures;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));
        if(tileentity instanceof TileEntitySpotLight)
        {
            switch(id)
            {
            case 1:
            {
                return new ContainerSpotLightTextures((TileEntitySpotLight)tileentity, player.inventory);
            }
            case 2:
            {
                return new ContainerSpotLightConfig((TileEntitySpotLight)tileentity, player.inventory);
            }
            default:
            {
                return new ContainerSpotLight((TileEntitySpotLight)tileentity, player.inventory, world, 8);
            }
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));
        if(tileentity instanceof TileEntitySpotLight)
        {
            switch(id)
            {
            case 1:
            {
                return new GuiSpotLightTextures(player.inventory, (TileEntitySpotLight)tileentity, world);
            }
            case 2:
            {
                return new GuiSpotLightConfig(player.inventory, (TileEntitySpotLight)tileentity, world);
            }
            default:
                return new GuiSpotLight(player.inventory, (TileEntitySpotLight)tileentity, world);
            }
        }
        return null;
    }
}