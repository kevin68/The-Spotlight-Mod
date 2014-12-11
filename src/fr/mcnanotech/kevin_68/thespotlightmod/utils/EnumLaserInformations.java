package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import java.util.ArrayList;
import java.util.List;

public enum EnumLaserInformations
{
	COMMANDSETDEFAULT((byte)4),
	COMMANDAPPLYCONFIG((byte)4),
	COMMANDREMOVECONFIG((byte)4),
	COMMANDCREATECONFIG((byte)4),
	LASERRED((byte)0),
	LASERGREEN((byte)0),
	LASERBLUE((byte)0),
	LASERSECRED((byte)0),
	LASERSECGREEN((byte)0),
	LASERSECBLUE((byte)0),
	LASERANGLE1((byte)1),
	LASERANGLE2((byte)0),
	LASERROTATIONSPEED((byte)0),
	LASERDISPLAYAXE((byte)0),
	LASERMAINSIZE((byte)0),
	LASERSECSIZE((byte)0),
	LASERSIDESNUMBER((byte)0),
	LASERMAINTEXTURE((byte)3),
	LASERSECTEXTURE((byte)3),
	LASERHEIGHT((byte)1),
	LASERAUTOROTATE((byte)2),
	LASERREVERSEROTATION((byte)2),
	LASERSECONDARY((byte)2),
	LASERDOUBLE((byte)2),
	TIMELINECREATEKEYTIME((byte)0),
	TIMELINELASTKEYSELECTED((byte)0),
	TIMELINETIME((byte)1),
	TIMELINEENABLED((byte)2),
	TIMELINESMOOTH((byte)2),
	TEXT((byte)3),
	TEXTRED((byte)0),
	TEXTGREEN((byte)0),
	TEXTBLUE((byte)0),
	TEXTROTATIONSPEED((byte)0),
	TEXTSCALE((byte)0),
	TEXTHEIGHT((byte)0),
	TEXTANGLE1((byte)1),
	TEXTENABLED((byte)2),
	TEXTAUTOROTATE((byte)2),
	TEXTREVERSEROTATION((byte)2);

	private byte type;

	/**
	 * @param type = Object type: 0 = byte, 1 = integer, 2 = boolean, 3 == string, 4 = commands
	 */
	private EnumLaserInformations(byte type)
	{
		this.type = type;
	}

	public byte getType()
	{
		return type;
	}
}