package fr.mcnanotech.kevin_68.thespotlightmod.tileentity;

import net.minecraft.util.BlockPos;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMVec3;

public interface IReflector
{
	public TSMVec3 normal();

	public TSMVec3 normalRev();

	public BlockPos getBlockPos();
}