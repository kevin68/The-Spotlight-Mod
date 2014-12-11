package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
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

public class GuiSpotLightConfirm extends GuiContainer
{
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");
	protected static final ResourceLocation icons = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

	private String action;
	private String yes;
	private String no;
	private int guiopen;
	protected InventoryPlayer invPlayer;
	protected TileEntitySpotLight tile;
	protected World world;

	public GuiSpotLightConfirm(TileEntitySpotLight tile, InventoryPlayer invplay, World world, String actionname, String yesbutton, String nobutton, int guiid)
	{
		super(new ContainerSpotLight(tile, invplay, world, 8));
		action = actionname;
		yes = yesbutton;
		no = nobutton;
		guiopen = guiid;
		invPlayer = invplay;
		this.tile = tile;
		this.world = world;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		buttonList.add(new GuiButton(1, x + 13, y + 90, 150, 20, yes));
		buttonList.add(new GuiButton(2, x + 13, y + 115, 150, 20, no));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		if(guibutton.id == 1)
		{
			if(guiopen == 0)
			{
				SpotLightEntry entry = new SpotLightEntry(false, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 0, (byte)0, false, false, (byte)0, true, (byte)0, false, (byte)0, (byte)0, 0, false, (byte)0, (byte)0, (byte)0, 0, false, false, (byte)0, (byte)0, (byte)0, (byte)0);
				PacketSender.sendSpotLightPacket(tile, (Byte)tile.get(EnumLaserInformations.TIMELINELASTKEYSELECTED) & 0xFF, entry);
				tile.setKey((Byte)tile.get(EnumLaserInformations.TIMELINELASTKEYSELECTED) & 0xFF, entry);
				mc.displayGuiScreen(new GuiSpotLightTimeLine(invPlayer, tile, world));
			}
			else
			{
				UtilSpotLight.createKey(tile);
				mc.displayGuiScreen(new GuiSpotLightCreateKey(invPlayer, tile, world));
			}
		}
		else
		{
			if(guiopen == 0)
			{
				mc.displayGuiScreen(new GuiSpotLightTimeLine(invPlayer, tile, world));
			}
			else
			{
				mc.displayGuiScreen(new GuiSpotLightCreateKey(invPlayer, tile, world));
			}
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
		this.drawTexturedModalRect(x + 15, y + 30, 0, 122, 57, 47);
		this.drawTexturedModalRect(x + 105, y + 30, 0, 122, 57, 47);
		drawCenteredString(fontRendererObj, action, width / 2, y + 10, 0xff0000);
	}
}