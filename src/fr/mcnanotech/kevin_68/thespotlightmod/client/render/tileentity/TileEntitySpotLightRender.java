package fr.mcnanotech.kevin_68.thespotlightmod.client.render.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.client.model.ModelSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMVec3;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.BeamVec;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;

@SideOnly(Side.CLIENT)
public class TileEntitySpotLightRender extends TileEntitySpecialRenderer// TileEntityInventorySpecialRenderer
{
	private final ModelSign modelSign = new ModelSign();
	private ModelSpotLight model;

	// @Override
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
		float f2 = tileentity.getWorld().getTotalWorldTime() + tick;
		double d3 = f2 * 0.025D * (1.0D - (b0 & 1) * 2.5D);
		double angle2Deg = (tileentity.getAngle2() & 0xFF);
		double a2 = tileentity.isAutoRotate() ? ((d3 * ((tileentity.getRotationSpeed() & 0xFF) / 4.0D)) * (tileentity.isReverseRotation() ? -1.0D : 1.0D)) : Math.toRadians(angle2Deg);

		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
		this.bindTexture(new ResourceLocation(TheSpotLightMod.MODID, "textures/blocks/spotlight.png"));
		int ti = (int)((tileentity.getWorld().getTotalWorldTime() / 4) % 3);
		model = new ModelSpotLight(ti);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);

		double angl = tileentity.getAngle1();
		double angl2 = Math.toDegrees(a2);

		if(tileentity.getAxe() == 0)
		{
			GL11.glRotated(angl2, 0.0F, 1.0F, 0.0F);
			GL11.glRotated(-angl, 0.0F, 0.0F, 1.0F);
			GL11.glTranslated(0.0F, Math.cos(Math.PI * (1.0F / 180.0F) * angl) - 1, 0.0F);
			GL11.glTranslated(Math.cos(Math.PI * (1.0F / 180.0F) * angl + Math.PI / 2.0F), 0.0F, 0.0F);

		}
		else if(tileentity.getAxe() == 1)
		{
			GL11.glRotated(-angl2, 1.0F, 0.0F, 0.0F);
			GL11.glRotated(angl + 90, 0.0F, 0.0F, 1.0F);
			GL11.glTranslated(Math.cos(Math.PI * 1 / 180 * angl) * Math.cos(Math.PI * 1 / 180 * angl2), Math.cos(Math.PI * 1 / 180 * angl2) * Math.cos(Math.PI * 1 / 180 * angl + Math.PI / 2) - 1, -Math.cos(Math.PI * 1 / 180 * angl2 + Math.PI / 2));
		}
		else if(tileentity.getAxe() == 2)
		{
			GL11.glRotated(-angl2, 0.0F, 0.0F, 1.0F);
			GL11.glRotated(angl, 1.0F, 0.0F, 0.0F);
			GL11.glRotated(90, 1.0F, 0.0F, 0.0F);
			GL11.glTranslated(Math.cos(Math.PI * 1 / 180 * angl2 + Math.PI / 2), Math.cos(Math.PI * 1 / 180 * angl2) * Math.cos(Math.PI * 1 / 180 * angl + Math.PI / 2) - 1, -Math.cos(Math.PI * 1 / 180 * angl2) * Math.cos(Math.PI * 1 / 180 * angl));
		}

		this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();

		float f1 = tileentity.isActive();
		GlStateManager.alphaFunc(516, 0.1F);
		if(f1 > 0.0F)
		{
			Tessellator tess = Tessellator.getInstance();
			WorldRenderer worldrenderer = tess.getWorldRenderer();
			GuiHelper.bindTexture(UtilSpotLight.getEntryByName(tileentity.getTextureName()).getPath());
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
			GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);

			float f3 = -f2 * 0.2F - MathHelper.floor_float(-f2 * 0.1F);

			double t2 = -1.0F - f3;
			double t3 = tileentity.getLaserHeight() * f1 * (0.5D / /* d4 */Math.sqrt(Math.pow(b0 * ((tileentity.getMainLaserSize() & 0xFF) / 200.0D), 2) / 2)/* d4 */) + t2;

			worldrenderer.startDrawingQuads();
			worldrenderer.setColorRGBA_F((tileentity.getRed() & 0xFF) / 255.0F, (tileentity.getGreen() & 0xFF) / 255.0F, (tileentity.getBlue() & 0xFF) / 255.0F, 0.125F);
			drawBeam(worldrenderer, x, y, z, t2, t3, tileentity.bVec[0]);
			tess.draw();

			if(tileentity.isSideLaser())
			{
				worldrenderer.startDrawingQuads();
				worldrenderer.setColorRGBA_F((tileentity.getRed() & 0xFF) / 255.0F, (tileentity.getGreen() & 0xFF) / 255.0F, (tileentity.getBlue() & 0xFF) / 255.0F, 0.125F);
				drawBeam(worldrenderer, x, y, z, t2, t3, tileentity.bVec[1]);
				tess.draw();
			}

			GuiHelper.bindTexture(UtilSpotLight.getEntryByName(tileentity.getSecTextureName()).getPath());
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.depthMask(false);

			if(tileentity.isSecondaryLaser())
			{
				worldrenderer.startDrawingQuads();
				worldrenderer.setColorRGBA_F((tileentity.getSecRed() & 0xFF) / 255.0F, (tileentity.getSecGreen() & 0xFF) / 255.0F, (tileentity.getSecBlue() & 0xFF) / 255.0F, 0.125F);
				drawBeam(worldrenderer, x, y, z, t2, t3, tileentity.bVec[2]);
				tess.draw();

				if(tileentity.isSideLaser())
				{
					worldrenderer.startDrawingQuads();
					worldrenderer.setColorRGBA_F((tileentity.getSecRed() & 0xFF) / 255.0F, (tileentity.getSecGreen() & 0xFF) / 255.0F, (tileentity.getSecBlue() & 0xFF) / 255.0F, 0.125F);
					drawBeam(worldrenderer, x, y, z, t2, t3, tileentity.bVec[3]);
					tess.draw();
				}
			}

			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			GlStateManager.depthMask(true);

			if(tileentity.isTextEnabled() || true)
			{
				GL11.glPushMatrix();
				float f11 = 0.6666667F;
				float f21 = 0.0F;
				float d21 = tileentity.getWorld().getTotalWorldTime() + tick;
				byte b1 = 1;
				double d31 = d21 * 0.025D * (1.0D - (b1 & 1) * 2.5D);
				double i1 = (tileentity.isTxtReverseRotation() ? -1.0D : 1.0D);
				GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f11, (float)z + 0.5F);
				GlStateManager.rotate(-f21, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.0F, -0.4F, 0.0F);
				GlStateManager.scale(0.9D, 0.9D, 0.9D);
				if(tileentity.isTxtAutoRotate())
				{
					GL11.glRotatef((float)((d31 * (tileentity.getTxtRotationSpeed() & 0xFF) * i1) * 16), 0.0F, 1.0F, 0.0F);
				}
				else
				{
					GL11.glRotatef(tileentity.getTxtAngle1(), 0.0F, 1.0F, 0.0F);
				}
				this.modelSign.signStick.showModel = false;
				GlStateManager.pushMatrix();
				GlStateManager.scale(f11, -f11, -f11);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				this.modelSign.renderSign();
				GlStateManager.popMatrix();
				FontRenderer fontrenderer = this.getFontRenderer();
				f21 = 0.016666668F * f11;
				GlStateManager.translate(0.0F, 0.5F * f11, 0.07F * f11);
				GlStateManager.scale(f21 * 5, -f21 * 5, f21 * 5);
				GL11.glNormal3f(0.0F, 0.0F, -1.0F * f21);
				GlStateManager.depthMask(false);
				GlStateManager.translate(0.0F, -((tileentity.getTxtHeight() & 0xFF) - 125.0F) * 2, 0.0F);
				GlStateManager.translate(0.0F, -13 + ((int)((tileentity.getTxtScale() & 0xFF) * 3.96F + 10) / 7.8F), 0.0F);
				GlStateManager.scale(((int)((tileentity.getTxtScale() & 0xFF) * 3.96F + 10)) / 100.0F, ((int)((tileentity.getTxtScale() & 0xFF) * 3.96F + 10)) / 100.0F, ((int)((tileentity.getTxtScale() & 0xFF) * 3.96F + 10)) / 100.0F);
				GlStateManager.scale(1.0, 1.0, 1.0);
				String s = tileentity.getDisplayText();
				fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, -20, ((tileentity.getTxtRed() & 0xFF) * 65536) + ((tileentity.getTxtGreen() & 0xFF) * 256) + (tileentity.getTxtBlue() & 0xFF));
				GlStateManager.depthMask(true);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.popMatrix();
			}
		}
		GlStateManager.alphaFunc(516, 0.5F);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float tick, int p_180535_9_)
	{
		this.renderTileEntitySpotLightAt((TileEntitySpotLight)tileentity, x, y, z, tick);
	}

	public void drawBeam(WorldRenderer worldrenderer, double x, double y, double z, double t2, double t3, BeamVec vec)
	{
		TSMVec3[] v = vec.getVecs();
		TSMVec3 e = vec.getLenVec();

		for(int i = 0; i < v.length; i++)
		{
			worldrenderer.addVertexWithUV(x + 0.5 + v[i].xCoord, y + 0.5 + v[i].yCoord, z + 0.5 + v[i].zCoord, 1.0F, t3);
			worldrenderer.addVertexWithUV(x + 0.5 + v[i].xCoord + e.xCoord, y + 0.5 + v[i].yCoord + e.yCoord, z + 0.5 + v[i].zCoord + e.zCoord, 1.0F, t2);
			worldrenderer.addVertexWithUV(x + 0.5 + v[(i == (v.length - 1) ? 0 : i + 1)].xCoord + e.xCoord, y + 0.5 + v[(i == (v.length - 1) ? 0 : i + 1)].yCoord + e.yCoord, z + 0.5 + v[(i == (v.length - 1) ? 0 : i + 1)].zCoord + e.zCoord, 0.0F, t2);
			worldrenderer.addVertexWithUV(x + 0.5 + v[(i == (v.length - 1) ? 0 : i + 1)].xCoord, y + 0.5 + v[(i == (v.length - 1) ? 0 : i + 1)].yCoord, z + 0.5 + v[(i == (v.length - 1) ? 0 : i + 1)].zCoord, 0.0F, t3);
		}
	}
}