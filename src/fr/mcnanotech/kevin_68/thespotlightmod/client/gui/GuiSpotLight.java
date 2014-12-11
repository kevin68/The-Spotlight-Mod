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
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLightSlotConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.EnumLaserInformations;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;

public class GuiSpotLight extends GuiContainer
{
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");
	protected static final ResourceLocation icons = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

	public InventoryPlayer invPlayer;
	public TileEntitySpotLight tile;
	public World world;
	public GuiBooleanButton timeButton, textButton, helpButton;

	public GuiSpotLight(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
	{
		super(new ContainerSpotLightSlotConfig(tileEntity, playerInventory, wrld));
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

		buttonList.add(new GuiButton(0, x + 5, y + 20, 80, 20, I18n.format("container.spotlight.color")));
		buttonList.add(new GuiButton(1, x + 90, y + 20, 50, 20, I18n.format("container.spotlight.textures")));
		buttonList.add(new GuiButton(5, x + 142, y + 20, 28, 20, I18n.format("container.spotlight.more")));
		buttonList.add(new GuiButton(2, x + 5, y + 43, 80, 20, I18n.format("container.spotlight.beamspecs")));
		buttonList.add(new GuiButton(10, x + 5, y + 66, 80, 20, I18n.format("container.spotlight.beamspecs")));
		buttonList.add(new GuiButton(3, x + 90, y + 43, 80, 20, I18n.format("container.spotlight.timeline")));
		buttonList.add(timeButton = new GuiBooleanButton(4, x + 90, y + 66, 80, 20, I18n.format("container.spotlight.timeline"), I18n.format("container.spotlight.timeline"), (Boolean)tile.get(EnumLaserInformations.TIMELINEENABLED)));
		buttonList.add(textButton = new GuiBooleanButton(6, x + 5, y + 89, 80, 20, I18n.format("container.spotlight.textEnabled") + " " + I18n.format("container.spotlight.true"), I18n.format("container.spotlight.textEnabled") + " " + I18n.format("container.spotlight.false"), (Boolean)tile.get(EnumLaserInformations.TEXTENABLED)));
		buttonList.add(new GuiButton(7, x + 90, y + 89, 80, 20, I18n.format("container.spotlight.text")));
		buttonList.add(new GuiButton(8, x + 90, y + 112, 80, 20, I18n.format("container.spotlight.text")));
		buttonList.add(new GuiButton(9, x + 27, y + 112, 58, 20, I18n.format("container.spotlight.configs")));
		buttonList.add(helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 0:
		{
			mc.displayGuiScreen(new GuiSpotLightColor(invPlayer, tile, world));
			break;
		}
		case 1:
		{
			mc.displayGuiScreen(new GuiSpotLightTexture(invPlayer, tile, world));
			break;
		}
		case 2:
		{
			mc.displayGuiScreen(new GuiSpotLightBeamSpec(invPlayer, tile, world));
			break;
		}
		case 3:
		{
			mc.displayGuiScreen(new GuiSpotLightTimeLine(invPlayer, tile, world));
			break;
		}
		case 4:
		{
			timeButton.toggle();
			PacketSender.send(EnumLaserInformations.TIMELINEENABLED, timeButton.getIsActive());
			break;
		}
		case 5:
		{
			mc.displayGuiScreen(new GuiSpotLightAddTexture(invPlayer, tile, world));
			break;
		}
		case 6:
		{
			textButton.toggle();
			PacketSender.send(EnumLaserInformations.TEXTENABLED, textButton.getIsActive());
			break;
		}
		case 7:
		{
			mc.displayGuiScreen(new GuiSpotLightText(invPlayer, tile, world));
			break;
		}
		case 8:
		{
			mc.displayGuiScreen(new GuiSpotLightText2(invPlayer, tile, world));
			break;
		}
		case 9:
		{
			mc.displayGuiScreen(new GuiSpotLightConfigs(invPlayer, tile, world));
			break;
		}
		case 10:
		{
			mc.displayGuiScreen(new GuiSpotLightBeamSpec2(invPlayer, tile, world));
			break;
		}
		case 20:
		{
			helpButton.toggle();
			break;
		}
		}
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
			if(mouseX > x + 5 && mouseX < x + 85)
			{
				if(mouseY > y + 20 && mouseY < y + 40)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.colors"), mouseX, width, reversed);
				}

				if(mouseY > y + 43 && mouseY < y + 63)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props"), mouseX, width, reversed);
				}

				if(mouseY > y + 66 && mouseY < y + 86)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.props2"), mouseX, width, reversed);
				}

				if(mouseY > y + 89 && mouseY < y + 109)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.txttoggle"), mouseX, width, reversed);
				}

				if(mouseY > y + 112 && mouseY < y + 132 && mouseX > x + 27)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.configs"), mouseX, width, reversed);
				}

				if(mouseY > y + 112 && mouseY < y + 132 && mouseX < x + 25)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.configslot"), mouseX, width, reversed);
				}
			}
			else if(mouseX > x + 90 && mouseX < x + 170)
			{
				if(mouseY > y + 20 && mouseY < y + 40 && mouseX < x + 140)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.textures"), mouseX, width, reversed);
				}

				if(mouseY > y + 20 && mouseY < y + 40 && mouseX > x + 142)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.more"), mouseX, width, reversed);
				}

				if(mouseY > y + 43 && mouseY < y + 63)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.timeline"), mouseX, width, reversed);
				}

				if(mouseY > y + 89 && mouseY < y + 109)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.txtconf1"), mouseX, width, reversed);
				}

				if(mouseY > y + 112 && mouseY < y + 132)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.txtconf2"), mouseX, width, reversed);
				}

				if(mouseY > y + 66 && mouseY < y + 86)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.timelineswitch"), mouseX, width, reversed);
				}
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
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		mc.renderEngine.bindTexture(icons);
		this.drawTexturedModalRect(x + 7, y + 113, 238, 0, 18, 18);
		fontRendererObj.drawString(I18n.format("container.spotlight.desc", "").replace("-", ""), x + 6, y + 7, 4210752);
	}
}
