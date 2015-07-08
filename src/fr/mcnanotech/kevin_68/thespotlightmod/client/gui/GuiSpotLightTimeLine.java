package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.EnumLaserInformations;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.SpotLightEntry;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;

public class GuiSpotLightTimeLine extends GuiContainer
{
	protected InventoryPlayer invPlayer;
	protected TileEntitySpotLight tile;
	protected World world;
	public GuiBooleanButton timeLineModeButton, smoothButton, helpButton;
	public GuiButton removebutton;
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight1.png");
	protected static final ResourceLocation texture2 = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight2.png");
	protected static final ResourceLocation icons = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

	public GuiSpotLightTimeLine(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World world)
	{
		super(new ContainerSpotLight(tileEntity, playerInventory, world, 11));
		this.invPlayer = playerInventory;
		this.tile = tileEntity;
		this.world = world;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.xSize = 256;
		this.ySize = 256;
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		PacketSender.send(EnumLaserInformations.TIMELINELASTKEYSELECTED, (byte)-1);
		this.buttonList.add(new GuiButton(2, x - 27, y + 184, 65, 20, I18n.format("container.spotlight.back")));
		this.buttonList.add(new GuiButton(3, x - 27, y + 69, 120, 20, I18n.format("container.spotlight.addKey")));
		this.buttonList.add(this.timeLineModeButton = new GuiBooleanButton(4, x - 27, y + 157, 120, 20, I18n.format("container.spotlight.timeline") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.timeline") + " " + I18n.format("container.spotlight.off"), (Boolean)this.tile.get(EnumLaserInformations.TIMELINEENABLED)));
		this.buttonList.add(this.removebutton = new GuiButton(5, x - 27, y + 91, 120, 20, I18n.format("container.spotlight.deleteKey")));
		this.buttonList.add(new GuiButton(6, x - 27, y + 113, 120, 20, I18n.format("container.spotlight.settimelineto") + " 0"));
		this.buttonList.add(this.smoothButton = new GuiBooleanButton(7, x - 27, y + 135, 120, 20, I18n.format("container.spotlight.smooth"), (Boolean)this.tile.get(EnumLaserInformations.TIMELINESMOOTH)));
		this.removebutton.enabled = false;
		this.buttonList.add(this.helpButton = new GuiBooleanButton(8, x + 220, y + 185, 20, 20, "?", false));

		for(int i = 0; i < 121; i++)
		{
			if(this.tile.getKey(i) != null && this.tile.getKey(i).isActive())
			{
				this.buttonList.add(new GuiTimeKey(10 + i, this.width / 2 - 149 + (int)(i * 2.5), y + 50 + i % 2 * 4));
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		if(guibutton.id == 2)
		{
			this.mc.displayGuiScreen(new GuiSpotLight(this.invPlayer, this.tile, this.world));
		}
		else if(guibutton.id == 3)
		{
			this.mc.displayGuiScreen(new GuiSpotLightCreateKey(this.invPlayer, this.tile, this.world));
		}
		else if(guibutton.id == 4)
		{
			this.timeLineModeButton.toggle();
			PacketSender.send(EnumLaserInformations.TIMELINEENABLED, this.timeLineModeButton.getIsActive());
		}
		else if(guibutton.id == 5)
		{
			if(this.tile.getKey((Byte)this.tile.get(EnumLaserInformations.TIMELINELASTKEYSELECTED) & 0xFF) != null && this.tile.getKey((Byte)this.tile.get(EnumLaserInformations.TIMELINECREATEKEYTIME) & 0xFF).isActive())
			{
				this.mc.displayGuiScreen(new GuiSpotLightConfirm(this.tile, this.invPlayer, this.world, I18n.format("container.spotlight.sure") + " " + I18n.format("container.spotlight.deleteKey"), I18n.format("container.spotlight.deleteKey"), I18n.format("container.spotlight.cancel"), 0));
			}
		}
		else if(guibutton.id == 6)
		{
			PacketSender.send(EnumLaserInformations.TIMELINETIME, 0);
		}
		else if(guibutton.id == 7)
		{
			this.smoothButton.toggle();
			PacketSender.send(EnumLaserInformations.TIMELINESMOOTH, this.smoothButton.getIsActive());
		}
		else if(guibutton.id == 8)
		{
			this.helpButton.toggle();
		}
		else if(guibutton.id >= 10)
		{
			int keyid = guibutton.id - 10;
			if(this.tile.getKey(keyid) != null)
			{
				PacketSender.send(EnumLaserInformations.TIMELINELASTKEYSELECTED, (byte)keyid);
				this.removebutton.enabled = true;
			}
		}
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
			if(mouseX > x - 27 && mouseX < x + 93)
			{
				if(mouseY > y + 69 && mouseY < y + 89)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.timeline.addkey"), mouseX, this.width, reversed);
				}

				if(mouseY > y + 91 && mouseY < y + 111)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.timeline.delkey"), mouseX, this.width, reversed);
				}

				if(mouseY > y + 113 && mouseY < y + 133)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.timeline.set0"), mouseX, this.width, reversed);
				}

				if(mouseY > y + 135 && mouseY < y + 155)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.timeline.smooth"), mouseX, this.width, reversed);
				}

				if(mouseY > y + 157 && mouseY < y + 177)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.timelineswitch"), mouseX, this.width, reversed);
				}
			}

