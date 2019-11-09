package fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons;

import net.minecraft.client.gui.widget.button.Button;

public class TSMButton extends Button implements IHelpButton {

    private String helpText;

    public TSMButton(int x, int y, int width, int height, String name, IPressable press, String helpText) {
        super(x, y, width, height, name, press);
        this.helpText = helpText;
    }

    @Override
    public String getHelpMessage() {
        return this.helpText;
    }

}
