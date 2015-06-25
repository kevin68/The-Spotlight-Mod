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

	public TSMVec3 add(TSMVec3 v)
	{
		set(xCoord + v.xCoord, yCoord + v.yCoord, zCoord + v.zCoord);
		return this;
	}

	public TSMVec3 multiply(double coef)
	{
		set(xCoord * coef, yCoord * coef, zCoord * coef);
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
		double d1 = -xCoord * f2 + yCoord * f1;
		double d2 = zCoord;
		set(d0, d1, d2);
	}

	public void rotateAround(TSMVec3 ar, float angle)
	{
		float f1 = MathHelper.cos(angle);
		float f2 = MathHelper.sin(angle);// TODO check
		double d0 = xCoord * f1 + (1 - f1) * (xCoord * ar.xCoord) * ar.xCoord + f2 * (zCoord * ar.yCoord - ar.yCoord * zCoord);
		double d1 = yCoord * f1 + (1 - f1) * (yCoord * ar.yCoord) * ar.yCoord + f2 * (yCoord * ar.xCoord - ar.xCoord * yCoord);
		double d2 = zCoord * f1 + (1 - f1) * (zCoord * ar.zCoord) * ar.zCoord + f2 * (xCoord * ar.zCoord - ar.zCoord * xCoord);
		set(d0, d1, d2);
	}

	public double norm()
	{
		return Math.sqrt(xCoord * xCoord + yCoord * yCoord + zCoord * zCoord);
	}
}