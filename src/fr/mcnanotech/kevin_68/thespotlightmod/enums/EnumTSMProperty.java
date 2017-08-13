package fr.mcnanotech.kevin_68.thespotlightmod.enums;

public enum EnumTSMProperty
{
    BEAM_RED(EnumTSMType.SHORT, 255, true, EnumSaveCategory.BEAM_COLOR, "R", "BR"),
    BEAM_GREEN(EnumTSMType.SHORT, 255, true, EnumSaveCategory.BEAM_COLOR, "G", "BG"),
    BEAM_BLUE(EnumTSMType.SHORT, 255, true, EnumSaveCategory.BEAM_COLOR, "B", "BB"),
    BEAM_SEC_RED(EnumTSMType.SHORT, 255, true, EnumSaveCategory.BEAM_COLOR, "SR", "BSR"),
    BEAM_SEC_GREEN(EnumTSMType.SHORT, 255, true, EnumSaveCategory.BEAM_COLOR, "SG", "BSG"),
    BEAM_SEC_BLUE(EnumTSMType.SHORT, 255, true, EnumSaveCategory.BEAM_COLOR, "SB", "BSB"),
    BEAM_ALPHA(EnumTSMType.FLOAT, 1.0F, true, EnumSaveCategory.BEAM_COLOR, "A", "BA"),
    BEAM_SEC_ALPHA(EnumTSMType.FLOAT, 0.125F, true, EnumSaveCategory.BEAM_COLOR, "SA", "BSA"),
    BEAM_ANGLE_X(EnumTSMType.SHORT, 0, true, EnumPropVecBehaviour.VALUE_CHANGED, EnumSaveCategory.BEAM_ANGLE, "X", "BAX"),
    BEAM_ANGLE_Y(EnumTSMType.SHORT, 0, true, EnumPropVecBehaviour.VALUE_CHANGED, EnumSaveCategory.BEAM_ANGLE, "Y", "BAY"),
    BEAM_ANGLE_Z(EnumTSMType.SHORT, 0, true, EnumPropVecBehaviour.VALUE_CHANGED, EnumSaveCategory.BEAM_ANGLE, "Z", "BAZ"),
    BEAM_R_SPEED_X(EnumTSMType.SHORT, 0, false, EnumSaveCategory.BEAM_ANGLE, "RSX", null),
    BEAM_R_SPEED_Y(EnumTSMType.SHORT, 0, false, EnumSaveCategory.BEAM_ANGLE, "RSY", null),
    BEAM_R_SPEED_Z(EnumTSMType.SHORT, 0, false, EnumSaveCategory.BEAM_ANGLE, "RSZ", null),
    BEAM_R_AUTO_X(EnumTSMType.BOOLEAN, false, true, EnumPropVecBehaviour.WHEN_ACTIVE, EnumSaveCategory.BEAM_ANGLE, "ARX", "BRAX"),
    BEAM_R_AUTO_Y(EnumTSMType.BOOLEAN, false, true, EnumPropVecBehaviour.WHEN_ACTIVE, EnumSaveCategory.BEAM_ANGLE, "ARY", "BRAY"),
    BEAM_R_AUTO_Z(EnumTSMType.BOOLEAN, false, true, EnumPropVecBehaviour.WHEN_ACTIVE, EnumSaveCategory.BEAM_ANGLE, "ARZ", "BRAZ"),
    BEAM_R_REVERSE_X(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.BEAM_ANGLE, "RRX", "BRRX"),
    BEAM_R_REVERSE_Y(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.BEAM_ANGLE, "RRY", "BRRY"),
    BEAM_R_REVERSE_Z(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.BEAM_ANGLE, "RRZ", "BRRZ"),
    BEAM_SIZE(EnumTSMType.SHORT, 40, true, EnumPropVecBehaviour.VALUE_CHANGED, EnumSaveCategory.BEAM_PROPERTIES, "Size", "BS"),
    BEAM_SEC_SIZE(EnumTSMType.SHORT, 75, true, EnumPropVecBehaviour.VALUE_CHANGED, EnumSaveCategory.BEAM_PROPERTIES, "SSize", "BSS"),
    BEAM_HEIGHT(EnumTSMType.SHORT, 256, true, EnumPropVecBehaviour.VALUE_CHANGED, EnumSaveCategory.BEAM_PROPERTIES, "H", "BH"),
    BEAM_SIDE(EnumTSMType.SHORT, 2, true, EnumPropVecBehaviour.VALUE_CHANGED, EnumSaveCategory.BEAM_PROPERTIES, "Sides", "BSd"),
    BEAM_SEC_ENABLED(EnumTSMType.BOOLEAN, true, true, EnumSaveCategory.BEAM_PROPERTIES, "SBeam", "BSBE"),
    BEAM_DOUBLE(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.BEAM_PROPERTIES, "Dbl", "BDbl"),
    TEXT(EnumTSMType.STRING, "", true, EnumSaveCategory.TEXT, "Text", "TText"),
    TEXT_RED(EnumTSMType.SHORT, 255, true, EnumSaveCategory.TEXT_COLOR, "R", "TR"),
    TEXT_GREEN(EnumTSMType.SHORT, 255, true, EnumSaveCategory.TEXT_COLOR, "G", "TG"),
    TEXT_BLUE(EnumTSMType.SHORT, 255, true, EnumSaveCategory.TEXT_COLOR, "B", "TB"),
    TEXT_ANGLE_Y(EnumTSMType.SHORT, 0, true, EnumSaveCategory.TEXT_ANGLE, "Y", "TAY"),
    TEXT_R_SPEED_Y(EnumTSMType.SHORT, 0, false, EnumSaveCategory.TEXT_ANGLE, "RSY", null),
    TEXT_R_AUTO_Y(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.TEXT_ANGLE, "ARY", "TRAY"),
    TEXT_R_REVERSE_Y(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.TEXT_ANGLE, "RRY", "TRRY"),
    TEXT_HEIGHT(EnumTSMType.SHORT, 0, true, EnumSaveCategory.TEXT_PROPERTIES, "H", "TH"),
    TEXT_SCALE(EnumTSMType.SHORT, 0, true, EnumSaveCategory.TEXT_PROPERTIES, "S", "TS"),
    TEXT_TRANSLATE_SPEED(EnumTSMType.SHORT, 0, false, EnumSaveCategory.TEXT_PROPERTIES, "TS", null),
    TEXT_BOLD(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.TEXT_PROPERTIES, "B", "TPB"),
    TEXT_STRIKE(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.TEXT_PROPERTIES, "ST", "TPST"),
    TEXT_UNDERLINE(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.TEXT_PROPERTIES, "U", "TPU"),
    TEXT_ITALIC(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.TEXT_PROPERTIES, "I", "TPI"),
    TEXT_OBFUSCATED(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.TEXT_PROPERTIES, "O", "TPO"),
    TEXT_SHADOW(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.TEXT_PROPERTIES,"Sh", "TPSh"),
    TEXT_TRANSLATING(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.TEXT_PROPERTIES, "T", "TPT"),
    TEXT_T_REVERSE(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.TEXT_PROPERTIES, "RT", "TPRT"),
    TEXT_3D(EnumTSMType.BOOLEAN, false, true, EnumSaveCategory.TEXT_PROPERTIES, "3D", "TP3D"),
    BEAM_SPEED(EnumTSMType.FLOAT, 3.0F, true, EnumSaveCategory.BEAM_PROPERTIES, "Speed", "BSpe");

    public final EnumTSMType type;
    public final Object def;
    public final boolean timelinable;
    public final EnumPropVecBehaviour vecBehaviour;
    public final EnumSaveCategory saveCat;
    public final String saveName, saveNameTL;

    EnumTSMProperty(EnumTSMType type, Object def, boolean timelinable, EnumSaveCategory saveCat, String saveName, String saveNameTL)
    {
        this(type, def, timelinable, EnumPropVecBehaviour.NONE, saveCat, saveName, saveNameTL);
    }

    EnumTSMProperty(EnumTSMType type, Object def, boolean timelinable, EnumPropVecBehaviour vecBehaviour, EnumSaveCategory saveCat, String saveName, String saveNameTL)
    {
        this.type = type;
        this.def = def;
        this.timelinable = timelinable;
        this.vecBehaviour = vecBehaviour;
        this.saveCat = saveCat;
        this.saveName = saveName;
        this.saveNameTL = saveNameTL;

        if(vecBehaviour == EnumPropVecBehaviour.WHEN_ACTIVE && type != EnumTSMType.BOOLEAN)
        {
            try
            {
                throw new Exception("Invalid use should be fixed in code :( " + this.name());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
