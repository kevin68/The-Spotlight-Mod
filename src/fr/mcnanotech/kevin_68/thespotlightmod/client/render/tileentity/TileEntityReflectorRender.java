package fr.mcnanotech.kevin_68.thespotlightmod.client.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.client.model.ModelReflector;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntityReflector;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.EnumLaserInformations;

@SideOnly(Side.CLIENT)
public class TileEntityReflectorRender extends TileEntitySpecialRenderer// TileEntityInventorySpecialRenderer
{
	private ModelReflector model = new ModelReflector();

	public void renderTileEntitySpotLightAt(TileEntityReflector tile, double x, double y, double z, float tick)
	{
		int angle1Deg = (Integer)tile.get(EnumLaserInformations.LASERANGLE1);
		double angle2Deg = (Byte)tile.get(EnumLaserInformations.LASERANGLE2) & 0xFF;

		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
		bindTexture(new ResourceLocation(TheSpotLightMod.MODID, "textures/blocks/reflector.png"));
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);

		GL11.glRotated(angle2Deg, 0.0F, 1.0F, 0.0F);
		GL11.glRotated(-angle1Deg, 0.0F, 0.0F, 1.0F);
		GL11.glTranslated(0.0F, Math.cos(Math.PI * (1.0F / 180.0F) * angle1Deg) - 1, 0.0F);
		GL11.glTranslated(Math.cos(Math.PI * (1.0F / 180.0F) * angle1Deg + Math.PI / 2.0F), 0.0F, 0.0F);

		model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float tick, int p_180535_9_)
	{
		renderTileEntitySpotLightAt((TileEntityReflector)tileentity, x, y, z, tick);
	}
}