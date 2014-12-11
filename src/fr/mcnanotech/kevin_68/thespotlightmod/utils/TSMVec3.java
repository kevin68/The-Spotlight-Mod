package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TSMVec3
{
	public double xCoord;
	public double yCoord;
	public double zCoord;

	public TSMVec3(double x, double y, double z)
	{
		if(x == -0.0D)
		{
			x = 0.0D;
		}

		if(y == -0.0D)
		{
			y = 0.0D;
		}

		if(z == -0.0D)
		{
			z = 0.0D;
		}

		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
	}

	protected TSMVec3 set(double x, double y, double z)
	{
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		return this;
	}

	public void rotateAroundX(float angle)
	{
		float f1 = MathHelper.cos(angle);
		float f2 = MathHelper.sin(angle);
		double d0 = this.xCoord;
		double d1 = this.yCoord * (double)f1 + this.zCoord * (double)f2;
		double d2 = this.zCoord * (double)f1 - this.yCoord * (double)f2;
		this.set(d0, d1, d2);
	}

	public void rotateAroundY(float angle)
	{
		float f1 = MathHelper.cos(angle);
		float f2 = MathHelper.sin(angle);
		double d0 = this.xCoord * (double)f1 + this.zCoord * (double)f2;
		double d1 = this.yCoord;
		double d2 = this.zCoord * (double)f1 - this.xCoord * (double)f2;
		this.set(d0, d1, d2);
	}

	public void rotateAroundZ(float angle)
	{
		float f1 = MathHelper.cos(angle);
		float f2 = MathHelper.sin(angle);
		double d0 = this.xCoord * (double)f1 + this.yCoord * (double)f2;
		double d1 = this.yCoord * (double)f1 - this.xCoord * (double)f2;
		double d2 = this.zCoord;
		this.set(d0, d1, d2);
	}

	public double norm()
	{
		return Math.sqrt(xCoord * xCoord + yCoord * yCoord + zCoord * zCoord);
	}

	public void getLaserBlocksCrossedByVector(World world, int xB, int yB, int zB)
	{
		double x = xCoord / norm();
		double y = yCoord / norm();
		double z = zCoord / norm();

		for(int i = 0; i < norm() + 1; i++)
		{
			int cX = MathHelper.floor_double(xB + x * i + 0.5);
			int cY = MathHelper.floor_double(yB + y * i + 0.5);
			int cZ = MathHelper.floor_double(zB + z * i + 0.5);
			if(cX != xB || cY != yB || cZ != zB)
			{

			}
		}
	}
}