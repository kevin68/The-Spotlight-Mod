package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.EnumLaserInformations;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiSliderButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.ISliderButton;

public class GuiSpotLightText extends GuiContainer implements ISliderButton
{
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

	public InventoryPlayer invPlayer;
	public TileEntitySpotLight tile;
	public World world;
	public GuiTextField txtField;
	public GuiBooleanButton rotateButton, revRotaButton, helpButton;

	public GuiSpotLightText(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
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

		Keyboard.enableRepeatEvents(true);
		txtField = new GuiTextField(10, fontRendererObj, x - 40, y - 40, 256, 12);
		byte r = (Byte)tile.get(EnumLaserInformations.TEXTRED);
		byte g = (Byte)tile.get(EnumLaserInformations.TEXTGREEN);
		byte b = (Byte)tile.get(EnumLaserInformations.TEXTBLUE);
		txtField.setTextColor((r & 0xFF * 65536) + (g & 0xFF * 256) + (b & 0xFF));
		txtField.setEnableBackgroundDrawing(true);
		txtField.setMaxStringLength(40);
		txtField.setEnabled(true);
		txtField.setText((String)tile.get(EnumLaserInformations.TEXT));

		buttonList.add(new GuiSliderButton(this, 0, x - 40, y - 20, 256, 20, I18n.format("container.spotlight.red") + " : " + (r & 0xFF), (r & 0xFF) / 255.0F));
		buttonList.add(new GuiSliderButton(this, 1, x - 40, y + 2, 256, 20, I18n.format("container.spotlight.green") + " : " + (g & 0xFF), (g & 0xFF) / 255.0F));
		buttonList.add(new GuiSliderButton(this, 2, x - 40, y + 25, 256, 20, I18n.format("container.spotlight.blue") + " : " + (b & 0xFF), (b & 0xFF) / 255.0F));
		int a = (Integer)tile.get(EnumLaserInformations.TEXTANGLE1);
		buttonList.add(new GuiSliderButton(this, 3, x - 40, y + 48, 256, 20, I18n.format("container.spotlight.angle") + " 1 : " + a, a / 360.0F));
		buttonList.add(rotateButton = new GuiBooleanButton(7, x - 40, y + 94, 127, 20, I18n.format("container.spotlight.rotate") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotate") + " " + I18n.format("container.spotlight.off"), (Boolean)tile.get(EnumLaserInformations.TEXTAUTOROTATE)));
		buttonList.add(revRotaButton = new GuiBooleanButton(8, x + 90, y + 72, 127, 20, I18n.format("container.spotlight.rotationreverse") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotationreverse") + " " + I18n.format("container.spotlight.off"), (Boolean)tile.get(EnumLaserInformations.TEXTREVERSEROTATION)));
		byte s = (Byte)tile.get(EnumLaserInformations.TEXTREVERSEROTATION);
		buttonList.add(new GuiSliderButton(this, 5, x - 40, y + 72, 127, 20, I18n.format("container.spotlight.rotationspeed") + " : " + (s & 0xFF), s / 20.0F));

		buttonList.add(new GuiButton(6, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
		buttonList.add(helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 6:
			mc.displayGuiScreen(new GuiSpotLight(invPlayer, tile, world));
			break;
		case 7:
			rotateButton.toggle();
			PacketSender.send(EnumLaserInformations.TEXTAUTOROTATE, rotateButton.getIsActive());
			break;
		case 8:
			revRotaButton.toggle();
			PacketSender.send(EnumLaserInformations.TEXTREVERSEROTATION, revRotaButton.getIsActive());
			break;
		case 20:
			helpButton.toggle();
			break;
		}
	}

	@Override
	public void handlerSliderAction(int sliderId, float sliderValue)
	{
		byte r = (Byte)tile.get(EnumLaserInformations.TEXTRED);
		byte g = (Byte)tile.get(EnumLaserInformations.TEXTGREEN);
		byte b = (Byte)tile.get(EnumLaserInformations.TEXTBLUE);

		switch(sliderId)
		{
		case 0:
			PacketSender.send(EnumLaserInformations.TEXTRED, (byte)(sliderValue * 255.0F));
			r = (Byte)tile.get(EnumLaserInformations.TEXTRED);
			txtField.setTextColor((r & 0xFF * 65536) + (g & 0xFF * 256) + (b & 0xFF));
			break;
		case 1:
			PacketSender.send(EnumLaserInformations.TEXTGREEN, (byte)(sliderValue * 255.0F));
			g = (Byte)tile.get(EnumLaserInformations.TEXTGREEN);
			txtField.setTextColor((r & 0xFF * 65536) + (g & 0xFF * 256) + (b & 0xFF));
			break;
		case 2:
			PacketSender.send(EnumLaserInformations.TEXTBLUE, (byte)(sliderValue * 255.0F));
			b = (Byte)tile.get(EnumLaserInformations.TEXTBLUE);
			txtField.setTextColor((r & 0xFF * 65536) + (g & 0xFF * 256) + (b & 0xFF));
			break;
		case 3:
			PacketSender.send(EnumLaserInformations.TEXTANGLE1, (int)(sliderValue * 360.0F));
			break;
		case 5:
			PacketSender.send(EnumLaserInformations.TEXTROTATIONSPEED, (byte)(sliderValue * 20.0F));
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
			name = I18n.format("container.spotlight.red") + " : " + ((byte)(sliderValue * 255.0F) & 0xFF);
			break;
		case 1:
			name = I18n.format("container.spotlight.green") + " : " + ((byte)(sliderValue * 255.0F) & 0xFF);
			break;
		case 2:
			name = I18n.format("container.spotlight.blue") + " : " + ((byte)(sliderValue * 255.0F) & 0xFF);
			break;
		case 3:
			name = I18n.format("container.spotlight.angle") + " 1 : " + (int)(sliderValue * 360.0F);
			break;
		case 5:
			name = I18n.format("container.spotlight.rotationspeed") + " : " + (int)(sliderValue * 20.0F);
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
		this.drawTexturedModalRect(x, y + 114, 69, 81, xSize, 52);
		fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.text")), x - 30, y - 55, 0xffffff);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialRendertTick)
	{
		super.drawScreen(mouseX, mouseY, partialRendertTick);
		GL11.glDisable(GL11.GL_LIGHTING);
		txtField.drawTextBox();

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		if(helpButton.getIsActive())
		{
			boolean reversed = mouseX > width / 2;
			ArrayList<String> list = new ArrayList<String>();
			if(mouseX > x - 40 && mouseX < x + 216)
			{
				if(mouseY > y - 40 && mouseY < y - 28)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.txt"), mouseX, width, reversed);
				}
				if(mouseY > y - 20 && mouseY < y)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.red"), mouseX, width, reversed);
				}
				if(mouseY > y + 2 && mouseY < y + 22)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.green"), mouseX, width, reversed);
				}
				if(mouseY > y + 24 && mouseY < y + 44)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.blue"), mouseX, width, reversed);
				}

				if(mouseY > y + 46 && mouseY < y + 66)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.angle1"), mouseX, width, reversed);
				}
			}

			if(mouseX > x - 40 && mouseX < x + 87)
			{
				if(mouseY > y + 72 && mouseY < y + 92)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.rotationspeed"), mouseX, width, reversed);
				}
				if(mouseY > y + 94 && mouseY < y + 114)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.rotate"), mouseX, width, reversed);
				}
			}

			if(mouseX > x + 90 && mouseX < x + 216 && mouseY > y + 72 && mouseY < y + 92)
			{
				list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.reverserotation"), mouseX, width, reversed);
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
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void keyTyped(char chr, int chrValue)
	{
		if(txtField.textboxKeyTyped(chr, chrValue))
		{
			PacketSender.send(EnumLaserInformations.TEXT, txtField.getText());
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		txtField.mouseClicked(mouseX, mouseY, mouseButton);
	}
}