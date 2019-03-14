package fr.mcnanotech.kevin_68.thespotlightmod;

import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockSpotLight extends Block {
	public BlockSpotLight() {
		super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(1.0F, 10.0F));
		// TODO: this.setLightLevel(1.0F);
	}

	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
		return new TileEntitySpotLight();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (!world.isRemote) {
			TSMJsonManager.generateNewFiles(placer.dimension, pos);
		}
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntitySpotLight) {
			TileEntitySpotLight tile = (TileEntitySpotLight) te;
			tile.dimension = placer.dimension;
		}
		IBlockState nState = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, nState, nState, 3);
	}

	@Override
	public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving) {
		if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntitySpotLight) {
			TileEntitySpotLight te = (TileEntitySpotLight) world.getTileEntity(pos);
			TSMJsonManager.deleteFile(te.dimension, pos);
		}
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return false;
		}
		TileEntity tileentity = world.getTileEntity(pos);

		if (player.isSneaking()) {
			return false;
		}
		if (tileentity instanceof TileEntitySpotLight) {
			TileEntitySpotLight tile = (TileEntitySpotLight) tileentity;
			if (tile.locked && !player.getGameProfile().getId().toString().equals(tile.lockerUUID)) {
				if (hand == EnumHand.MAIN_HAND) {
					player.sendMessage(new TextComponentString(I18n.format("message.spotlight.locked.open")));
				}
				return false;
			}
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
            NetworkHooks.openGui(entityPlayerMP, (IInteractionObject)tileentity, buf -> buf.writeBlockPos(pos));
			return true;
		}
		return false;
	}

	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return Block.makeCuboidShape(1.0D, 1.0D, 1.0D, 14.0D, 14.0D, 14.0D);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
}