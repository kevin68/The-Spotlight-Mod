package fr.mcnanotech.kevin_68.thespotlightmod.client.render.tileentity;

import static java.lang.Math.cos;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.client.model.ModelSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;
import fr.minecraftforgefrance.ffmtlibs.client.renderer.TileEntityInventorySpecialRenderer;

@SideOnly(Side.CLIENT)
public class TileEntitySpotLightRender extends TileEntityInventorySpecialRenderer
{
    private final ModelSign modelSign = new ModelSign();
    private ModelSpotLight model;

    @Override
    public void renderInventory(double x, double y, double z)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
        this.bindTexture(new ResourceLocation(TheSpotLightMod.MODID, "textures/blocks/spotlight.png"));
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(0.8F, 0.8F, 0.8F);
        GL11.glTranslatef(0.0F, 0.38F, 0.0F);
        model = new ModelSpotLight(0);
        this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    public void renderTileEntitySpotLightAt(TileEntitySpotLight tileentity, double x, double y, double z, float tick)
    {
        byte b0 = 1;
        float f2 = tileentity.getWorldObj().getTotalWorldTime() + tick;
        double d3 = f2 * 0.025D * (1.0D - (b0 & 1) * 2.5D);
        double angle1Deg = tileentity.getAngle1();
        double angle2Deg = (tileentity.getAngle2() & 0xFF);
        double a1 = Math.toRadians(angle1Deg);
        double a2 = tileentity.isAutoRotate() ? ((d3 * ((tileentity.getRotationSpeed() & 0xFF) / 4.0D)) * (tileentity.isReverseRotation() ? -1.0D : 1.0D)) : Math.toRadians(angle2Deg);

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
        this.bindTexture(new ResourceLocation(TheSpotLightMod.MODID, "textures/blocks/spotlight.png"));
        int ti = (int)((tileentity.getWorldObj().getTotalWorldTime() / 4) % 3);
        model = new ModelSpotLight(ti);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);

        double angl = Math.toDegrees(a1);
        double angl2 = Math.toDegrees(a2);

        if(tileentity.getDisplayAxe() == 0)
        {
            GL11.glRotated(angl2, 0.0F, 1.0F, 0.0F);
            GL11.glRotated(-angl, 0.0F, 0.0F, 1.0F);
            GL11.glTranslated(0.0F, cos(Math.PI * (1.0F / 180.0F) * angl) - 1, 0.0F);
            GL11.glTranslated(cos(Math.PI * (1.0F / 180.0F) * angl + Math.PI / 2.0F), 0.0F, 0.0F);

        }
        else if(tileentity.getDisplayAxe() == 1)
        {
            GL11.glRotated(-angl2, 1.0F, 0.0F, 0.0F);
            GL11.glRotated(angl + 90, 0.0F, 0.0F, 1.0F);
            GL11.glTranslated(cos(Math.PI * 1 / 180 * angl) * cos(Math.PI * 1 / 180 * angl2), cos(Math.PI * 1 / 180 * angl2) * cos(Math.PI * 1 / 180 * angl + Math.PI / 2) - 1, -cos(Math.PI * 1 / 180 * angl2 + Math.PI / 2));
        }
        else if(tileentity.getDisplayAxe() == 2)
        {
            GL11.glRotated(-angl2, 0.0F, 0.0F, 1.0F);
            GL11.glRotated(angl, 1.0F, 0.0F, 0.0F);
            GL11.glRotated(90, 1.0F, 0.0F, 0.0F);
            double a = cos(Math.PI * 1 / 180 * angl2 + Math.PI / 2);
            double b = cos(Math.PI * 1 / 180 * angl2) * cos(Math.PI * 1 / 180 * angl + Math.PI / 2) - 1;
            double c = -cos(Math.PI * 1 / 180 * angl2) * cos(Math.PI * 1 / 180 * angl);
            GL11.glTranslated(a, b, c);
        }

