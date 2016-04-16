package fr.mcnanotech.kevin_68.thespotlightmod;

import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockSpotLight extends BlockContainer
{
    public BlockSpotLight()
    {
        super(Material.IRON);
        this.setLightLevel(1.0F);
        this.setSoundType(SoundType.METAL);
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
            TSMJsonManager.generateNewFiles(placer.dimension, pos);
        }
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntitySpotLight)
        {
            TileEntitySpotLight tile = (TileEntitySpotLight)te;
            tile.dimensionID = placer.dimension;
        }
        IBlockState nState = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, nState, nState, 3);
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tileentity = world.getTileEntity(pos);

        if(tileentity == null || player.isSneaking())
        {
            return false;
        }
        if(tileentity instanceof TileEntitySpotLight)
        {
            TileEntitySpotLight tile = (TileEntitySpotLight)tileentity;
            if(tile.locked && !player.getGameProfile().getId().toString().equals(tile.lockerUUID))
            {
                player.addChatMessage(new TextComponentString(I18n.format("message.spotlight.locked.open")));
                return false;
            }
            player.openGui(TheSpotLightMod.modInstance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}