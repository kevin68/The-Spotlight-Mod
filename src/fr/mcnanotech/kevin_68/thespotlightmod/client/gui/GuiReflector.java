package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerReflector;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntityReflector;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.EnumLaserInformations;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiSliderButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.ISliderButton;

public class GuiReflector extends GuiContainer implements ISliderButton
{
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

	private TileEntityReflector te;

	public GuiReflector(InventoryPlayer playerInventory, TileEntityReflector tileEntity, World wrld)
	{
		super(new ContainerReflector(tileEntity, playerInventory, wrld));
		te = tileEntity;
	}

	@Override
	public void initGui()
	{
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		int a1 = (Integer)te.get(EnumLaserInformations.LASERANGLE1);
		byte a2 = (Byte)te.get(EnumLaserInformations.LASERANGLE2);
		buttonList.add(new GuiSliderButton(this, 0, x - 40, y - 20, 256, 20, I18n.format("container.spotlight.angle") + " 1 : " + a1, a1 / 360.0F));
		buttonList.add(new GuiSliderButton(this, 1, x - 40, y + 2, 256, 20, I18n.format("container.spotlight.angle") + " 2 : " + (a2 & 0xFF), (a2 & 0xFF) / 180.0F));

	}

	@Override
	public void handlerSliderAction(int sliderId, float sliderValue)
	{
		switch(sliderId)
		{
		case 0:
			PacketSender.send(EnumLaserInformations.LASERANGLE1, (int)(sliderValue * 360));
			break;
		case 1:
			PacketSender.send(EnumLaserInformations.LASERANGLE2, (byte)(sliderValue * 180));
			break;
		}
	}

	@Override
	public String getSliderName(int sliderId, float sliderValue)
	{
		String name = "";
		switch(sliderId)
		{
		case 0:
			name = I18n.format("container.spotlight.angle") + " 1 : " + (int)(sliderValue * 360);
			break;
		case 1:
			name = I18n.format("container.spotlight.angle") + " 2 : " + ((byte)(sliderValue * 180) & 0xFF);
			break;
		}
		return name;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(x, y + 114, 69, 134, xSize, 33);
		fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.beamspecs")), x - 30, y - 35, 0xffffff);
	}
}