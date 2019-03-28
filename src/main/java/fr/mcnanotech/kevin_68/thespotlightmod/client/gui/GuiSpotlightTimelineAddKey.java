package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;

public class GuiSpotlightTimelineAddKey extends GuiContainer implements ISlider {
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");

	protected InventoryPlayer invPlayer;
	protected TileEntitySpotLight tile;
	protected World world;
	private GuiBooleanButton buttonHelp;
	private short time;

	public GuiSpotlightTimelineAddKey(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World world, ContainerSpotLight spotlightContainer) {
		super(spotlightContainer);
		this.invPlayer = playerInventory;
		this.tile = tileEntity;
		this.world = world;
	}

	@Override
	public void initGui() {
		super.initGui();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.addButton(new GuiSlider(0, x + 3, y + 20, 170, 20, I18n.format("container.spotlight.time"), "", 0, 119, 0, false, true, this));
		this.addButton(new GuiButton(1, x + 13, y + 115, 150, 20, I18n.format("container.spotlight.back")) {
			@Override
			public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(new GuiSpotlightTimeline(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
			}
		});
		this.addButton(new GuiButton(2, x + 13, y + 90, 150, 20, I18n.format("container.spotlight.createkey")) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				if (tile.getKey(time) != null) {
					mc.displayGuiScreen(new GuiYesNo((confirmed, id) -> {
						if (confirmed) {
							tile.setKey(time, TSMUtils.createKey(time, tile));
							mc.displayGuiScreen(new GuiSpotlightTimeline(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
						} else {
							mc.displayGuiScreen(GuiSpotlightTimelineAddKey.this);
						}
					}, I18n.format("container.spotlight.askoverwrite"), "", I18n.format("container.spotlight.overwrite"), I18n.format("container.spotlight.cancel"), 2));
				} else {
					tile.setKey(time, TSMUtils.createKey(time, tile));
					mc.displayGuiScreen(new GuiSpotlightTimeline(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
				}
			}
		});
		this.addButton(this.buttonHelp = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", this.tile.helpMode) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonHelp.toggle();
				tile.helpMode = buttonHelp.isActive();
			}
		});
	}

	@Override
	public void onGuiClosed() {
		TSMNetwork.CHANNEL.sendToServer(new PacketUpdateTLData(this.tile.getPos(), TSMJsonManager.getTlDataFromTile(this.tile).toString()));
		super.onGuiClosed();
	}

	@Override
	public void onChangeSliderValue(GuiSlider slider) {
		this.time = (short) slider.getValueInt();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialRenderTick) {
		super.render(mouseX, mouseY, partialRenderTick);

		if (this.buttonHelp.isActive()) {
			for (GuiButton button : this.buttons) {
				if (button.isMouseOver()) {
					String text = "";
					switch (button.id) {
					case 0:
						text = I18n.format("tutorial.spotlight.addkey.time");
						break;
					case 1:
						text = I18n.format("tutorial.spotlight.back");
						break;
					case 2:
						text = I18n.format("tutorial.spotlight.addkey.create");
						break;
					case 20:
						text = I18n.format("tutorial.spotlight.help");
						break;
					}
					if (!text.isEmpty()) {
						this.drawHoveringText(this.fontRenderer.listFormattedStringToWidth(TextFormatting.GREEN + text, (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
					}
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.addKey")), x + 5, y + 8, 4210752);
	}
}