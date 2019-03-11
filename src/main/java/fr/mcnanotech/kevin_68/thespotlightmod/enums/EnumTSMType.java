package fr.mcnanotech.kevin_68.thespotlightmod.enums;

public enum EnumTSMType
{
	SHORT(true),
	FLOAT(true),
	BOOLEAN(false),
	STRING(false);

	public final boolean smoothable;

	EnumTSMType(boolean smoothable)
	{
		this.smoothable = smoothable;
	}
}
