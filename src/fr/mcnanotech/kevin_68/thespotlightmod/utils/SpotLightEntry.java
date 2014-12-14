package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

public class SpotLightEntry
{
	private boolean active;

	private Map<EnumLaserInformations, Object> map = new HashMap<EnumLaserInformations, Object>();

	public SpotLightEntry(boolean active, byte red, byte green, byte blue, byte secRed, byte secGreen, byte secBlue, int angle1, byte angle2, boolean autoRot, boolean revRot, byte rotSpe, boolean secLas, byte displayAxe, boolean sideLaser, byte mainLaserSize, byte secLaserSize, int laserHeight, boolean textEnabled, byte keyTxtRed, byte keyTxtGreen, byte keyTxtBlue, int txtAngle1, boolean txtAutoRotate, boolean txtReveseRotation, byte txtRotationSpeed, byte txtScale, byte txtHeight, byte sides)
	{
		this.active = active;
		map.put(EnumLaserInformations.LASERRED, red);
		map.put(EnumLaserInformations.LASERGREEN, green);
		map.put(EnumLaserInformations.LASERBLUE, blue);
		map.put(EnumLaserInformations.LASERSECRED, secRed);
		map.put(EnumLaserInformations.LASERSECGREEN, secGreen);
		map.put(EnumLaserInformations.LASERSECBLUE, secBlue);
		map.put(EnumLaserInformations.LASERANGLE1, angle1);
		map.put(EnumLaserInformations.LASERANGLE2, angle2);
		map.put(EnumLaserInformations.LASERAUTOROTATE, autoRot);
		map.put(EnumLaserInformations.LASERREVERSEROTATION, revRot);
		map.put(EnumLaserInformations.LASERROTATIONSPEED, rotSpe);
		map.put(EnumLaserInformations.LASERSECONDARY, secLas);
		map.put(EnumLaserInformations.LASERDISPLAYAXE, displayAxe);
		map.put(EnumLaserInformations.LASERDOUBLE, sideLaser);
		map.put(EnumLaserInformations.LASERMAINSIZE, mainLaserSize);
		map.put(EnumLaserInformations.LASERSECSIZE, secLaserSize);
		map.put(EnumLaserInformations.LASERHEIGHT, laserHeight);
		map.put(EnumLaserInformations.LASERSIDESNUMBER, sides);
		map.put(EnumLaserInformations.TEXTENABLED, textEnabled);
		map.put(EnumLaserInformations.TEXTRED, keyTxtRed);
		map.put(EnumLaserInformations.TEXTGREEN, keyTxtGreen);
		map.put(EnumLaserInformations.TEXTBLUE, keyTxtBlue);
		map.put(EnumLaserInformations.TEXTANGLE1, txtAngle1);
		map.put(EnumLaserInformations.TEXTAUTOROTATE, txtAutoRotate);
		map.put(EnumLaserInformations.TEXTREVERSEROTATION, txtReveseRotation);
		map.put(EnumLaserInformations.TEXTROTATIONSPEED, txtRotationSpeed);
		map.put(EnumLaserInformations.TEXTSCALE, txtScale);
		map.put(EnumLaserInformations.TEXTHEIGHT, txtHeight);
	}

	private SpotLightEntry()
	{}

	public boolean isActive()
	{
		return active;
	}

	public Object get(EnumLaserInformations e)
	{
		return map.get(e);
	}

