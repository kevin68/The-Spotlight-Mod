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
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiSliderButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.ISliderButton;

public class GuiSpotLightCreateKey extends GuiContainer implements ISliderButton
{
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");

	protected InventoryPlayer invPlayer;
	protected TileEntitySpotLight tile;
	protected World world;
	private GuiBooleanButton helpButton;

	public GuiSpotLightCreateKey(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World world)
	{
		super(new ContainerSpotLight(tileEntity, playerInventory, world, 8));
		this.invPlayer = playerInventory;
		this.tile = tileEntity;
		this.world = world;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiSliderButton(this, 0, x + 3, y + 20, 170, 20, I18n.format("container.spotlight.time") + ": 0.0", 0));
		this.buttonList.add(new GuiButton(1, x + 13, y + 115, 150, 20, I18n.format("container.spotlight.back")));
		this.buttonList.add(new GuiButton(2, x + 13, y + 90, 150, 20, I18n.format("container.spotlight.createkey")));
		this.buttonList.add(this.helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
		PacketSender.send(EnumLaserInformations.TIMELINECREATEKEYTIME, (byte)0);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		if(guibutton.id == 1)
		{
			this.mc.displayGuiScreen(new GuiSpotLightTimeLine(this.invPlayer, this.tile, this.world));
		}
		if(guibutton.id == 2)
		{
			if(this.tile.getKey((Byte)this.tile.get(EnumLaserInformations.TIMELINECREATEKEYTIME) & 0xFF) != null && this.tile.getKey((Byte)this.tile.get(EnumLaserInformations.TIMELINECREATEKEYTIME) & 0xFF).isActive())
			{
				this.mc.displayGuiScreen(new GuiSpotLightConfirm(this.tile, this.invPlayer, this.world, I18n.format("container.spotlight.sure") + " " + I18n.format("container.spotlight.overwrite"), I18n.format("container.spotlight.overwrite"), I18n.format("container.spotlight.cancel"), 1));
			}
			else
			{
				UtilSpotLight.createKey(this.tile);
			}
		}
		if(guibutton.id == 20)
		{
			this.helpButton.toggle();
		}
	}

	@Override
	public void handlerSliderAction(int sliderId, float sliderValue)
	{
		if(sliderId == 0)
		{
			PacketSender.send(EnumLaserInformations.TIMELINECREATEKEYTIME, (byte)(sliderValue * 119));
		}
	}

	@Override
	public String getSliderName(int sliderId, float sliderValue)
	{
		String name = "";
		if(sliderId == 0)
		{
			name = I18n.format("container.spotlight.time") + ": " + ((byte)(sliderValue * 119) & 0xFF) / 2.0F;
		}
		return name;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
	{
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		super.drawScreen(mouseX, mouseY, partialRenderTick);

		if(this.helpButton.getIsActive())
		{
			boolean reversed = mouseX > this.width / 2;
			ArrayList<String> list = new ArrayList<String>();
			if(mouseX > x + 13 && mouseX < x + 163)
			{
				if(mouseY > y + 90 && mouseY < y + 110)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.timmeline.addkey.create"), mouseX, this.width, reversed);
				}
				if(mouseY > y + 117 && mouseY < y + 137)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.back"), mouseX, this.width, reversed);
				}
			}

			if(mouseX > x + 0 && mouseX < x + 173 && mouseY > y + 20 && mouseY < y + 40)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.timmeline.addkey.time"), mouseX, this.width, reversed);
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
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.addKey")), x + 5, y + 8, 4210752);
	}
}