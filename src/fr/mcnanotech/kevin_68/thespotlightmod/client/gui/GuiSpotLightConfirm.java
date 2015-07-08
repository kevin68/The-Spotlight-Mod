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
		this.action = actionname;
		this.yes = yesbutton;
		this.no = nobutton;
		this.guiopen = guiid;
		this.invPlayer = invplay;
		this.tile = tile;
		this.world = world;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(1, x + 13, y + 90, 150, 20, this.yes));
		this.buttonList.add(new GuiButton(2, x + 13, y + 115, 150, 20, this.no));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		if(guibutton.id == 1)
		{
			if(this.guiopen == 0)
			{
				SpotLightEntry entry = new SpotLightEntry(false, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 0, (byte)0, false, false, (byte)0, true, (byte)0, false, (byte)0, (byte)0, 0, false, (byte)0, (byte)0, (byte)0, 0, false, false, (byte)0, (byte)0, (byte)0, (byte)0);
				PacketSender.sendSpotLightPacket(this.tile, (Byte)this.tile.get(EnumLaserInformations.TIMELINELASTKEYSELECTED) & 0xFF, entry);
				this.tile.setKey((Byte)this.tile.get(EnumLaserInformations.TIMELINELASTKEYSELECTED) & 0xFF, entry);
				this.mc.displayGuiScreen(new GuiSpotLightTimeLine(this.invPlayer, this.tile, this.world));
			}
			else
			{
				UtilSpotLight.createKey(this.tile);
				this.mc.displayGuiScreen(new GuiSpotLightCreateKey(this.invPlayer, this.tile, this.world));
			}
		}
		else
		{
			if(this.guiopen == 0)
			{
				this.mc.displayGuiScreen(new GuiSpotLightTimeLine(this.invPlayer, this.tile, this.world));
			}
			else
			{
				this.mc.displayGuiScreen(new GuiSpotLightCreateKey(this.invPlayer, this.tile, this.world));
			}
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
		this.mc.renderEngine.bindTexture(icons);
		this.drawTexturedModalRect(x + 15, y + 30, 0, 122, 57, 47);
		this.drawTexturedModalRect(x + 105, y + 30, 0, 122, 57, 47);
		drawCenteredString(this.fontRendererObj, this.action, this.width / 2, y + 10, 0xff0000);
	}
}