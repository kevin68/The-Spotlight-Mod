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
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;
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
		this.invPlayer = playerInventory;
		this.tile = tileEntity;
		this.world = wrld;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		Keyboard.enableRepeatEvents(true);
		this.txtField = new GuiTextField(10, this.fontRendererObj, x - 40, y - 40, 256, 12);
//		byte r = (Byte)this.tile.get(EnumLaserInformations.TEXTRED);
//		byte g = (Byte)this.tile.get(EnumLaserInformations.TEXTGREEN);
//		byte b = (Byte)this.tile.get(EnumLaserInformations.TEXTBLUE);
//		this.txtField.setTextColor((r & 0xFF * 65536) + (g & 0xFF * 256) + (b & 0xFF));
		this.txtField.setEnableBackgroundDrawing(true);
		this.txtField.setMaxStringLength(40);
		this.txtField.setEnabled(true);
//		this.txtField.setText((String)this.tile.get(EnumLaserInformations.TEXT));

//		this.buttonList.add(new GuiSliderButton(this, 0, x - 40, y - 20, 256, 20, I18n.format("container.spotlight.red") + " : " + (r & 0xFF), (r & 0xFF) / 255.0F));
//		this.buttonList.add(new GuiSliderButton(this, 1, x - 40, y + 2, 256, 20, I18n.format("container.spotlight.green") + " : " + (g & 0xFF), (g & 0xFF) / 255.0F));
//		this.buttonList.add(new GuiSliderButton(this, 2, x - 40, y + 25, 256, 20, I18n.format("container.spotlight.blue") + " : " + (b & 0xFF), (b & 0xFF) / 255.0F));
//		int a = (Integer)this.tile.get(EnumLaserInformations.TEXTANGLE1);
//		this.buttonList.add(new GuiSliderButton(this, 3, x - 40, y + 48, 256, 20, I18n.format("container.spotlight.angle") + " 1 : " + a, a / 360.0F));
//		this.buttonList.add(this.rotateButton = new GuiBooleanButton(7, x - 40, y + 94, 127, 20, I18n.format("container.spotlight.rotate") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotate") + " " + I18n.format("container.spotlight.off"), (Boolean)this.tile.get(EnumLaserInformations.TEXTAUTOROTATE)));
//		this.buttonList.add(this.revRotaButton = new GuiBooleanButton(8, x + 90, y + 72, 127, 20, I18n.format("container.spotlight.rotationreverse") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotationreverse") + " " + I18n.format("container.spotlight.off"), (Boolean)this.tile.get(EnumLaserInformations.TEXTREVERSEROTATION)));
//		byte s = (Byte)this.tile.get(EnumLaserInformations.TEXTROTATIONSPEED);
//		this.buttonList.add(new GuiSliderButton(this, 5, x - 40, y + 72, 127, 20, I18n.format("container.spotlight.rotationspeed") + " : " + (s & 0xFF), s / 20.0F));

		this.buttonList.add(new GuiButton(6, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
		this.buttonList.add(this.helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 6:
			this.mc.displayGuiScreen(new GuiSpotLight(this.invPlayer, this.tile, this.world));
			break;
		case 7:
			this.rotateButton.toggle();
//			PacketSender.send(EnumLaserInformations.TEXTAUTOROTATE, this.rotateButton.isActive());
			break;
		case 8:
			this.revRotaButton.toggle();
//			PacketSender.send(EnumLaserInformations.TEXTREVERSEROTATION, this.revRotaButton.isActive());
			break;
		case 20:
			this.helpButton.toggle();
			break;
		}
	}

	@Override
	public void handlerSliderAction(int sliderId, float sliderValue)
	{
//		byte r = (Byte)this.tile.get(EnumLaserInformations.TEXTRED);
//		byte g = (Byte)this.tile.get(EnumLaserInformations.TEXTGREEN);
//		byte b = (Byte)this.tile.get(EnumLaserInformations.TEXTBLUE);

		switch(sliderId)
		{
		case 0:
//			PacketSender.send(EnumLaserInformations.TEXTRED, (byte)(sliderValue * 255.0F));
//			r = (Byte)this.tile.get(EnumLaserInformations.TEXTRED);
//			this.txtField.setTextColor((r & 0xFF * 65536) + (g & 0xFF * 256) + (b & 0xFF));
			break;
		case 1:
//			PacketSender.send(EnumLaserInformations.TEXTGREEN, (byte)(sliderValue * 255.0F));
//			g = (Byte)this.tile.get(EnumLaserInformations.TEXTGREEN);
//			this.txtField.setTextColor((r & 0xFF * 65536) + (g & 0xFF * 256) + (b & 0xFF));
			break;
		case 2:
//			PacketSender.send(EnumLaserInformations.TEXTBLUE, (byte)(sliderValue * 255.0F));
//			b = (Byte)this.tile.get(EnumLaserInformations.TEXTBLUE);
//			this.txtField.setTextColor((r & 0xFF * 65536) + (g & 0xFF * 256) + (b & 0xFF));
			break;
		case 3:
//			PacketSender.send(EnumLaserInformations.TEXTANGLE1, (int)(sliderValue * 360.0F));
			break;
		case 5:
//			PacketSender.send(EnumLaserInformations.TEXTROTATIONSPEED, (byte)(sliderValue * 20.0F));
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
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
		this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.text")), x - 30, y - 55, 0xffffff);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialRendertTick)
	{
		super.drawScreen(mouseX, mouseY, partialRendertTick);
		GL11.glDisable(GL11.GL_LIGHTING);
		this.txtField.drawTextBox();

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		if(this.helpButton.isActive())
		{
			boolean reversed = mouseX > this.width / 2;
			ArrayList<String> list = new ArrayList<String>();
			if(mouseX > x - 40 && mouseX < x + 216)
			{
				if(mouseY > y - 40 && mouseY < y - 28)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.txt"), mouseX, this.width, reversed);
				}
				if(mouseY > y - 20 && mouseY < y)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.red"), mouseX, this.width, reversed);
				}
				if(mouseY > y + 2 && mouseY < y + 22)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.green"), mouseX, this.width, reversed);
				}
				if(mouseY > y + 24 && mouseY < y + 44)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.blue"), mouseX, this.width, reversed);
				}

				if(mouseY > y + 46 && mouseY < y + 66)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.angle1"), mouseX, this.width, reversed);
				}
			}

			if(mouseX > x - 40 && mouseX < x + 87)
			{
				if(mouseY > y + 72 && mouseY < y + 92)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.rotationspeed"), mouseX, this.width, reversed);
				}
				if(mouseY > y + 94 && mouseY < y + 114)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.rotate"), mouseX, this.width, reversed);
				}
			}

			if(mouseX > x + 90 && mouseX < x + 216 && mouseY > y + 72 && mouseY < y + 92)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.txtconf1.reverserotation"), mouseX, this.width, reversed);
			}

			if(mouseX > x + 38 && mouseX < x + 138 && mouseY > y + 117 && mouseY < y + 137)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.back"), mouseX, this.width, reversed);
			}

			if(mouseX > x + 180 && mouseX < x + 200 && mouseY > y + 140 && mouseY < y + 160)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.help"), mouseX, this.width, reversed);
			}

			if(list.size() > 0 && (list.get(list.size() - 1) == " " || list.get(list.size() - 1).isEmpty()))
			{
				list.remove(list.size() - 1);
			}
			GuiHelper.drawHoveringText(list, mouseX, mouseY, this.fontRendererObj, reversed ? 0 : 200000, this.height, 0x00ff00);
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
		if(this.txtField.textboxKeyTyped(chr, chrValue))
		{
//			PacketSender.send(EnumLaserInformations.TEXT, this.txtField.getText());
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.txtField.mouseClicked(mouseX, mouseY, mouseButton);
	}
}