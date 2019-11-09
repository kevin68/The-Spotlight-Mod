package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;

public class GuiSpotlightTimelineKeyButton extends Button
{
    protected static final ResourceLocation textures = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

    public GuiSpotlightTimelineKeyButton(int x, int y, Button.IPressable press)
    {
        super(x, y, 3, 3, "", press);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if(this.visible)
        {
            Minecraft mc = Minecraft.getInstance();
            mc.getTextureManager().bindTexture(textures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            this.blit(this.x, this.y, 0, 102, 3, 3);
            this.renderBg(mc, mouseX, mouseY);
        }
    }
}
