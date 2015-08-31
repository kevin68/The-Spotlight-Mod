package fr.mcnanotech.kevin_68.thespotlightmod.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;

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
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if(!world.isRemote)
        {
            TSMJsonManager.generateNewFile(placer.dimension, pos);
        }
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntitySpotLight)
        {
            TileEntitySpotLight tile = (TileEntitySpotLight)te;
            tile.dimensionID = placer.dimension;
        }
        world.markBlockForUpdate(pos);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        if(!world.isRemote)
        {
            if(world.getTileEntity(pos) instanceof TileEntitySpotLight)
            {
                TileEntitySpotLight te = (TileEntitySpotLight)world.getTileEntity(pos);
                TSMJsonManager.deleteFile(te.dimensionID, pos);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tileentity = world.getTileEntity(pos);

        if(tileentity == null || player.isSneaking())
        {
            return false;
        }
        player.openGui(TheSpotLightMod.modInstance, 0, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return 2;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }
}