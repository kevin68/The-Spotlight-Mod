package fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons;

import net.minecraftforge.fml.client.config.GuiSlider;

public class TSMButtonSlider extends GuiSlider implements IHelpButton {

    private String helpText;

    public TSMButtonSlider(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, IPressable handler, ISlider par, String helpText) {
        super(xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, handler, par);
        this.helpText = helpText;
    }

    @Override
    public String getHelpMessage() {
        return this.helpText;
    }
}
