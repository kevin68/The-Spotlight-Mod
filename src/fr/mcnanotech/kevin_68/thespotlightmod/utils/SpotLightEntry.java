package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import net.minecraft.nbt.NBTTagCompound;

public class SpotLightEntry
{
	private boolean active;

//	private Map<EnumLaserInformations, Object> map = new HashMap<EnumLaserInformations, Object>();

	public SpotLightEntry(boolean active, byte red, byte green, byte blue, byte secRed, byte secGreen, byte secBlue, int angle1, byte angle2, boolean autoRot, boolean revRot, byte rotSpe, boolean secLas, byte displayAxe, boolean sideLaser, byte mainLaserSize, byte secLaserSize, int laserHeight, boolean textEnabled, byte keyTxtRed, byte keyTxtGreen, byte keyTxtBlue, int txtAngle1, boolean txtAutoRotate, boolean txtReveseRotation, byte txtRotationSpeed, byte txtScale, byte txtHeight, byte sides)
	{
		this.active = active;
//		this.map.put(EnumLaserInformations.LASERRED, red);
//		this.map.put(EnumLaserInformations.LASERGREEN, green);
//		this.map.put(EnumLaserInformations.LASERBLUE, blue);
//		this.map.put(EnumLaserInformations.LASERSECRED, secRed);
//		this.map.put(EnumLaserInformations.LASERSECGREEN, secGreen);
//		this.map.put(EnumLaserInformations.LASERSECBLUE, secBlue);
//		this.map.put(EnumLaserInformations.LASERANGLE1, angle1);
//		this.map.put(EnumLaserInformations.LASERANGLE2, angle2);
//		this.map.put(EnumLaserInformations.LASERAUTOROTATE, autoRot);
//		this.map.put(EnumLaserInformations.LASERREVERSEROTATION, revRot);
//		this.map.put(EnumLaserInformations.LASERROTATIONSPEED, rotSpe);
//		this.map.put(EnumLaserInformations.LASERSECONDARY, secLas);
//		this.map.put(EnumLaserInformations.LASERDISPLAYAXE, displayAxe);
//		this.map.put(EnumLaserInformations.LASERDOUBLE, sideLaser);
//		this.map.put(EnumLaserInformations.LASERMAINSIZE, mainLaserSize);
//		this.map.put(EnumLaserInformations.LASERSECSIZE, secLaserSize);
//		this.map.put(EnumLaserInformations.LASERHEIGHT, laserHeight);
//		this.map.put(EnumLaserInformations.LASERSIDESNUMBER, sides);
//		this.map.put(EnumLaserInformations.TEXTENABLED, textEnabled);
//		this.map.put(EnumLaserInformations.TEXTRED, keyTxtRed);
//		this.map.put(EnumLaserInformations.TEXTGREEN, keyTxtGreen);
//		this.map.put(EnumLaserInformations.TEXTBLUE, keyTxtBlue);
//		this.map.put(EnumLaserInformations.TEXTANGLE1, txtAngle1);
//		this.map.put(EnumLaserInformations.TEXTAUTOROTATE, txtAutoRotate);
//		this.map.put(EnumLaserInformations.TEXTREVERSEROTATION, txtReveseRotation);
//		this.map.put(EnumLaserInformations.TEXTROTATIONSPEED, txtRotationSpeed);
//		this.map.put(EnumLaserInformations.TEXTSCALE, txtScale);
//		this.map.put(EnumLaserInformations.TEXTHEIGHT, txtHeight);
	}

	private SpotLightEntry()
	{}

	public boolean isActive()
	{
		return this.active;
	}

//	public Object get(EnumLaserInformations e)
//	{
//		return this.map.get(e);
//	}

