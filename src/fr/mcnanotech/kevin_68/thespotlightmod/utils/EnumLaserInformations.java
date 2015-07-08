package fr.mcnanotech.kevin_68.thespotlightmod.utils;

//@formatter:off
public enum EnumLaserInformations
{
	COMMANDSETDEFAULT((byte)4,false),
	COMMANDAPPLYCONFIG((byte)4,false),
	COMMANDREMOVECONFIG((byte)4,false),
	COMMANDCREATECONFIG((byte)4,false),
	LASERRED((byte)0, true),
	LASERGREEN((byte)0, true),
	LASERBLUE((byte)0, true),
	LASERSECRED((byte)0, true),
	LASERSECGREEN((byte)0, true),
	LASERSECBLUE((byte)0, true),
	LASERANGLE1((byte)1, true),
	LASERANGLE2((byte)0, true),
	LASERROTATIONSPEED((byte)0, true),
	LASERDISPLAYAXE((byte)0,true),
	LASERMAINSIZE((byte)0,true),
	LASERSECSIZE((byte)0,true),
	LASERSIDESNUMBER((byte)0,true),
	LASERMAINTEXTURE((byte)3, false),
	LASERSECTEXTURE((byte)3, false),
	LASERHEIGHT((byte)1, true),
	LASERAUTOROTATE((byte)2, true),
	LASERREVERSEROTATION((byte)2, true),
	LASERSECONDARY((byte)2, true),
	LASERDOUBLE((byte)2, true),
	TIMELINECREATEKEYTIME((byte)0, false),
	TIMELINELASTKEYSELECTED((byte)0, false),
	TIMELINETIME((byte)1, false),
	TIMELINEENABLED((byte)2, false),
	TIMELINESMOOTH((byte)2, false),
	TEXT((byte)3, false),
	TEXTRED((byte)0, true),
	TEXTGREEN((byte)0, true),
	TEXTBLUE((byte)0, true),
	TEXTROTATIONSPEED((byte)0, true),
	TEXTSCALE((byte)0, true),
	TEXTHEIGHT((byte)0, true),
	TEXTANGLE1((byte)1, true),
	TEXTENABLED((byte)2, true),
	TEXTAUTOROTATE((byte)2, true),
	TEXTREVERSEROTATION((byte)2, true);

	private byte type;
	private boolean key;

	/**
	 * @param type
	 *            = Object type: 0 = byte, 1 = integer, 2 = boolean, 3 ==
	 *            string, 4 = commands
	 */
	private EnumLaserInformations(byte type, boolean key)
	{
		this.type = type;
	}

	public byte getType()
	{
		return this.type;
	}

	public boolean shouldProcessKeys()
	{
		return this.key;
	}
}