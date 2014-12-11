package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.EnumLaserInformations;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiMultipleOptionButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiSliderButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.ISliderButton;

public class GuiSpotLightBeamSpec extends GuiContainer implements ISliderButton
{
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

	public InventoryPlayer invPlayer;
	public TileEntitySpotLight tile;
	public World world;
	public GuiBooleanButton rotateButton, revRotaButton, secLaserButton, sideLaser;
	public GuiMultipleOptionButton axeButton;
	public GuiBooleanButton helpButton;

	public GuiSpotLightBeamSpec(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
	{
		super(new ContainerSpotLight(tileEntity, playerInventory, wrld, 8));
		invPlayer = playerInventory;
		tile = tileEntity;
		world = wrld;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		int a1 = (Integer)tile.get(EnumLaserInformations.LASERANGLE1);
		byte a2 = (Byte)tile.get(EnumLaserInformations.LASERANGLE2);
		buttonList.add(new GuiSliderButton(this, 0, x - 40, y - 20, 256, 20, I18n.format("container.spotlight.angle") + " 1 : " + a1, a1 / 360.0F));
		buttonList.add(new GuiSliderButton(this, 1, x - 40, y + 2, 256, 20, I18n.format("container.spotlight.angle") + " 2 : " + (a2 & 0xFF), (a2 & 0xFF) / 180.0F));
		buttonList.add(rotateButton = new GuiBooleanButton(2, x - 40, y + 24, 127, 20, I18n.format("container.spotlight.rotate") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotate") + " " + I18n.format("container.spotlight.off"), (Boolean)tile.get(EnumLaserInformations.LASERAUTOROTATE)));
		buttonList.add(revRotaButton = new GuiBooleanButton(3, x + 90, y + 24, 127, 20, I18n.format("container.spotlight.rotationreverse") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotationreverse") + " " + I18n.format("container.spotlight.off"), (Boolean)tile.get(EnumLaserInformations.LASERREVERSEROTATION)));
		byte s = (Byte)tile.get(EnumLaserInformations.LASERROTATIONSPEED);
		buttonList.add(new GuiSliderButton(this, 4, x - 40, y + 48, 127, 20, I18n.format("container.spotlight.rotationspeed") + " : " + (s & 0xFF), (s & 0xFF) / 20.0F));
		buttonList.add(secLaserButton = new GuiBooleanButton(5, x + 90, y + 48, 127, 20, I18n.format("container.spotlight.secondlazer") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.secondlazer") + " " + I18n.format("container.spotlight.off"), (Boolean)tile.get(EnumLaserInformations.LASERSECONDARY)));
		buttonList.add(axeButton = new GuiMultipleOptionButton(7, x + 146, y + 113, 70, 20, I18n.format("container.spotlight.axis") + " : ", new String[] {"y", "x", "z"}, (Byte)tile.get(EnumLaserInformations.LASERDISPLAYAXE) & 0xFF));
		buttonList.add(new GuiSliderButton(this, 11, x - 40, y + 70, 256, 20, I18n.format("container.spotlight.laserHeight") + " : " + tile.get(EnumLaserInformations.LASERHEIGHT), (Integer)tile.get(EnumLaserInformations.LASERHEIGHT) / 256.0F));
		buttonList.add(sideLaser = new GuiBooleanButton(8, x - 40, y + 113, 70, 20, I18n.format("container.spotlight.double"), I18n.format("container.spotlight.simple"), (Boolean)tile.get(EnumLaserInformations.LASERDOUBLE)));
		byte mS = (Byte)tile.get(EnumLaserInformations.LASERMAINSIZE), sS = (Byte)tile.get(EnumLaserInformations.LASERSECSIZE);
		buttonList.add(new GuiSliderButton(this, 9, x - 40, y + 92, 127, 20, I18n.format("container.spotlight.sizeMain") + " : " + (mS & 0xFF), (mS & 0xFF) / 100.0F));
		buttonList.add(new GuiSliderButton(this, 10, x + 89, y + 92, 127, 20, I18n.format("container.spotlight.sizeSec") + " : " + (sS & 0xFF), (sS & 0xFF) / 100.0F));
		buttonList.add(new GuiButton(6, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
		buttonList.add(helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 2:
			rotateButton.toggle();
			PacketSender.send(EnumLaserInformations.LASERAUTOROTATE, rotateButton.getIsActive());
			break;
		case 3:
			revRotaButton.toggle();
			PacketSender.send(EnumLaserInformations.LASERREVERSEROTATION, revRotaButton.getIsActive());
			break;
		case 5:
			secLaserButton.toggle();
			PacketSender.send(EnumLaserInformations.LASERSECONDARY, secLaserButton.getIsActive());
			break;
		case 6:
			mc.displayGuiScreen(new GuiSpotLight(invPlayer, tile, world));
			break;
		case 7:
			axeButton.next();
			PacketSender.send(EnumLaserInformations.LASERDISPLAYAXE, (byte)axeButton.getState());
			break;
		case 8:
			sideLaser.toggle();
			PacketSender.send(EnumLaserInformations.LASERDOUBLE, sideLaser.getIsActive());
			break;
		case 20:
			helpButton.toggle();
			break;
		}
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
		case 4:
			PacketSender.send(EnumLaserInformations.LASERROTATIONSPEED, (byte)(sliderValue * 20));
			break;
		case 9:
			PacketSender.send(EnumLaserInformations.LASERMAINSIZE, (byte)(sliderValue * 100));
			break;
		case 10:
			PacketSender.send(EnumLaserInformations.LASERSECSIZE, (byte)(sliderValue * 100));
			break;
		case 11:
			PacketSender.send(EnumLaserInformations.LASERHEIGHT, (int)(sliderValue * 256));
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
		case 4:
			name = I18n.format("container.spotlight.rotationspeed") + " : " + ((byte)(sliderValue * 20) & 0xFF);
			break;
		case 9:
			name = I18n.format("container.spotlight.sizeMain") + " : " + ((byte)(sliderValue * 100) & 0xFF);
			break;
		case 10:
			name = I18n.format("container.spotlight.sizeSec") + " : " + ((byte)(sliderValue * 100) & 0xFF);
			break;
		case 11:
			name = I18n.format("container.spotlight.laserHeight") + " : " + (int)(sliderValue * 256);
			break;
		}
		return name;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
	{
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		super.drawScreen(mouseX, mouseY, partialRenderTick);

		if(helpButton.getIsActive())
		{
			boolean reversed = mouseX > width / 2;
			ArrayList<String> list = new ArrayList<String>();

			if(mouseX > x - 40 && mouseX < x + 216)
			{
				if(mouseY > y - 20 && mouseY < y)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props.angle1"), mouseX, width, reversed);
				}
				if(mouseY > y + 2 && mouseY < y + 22)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props.angle2"), mouseX, width, reversed);
				}
				if(mouseY > y + 68 && mouseY < y + 88)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props.height"), mouseX, width, reversed);
				}
			}

			if(mouseX > x - 40 && mouseX < x + 87)
			{
				if(mouseY > y + 24 && mouseY < y + 44)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props.autorotate"), mouseX, width, reversed);
				}
				if(mouseY > y + 46 && mouseY < y + 66)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props.rotationspeed"), mouseX, width, reversed);
				}
				if(mouseY > y + 90 && mouseY < y + 110)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props.mainlasersize"), mouseX, width, reversed);
				}

				if(mouseY > y + 113 && mouseY < y + 133 && mouseX < x + 30)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props.simple"), mouseX, width, reversed);
				}
			}

			if(mouseX > x + 90 && mouseX < x + 216)
			{
				if(mouseY > y + 24 && mouseY < y + 44)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props.reverserotate"), mouseX, width, reversed);
				}
				if(mouseY > y + 46 && mouseY < y + 66)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props.seclaserswitch"), mouseX, width, reversed);
				}
				if(mouseY > y + 90 && mouseY < y + 110)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props.seclasersize"), mouseX, width, reversed);
				}

				if(mouseY > y + 113 && mouseY < y + 133 && mouseX > x + 146)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props.axe"), mouseX, width, reversed);
				}
			}

			if(mouseX > x + 38 && mouseX < x + 138 && mouseY > y + 117 && mouseY < y + 137)
			{
				list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.back"), mouseX, width, reversed);
			}

			if(mouseX > x + 180 && mouseX < x + 200 && mouseY > y + 140 && mouseY < y + 160)
			{
				list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.help"), mouseX, width, reversed);
			}

			if(list.size() > 0 && (list.get(list.size() - 1) == " " || list.get(list.size() - 1).isEmpty()))
			{
				list.remove(list.size() - 1);
			}
			GuiHelper.drawHoveringText(list, mouseX, mouseY, fontRendererObj, reversed ? 0 : 200000, height, 0x00ff00);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(x, y + 114, 69, 81, xSize, 52);
		fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.beamspecs")), x - 30, y - 35, 0xffffff);
	}
}