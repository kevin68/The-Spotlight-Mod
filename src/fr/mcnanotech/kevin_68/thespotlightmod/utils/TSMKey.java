package fr.mcnanotech.kevin_68.thespotlightmod.utils;

public class TSMKey
{
    public final short time, bRed, bGreen, bBlue, secBRed, secBGreen, secBBlue, bAngleX, bAngleY, bAngleZ;
    public final boolean bARX, bARY, bARZ, bRRX, bRRY, bRRZ;
    public final float bAlpha, secBAlpha;

    public TSMKey(short time, short bRed, short bGreen, short bBlue, float bAlpha, short secBRed, short secBGreen, short secBBlue, float secBAlpha, short bAngleX, short bAngleY, short bAngleZ, boolean bARX, boolean bARY, boolean bARZ, boolean bRRX, boolean bRRY, boolean bRRZ)// TODO fill
    {
        this.time = time;
        this.bRed = bRed;
        this.bGreen = bGreen;
        this.bBlue = bBlue;
        this.bAlpha = bAlpha;
        this.secBRed = secBRed;
        this.secBGreen = secBGreen;
        this.secBBlue = secBBlue;
        this.secBAlpha = secBAlpha;
        this.bAngleX = bAngleX;
        this.bAngleY = bAngleY;
        this.bAngleZ = bAngleZ;
        this.bARX = bARX;
        this.bARY = bARY;
        this.bARZ = bARZ;
        this.bRRX = bRRX;
        this.bRRY = bRRY;
        this.bRRZ = bRRZ;
    }

    @Override
    public String toString()
    {
        return "TSMKey: time=" + this.time;
    }
}