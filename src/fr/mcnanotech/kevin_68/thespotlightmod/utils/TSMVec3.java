package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import net.minecraft.util.MathHelper;

public class TSMVec3
{
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public TSMVec3(double x, double y, double z)
    {
        if (x == -0.0D)
        {
            x = 0.0D;
        }

        if (y == -0.0D)
        {
            y = 0.0D;
        }

        if (z == -0.0D)
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
}