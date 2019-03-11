package fr.mcnanotech.kevin_68.thespotlightmod.enums;

public enum EnumSaveCategory
{
    BEAM(null, "Beam"),
    BEAM_COLOR(BEAM, "Colors"),
    BEAM_ANGLE(BEAM, "Angles"),
    BEAM_PROPERTIES(BEAM, "Properties"),
    TEXT(null, "Text"),
    TEXT_COLOR(TEXT, "Colors"),
    TEXT_ANGLE(TEXT, "Angles"),
    TEXT_PROPERTIES(TEXT, "Properties");

    public final EnumSaveCategory parent;
    public final String saveName;

    EnumSaveCategory(EnumSaveCategory parent, String saveName)
    {
        this.parent = parent;
        this.saveName = saveName;
    }
}
