package fr.mcnanotech.kevin_68.thespotlightmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;

public class TSMBlocks
{
    public static Block spotlight;

    public static void initBlock()
    {
        spotlight = new BlockSpotLight().setHardness(1.0F).setResistance(10.0F).setBlockName("nanotech.spotlight").setCreativeTab(TheSpotLightMod.blockTab).setStepSound(Block.soundTypeMetal).setBlockTextureName(TheSpotLightMod.MODID + ":spotlight");
        GameRegistry.registerBlock(spotlight, ItemBlock.class, "NanotechSpotLight");
    }
}