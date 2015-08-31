package fr.mcnanotech.kevin_68.thespotlightmod.client.render.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.client.model.ModelSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.BeamVec;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMVec3;

@SideOnly(Side.CLIENT)
public class TileEntitySpotLightRender extends TileEntitySpecialRenderer
{
    private ModelSpotLight model;
    private static final ResourceLocation tex = new ResourceLocation(TheSpotLightMod.MODID, "textures/blocks/spotlight.png");
    private static final ResourceLocation defaultBeam = new ResourceLocation("textures/entity/beacon_beam.png");

    @SuppressWarnings("unused")
    public void renderTileEntitySpotLightAt(TileEntitySpotLight tile, double x, double y, double z, float tick)
    {
        byte b0 = 1;
        float f2 = tile.getWorld().getTotalWorldTime() + tick;
        double d3 = f2 * 0.025D * (1.0D - (b0 & 1) * 2.5D);
        // int angleZ = (Integer)tile.get(EnumLaserInformations.LASERANGLE1);
        // double angle2Deg = (Byte)tile.get(EnumLaserInformations.LASERANGLE2)
        // & 0xFF;
        float timer = getWorld().getTotalWorldTime() * 0.00125F;
        float angleX = tile.beamAutoRotateX ? timer * tile.beamRotationSpeedX * (tile.beamReverseRotateX ? -1.0F : 1.0F) : (float)Math.toRadians(tile.beamAngleX);
        float angleY = tile.beamAutoRotateY ? timer * tile.beamRotationSpeedY * (tile.beamReverseRotateY ? -1.0F : 1.0F) : (float)Math.toRadians(tile.beamAngleY);
        float angleZ = tile.beamAutoRotateZ ? timer * tile.beamRotationSpeedZ * (tile.beamReverseRotateZ ? -1.0F : 1.0F) : (float)Math.toRadians(tile.beamAngleZ);

        // TODO autorotate all angles
        // double a2 = (Boolean)tile.get(EnumLaserInformations.LASERAUTOROTATE)
        // ? d3 * (((Byte)tile.get(EnumLaserInformations.LASERROTATIONSPEED) &
        // 0xFF) / 4.0D) *
        // ((Boolean)tile.get(EnumLaserInformations.LASERREVERSEROTATION) ?
        // -1.0D : 1.0D) : Math.toRadians(angle2Deg);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
        bindTexture(tex);
        int ti = (int)(tile.getWorld().getTotalWorldTime() / 4 % 3);
        this.model = new ModelSpotLight();
        this.model.setRotation(-angleX, angleY, -angleZ);
        GlStateManager.scale(1.2F, 1.2F, 1.2F);
        // GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);

        // System.out.println((float)Math.cos(Math.PI / 180 * angleX +
        // Math.PI));

        // GlStateManager.rotate(angleX, -1.0F, 0.0F, 0.0F);
        // GlStateManager.rotate(angleY, 0.0F, 1.0F, 0.0F);
        // GlStateManager.rotate(-angleZ, 0.0F, 0.0F, 1.0F);
        // GlStateManager.translate(Math.cos(Math.PI / 180 * angleY +
        // Math.PI/2), Math.cos(Math.PI / 180 * angleX) - 1, Math.cos(Math.PI *
        // 1 / 180 * angleX + Math.PI/2) * Math.cos(Math.PI * 1 / 180 *
        // angleY));
        // double angleY = Math.toDegrees(a2);
        // byte axe = 0;//
        // (Byte)tile.get(EnumLaserInformations.LASERDISPLAYAXE);
        // if(axe == 0)
        // {
        // GL11.glRotated(angleY, 0.0F, 1.0F, 0.0F);
        // GL11.glRotated(-angleZ, 0.0F, 0.0F, 1.0F);
        // GL11.glTranslated(Math.cos(Math.PI * (1.0F / 180.0F) * angleZ +
        // Math.PI / 2.0F), Math.cos(Math.PI * (1.0F / 180.0F) * angleZ) - 1,
        // 0.0F);
        // }
        // else if(axe == 1)
        // {
        // GL11.glRotated(-angleY, 1.0F, 0.0F, 0.0F);
        // GL11.glRotated(angleZ + 90, 0.0F, 0.0F, 1.0F);
        // GL11.glTranslated(Math.cos(Math.PI * 1 / 180 * angleZ) *
        // Math.cos(Math.PI * 1 / 180 * angleY), Math.cos(Math.PI * 1 / 180 *
        // angleY) * Math.cos(Math.PI * 1 / 180 * angleZ + Math.PI / 2) - 1,
        // -Math.cos(Math.PI * 1 / 180 * angleY + Math.PI / 2));
        // }
        // else if(axe == 2)
        // {
        // GL11.glRotated(-angleY, 0.0F, 0.0F, 1.0F);
        // GL11.glRotated(angleZ, 1.0F, 0.0F, 0.0F);
        // GL11.glRotated(90, 1.0F, 0.0F, 0.0F);
        // GL11.glTranslated(Math.cos(Math.PI * 1 / 180 * angleY + Math.PI / 2),
        // Math.cos(Math.PI * 1 / 180 * angleY) * Math.cos(Math.PI * 1 / 180 *
        // angleZ + Math.PI / 2) - 1, -Math.cos(Math.PI * 1 / 180 * angleY) *
        // Math.cos(Math.PI * 1 / 180 * angleZ));
        // }

        this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        float f1 = tile.isActive();
        GlStateManager.alphaFunc(516, 0.1F);
        if(f1 > 0.0F)
        {
            Tessellator tess = Tessellator.getInstance();
            WorldRenderer worldrenderer = tess.getWorldRenderer();
            ItemStack s = tile.getStackInSlot(1);
            if(s != null && s.getItem() != null)
            {
                bindTexture(getResourceLocationStack(s));
            }
            else
            {
                bindTexture(defaultBeam);
            }
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            float f3 = -f2 * 0.2F - MathHelper.floor_float(-f2 * 0.1F);
            double t2 = -1.0F - f3;
            double t3 = tile.bVec[0].getLenVec().norm() * f1 * (0.5D / Math.sqrt(Math.pow(b0 * ((tile.beamSize) / 200.0D), 2) / 2)/* d4 */) + t2;
            double t4 = tile.bVec[1].getLenVec().norm() * f1 * (0.5D / Math.sqrt(Math.pow(b0 * ((tile.beamSize) / 200.0D), 2) / 2)/* d4 */) + t2;
            float r = tile.beamRed / 255.0F;
            float g = tile.beamGreen / 255.0F;
            float b = tile.beamBlue / 255.0F;
            worldrenderer.startDrawingQuads();
            worldrenderer.setColorRGBA_F(r, g, b, 0.125F);
            drawBeam(worldrenderer, x, y, z, t2, t3, tile.bVec[0]);
            tess.draw();
            if(tile.beamDouble)
            {
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_F(r, g, b, 0.125F);
                drawBeam(worldrenderer, x, y, z, t2, t4, tile.bVec[1]);
                tess.draw();
            }
            ItemStack s2 = tile.getStackInSlot(2);
            if(s2 != null && s2.getItem() != null)
            {
                bindTexture(getResourceLocationStack(s2));
            }
            else
            {
                bindTexture(defaultBeam);
            }
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.depthMask(false);
            if(tile.secBeamEnabled)
            {
                float sR = tile.secBeamRed / 255.0F;
                float sG = tile.secBeamGreen / 255.0F;
                float sB = tile.secBeamBlue / 255.0F;
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_F(sR, sG, sB, 0.125F);
                drawBeam(worldrenderer, x, y, z, t2, t3, tile.bVec[2]);
                tess.draw();
                if(tile.beamDouble)
                {
                    worldrenderer.startDrawingQuads();
                    worldrenderer.setColorRGBA_F(sR, sG, sB, 0.125F);
                    drawBeam(worldrenderer, x, y, z, t2, t4, tile.bVec[3]);
                    tess.draw();
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.depthMask(true);
            // if((Boolean)tile.get(EnumLaserInformations.TEXTENABLED))
            // {
            // GL11.glPushMatrix();
            // float f11 = 0.6666667F;
            // float f21 = 0.0F;
            // float d21 = tile.getWorld().getTotalWorldTime() + tick;
            // byte b1 = 1;
            // double d31 = d21 * 0.025D * (1.0D - (b1 & 1) * 2.5D);
            // double i1 =
            // (Boolean)tile.get(EnumLaserInformations.TEXTREVERSEROTATION) ?
            // -1.0D : 1.0D;
            // GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f11,
            // (float)z + 0.5F);
            // GlStateManager.rotate(-f21, 0.0F, 1.0F, 0.0F);
            // GlStateManager.translate(0.0F, -0.4F, 0.0F);
            // GlStateManager.scale(0.9D, 0.9D, 0.9D);
            // if((Boolean)tile.get(EnumLaserInformations.TEXTAUTOROTATE))
            // {
            // GL11.glRotatef((float)(d31 *
            // ((Byte)tile.get(EnumLaserInformations.TEXTROTATIONSPEED) & 0xFF)
            // * i1 * 16), 0.0F, 1.0F, 0.0F);
            // }
            // else
            // {
            // GL11.glRotatef((Integer)tile.get(EnumLaserInformations.TEXTANGLE1),
            // 0.0F, 1.0F, 0.0F);
            // }
            // this.modelSign.signStick.showModel = false;
            // GlStateManager.pushMatrix();
            // GlStateManager.scale(f11, -f11, -f11);
            // GlStateManager.scale(0.5F, 0.5F, 0.5F);
            // this.modelSign.renderSign();
            // GlStateManager.popMatrix();
            // FontRenderer fontrenderer = getFontRenderer();
            // f21 = 0.016666668F * f11;
            // GlStateManager.translate(0.0F, 0.5F * f11, 0.07F * f11);
            // GlStateManager.scale(f21 * 5, -f21 * 5, f21 * 5);
            // GL11.glNormal3f(0.0F, 0.0F, -1.0F * f21);
            // GlStateManager.depthMask(false);
            // GlStateManager.translate(0.0F,
            // -(((Byte)tile.get(EnumLaserInformations.TEXTHEIGHT) & 0xFF) -
            // 125.0F) * 2, 0.0F);
            // byte tS = (Byte)tile.get(EnumLaserInformations.TEXTSCALE);
            // GlStateManager.translate(0.0F, -13 + (int)((tS & 0xFF) * 3.96F +
            // 10) / 7.8F, 0.0F);
            // GlStateManager.scale((int)((tS & 0xFF) * 3.96F + 10) / 100.0F,
            // (int)((tS & 0xFF) * 3.96F + 10) / 100.0F, (int)((tS & 0xFF) *
            // 3.96F + 10) / 100.0F);
            // GlStateManager.scale(1.0, 1.0, 1.0);
            // String s = (String)tile.get(EnumLaserInformations.TEXT);
            // fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2,
            // -20, ((Byte)tile.get(EnumLaserInformations.TEXTRED) & 0xFF) *
            // 65536 + ((Byte)tile.get(EnumLaserInformations.TEXTGREEN) & 0xFF)
            // * 256 + ((Byte)tile.get(EnumLaserInformations.TEXTBLUE) & 0xFF));
            // GlStateManager.depthMask(true);
            // GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            // GlStateManager.popMatrix();
            // }
        }
        GlStateManager.alphaFunc(516, 0.5F);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float tick, int p_180535_9_)
    {
        renderTileEntitySpotLightAt((TileEntitySpotLight)tileentity, x, y, z, tick);
    }

    public void drawBeam(WorldRenderer worldrenderer, double x, double y, double z, double t2, double t3, BeamVec vec)
    {
        TSMVec3[] v = vec.getVecs();
        TSMVec3 e = vec.getLenVec();
        for(int i = 0; i < v.length; i++)
        {
            worldrenderer.addVertexWithUV(x + 0.5 + v[i].xCoord, y + 0.5 + v[i].yCoord, z + 0.5 + v[i].zCoord, 1.0F, t3);
            worldrenderer.addVertexWithUV(x + 0.5 + v[i].xCoord + e.xCoord, y + 0.5 + v[i].yCoord + e.yCoord, z + 0.5 + v[i].zCoord + e.zCoord, 1.0F, t2);
            worldrenderer.addVertexWithUV(x + 0.5 + v[i == v.length - 1 ? 0 : i + 1].xCoord + e.xCoord, y + 0.5 + v[i == v.length - 1 ? 0 : i + 1].yCoord + e.yCoord, z + 0.5 + v[i == v.length - 1 ? 0 : i + 1].zCoord + e.zCoord, 0.0F, t2);
            worldrenderer.addVertexWithUV(x + 0.5 + v[i == v.length - 1 ? 0 : i + 1].xCoord, y + 0.5 + v[i == v.length - 1 ? 0 : i + 1].yCoord, z + 0.5 + v[i == v.length - 1 ? 0 : i + 1].zCoord, 0.0F, t3);
        }
    }

    private ResourceLocation getResourceLocationStack(ItemStack stack)
    {
        TextureAtlasSprite sprite = null;
        Block b = Block.getBlockFromItem(stack.getItem());
        if(b != null)
        {
            sprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(b.getDefaultState());
        }
        else
        {
            sprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(stack.getItem());
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
}