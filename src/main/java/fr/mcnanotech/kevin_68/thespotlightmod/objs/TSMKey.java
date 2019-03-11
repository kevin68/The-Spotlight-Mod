package fr.mcnanotech.kevin_68.thespotlightmod.objs;

import java.util.Map;

import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMType;

public class TSMKey
{
	private final Map<EnumTSMProperty, Object> properties;
    public final short time;

    public TSMKey(short time, Map<EnumTSMProperty, Object> properties)
    {
        this.time = time;
        this.properties = properties;
    }

    @Override
    public String toString()
    {
        return "TSMKey: time=" + this.time;
    }

    public short getS(EnumTSMProperty prop)
    {
        if(prop.type == EnumTSMType.SHORT)
        {
            return ((Number)properties.get(prop)).shortValue();
        }
        System.out.println("Invalid use for prop: " + prop.name() + " in TSMKey");
        return 0;
    }

    public float getF(EnumTSMProperty prop)
    {
        if(prop.type == EnumTSMType.FLOAT)
        {
            return ((Float)properties.get(prop)).floatValue();
        }
        System.out.println("Invalid use for prop: " + prop.name() + " in TSMKey");
        return 0.0F;
    }

    public boolean getB(EnumTSMProperty prop)
    {
        if(prop.type == EnumTSMType.BOOLEAN)
        {
            return ((Boolean)properties.get(prop)).booleanValue();
        }
        System.out.println("Invalid use for prop: " + prop.name() + " in TSMKey");
        return false;
    }

    public String getStr(EnumTSMProperty prop)
    {
        if(prop.type == EnumTSMType.STRING)
        {
            return ((String)properties.get(prop)).substring(0);
        }
        System.out.println("Invalid use for prop: " + prop.name() + " in TSMKey");
        return "";
    }
}