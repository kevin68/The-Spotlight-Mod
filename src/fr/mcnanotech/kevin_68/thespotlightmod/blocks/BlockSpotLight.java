package fr.mcnanotech.kevin_68.thespotlightmod.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.minecraftforgefrance.ffmtlibs.FFMTClientRegistry;

public class BlockSpotLight extends BlockContainer
{
    public BlockSpotLight()
    {
        super(Material.iron);
        this.setLightLevel(1.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TileEntitySpotLight();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase living, ItemStack stack)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te instanceof TileEntitySpotLight)
        {
            TileEntitySpotLight tile = (TileEntitySpotLight)te;
            tile.setDefaultValue();
        }
        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntity tileentity = world.getTileEntity(x, y, z);

        if(tileentity == null || player.isSneaking())
        {
            return false;
        }
        else
        {
            player.openGui(TheSpotLightMod.modInstance, 0, world, x, y, z);
            return true;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta)
    {
        return Blocks.stone.getBlockTextureFromSide(0);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return FFMTClientRegistry.tesrRenderId;
    }
}