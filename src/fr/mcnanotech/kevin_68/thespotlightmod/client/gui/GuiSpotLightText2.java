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
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;
import fr.minecraftforgefrance.ffmtlibs.client.gui.ISliderButton;

public class GuiSpotLightText2 extends GuiContainer implements ISliderButton
{
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

	public InventoryPlayer invPlayer;
	public TileEntitySpotLight tile;
	public World world;
	public GuiBooleanButton helpButton;

	public GuiSpotLightText2(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
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

//		byte h = (Byte)this.tile.get(EnumLaserInformations.TEXTHEIGHT);
//		byte s = (Byte)this.tile.get(EnumLaserInformations.TEXTSCALE);
//		this.buttonList.add(new GuiSliderButton(this, 0, x - 87, y - 20, 350, 20, I18n.format("container.spotlight.scale") + " : " + (int)((s & 0xFF) * 3.96F + 10) + " %", (s & 0xFF) / 250.0F));
//		this.buttonList.add(new GuiSliderButton(this, 1, x - 87, y + 2, 350, 20, I18n.format("container.spotlight.height") + " : " + ((h & 0xFF) - 125), (h & 0xFF) / 250.0F));

		this.buttonList.add(new GuiButton(6, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
		this.buttonList.add(this.helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 6:
		{
			this.mc.displayGuiScreen(new GuiSpotLight(this.invPlayer, this.tile, this.world));
			break;
		}
		case 20:
		{
			this.helpButton.toggle();
			break;
		}
		}
	}

	@Override
	public void handlerSliderAction(int sliderId, float sliderValue)
	{
		switch(sliderId)
		{
		case 0:
//			PacketSender.send(EnumLaserInformations.TEXTSCALE, (byte)(sliderValue * 250.0F));
			break;
		case 1:
//			PacketSender.send(EnumLaserInformations.TEXTHEIGHT, (byte)(sliderValue * 250.0F));
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
		{
			name = I18n.format("container.spotlight.scale") + " : " + (int)(((byte)(sliderValue * 250.0F) & 0xFF) * 3.96F + 10) + " %";
			break;
		}
		case 1:
		{
			name = I18n.format("container.spotlight.height") + " : " + (((byte)(sliderValue * 250.0F) & 0xFF) - 125);
			break;
		}
		}
		return name;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialRendertTick)
	{
		super.drawScreen(mouseX, mouseY, partialRendertTick);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		if(this.helpButton.isActive())
		{
			boolean reversed = mouseX > this.width / 2;
			ArrayList<String> list = new ArrayList<String>();

			if(mouseX > x - 87 && mouseX < x + 263)
			{
				if(mouseY > y - 20 && mouseY < y)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.txtconf2.size"), mouseX, this.width, reversed);
				}
				if(mouseY > y + 2 && mouseY < y + 22)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.txtconf2.height"), mouseX, this.width, reversed);
				}
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
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseYS)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
		this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.text")), x - 30, y - 35, 0xffffff);
	}
}