        this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();

        float f1 = tileentity.isActive();
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        if(f1 > 0.0F)
        {
            Tessellator tessellator = Tessellator.instance;
            GuiHelper.bindTexture(UtilSpotLight.getEntryByName(tileentity.getTextureName()).getPath());
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            OpenGlHelper.glBlendFunc(770, 1, 1, 0);

            float f3 = -f2 * 0.2F - MathHelper.floor_float(-f2 * 0.1F);
            double d4 = Math.sqrt(Math.pow(b0 * ((tileentity.getMainLaserSize() & 0xFF) / 200.0D), 2) / 2);// taille

            double t2 = -1.0F - f3;
            double t3 = tileentity.getLaserHeight() * f1 * (0.5D / d4) + t2;

            tessellator.startDrawingQuads();
            tessellator.setColorRGBA(tileentity.getRed() & 0xFF, tileentity.getGreen() & 0xFF, tileentity.getBlue() & 0xFF, 32);
            drawBeam(d4, tileentity.getLaserHeight(), a1, a2, tileentity.getDisplayAxe(), tessellator, x, y, z, t2, t3);
            tessellator.draw();

            if(tileentity.isSideLaser())
            {
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA(tileentity.getRed() & 0xFF, tileentity.getGreen() & 0xFF, tileentity.getBlue() & 0xFF, 32);
                drawBeam(d4, -tileentity.getLaserHeight(), a1, a2, tileentity.getDisplayAxe(), tessellator, x, y, z, t2, t3);
                tessellator.draw();
            }

            GuiHelper.bindTexture(UtilSpotLight.getEntryByName(tileentity.getSecTextureName()).getPath());
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glDepthMask(false);

            double d5 = Math.sqrt(Math.pow(b0 * ((tileentity.getSecLaserSize() & 0xFF) / 200.0D), 2) / 2);

            if(tileentity.isSecondaryLaser())
            {
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA(tileentity.getSecRed() & 0xFF, tileentity.getSecGreen() & 0xFF, tileentity.getSecBlue() & 0xFF, 32);
                drawBeam(d5, tileentity.getLaserHeight(), a1, a2, tileentity.getDisplayAxe(), tessellator, x, y, z, t2, t3);
                tessellator.draw();

                if(tileentity.isSideLaser())
                {
                    tessellator.startDrawingQuads();
                    tessellator.setColorRGBA(tileentity.getSecRed() & 0xFF, tileentity.getSecGreen() & 0xFF, tileentity.getSecBlue() & 0xFF, 32);
                    drawBeam(d5, -tileentity.getLaserHeight(), a1, a2, tileentity.getDisplayAxe(), tessellator, x, y, z, t2, t3);
                    tessellator.draw();
                }
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(true);

            if(tileentity.isTextEnabled())
            {
                GL11.glPushMatrix();
                float f11 = 0.6666667F;
                float f21 = 0.0F;
                float d21 = tileentity.getWorldObj().getTotalWorldTime() + tick;
                byte b1 = 1;
                double d31 = d21 * 0.025D * (1.0D - (b1 & 1) * 2.5D);
                double i1 = (tileentity.isTxtReverseRotation() ? -1.0D : 1.0D);
                GL11.glTranslatef((float)x + 0.5F, (float)y + 0.75F * f11, (float)z + 0.5F);
                GL11.glRotatef(-f21, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.4F, 0.0F);
                GL11.glScaled(0.9D, 0.9D, 0.9D);
                if(tileentity.isTxtAutoRotate())
                {
                    GL11.glRotatef((float)((d31 * (tileentity.getTxtRotationSpeed() & 0xFF) * i1) * 16), 0.0F, 1.0F, 0.0F);
                }
                else
                {
                    GL11.glRotatef(tileentity.getTxtAngle1(), 0.0F, 1.0F, 0.0F);
                }
                this.modelSign.signStick.showModel = false;
                GL11.glPushMatrix();
                GL11.glScalef(f11, -f11, -f11);
                GL11.glScaled(0.5F, 0.5F, 0.5F);
                this.modelSign.renderSign();
                GL11.glPopMatrix();
                FontRenderer fontrenderer = this.func_147498_b();
                f21 = 0.016666668F * f11;
                GL11.glTranslatef(0.0F, 0.5F * f11, 0.07F * f11);
                GL11.glScalef(f21 * 5, -f21 * 5, f21 * 5);
                GL11.glNormal3f(0.0F, 0.0F, -1.0F * f21);
                GL11.glDepthMask(false);
                GL11.glTranslatef(0.0F, -((tileentity.getTxtHeight() & 0xFF) - 125.0F) * 2, 0.0F);
                GL11.glTranslatef(0.0F, -13 + ((int)((tileentity.getTxtScale() & 0xFF) * 3.96F + 10) / 7.8F), 0.0F);
                GL11.glScalef(((int)((tileentity.getTxtScale() & 0xFF) * 3.96F + 10)) / 100.0F, ((int)((tileentity.getTxtScale() & 0xFF) * 3.96F + 10)) / 100.0F, ((int)((tileentity.getTxtScale() & 0xFF) * 3.96F + 10)) / 100.0F);
                GL11.glScaled(1.0, 1.0, 1.0);
                String s = tileentity.getDisplayText();
                fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, -20, ((tileentity.getTxtRed() & 0xFF) * 65536) + ((tileentity.getTxtGreen() & 0xFF) * 256) + (tileentity.getTxtBlue() & 0xFF));
                GL11.glDepthMask(true);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float tick)
    {
        this.renderTileEntitySpotLightAt((TileEntitySpotLight)tileentity, x, y, z, tick);
    }

    public void drawBeam(double size, int height, double a1, double a2, byte axe, Tessellator tessellator, double x, double y, double z, double t2, double t3)
    {
        Vec3 a = null, b = null, c = null, d = null, e = null;
        if(axe == 0)
        {
            a = Vec3.createVectorHelper(size, 0.0D, size);
            b = Vec3.createVectorHelper(-size, 0.0D, size);
            c = Vec3.createVectorHelper(-size, 0.0D, -size);
            d = Vec3.createVectorHelper(size, 0.0D, -size);
            e = Vec3.createVectorHelper(0, height, 0);
            a.rotateAroundZ((float)a1);
            b.rotateAroundZ((float)a1);
            c.rotateAroundZ((float)a1);
            d.rotateAroundZ((float)a1);
            e.rotateAroundZ((float)a1);
            a.rotateAroundY(-(float)a2);
            b.rotateAroundY(-(float)a2);
            c.rotateAroundY(-(float)a2);
            d.rotateAroundY(-(float)a2);
            e.rotateAroundY(-(float)a2);
        }
        else if(axe == 1)
        {
            a = Vec3.createVectorHelper(0, size, size);
            b = Vec3.createVectorHelper(0, -size, size);
            c = Vec3.createVectorHelper(0, -size, -size);
            d = Vec3.createVectorHelper(0, size, -size);
            e = Vec3.createVectorHelper(height, 0, 0);
            a.rotateAroundZ(-(float)a1);
            b.rotateAroundZ(-(float)a1);
            c.rotateAroundZ(-(float)a1);
            d.rotateAroundZ(-(float)a1);
            e.rotateAroundZ(-(float)a1);
            a.rotateAroundX(-(float)a2);
            b.rotateAroundX(-(float)a2);
            c.rotateAroundX(-(float)a2);
            d.rotateAroundX(-(float)a2);
            e.rotateAroundX(-(float)a2);
        }
        else
        {
            a = Vec3.createVectorHelper(size, size, 0);
            b = Vec3.createVectorHelper(-size, size, 0);
            c = Vec3.createVectorHelper(-size, -size, 0);
            d = Vec3.createVectorHelper(size, -size, -0);
            e = Vec3.createVectorHelper(0, 0, height);
            a.rotateAroundX((float)a1);
            b.rotateAroundX((float)a1);
            c.rotateAroundX((float)a1);
            d.rotateAroundX((float)a1);
            e.rotateAroundX((float)a1);
            a.rotateAroundZ((float)a2);
            b.rotateAroundZ((float)a2);
            c.rotateAroundZ((float)a2);
            d.rotateAroundZ((float)a2);
            e.rotateAroundZ((float)a2);
        }

        tessellator.addVertexWithUV(x + 0.5 + a.xCoord, y + 0.5 + a.yCoord, z + 0.5 + a.zCoord, 1.0F, t3);
        tessellator.addVertexWithUV(x + 0.5 + a.xCoord + e.xCoord, y + 0.5 + a.yCoord + e.yCoord, z + 0.5 + a.zCoord + e.zCoord, 1.0F, t2);
        tessellator.addVertexWithUV(x + 0.5 + b.xCoord + e.xCoord, y + 0.5 + b.yCoord + e.yCoord, z + 0.5 + b.zCoord + e.zCoord, 0.0F, t2);
        tessellator.addVertexWithUV(x + 0.5 + b.xCoord, y + 0.5 + b.yCoord, z + 0.5 + b.zCoord, 0.0F, t3);

        tessellator.addVertexWithUV(x + 0.5 + b.xCoord, y + 0.5 + b.yCoord, z + 0.5 + b.zCoord, 1.0F, t3);
        tessellator.addVertexWithUV(x + 0.5 + b.xCoord + e.xCoord, y + 0.5 + b.yCoord + e.yCoord, z + 0.5 + b.zCoord + e.zCoord, 1.0F, t2);
        tessellator.addVertexWithUV(x + 0.5 + c.xCoord + e.xCoord, y + 0.5 + c.yCoord + e.yCoord, z + 0.5 + c.zCoord + e.zCoord, 0.0F, t2);
        tessellator.addVertexWithUV(x + 0.5 + c.xCoord, y + 0.5 + c.yCoord, z + 0.5 + c.zCoord, 0.0F, t3);

        tessellator.addVertexWithUV(x + 0.5 + c.xCoord, y + 0.5 + c.yCoord, z + 0.5 + c.zCoord, 1.0F, t3);
        tessellator.addVertexWithUV(x + 0.5 + c.xCoord + e.xCoord, y + 0.5 + c.yCoord + e.yCoord, z + 0.5 + c.zCoord + e.zCoord, 1.0F, t2);
        tessellator.addVertexWithUV(x + 0.5 + d.xCoord + e.xCoord, y + 0.5 + d.yCoord + e.yCoord, z + 0.5 + d.zCoord + e.zCoord, 0.0F, t2);
        tessellator.addVertexWithUV(x + 0.5 + d.xCoord, y + 0.5 + d.yCoord, z + 0.5 + d.zCoord, 0.0F, t3);

        tessellator.addVertexWithUV(x + 0.5 + d.xCoord, y + 0.5 + d.yCoord, z + 0.5 + d.zCoord, 1.0F, t3);
        tessellator.addVertexWithUV(x + 0.5 + d.xCoord + e.xCoord, y + 0.5 + d.yCoord + e.yCoord, z + 0.5 + d.zCoord + e.zCoord, 1.0F, t2);
        tessellator.addVertexWithUV(x + 0.5 + a.xCoord + e.xCoord, y + 0.5 + a.yCoord + e.yCoord, z + 0.5 + a.zCoord + e.zCoord, 0.0F, t2);
        tessellator.addVertexWithUV(x + 0.5 + a.xCoord, y + 0.5 + a.yCoord, z + 0.5 + a.zCoord, 0.0F, t3);
    }
}