	public static SpotLightEntry loadSpotLightEntryFromNBT(NBTTagCompound nbtTagCompound)
	{
		SpotLightEntry entry = new SpotLightEntry();
		entry.readFromNBT(nbtTagCompound);
		return entry;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
	{
		nbtTagCompound.setBoolean("Active", this.active);
//		nbtTagCompound.setByte("red", (Byte)this.map.get(EnumLaserInformations.LASERRED));
//		nbtTagCompound.setByte("green", (Byte)this.map.get(EnumLaserInformations.LASERGREEN));
//		nbtTagCompound.setByte("blue", (Byte)this.map.get(EnumLaserInformations.LASERBLUE));
//		nbtTagCompound.setByte("secRed", (Byte)this.map.get(EnumLaserInformations.LASERSECRED));
//		nbtTagCompound.setByte("secGreen", (Byte)this.map.get(EnumLaserInformations.LASERSECGREEN));
//		nbtTagCompound.setByte("secBlue", (Byte)this.map.get(EnumLaserInformations.LASERSECBLUE));
//		nbtTagCompound.setInteger("angle1", (Integer)this.map.get(EnumLaserInformations.LASERANGLE1));
//		nbtTagCompound.setByte("angle2", (Byte)this.map.get(EnumLaserInformations.LASERANGLE2));
//		nbtTagCompound.setBoolean("autoRot", (Boolean)this.map.get(EnumLaserInformations.LASERAUTOROTATE));
//		nbtTagCompound.setBoolean("revRot", (Boolean)this.map.get(EnumLaserInformations.LASERREVERSEROTATION));
//		nbtTagCompound.setByte("rotSpe", (Byte)this.map.get(EnumLaserInformations.LASERROTATIONSPEED));
//		nbtTagCompound.setBoolean("secLas", (Boolean)this.map.get(EnumLaserInformations.LASERSECONDARY));
//		nbtTagCompound.setByte("displayAxe", (Byte)this.map.get(EnumLaserInformations.LASERDISPLAYAXE));
//		nbtTagCompound.setBoolean("sideLaser", (Boolean)this.map.get(EnumLaserInformations.LASERDOUBLE));
//		nbtTagCompound.setByte("mainLaserSize", (Byte)this.map.get(EnumLaserInformations.LASERMAINSIZE));
//		nbtTagCompound.setByte("secLaserSize", (Byte)this.map.get(EnumLaserInformations.LASERSECSIZE));
//		nbtTagCompound.setInteger("laserHeight", (Integer)this.map.get(EnumLaserInformations.LASERHEIGHT));
//		nbtTagCompound.setByte("Sides", (Byte)this.map.get(EnumLaserInformations.LASERSIDESNUMBER));
//		nbtTagCompound.setBoolean("textEnabled", (Boolean)this.map.get(EnumLaserInformations.TEXTENABLED));
//		nbtTagCompound.setByte("txtRed", (Byte)this.map.get(EnumLaserInformations.TEXTRED));
//		nbtTagCompound.setByte("txtGreen", (Byte)this.map.get(EnumLaserInformations.TEXTGREEN));
//		nbtTagCompound.setByte("txtBlue", (Byte)this.map.get(EnumLaserInformations.TEXTBLUE));
//		nbtTagCompound.setInteger("txtAngle1", (Integer)this.map.get(EnumLaserInformations.TEXTANGLE1));
//		nbtTagCompound.setBoolean("txtAutoRot", (Boolean)this.map.get(EnumLaserInformations.TEXTAUTOROTATE));
//		nbtTagCompound.setBoolean("txtRevRot", (Boolean)this.map.get(EnumLaserInformations.TEXTREVERSEROTATION));
//		nbtTagCompound.setByte("txtRotSpe", (Byte)this.map.get(EnumLaserInformations.TEXTROTATIONSPEED));
		return nbtTagCompound;
	}

	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		this.active = nbtTagCompound.getBoolean("Active");
//		this.map.put(EnumLaserInformations.LASERRED, nbtTagCompound.getByte("red"));
//		numLaserInformations.TEXTROTATIONSPEED, nbtTagCothis.map.put(EnumLaserInformations.LASERGREEN, nbtTagCompound.getByte("green"));
//		this.map.put(EnumLaserInformations.LASERBLUE, nbtTagCompound.getByte("blue"));
//		this.map.put(EnumLaserInformations.LASERSECRED, nbtTagCompound.getByte("secRed"));
//		this.map.put(EnumLaserInformations.LASERSECGREEN, nbtTagCompound.getByte("secGreen"));
//		this.map.put(EnumLaserInformations.LASERSECBLUE, nbtTagCompound.getByte("secBlue"));
//		this.map.put(EnumLaserInformations.LASERANGLE1, nbtTagCompound.getInteger("angle1"));
//		this.map.put(EnumLaserInformations.LASERANGLE2, nbtTagCompound.getByte("angle2"));
//		this.map.put(EnumLaserInformations.LASERAUTOROTATE, nbtTagCompound.getBoolean("autoRot"));
//		this.map.put(EnumLaserInformations.LASERREVERSEROTATION, nbtTagCompound.getBoolean("revRot"));
//		this.map.put(EnumLaserInformations.LASERROTATIONSPEED, nbtTagCompound.getByte("rotSpe"));
//		this.map.put(EnumLaserInformations.LASERSECONDARY, nbtTagCompound.getBoolean("secLas"));
//		this.map.put(EnumLaserInformations.LASERDISPLAYAXE, nbtTagCompound.getByte("displayAxe"));
//		this.map.put(EnumLaserInformations.LASERDOUBLE, nbtTagCompound.getBoolean("sideLaser"));
//		this.map.put(EnumLaserInformations.LASERMAINSIZE, nbtTagCompound.getByte("mainLaserSize"));
//		this.map.put(EnumLaserInformations.LASERSECSIZE, nbtTagCompound.getByte("secLaserSize"));
//		this.map.put(EnumLaserInformations.LASERHEIGHT, nbtTagCompound.getInteger("laserHeight"));
//		this.map.put(EnumLaserInformations.LASERSIDESNUMBER, nbtTagCompound.getByte("Sides"));
//		this.map.put(EnumLaserInformations.TEXTENABLED, nbtTagCompound.getBoolean("textEnabled"));
//		this.map.put(EnumLaserInformations.TEXTRED, nbtTagCompound.getByte("txtRed"));
//		this.map.put(EnumLaserInformations.TEXTGREEN, nbtTagCompound.getByte("txtGreen"));
//		this.map.put(EnumLaserInformations.TEXTBLUE, nbtTagCompound.getByte("txtBlue"));
//		this.map.put(EnumLaserInformations.TEXTANGLE1, nbtTagCompound.getInteger("txtAngle1"));
//		this.map.put(EnumLaserInformations.TEXTAUTOROTATE, nbtTagCompound.getBoolean("txtAutoRot"));
//		this.map.put(EnumLaserInformations.TEXTREVERSEROTATION, nbtTagCompound.getBoolean("txtRevRot"));
//		this.map.put(Empound.getByte("txtRotSpe"));
//		this.map.put(EnumLaserInformations.TEXTSCALE, nbtTagCompound.getByte("txtScale"));
//		this.map.put(EnumLaserInformations.TEXTHEIGHT, nbtTagCompound.getByte("txtHeight"));
	}
}