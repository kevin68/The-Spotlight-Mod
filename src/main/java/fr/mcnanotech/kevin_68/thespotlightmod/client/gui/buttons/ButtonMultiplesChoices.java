package fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons;

import net.minecraft.client.gui.widget.button.Button;

public class ButtonMultiplesChoices extends Button {
    private String[] changTxt;
    private int currentState;
    private int stateCount;

    public ButtonMultiplesChoices(int x, int y, int width, int height, String s, int currentState, int stateCount, Button.IPressable onPress) {
        super(x, y, width, height, s, onPress);
        this.currentState = currentState;
        this.stateCount = stateCount;
    }

    public void setTexts(String[] texts) {
        this.setMessage(texts[this.currentState]);
        this.changTxt = texts;
    }

    public void next() {
        this.currentState++;
        if(this.currentState >= this.stateCount) {
            this.currentState = 0;
        }
        this.setMessage(changTxt[this.currentState]);
    }

    public int getState() {
        return this.currentState;
    }
}
