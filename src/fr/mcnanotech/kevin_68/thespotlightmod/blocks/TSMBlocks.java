package fr.mcnanotech.kevin_68.thespotlightmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;

public class TSMBlocks
{
	public static Block spotlight;

	public static void initBlock()
	{
		spotlight = new BlockSpotLight().setHardness(1.0F).setResistance(10.0F).setUnlocalizedName("thespotlightmod.spotlight").setCreativeTab(TheSpotLightMod.tab).setStepSound(Block.soundTypeMetal);
		GameRegistry.registerBlock(spotlight, ItemBlock.class, "tsm_spotlight");
	}
}