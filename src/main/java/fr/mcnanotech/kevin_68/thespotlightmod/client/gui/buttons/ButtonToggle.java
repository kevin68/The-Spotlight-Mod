package fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ButtonToggle extends TSMButton 
{
    private ResourceLocation buttonTex = new ResourceLocation("textures/gui/widgets.png");
    private String textNotActive, textActive;
    private int yTex = 46;
    private boolean useHoverState = true, otherTextureWhenActive = true, doNotChangeTextColor = false;
    
    public ButtonToggle(int x, int y, int width, int height, String text, boolean active, Button.IPressable onPress, String helpText)
    {
        super(x, y, width, height, text, onPress, helpText);
        this.active = active;
        this.textActive = this.textNotActive = text;
    }
    
    public void setTexts(String active, String notActive)
    {
        this.textActive = active;
        this.textNotActive = notActive;
    }
    
    /**
     * Follow the default texture model!
     * @param loc the resource location of the texture
     * @param textureY y coord of the texture in the file
     */
    public void setCustomTexture(ResourceLocation loc, int textureY)
    {
        this.yTex = textureY;
        this.buttonTex = loc;
    }

    public void shouldUseHoverState(boolean should)
    {
        this.useHoverState = should;
    }
    
    public void shouldChangeTextureOnToggle(boolean should)
    {
        this.otherTextureWhenActive = should;
    }
    
    public void changeTextColorWhenNotActive(boolean change)
    {
        this.doNotChangeTextColor = change;
    }

    @Override
    public void onPress() {
        this.toggle();
        this.onPress.onPress(this);
    }

    public void toggle()
    {
        this.active = !active;
    }

    public boolean isActive()
    {
        return this.active;
    }

    @Override
    public int getYImage(boolean hovered)
    {
        return getYImage(hovered, true);
    }

    public int getYImage(boolean getYImage, boolean otherActive)
    {
        byte b0 = 1;
        if((isActive() || this.active) && otherActive)
        {
            b0 = 1;
        }
        if(getYImage)
        {
            b0 = 2;
        }

        return b0;
    }

    public void shouldNotChangeTextColor(boolean b)
    {
        this.doNotChangeTextColor = b;
    }

    @Override
    public void render(int x, int y, float partialTicks)
    {
        if(this.visible)
        {
        	Minecraft mc = Minecraft.getInstance();
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(this.buttonTex);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
            int k = this.getYImage(this.isHovered, this.otherTextureWhenActive);
            this.blit(this.x, this.y, 0, this.yTex + (this.useHoverState ? (k * 20) : 0), this.width / 2, this.height);
            this.blit(this.x + this.width / 2, this.y, 200 - this.width / 2, this.yTex + (this.useHoverState ? (k * 20) : 0), this.width / 2, this.height);
            this.renderBg(mc, x, y);
            int l = 14737632;
            String str;
            if(!this.active)
            {
                l = -6250336;
            }
            else if(this.isHovered)
            {
                l = 16777120;
            }

            if(!isActive())
            {
                l = this.doNotChangeTextColor ? 14737632 : 6316128;
                str = this.textNotActive;
            }
            else
            {
                str = this.textActive;
            }

            this.drawCenteredString(fontrenderer, str, this.x + this.width / 2, this.y + (this.height - 8) / 2, l);
        }
    }
}
