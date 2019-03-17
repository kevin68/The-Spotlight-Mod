package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiSpotlightTimelineKeyButton extends GuiButton
{
    protected static final ResourceLocation textures = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public GuiSpotlightTimelineKeyButton(int buttonId, int x, int y)
    {
        super(buttonId, x, y, 3, 3, "");
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if(this.visible)
        {
            Minecraft mc = Minecraft.getInstance();
            mc.getTextureManager().bindTexture(textures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            this.drawTexturedModalRect(this.x, this.y, 0, 102, 3, 3);
            this.renderBg(mc, mouseX, mouseY);
        }
    }

    @Override
    protected boolean isPressable(double mouseX, double mouseY) {
        return this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    @Override
    public int getHoverState(boolean isHover)
    {
        byte b0 = 1;

        if(!this.enabled)
        {
            b0 = 0;
        }
        else if(isHover)
        {
            b0 = 2;
        }

        return b0;
    }
}