package fr.mcnanotech.kevin_68.thespotlightmod.client;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.client.text3d.Model3DTextDefault;
import fr.mcnanotech.kevin_68.thespotlightmod.client.text3d.Text3D;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.objs.BeamVec;
import fr.mcnanotech.kevin_68.thespotlightmod.objs.TSMVec3;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntitySpotLightRender extends TileEntitySpecialRenderer<TileEntitySpotLight>
{
    private ModelSpotLight model = new ModelSpotLight();
    private static final ResourceLocation tex = new ResourceLocation(TheSpotLightMod.MODID, "textures/blocks/spotlight.png");
    private static final ResourceLocation defaultBeam = new ResourceLocation("textures/entity/beacon_beam.png");
    private static final Text3D txt3d = new Text3D(Model3DTextDefault.instance);

    @Override
    public void render(TileEntitySpotLight tile, double x, double y, double z, float tick, int destroyStage, float alpha)
    {
        try
        {
            byte b0 = 1;
            float f2 = tile.getWorld().getTotalWorldTime() + tick;
            float timer = getWorld().getTotalWorldTime() * 0.00125F;
            float angleX = tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_X) ? timer * tile.getShort(EnumTSMProperty.BEAM_R_SPEED_X) * (tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_X) ? -1.0F : 1.0F) : (float)Math.toRadians(tile.getShort(EnumTSMProperty.BEAM_ANGLE_X));
            float angleY = tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_Y) ? timer * tile.getShort(EnumTSMProperty.BEAM_R_SPEED_Y) * (tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_Y) ? -1.0F : 1.0F) : (float)Math.toRadians(tile.getShort(EnumTSMProperty.BEAM_ANGLE_Y));
            float angleZ = tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_Z) ? timer * tile.getShort(EnumTSMProperty.BEAM_R_SPEED_Z) * (tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_Z) ? -1.0F : 1.0F) : (float)Math.toRadians(tile.getShort(EnumTSMProperty.BEAM_ANGLE_Z));
            if(!tile.isBeam)
            {
                angleX = 0.0F;
                angleY = tile.getBoolean(EnumTSMProperty.TEXT_R_AUTO_Y) ? timer * tile.getShort(EnumTSMProperty.TEXT_R_SPEED_Y) * (tile.getBoolean(EnumTSMProperty.TEXT_R_REVERSE_Y) ? -1.0F : 1.0F) : (float)Math.toRadians(tile.getShort(EnumTSMProperty.TEXT_ANGLE_Y));
                angleZ = 0.0F;
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
            bindTexture(tex);
            this.model.setRotation(-angleX, angleY, -angleZ);
            GlStateManager.scale(1.2F, 1.2F, 1.2F);
            this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.popMatrix();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.disableFog();
            if(tile.isActive)
            {
                if(tile.isBeam)
                {
                    Tessellator tess = Tessellator.getInstance();

                    ItemStack s = tile.getStackInSlot(6);
                    if(!s.isEmpty())
                    {
                        bindTexture(getResourceLocationStack(s) != null ? getResourceLocationStack(s) : TextureMap.LOCATION_MISSING_TEXTURE);
                    }
                    else
                    {
                        bindTexture(defaultBeam);
                    }
                    GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
                    GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
                    GlStateManager.disableLighting();
                    GlStateManager.disableCull();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

                    float f3 = -f2 * 0.2F - MathHelper.floor(-f2 * 0.1F);
                    double t2 = -1.0F - f3 * (tile.getFloat(EnumTSMProperty.BEAM_SPEED)-2);
                    double t3 = tile.bVec[0].getLenVec().norm() * (0.5D / Math.sqrt(Math.pow(b0 * ((tile.getShort(EnumTSMProperty.BEAM_SIZE)) / 200.0D), 2) / 2)) + t2;
                    double t4 = tile.bVec[1].getLenVec().norm() * (0.5D / Math.sqrt(Math.pow(b0 * ((tile.getShort(EnumTSMProperty.BEAM_SIZE)) / 200.0D), 2) / 2)) + t2;
                    float r = tile.getShort(EnumTSMProperty.BEAM_RED) / 255.0F;
                    float g = tile.getShort(EnumTSMProperty.BEAM_GREEN) / 255.0F;
                    float b = tile.getShort(EnumTSMProperty.BEAM_BLUE) / 255.0F;
                    float a = tile.getFloat(EnumTSMProperty.BEAM_ALPHA);
                    if(a < 0.8F)
                    {
                        GlStateManager.depthMask(false);
                    }
                    else
                    {
                        GlStateManager.depthMask(true);
                    }
                    drawBeam(tess, x, y, z, t2, t3, tile.bVec[0], r, g, b, a);

                    if(tile.getBoolean(EnumTSMProperty.BEAM_DOUBLE))
                    {
                        drawBeam(tess, x, y, z, t2, t4, tile.bVec[1], r, g, b, a);
                    }
                    ItemStack s2 = tile.getStackInSlot(7);
                    if(!s2.isEmpty())
                    {
                        bindTexture(getResourceLocationStack(s2) != null ? getResourceLocationStack(s2) : TextureMap.LOCATION_MISSING_TEXTURE);
                    }
                    else
                    {
                        bindTexture(defaultBeam);
                    }
                    if(tile.getBoolean(EnumTSMProperty.BEAM_SEC_ENABLED))
                    {
                        float sR = tile.getShort(EnumTSMProperty.BEAM_SEC_RED) / 255.0F;
                        float sG = tile.getShort(EnumTSMProperty.BEAM_SEC_GREEN) / 255.0F;
                        float sB = tile.getShort(EnumTSMProperty.BEAM_SEC_BLUE) / 255.0F;
                        float sA = tile.getFloat(EnumTSMProperty.BEAM_SEC_ALPHA);
                        if(sA < 0.8F)
                        {
                            GlStateManager.depthMask(false);
                        }
                        else
                        {
                            GlStateManager.depthMask(true);
                        }
                        drawBeam(tess, x, y, z, t2, t3, tile.bVec[2], sR, sG, sB, sA);
                        if(tile.getBoolean(EnumTSMProperty.BEAM_DOUBLE))
                        {
                            drawBeam(tess, x, y, z, t2, t4, tile.bVec[3], sR, sG, sB, sA);
                        }
                    }
                    GlStateManager.enableLighting();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.depthMask(true);
                }
                else
                {
                    GL11.glPushMatrix();
                    GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * 0.6666667F, (float)z + 0.5F);
                    short tscale = tile.getShort(EnumTSMProperty.TEXT_SCALE);
                    if(tile.getBoolean(EnumTSMProperty.TEXT_3D))
                    {
                        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                        GlStateManager.translate(0.0F, -0.2F, 0.0F);
                        GlStateManager.rotate((float)Math.toDegrees(angleY), 0.0F, 1.0F, 0.0F);
                        float f21 = 0.016666668F * 0.6666667F;
                        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f21);
                        GlStateManager.translate(0.0F, (-200.0F + tile.getShort(EnumTSMProperty.BEAM_HEIGHT) * 2.0F) / 20.0F, 0.0F);
                        GlStateManager.translate(0.0F, (tscale * 0.8F + 1.0F) / 30.0F, 0.0F);
                        if(tile.getShort(EnumTSMProperty.BEAM_HEIGHT) < 50)
                        {
                            GlStateManager.translate(0.0F, -(25.0F + 1.0F + tscale * 0.45F) / 20.0F, 0.0F);
                        }
                        GlStateManager.scale(1.0 + tscale / 16.0F, 1.0 + tscale / 16.0F, 1.0 + tscale / 16.0F);
                        txt3d.renderTextAlignedCenter(tile.getBoolean(EnumTSMProperty.TEXT_TRANSLATING) ? getTranslatingText(tile.getString(EnumTSMProperty.TEXT), tile) : tile.getString(EnumTSMProperty.TEXT), tile.getShort(EnumTSMProperty.TEXT_RED) / 255.0F, tile.getShort(EnumTSMProperty.TEXT_GREEN) / 255.0F, tile.getShort(EnumTSMProperty.TEXT_BLUE) / 255.0F);
                    }
                    else
                    {
                        GlStateManager.scale(0.9D, 0.9D, 0.9D);
                        GlStateManager.rotate((float)Math.toDegrees(angleY), 0.0F, 1.0F, 0.0F);
                        FontRenderer fontrenderer = getFontRenderer();
                        float f21 = 0.016666668F * 0.6666667F;
                        GlStateManager.scale(f21 * 5, -f21 * 5, f21 * 5);
                        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f21);
                        GlStateManager.depthMask(false);
                        GlStateManager.translate(0.0F, 200.0F - tile.getShort(EnumTSMProperty.BEAM_HEIGHT) * 2.0F, 0.0F);
                        GlStateManager.translate(0.0F, tscale * 0.8F + 1.0F, 0.0F);
                        if(tile.getShort(EnumTSMProperty.BEAM_HEIGHT) < 50)
                        {
                            GlStateManager.translate(0.0F, 25.0F + 1.0F + tscale * 0.45F, 0.0F);
                        }
                        GlStateManager.scale(1.0 + tscale / 16.0F, 1.0 + tscale / 16.0F, 1.0 + tscale / 16.0F);
                        String text = (tile.getBoolean(EnumTSMProperty.TEXT_BOLD) ? TextFormatting.BOLD : "") + "" + (tile.getBoolean(EnumTSMProperty.TEXT_STRIKE) ? TextFormatting.STRIKETHROUGH : "") + "" + (tile.getBoolean(EnumTSMProperty.TEXT_UNDERLINE) ? TextFormatting.UNDERLINE : "") + "" + (tile.getBoolean(EnumTSMProperty.TEXT_ITALIC) ? TextFormatting.ITALIC : "") + "" + (tile.getBoolean(EnumTSMProperty.TEXT_OBFUSCATED) ? TextFormatting.OBFUSCATED : "") + "" + (tile.getBoolean(EnumTSMProperty.TEXT_TRANSLATING) ? getTranslatingText(tile.getString(EnumTSMProperty.TEXT), tile) : tile.getString(EnumTSMProperty.TEXT));
                        fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, -20, tile.getShort(EnumTSMProperty.TEXT_RED) * 65536 + tile.getShort(EnumTSMProperty.TEXT_GREEN) * 256 + tile.getShort(EnumTSMProperty.TEXT_BLUE), tile.getBoolean(EnumTSMProperty.TEXT_SHADOW));
                        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                        fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, -20, tile.getShort(EnumTSMProperty.TEXT_RED) * 65536 + tile.getShort(EnumTSMProperty.TEXT_GREEN) * 256 + tile.getShort(EnumTSMProperty.TEXT_BLUE), tile.getBoolean(EnumTSMProperty.TEXT_SHADOW));
                        GlStateManager.depthMask(true);
                    }
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.popMatrix();
                }
            }
            GlStateManager.enableFog();
            GlStateManager.alphaFunc(516, 0.5F);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isGlobalRenderer(TileEntitySpotLight tile)
    {
        return true;
    }

    public void drawBeam(Tessellator tess, double x, double y, double z, double t2, double t3, BeamVec vec, float red, float green, float blue, float alpha)
    {
        BufferBuilder worldrenderer = tess.getBuffer();
        TSMVec3[] v = vec.getVecs();
        TSMVec3 e = vec.getLenVec();
        for(int i = 0; i < v.length; i++)
        {
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(x + 0.5 + v[i].xCoord, y + 0.5 + v[i].yCoord, z + 0.5 + v[i].zCoord).tex(1.0F, t3).color(red, green, blue, alpha).endVertex();
            worldrenderer.pos(x + 0.5 + v[i].xCoord + e.xCoord, y + 0.5 + v[i].yCoord + e.yCoord, z + 0.5 + v[i].zCoord + e.zCoord).tex(1.0F, t2).color(red, green, blue, alpha).endVertex();
            worldrenderer.pos(x + 0.5 + v[i == v.length - 1 ? 0 : i + 1].xCoord + e.xCoord, y + 0.5 + v[i == v.length - 1 ? 0 : i + 1].yCoord + e.yCoord, z + 0.5 + v[i == v.length - 1 ? 0 : i + 1].zCoord + e.zCoord).tex(0.0F, t2).color(red, green, blue, alpha).endVertex();
            worldrenderer.pos(x + 0.5 + v[i == v.length - 1 ? 0 : i + 1].xCoord, y + 0.5 + v[i == v.length - 1 ? 0 : i + 1].yCoord, z + 0.5 + v[i == v.length - 1 ? 0 : i + 1].zCoord).tex(0.0F, t3).color(red, green, blue, alpha).endVertex();
            tess.draw();
        }
    }

    private ResourceLocation getResourceLocationStack(ItemStack stack)
    {
        TextureAtlasSprite sprite = null;
        Block b = Block.getBlockFromItem(stack.getItem());
        if(b != Blocks.AIR)
        {
            sprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(b.getStateFromMeta(stack.getMetadata()));//TODO check
        }
        else
        {
            sprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack).getParticleTexture();
        }

        if(sprite == null)
        {
            return null;
        }
        String iconName = sprite.getIconName();
        String[] strs = iconName.split(":");
        if(strs.length > 1)
        {
            String resource = strs[0] + ":textures/" + strs[1] + ".png";
            return new ResourceLocation(resource);
        }
        return null;
    }

    private String getTranslatingText(String str, TileEntitySpotLight tile)
    {
        if(str != null && str.length() > 1)
        {
            int t = (int)((tile.getWorld().getTotalWorldTime() * ((tile.getShort(EnumTSMProperty.TEXT_TRANSLATE_SPEED) + 1) / 100.0F)) % str.length());
            if(tile.getBoolean(EnumTSMProperty.TEXT_T_REVERSE))
            {
                t = str.length() - t;
            }
            return str.substring(t) + str.substring(0, t);
        }
        return str;
    }
}