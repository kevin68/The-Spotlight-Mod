package fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.client.resources.I18n;

public class ButtonToggleHelp extends ButtonToggle {

    public ButtonToggleHelp(int x, int y, int width, int height, TileEntitySpotLight tile) {
        super(x, y, width, height, "?", tile.helpMode, b -> {
            tile.helpMode = ((ButtonToggle)b).isActive();
        }, I18n.format("tutorial.spotlight.help"));
    }
}
