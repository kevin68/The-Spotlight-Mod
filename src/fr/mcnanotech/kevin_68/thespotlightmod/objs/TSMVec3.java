package fr.mcnanotech.kevin_68.thespotlightmod.objs;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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

    public TSMVec3 add(TSMVec3 v)
    {
        set(this.xCoord + v.xCoord, this.yCoord + v.yCoord, this.zCoord + v.zCoord);
        return this;
    }

    public TSMVec3 multiply(double coef)
    {
        set(this.xCoord * coef, this.yCoord * coef, this.zCoord * coef);
        return this;
    }

    public void rotateAroundX(float angle)
    {
        float f1 = MathHelper.cos(angle);
        float f2 = MathHelper.sin(angle);
        double d0 = this.xCoord;
        double d1 = this.yCoord * f1 + this.zCoord * f2;
        double d2 = this.zCoord * f1 - this.yCoord * f2;
        set(d0, d1, d2);
    }

    public void rotateAroundY(float angle)
    {
        float f1 = MathHelper.cos(angle);
        float f2 = MathHelper.sin(angle);
        double d0 = this.xCoord * f1 + this.zCoord * f2;
        double d1 = this.yCoord;
        double d2 = this.zCoord * f1 - this.xCoord * f2;
        set(d0, d1, d2);
    }

    public void rotateAroundZ(float angle)
    {
        float f1 = MathHelper.cos(angle);
        float f2 = MathHelper.sin(angle);
        double d0 = this.xCoord * f1 + this.yCoord * f2;
        double d1 = -this.xCoord * f2 + this.yCoord * f1;
        double d2 = this.zCoord;
        set(d0, d1, d2);
    }

    public void rotateAround(TSMVec3 ar, float angle)
    {
        float f1 = MathHelper.cos(angle);
        float f2 = MathHelper.sin(angle);
        double d0 = this.xCoord * f1 + (1 - f1) * (this.xCoord * ar.xCoord) * ar.xCoord + f2 * (this.zCoord * ar.yCoord - ar.yCoord * this.zCoord);
        double d1 = this.yCoord * f1 + (1 - f1) * (this.yCoord * ar.yCoord) * ar.yCoord + f2 * (this.yCoord * ar.xCoord - ar.xCoord * this.yCoord);
        double d2 = this.zCoord * f1 + (1 - f1) * (this.zCoord * ar.zCoord) * ar.zCoord + f2 * (this.xCoord * ar.zCoord - ar.zCoord * this.xCoord);
        set(d0, d1, d2);
    }

    public double norm()
    {
        return Math.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public Vec3d toMcVec3()
    {
        return new Vec3d(this.xCoord, this.yCoord, this.zCoord);
    }
}