			if(mouseX > x - 20 && mouseX < x + 282 && mouseY > y + 40 && mouseY < y + 61)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.timeline.timeline"), mouseX, this.width, reversed);
			}

			if(mouseX > x + 95 && mouseX < x + 282 && mouseY > y + 69 && mouseY < y + 180)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.timeline.infos"), mouseX, this.width, reversed);
			}

			if(mouseX > x - 27 && mouseX < x + 38 && mouseY > y + 184 && mouseY < y + 204)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.back"), mouseX, this.width, reversed);
			}

			if(mouseX > x + 220 && mouseX < x + 240 && mouseY > y + 182 && mouseY < y + 202)
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
		this.mc.renderEngine.bindTexture(texture);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x - 35, y + 19, 0, 0, this.xSize, this.ySize);
		this.mc.renderEngine.bindTexture(texture2);
		this.drawTexturedModalRect(x + 135, y + 19, 0, 0, this.xSize, this.ySize);
		this.mc.renderEngine.bindTexture(icons);
		this.drawTexturedModalRect(x - 20, y + 40, 0, 59, 256, 21);
		this.drawTexturedModalRect(x + 225, y + 40, 0, 81, 57, 21);
		this.drawTexturedModalRect(x - 20 + (Integer)this.tile.get(EnumLaserInformations.TIMELINETIME) / 4, y + 40, 0, 105, 1, 12);

		if(this.tile.getKey((Byte)this.tile.get(EnumLaserInformations.TIMELINELASTKEYSELECTED) & 0xFF) != null)
		{
			SpotLightEntry entry = this.tile.getKey((Byte)this.tile.get(EnumLaserInformations.TIMELINELASTKEYSELECTED) & 0xFF);
			this.drawTexturedModalRect(x - 22 + (int)(((Byte)this.tile.get(EnumLaserInformations.TIMELINELASTKEYSELECTED) & 0xFF) * 2.5), y + 62, 0, 115, 5, 6);
			drawString(this.fontRendererObj, EnumChatFormatting.RED + I18n.format("container.spotlight.red") + " : " + ((Byte)entry.get(EnumLaserInformations.LASERRED) & 0xFF), x + 100, y + 70, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.GREEN + I18n.format("container.spotlight.green") + " : " + ((Byte)entry.get(EnumLaserInformations.LASERGREEN) & 0xFF), x + 100, y + 80, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.BLUE + I18n.format("container.spotlight.blue") + " : " + ((Byte)entry.get(EnumLaserInformations.LASERBLUE) & 0xFF), x + 100, y + 90, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.DARK_RED + I18n.format("container.spotlight.red") + " : " + ((Byte)entry.get(EnumLaserInformations.LASERSECRED) & 0xFF), x + 185, y + 70, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.DARK_GREEN + I18n.format("container.spotlight.green") + " : " + ((Byte)entry.get(EnumLaserInformations.LASERSECGREEN) & 0xFF), x + 185, y + 80, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.DARK_BLUE + I18n.format("container.spotlight.blue") + " : " + ((Byte)entry.get(EnumLaserInformations.LASERSECBLUE) & 0xFF), x + 185, y + 90, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.WHITE + I18n.format("container.spotlight.angle") + " 1 : " + entry.get(EnumLaserInformations.LASERANGLE1), x + 100, y + 100, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.WHITE + I18n.format("container.spotlight.angle") + " 2 : " + entry.get(EnumLaserInformations.LASERANGLE2), x + 185, y + 100, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.WHITE + I18n.format("container.spotlight.rotate") + " : " + ((Boolean)entry.get(EnumLaserInformations.LASERAUTOROTATE) ? I18n.format("container.spotlight.true") : I18n.format("container.spotlight.false")), x + 100, y + 110, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.WHITE + I18n.format("container.spotlight.rotationspeed") + " : " + entry.get(EnumLaserInformations.LASERROTATIONSPEED), x + 100, y + 120, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.WHITE + I18n.format("container.spotlight.rotationreverse") + " : " + ((Boolean)entry.get(EnumLaserInformations.LASERREVERSEROTATION) ? I18n.format("container.spotlight.true") : I18n.format("container.spotlight.false")), x + 100, y + 130, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.WHITE + I18n.format("container.spotlight.secondlazer") + " : " + ((Boolean)entry.get(EnumLaserInformations.LASERSECONDARY) ? I18n.format("container.spotlight.true") : I18n.format("container.spotlight.false")), x + 100, y + 140, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.WHITE + I18n.format("container.spotlight.axis") + " : " + ((Byte)entry.get(EnumLaserInformations.LASERDISPLAYAXE) == 0 ? "y" : (Byte)entry.get(EnumLaserInformations.LASERDISPLAYAXE) == 1 ? "x" : "z"), x + 100, y + 150, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.WHITE + I18n.format("container.spotlight.laserMode") + " : " + ((Boolean)entry.get(EnumLaserInformations.LASERDOUBLE) ? I18n.format("container.spotlight.double") : I18n.format("container.spotlight.simple")), x + 100, y + 160, 0xffffff);
			drawString(this.fontRendererObj, EnumChatFormatting.WHITE + I18n.format("container.spotlight.size") + " " + I18n.format("container.spotlight.main") + " : " + entry.get(EnumLaserInformations.LASERMAINSIZE) + " " + I18n.format("container.spotlight.sec") + " : " + entry.get(EnumLaserInformations.LASERSECSIZE), x + 100, y + 170, 0xffffff);
		}

		this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.timeline")), x - 25, y + 28, 4210752);
	}
}