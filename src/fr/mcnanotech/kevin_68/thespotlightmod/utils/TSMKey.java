package fr.mcnanotech.kevin_68.thespotlightmod.utils;

public class TSMKey
{
    public short time, bRed, bGreen, bBlue;

    public TSMKey(short time, short bRed, short bGreen, short bBlue)// TODO fill
    {
        this.time = time;
        this.bRed = bRed;
        this.bGreen = bGreen;
        this.bBlue = bBlue;
    }

    @Override
    public String toString()
    {
        return "TSMKey: time=" + this.time;
    }
}