	public static SpotLightEntry loadSpotLightEntryFromNBT(NBTTagCompound nbtTagCompound)
	{
		SpotLightEntry entry = new SpotLightEntry();
		entry.readFromNBT(nbtTagCompound);
		return entry;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
	{
		nbtTagCompound.setBoolean("Active", active);
		nbtTagCompound.setByte("red", (Byte)map.get(EnumLaserInformations.LASERRED));
		nbtTagCompound.setByte("green", (Byte)map.get(EnumLaserInformations.LASERGREEN));
		nbtTagCompound.setByte("blue", (Byte)map.get(EnumLaserInformations.LASERBLUE));
		nbtTagCompound.setByte("secRed", (Byte)map.get(EnumLaserInformations.LASERSECRED));
		nbtTagCompound.setByte("secGreen", (Byte)map.get(EnumLaserInformations.LASERSECGREEN));
		nbtTagCompound.setByte("secBlue", (Byte)map.get(EnumLaserInformations.LASERSECBLUE));
		nbtTagCompound.setInteger("angle1", (Integer)map.get(EnumLaserInformations.LASERANGLE1));
		nbtTagCompound.setByte("angle2", (Byte)map.get(EnumLaserInformations.LASERANGLE2));
		nbtTagCompound.setBoolean("autoRot", (Boolean)map.get(EnumLaserInformations.LASERAUTOROTATE));
		nbtTagCompound.setBoolean("revRot", (Boolean)map.get(EnumLaserInformations.LASERREVERSEROTATION));
		nbtTagCompound.setByte("rotSpe", (Byte)map.get(EnumLaserInformations.LASERROTATIONSPEED));
		nbtTagCompound.setBoolean("secLas", (Boolean)map.get(EnumLaserInformations.LASERSECONDARY));
		nbtTagCompound.setByte("displayAxe", (Byte)map.get(EnumLaserInformations.LASERDISPLAYAXE));
		nbtTagCompound.setBoolean("sideLaser", (Boolean)map.get(EnumLaserInformations.LASERDOUBLE));
		nbtTagCompound.setByte("mainLaserSize", (Byte)map.get(EnumLaserInformations.LASERMAINSIZE));
		nbtTagCompound.setByte("secLaserSize", (Byte)map.get(EnumLaserInformations.LASERSECSIZE));
		nbtTagCompound.setInteger("laserHeight", (Integer)map.get(EnumLaserInformations.LASERHEIGHT));
		nbtTagCompound.setByte("Sides", (Byte)map.get(EnumLaserInformations.LASERSIDESNUMBER));
		nbtTagCompound.setBoolean("textEnabled", (Boolean)map.get(EnumLaserInformations.TEXTENABLED));
		nbtTagCompound.setByte("txtRed", (Byte)map.get(EnumLaserInformations.TEXTRED));
		nbtTagCompound.setByte("txtGreen", (Byte)map.get(EnumLaserInformations.TEXTGREEN));
		nbtTagCompound.setByte("txtBlue", (Byte)map.get(EnumLaserInformations.TEXTBLUE));
		nbtTagCompound.setInteger("txtAngle1", (Integer)map.get(EnumLaserInformations.TEXTANGLE1));
		nbtTagCompound.setBoolean("txtAutoRot", (Boolean)map.get(EnumLaserInformations.TEXTAUTOROTATE));
		nbtTagCompound.setBoolean("txtRevRot", (Boolean)map.get(EnumLaserInformations.TEXTREVERSEROTATION));
		nbtTagCompound.setByte("txtRotSpe", (Byte)map.get(EnumLaserInformations.TEXTROTATIONSPEED));
		return nbtTagCompound;
	}

	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		active = nbtTagCompound.getBoolean("Active");
		map.put(EnumLaserInformations.LASERRED, nbtTagCompound.getByte("red"));
		map.put(EnumLaserInformations.LASERGREEN, nbtTagCompound.getByte("green"));
		map.put(EnumLaserInformations.LASERBLUE, nbtTagCompound.getByte("blue"));
		map.put(EnumLaserInformations.LASERSECRED, nbtTagCompound.getByte("secRed"));
		map.put(EnumLaserInformations.LASERSECGREEN, nbtTagCompound.getByte("secGreen"));
		map.put(EnumLaserInformations.LASERSECBLUE, nbtTagCompound.getByte("secBlue"));
		map.put(EnumLaserInformations.LASERANGLE1, nbtTagCompound.getInteger("angle1"));
		map.put(EnumLaserInformations.LASERANGLE2, nbtTagCompound.getByte("angle2"));
		map.put(EnumLaserInformations.LASERAUTOROTATE, nbtTagCompound.getBoolean("autoRot"));
		map.put(EnumLaserInformations.LASERREVERSEROTATION, nbtTagCompound.getBoolean("revRot"));
		map.put(EnumLaserInformations.LASERROTATIONSPEED, nbtTagCompound.getByte("rotSpe"));
		map.put(EnumLaserInformations.LASERSECONDARY, nbtTagCompound.getBoolean("secLas"));
		map.put(EnumLaserInformations.LASERDISPLAYAXE, nbtTagCompound.getByte("displayAxe"));
		map.put(EnumLaserInformations.LASERDOUBLE, nbtTagCompound.getBoolean("sideLaser"));
		map.put(EnumLaserInformations.LASERMAINSIZE, nbtTagCompound.getByte("mainLaserSize"));
		map.put(EnumLaserInformations.LASERSECSIZE, nbtTagCompound.getByte("secLaserSize"));
		map.put(EnumLaserInformations.LASERHEIGHT, nbtTagCompound.getInteger("laserHeight"));
		map.put(EnumLaserInformations.LASERSIDESNUMBER, nbtTagCompound.getByte("Sides"));
		map.put(EnumLaserInformations.TEXTENABLED, nbtTagCompound.getBoolean("textEnabled"));
		map.put(EnumLaserInformations.TEXTRED, nbtTagCompound.getByte("txtRed"));
		map.put(EnumLaserInformations.TEXTGREEN, nbtTagCompound.getByte("txtGreen"));
		map.put(EnumLaserInformations.TEXTBLUE, nbtTagCompound.getByte("txtBlue"));
		map.put(EnumLaserInformations.TEXTANGLE1, nbtTagCompound.getInteger("txtAngle1"));
		map.put(EnumLaserInformations.TEXTAUTOROTATE, nbtTagCompound.getBoolean("txtAutoRot"));
		map.put(EnumLaserInformations.TEXTREVERSEROTATION, nbtTagCompound.getBoolean("txtRevRot"));
		map.put(EnumLaserInformations.TEXTROTATIONSPEED, nbtTagCompound.getByte("txtRotSpe"));
		map.put(EnumLaserInformations.TEXTSCALE, nbtTagCompound.getByte("txtScale"));
		map.put(EnumLaserInformations.TEXTHEIGHT, nbtTagCompound.getByte("txtHeight"));
	}
}