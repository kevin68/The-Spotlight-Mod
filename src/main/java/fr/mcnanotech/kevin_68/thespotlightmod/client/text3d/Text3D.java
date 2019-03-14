package fr.mcnanotech.kevin_68.thespotlightmod.client.text3d;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Text3D
{
    private final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID, "textures/white.png");
    private Model3DTextBase font;
    private final Minecraft mc = Minecraft.getInstance();

    public Text3D(Model3DTextBase font)
    {
        this.font = font;
    }

    public void renderTextAlignedLeft(String text)
    {
        renderTextAlignedLeft(text, 1.0F, 1.0F, 1.0F);
    }

    public void renderTextAlignedLeft(String text, float red, float green, float blue)
    {
        renderTextAlignedLeft(text, 0.07F, red, green, blue);
    }

    public void renderTextAlignedLeft(String text, float spacing, float red, float green, float blue)
    {
        if(text != null && text.length() > 0)
        {
            GlStateManager.color3f(red, green, blue);
            this.mc.textureManager.bindTexture(this.texture);
            char[] str = text.toCharArray();
            float prevspacing = 0;
            for(int i = 0; i < str.length; i++)
            {
                prevspacing += spacing * (this.font.charSizes.keySet().contains(str[i]) ? this.font.charSizes.get(str[i])+2.0F:5.0F);
                this.font.renderChar(str[i], 1.0F / 16.0F, -prevspacing);
            }
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        }
    }

    public void renderTextAlignedRight(String text)
    {
        renderTextAlignedRight(text, 1.0F, 1.0F, 1.0F);
    }

    public void renderTextAlignedRight(String text, float red, float green, float blue)
    {
        renderTextAlignedRight(text, 0.07F, red, green, blue);
    }

    public void renderTextAlignedRight(String text, float spacing, float red, float green, float blue)
    {
        if(text != null && text.length() > 0)
        {
            GlStateManager.color3f(red, green, blue);
            this.mc.textureManager.bindTexture(this.texture);
            char[] str = text.toCharArray();
            float prevspacing = 0;
            for(int i = str.length-1; i >=0 ; i--)
            {
                prevspacing += spacing * (this.font.charSizes.keySet().contains(str[i]) ? this.font.charSizes.get(str[i])+2.0F:5.0F);
                this.font.renderChar(str[i], 1.0F / 16.0F, prevspacing);
            }
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        }
    }

    public void renderTextAlignedCenter(String text)
    {
        renderTextAlignedCenter(text, 1.0F, 1.0F, 1.0F);
    }

    public void renderTextAlignedCenter(String text, float red, float green, float blue)
    {
        renderTextAlignedCenter(text, 0.07F, red, green, blue);
    }

    public void renderTextAlignedCenter(String text, float spacing, float red, float green, float blue)
    {
        if(text != null && text.length() > 0)
        {
            GlStateManager.color3f(red, green, blue);
            this.mc.textureManager.bindTexture(this.texture);
            char[] str = text.toCharArray();
            float prevspacing = -spacing*2.0F; // fix text not centered caused by an extra space
            float tLen = 0;
            for(int i = str.length-1; i >=0 ; i--)
            {
                tLen += spacing * (this.font.charSizes.keySet().contains(str[i]) ? this.font.charSizes.get(str[i])+2.0F:5.0F);
            }
            tLen /= 2.0F;
            for(int i = str.length-1; i >=0 ; i--)
            {
                prevspacing += spacing * (this.font.charSizes.keySet().contains(str[i]) ? this.font.charSizes.get(str[i])+2.0F:5.0F);
                this.font.renderChar(str[i], 1.0F / 16.0F, prevspacing - tLen);
            }
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        }
    }
}