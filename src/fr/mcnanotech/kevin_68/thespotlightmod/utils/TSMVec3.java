package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import net.minecraft.util.MathHelper;

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

		xCoord = x;
		yCoord = y;
		zCoord = z;
	}

	protected TSMVec3 set(double x, double y, double z)
	{
		xCoord = x;
		yCoord = y;
		zCoord = z;
		return this;
	}

	public void rotateAroundX(float angle)
	{
		float f1 = MathHelper.cos(angle);
		float f2 = MathHelper.sin(angle);
		double d0 = xCoord;
		double d1 = yCoord * f1 + zCoord * f2;
		double d2 = zCoord * f1 - yCoord * f2;
		set(d0, d1, d2);
	}

	public void rotateAroundY(float angle)
	{
		float f1 = MathHelper.cos(angle);
		float f2 = MathHelper.sin(angle);
		double d0 = xCoord * f1 + zCoord * f2;
		double d1 = yCoord;
		double d2 = zCoord * f1 - xCoord * f2;
		set(d0, d1, d2);
	}

	public void rotateAroundZ(float angle)
	{
		float f1 = MathHelper.cos(angle);
		float f2 = MathHelper.sin(angle);
		double d0 = xCoord * f1 + yCoord * f2;
		double d1 = yCoord * f1 - xCoord * f2;
		double d2 = zCoord;
		set(d0, d1, d2);
	}

	public double norm()
	{
		return Math.sqrt(xCoord * xCoord + yCoord * yCoord + zCoord * zCoord);
	}
}