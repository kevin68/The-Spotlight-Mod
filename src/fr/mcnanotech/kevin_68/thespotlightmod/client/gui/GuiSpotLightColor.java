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
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiSliderButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.ISliderButton;

public class GuiSpotLightColor extends GuiContainer implements ISliderButton
{
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

	public InventoryPlayer invPlayer;
	public TileEntitySpotLight tileSpotLight;
	public World world;
	public GuiBooleanButton helpButton;

	public GuiSpotLightColor(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
	{
		super(new ContainerSpotLight(tileEntity, playerInventory, wrld, 8));
		invPlayer = playerInventory;
		tileSpotLight = tileEntity;
		world = wrld;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;

		this.buttonList.add(new GuiSliderButton(this, 0, x - 40, y - 20, 256, 20, I18n.format("container.spotlight.red") + " : " + (tileSpotLight.getRed() & 0xFF), (tileSpotLight.getRed() & 0xFF) / 255.0F));
		this.buttonList.add(new GuiSliderButton(this, 1, x - 40, y + 2, 256, 20, I18n.format("container.spotlight.green") + " : " + (tileSpotLight.getGreen() & 0xFF), (tileSpotLight.getGreen() & 0xFF) / 255.0F));
		this.buttonList.add(new GuiSliderButton(this, 2, x - 40, y + 24, 256, 20, I18n.format("container.spotlight.blue") + " : " + (tileSpotLight.getBlue() & 0xFF), (tileSpotLight.getBlue() & 0xFF) / 255.0F));

		this.buttonList.add(new GuiSliderButton(this, 3, x - 40, y + 46, 256, 20, I18n.format("container.spotlight.red") + " : " + (tileSpotLight.getSecRed() & 0xFF), (tileSpotLight.getSecRed() & 0xFF) / 255.0F));
		this.buttonList.add(new GuiSliderButton(this, 4, x - 40, y + 68, 256, 20, I18n.format("container.spotlight.green") + " : " + (tileSpotLight.getSecGreen() & 0xFF), (tileSpotLight.getSecGreen() & 0xFF) / 255.0F));
		this.buttonList.add(new GuiSliderButton(this, 5, x - 40, y + 90, 256, 20, I18n.format("container.spotlight.blue") + " : " + (tileSpotLight.getSecBlue() & 0xFF), (tileSpotLight.getSecBlue() & 0xFF) / 255.0F));

		this.buttonList.add(new GuiButton(6, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
		this.buttonList.add(helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 6:
		{
			this.mc.displayGuiScreen(new GuiSpotLight(invPlayer, tileSpotLight, world));
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
		// TheSpotLightMod.networkTest.sendToServer(new PacketTest("Sa"));
		PacketSender.sendSpotLightPacketByte(tileSpotLight, (byte)sliderId, (byte)(sliderValue * 255.0F));
	}

	@Override
	public String getSliderName(int sliderId, float sliderValue)
	{
		String name = "";
		switch(sliderId)
		{
		case 0:
		{
			name = I18n.format("container.spotlight.red") + " : " + ((byte)(sliderValue * 255.0F) & 0xFF);
			break;
		}
		case 1:
		{
			name = I18n.format("container.spotlight.green") + " : " + ((byte)(sliderValue * 255.0F) & 0xFF);
			break;
		}
		case 2:
		{
			name = I18n.format("container.spotlight.blue") + " : " + ((byte)(sliderValue * 255.0F) & 0xFF);
			break;
		}
		case 3:
		{
			name = I18n.format("container.spotlight.red") + " : " + ((byte)(sliderValue * 255.0F) & 0xFF);
			break;
		}
		case 4:
		{
			name = I18n.format("container.spotlight.green") + " : " + ((byte)(sliderValue * 255.0F) & 0xFF);
			break;
		}
		case 5:
		{
			name = I18n.format("container.spotlight.blue") + " : " + ((byte)(sliderValue * 255.0F) & 0xFF);
			break;
		}
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
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.colors.red"), mouseX, width, reversed);
				}
				if(mouseY > y + 2 && mouseY < y + 22)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.colors.green"), mouseX, width, reversed);
				}
				if(mouseY > y + 24 && mouseY < y + 44)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.colors.blue"), mouseX, width, reversed);
				}
				if(mouseY > y + 46 && mouseY < y + 66)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.colors.secred"), mouseX, width, reversed);
				}
				if(mouseY > y + 68 && mouseY < y + 88)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.colors.secgreen"), mouseX, width, reversed);
				}
				if(mouseY > y + 90 && mouseY < y + 110)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.colors.secblue"), mouseX, width, reversed);
				}
			}

			if(mouseX > x + 38 && mouseX < x + 138 && mouseY > y + 117 && mouseY < y + 137)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.back"), mouseX, width, reversed);
			}

			if(mouseX > x + 180 && mouseX < x + 200 && mouseY > y + 140 && mouseY < y + 160)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.help"), mouseX, width, reversed);
			}

			if(list.size() > 0 && (list.get(list.size() - 1) == " " || list.get(list.size() - 1).isEmpty()))
			{
				list.remove(list.size() - 1);
			}
			GuiHelper.drawHoveringText(list, mouseX, mouseY, this.fontRendererObj, reversed ? 0 : 200000, height, 0x00ff00);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(x, y + 114, 69, 81, xSize, 52);
		this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.color")), x - 30, y - 35, 0xffffff);
	}
}