package fr.mcnanotech.kevin_68.thespotlightmod;

import javax.annotation.Nullable;

import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockSpotLight extends Block {
	public BlockSpotLight() {
		super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(1.0F, 10.0F).lightValue(15));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntitySpotLight();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (!world.isRemote) {
			TSMJsonManager.generateNewFiles((ServerWorld)world, pos);
		}
		BlockState nState = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, nState, nState, 3);
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntitySpotLight) {
			TSMJsonManager.deleteFile((ServerWorld)world, pos);
		}
		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote) {
			return false;
		}
		TileEntity tileEntity = world.getTileEntity(pos);

		if (player.isSneaking()) {
			return false;
		}
		if (tileEntity instanceof TileEntitySpotLight) {
			TileEntitySpotLight tile = (TileEntitySpotLight) tileEntity;
			if (tile.locked && !player.getGameProfile().getId().equals(tile.lockerUUID)) {
				if (hand == Hand.MAIN_HAND) {
					player.sendMessage(new TranslationTextComponent("message.spotlight.locked.open"));
				}
				return false;
			}
			NetworkHooks.openGui((ServerPlayerEntity)player, tile, (buffer) -> {
                buffer.writeBlockPos(pos);
            });
			return true;
		}
		return false;
	}

	@Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return Block.makeCuboidShape(1.0D, 1.0D, 1.0D, 14.0D, 14.0D, 14.0D);
	}

	@Override
    public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	// isFullCube
	@Override
	public boolean func_220074_n(BlockState state) {
		return false;
	}
}
