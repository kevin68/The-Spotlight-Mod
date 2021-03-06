package fr.mcnanotech.kevin_68.thespotlightmod.client;

import com.mojang.blaze3d.platform.GlStateManager;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.client.text3d.Model3DTextDefault;
import fr.mcnanotech.kevin_68.thespotlightmod.client.text3d.Text3D;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.objs.BeamVec;
import fr.mcnanotech.kevin_68.thespotlightmod.objs.TSMVec3;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntitySpotLightRender extends TileEntityRenderer<TileEntitySpotLight>
{
    private ModelSpotLight model = new ModelSpotLight();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TheSpotLightMod.MOD_ID, "textures/block/spotlight.png");
    private static final ResourceLocation DEFAULT_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");
    private static final Text3D TXT3D = new Text3D(Model3DTextDefault.instance);

    @Override
    public void render(TileEntitySpotLight tile, double x, double y, double z, float partialTick, int destroyStage)
    {
        float renderTick = tile.getWorld().getGameTime() + partialTick;
        float timer = getWorld().getGameTime() * 0.00125F;
        float angleX = this.getAngleValue(tile, timer, EnumTSMProperty.BEAM_R_AUTO_X, EnumTSMProperty.BEAM_R_SPEED_X, EnumTSMProperty.BEAM_R_REVERSE_X, EnumTSMProperty.BEAM_ANGLE_X);
        float angleY = this.getAngleValue(tile, timer, EnumTSMProperty.BEAM_R_AUTO_Y, EnumTSMProperty.BEAM_R_SPEED_Y, EnumTSMProperty.BEAM_R_REVERSE_Y, EnumTSMProperty.BEAM_ANGLE_Y);
        float angleZ = this.getAngleValue(tile, timer, EnumTSMProperty.BEAM_R_AUTO_Z, EnumTSMProperty.BEAM_R_SPEED_Z, EnumTSMProperty.BEAM_R_REVERSE_Z, EnumTSMProperty.BEAM_ANGLE_Z);

        if (!tile.isBeam)
        {
            angleX = 0.0F;
            angleY = this.getAngleValue(tile, timer, EnumTSMProperty.TEXT_R_AUTO_Y, EnumTSMProperty.TEXT_R_SPEED_Y, EnumTSMProperty.TEXT_R_REVERSE_Y, EnumTSMProperty.TEXT_ANGLE_Y);
            angleZ = 0.0F;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
        bindTexture(TEXTURE);
        this.model.setRotation(-angleX, angleY, -angleZ);
        GlStateManager.scaled(1.2F, 1.2F, 1.2F);
        this.model.render(0.0625F);
        GlStateManager.popMatrix();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.disableFog();
        if (tile.isActive)
        {
            if (tile.isBeam)
            {
                this.renderBeam(tile, x, y, z, renderTick);
            }
            else
            {
                this.renderText(tile, x, y, z, angleY);
            }
        }
        GlStateManager.enableFog();
        GlStateManager.alphaFunc(516, 0.5F);
    }

    @Override
    public boolean isGlobalRenderer(TileEntitySpotLight tile)
    {
        return true;
    }

    private float getAngleValue(TileEntitySpotLight tile, float timer, EnumTSMProperty auto, EnumTSMProperty speed, EnumTSMProperty reverse, EnumTSMProperty angle)
    {
        if (tile.getBoolean(auto))
        {
            return timer * tile.getShort(speed) * (tile.getBoolean(reverse) ? -1.0F : 1.0F);
        }
        return (float)Math.toRadians(tile.getShort(angle));
    }

    private void renderBeam(TileEntitySpotLight tile, double x, double y, double z, float renderTick)
    {
        Tessellator tess = Tessellator.getInstance();

        ItemStack s = tile.getStackInSlot(6);
        if (!s.isEmpty())
        {
            bindTexture(getResourceLocationStack(s) != null ? getResourceLocationStack(s) : MissingTextureSprite.getLocation());
        }
        else
        {
            bindTexture(DEFAULT_BEAM);
        }
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);

        float f3 = -renderTick * 0.2F - MathHelper.floor(-renderTick * 0.1F);
        double t2 = -1.0F - f3 * (tile.getFloat(EnumTSMProperty.BEAM_SPEED)-2);
        double t3 = tile.bVec[0].getLenVec().norm() * (0.5D / Math.sqrt(Math.pow((tile.getShort(EnumTSMProperty.BEAM_SIZE) / 200.0D), 2) / 2)) + t2;
        double t4 = tile.bVec[1].getLenVec().norm() * (0.5D / Math.sqrt(Math.pow((tile.getShort(EnumTSMProperty.BEAM_SIZE) / 200.0D), 2) / 2)) + t2;
        float r = tile.getShort(EnumTSMProperty.BEAM_RED) / 255.0F;
        float g = tile.getShort(EnumTSMProperty.BEAM_GREEN) / 255.0F;
        float b = tile.getShort(EnumTSMProperty.BEAM_BLUE) / 255.0F;
        float a = tile.getFloat(EnumTSMProperty.BEAM_ALPHA);
        if (a < 0.8F)
        {
            GlStateManager.depthMask(false);
        }
        else
        {
            GlStateManager.depthMask(true);
        }
        drawBeam(tess, x, y, z, t2, t3, tile.bVec[0], r, g, b, a);

        if (tile.getBoolean(EnumTSMProperty.BEAM_DOUBLE))
        {
            drawBeam(tess, x, y, z, t2, t4, tile.bVec[1], r, g, b, a);
        }
        ItemStack s2 = tile.getStackInSlot(7);
        if (!s2.isEmpty())
        {
            bindTexture(getResourceLocationStack(s2) != null ? getResourceLocationStack(s2) : MissingTextureSprite.getLocation());
        }
        else
        {
            bindTexture(DEFAULT_BEAM);
        }
        if (tile.getBoolean(EnumTSMProperty.BEAM_SEC_ENABLED))
        {
            float sR = tile.getShort(EnumTSMProperty.BEAM_SEC_RED) / 255.0F;
            float sG = tile.getShort(EnumTSMProperty.BEAM_SEC_GREEN) / 255.0F;
            float sB = tile.getShort(EnumTSMProperty.BEAM_SEC_BLUE) / 255.0F;
            float sA = tile.getFloat(EnumTSMProperty.BEAM_SEC_ALPHA);
            if (sA < 0.8F)
            {
                GlStateManager.depthMask(false);
            }
            else
            {
                GlStateManager.depthMask(true);
            }
            drawBeam(tess, x, y, z, t2, t3, tile.bVec[2], sR, sG, sB, sA);
            if (tile.getBoolean(EnumTSMProperty.BEAM_DOUBLE))
            {
                drawBeam(tess, x, y, z, t2, t4, tile.bVec[3], sR, sG, sB, sA);
            }
        }
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

    private void renderText(TileEntitySpotLight tile, double x, double y, double z, float angleY)
    {
        GL11.glPushMatrix();
        GlStateManager.translatef((float)x + 0.5F, (float)y + 0.75F * 0.6666667F, (float)z + 0.5F);
        short tscale = tile.getShort(EnumTSMProperty.TEXT_SCALE);
        if (tile.getBoolean(EnumTSMProperty.TEXT_3D))
        {
            GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(0.0F, -0.2F, 0.0F);
            GlStateManager.rotatef((float)Math.toDegrees(angleY), 0.0F, 1.0F, 0.0F);
            float f21 = 0.016666668F * 0.6666667F;
            GL11.glNormal3f(0.0F, 0.0F, -1.0F * f21);
            GlStateManager.translatef(0.0F, (8 + tile.getShort(EnumTSMProperty.TEXT_HEIGHT)) / 10.0F, 0.0F);
            GlStateManager.translatef(0.0F, (tscale * 0.8F + 1.0F) / 30.0F, 0.0F);
            if (tile.getShort(EnumTSMProperty.TEXT_HEIGHT) < 0)
            {
                GlStateManager.translatef(0.0F, -(25.0F + 1.0F + tscale * 0.45F) / 20.0F, 0.0F);
            }
            GlStateManager.scaled(1.0 + tscale / 16.0F, 1.0 + tscale / 16.0F, 1.0 + tscale / 16.0F);
            GlStateManager.disableLighting();
            TXT3D.renderTextAlignedCenter(tile.getBoolean(EnumTSMProperty.TEXT_TRANSLATING) ? getTranslatingText(tile.getString(EnumTSMProperty.TEXT), tile) : tile.getString(EnumTSMProperty.TEXT), tile.getShort(EnumTSMProperty.TEXT_RED) / 255.0F, tile.getShort(EnumTSMProperty.TEXT_GREEN) / 255.0F, tile.getShort(EnumTSMProperty.TEXT_BLUE) / 255.0F);
            GlStateManager.enableLighting();
        }
        else
        {
            GlStateManager.scaled(0.9D, 0.9D, 0.9D);
            GlStateManager.rotatef((float)Math.toDegrees(angleY), 0.0F, 1.0F, 0.0F);
            FontRenderer fontRenderer = getFontRenderer();
            float f21 = 0.016666668F * 0.6666667F;
            GlStateManager.scaled(f21 * 5, -f21 * 5, f21 * 5);
            GL11.glNormal3f(0.0F, 0.0F, -1.0F * f21);
            GlStateManager.disableLighting();
            GlStateManager.translatef(0.0F, -tile.getShort(EnumTSMProperty.TEXT_HEIGHT) * 2.0F, 0.0F);
            GlStateManager.translatef(0.0F, tscale * 0.8F + 1.0F, 0.0F);
            if (tile.getShort(EnumTSMProperty.TEXT_HEIGHT) < 0)
            {
                GlStateManager.translatef(0.0F, 25.0F + 1.0F + tscale * 0.45F, 0.0F);
            }
            GlStateManager.scaled(1.0 + tscale / 16.0F, 1.0 + tscale / 16.0F, 1.0 + tscale / 16.0F);
            String text = (tile.getBoolean(EnumTSMProperty.TEXT_BOLD) ? TextFormatting.BOLD : "") + "" + (tile.getBoolean(EnumTSMProperty.TEXT_STRIKE) ? TextFormatting.STRIKETHROUGH : "") + "" + (tile.getBoolean(EnumTSMProperty.TEXT_UNDERLINE) ? TextFormatting.UNDERLINE : "") + "" + (tile.getBoolean(EnumTSMProperty.TEXT_ITALIC) ? TextFormatting.ITALIC : "") + "" + (tile.getBoolean(EnumTSMProperty.TEXT_OBFUSCATED) ? TextFormatting.OBFUSCATED : "") + "" + (tile.getBoolean(EnumTSMProperty.TEXT_TRANSLATING) ? getTranslatingText(tile.getString(EnumTSMProperty.TEXT), tile) : tile.getString(EnumTSMProperty.TEXT));
            int color = tile.getShort(EnumTSMProperty.TEXT_RED) * 65536 + tile.getShort(EnumTSMProperty.TEXT_GREEN) * 256 + tile.getShort(EnumTSMProperty.TEXT_BLUE);
            if (tile.getBoolean(EnumTSMProperty.TEXT_SHADOW))
            {
                fontRenderer.drawStringWithShadow(text, -fontRenderer.getStringWidth(text) / 2.0F, -20, color);
            }
            else
            {                        	
                fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2.0F, -20, color);
            }
            GlStateManager.enableLighting();
        }
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public void drawBeam(Tessellator tess, double x, double y, double z, double t2, double t3, BeamVec vec, float red, float green, float blue, float alpha)
    {
        BufferBuilder bufferBuilder = tess.getBuffer();
        TSMVec3[] v = vec.getVecs();
        TSMVec3 e = vec.getLenVec();
        for (int i = 0; i < v.length; i++)
        {
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferBuilder.pos(x + 0.5 + v[i].xCoord, y + 0.5 + v[i].yCoord, z + 0.5 + v[i].zCoord).tex(1.0F, t3).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(x + 0.5 + v[i].xCoord + e.xCoord, y + 0.5 + v[i].yCoord + e.yCoord, z + 0.5 + v[i].zCoord + e.zCoord).tex(1.0F, t2).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(x + 0.5 + v[i == v.length - 1 ? 0 : i + 1].xCoord + e.xCoord, y + 0.5 + v[i == v.length - 1 ? 0 : i + 1].yCoord + e.yCoord, z + 0.5 + v[i == v.length - 1 ? 0 : i + 1].zCoord + e.zCoord).tex(0.0F, t2).color(red, green, blue, alpha).endVertex();
            bufferBuilder.pos(x + 0.5 + v[i == v.length - 1 ? 0 : i + 1].xCoord, y + 0.5 + v[i == v.length - 1 ? 0 : i + 1].yCoord, z + 0.5 + v[i == v.length - 1 ? 0 : i + 1].zCoord).tex(0.0F, t3).color(red, green, blue, alpha).endVertex();
            tess.draw();
        }
    }

    private ResourceLocation getResourceLocationStack(ItemStack stack)
    {
        TextureAtlasSprite sprite = null;
        Block b = Block.getBlockFromItem(stack.getItem());
        if(b != Blocks.AIR)
        {
            sprite = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(b.getDefaultState());
        }
        else
        {
            sprite = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getParticleIcon(stack);
        }

        if(sprite == null)
        {
            return null;
        }
        ResourceLocation rl = sprite.getName();
        return new ResourceLocation(rl.getNamespace(), "textures/" + rl.getPath() + ".png");
    }

    private String getTranslatingText(String str, TileEntitySpotLight tile)
    {
        if(str != null && str.length() > 1)
        {
            int t = (int)((tile.getWorld().getGameTime() * ((tile.getShort(EnumTSMProperty.TEXT_TRANSLATE_SPEED) + 1) / 100.0F)) % str.length());
            if(tile.getBoolean(EnumTSMProperty.TEXT_T_REVERSE))
            {
                t = str.length() - t;
            }
            return str.substring(t) + str.substring(0, t);
        }
        return str;
    }
}
