package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class GuiSpotLightBeamTextures extends ContainerScreen<ContainerSpotLight> {
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/spotlight.png");
	protected static final ResourceLocation tsmIcons = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

	public PlayerInventory invPlayer;
	public TileEntitySpotLight tile;
	public World world;
	private GuiBooleanButton buttonHelp;

	public GuiSpotLightBeamTextures(PlayerInventory inventory, TileEntitySpotLight tile, World world, ContainerSpotLight spotlightContainer) {
		super(spotlightContainer);
		spotlightContainer.showTextureSlot(true);
		this.invPlayer = inventory;
		this.tile = tile;
		this.world = world;
	}

	@Override
	public void init() {
		super.init();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.addButton(new GuiButton(19, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				mc.displayGuiScreen(new GuiSpotLight(invPlayer, tile, world, inventorySlots));
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
	public void onClose() {
		TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos(), TSMJsonManager.getDataFromTile(this.tile).toString()));
		super.onClose();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialRenderTick) {
		super.render(mouseX, mouseY, partialRenderTick);
		if (this.buttonHelp.isActive()) {
			Slot s = this.getSlotUnderMouse();
			if (s != null && s.slotNumber < 2) {
				String text = s.slotNumber == 0 ? I18n.format("tutorial.spotlight.textures.main") : I18n.format("tutorial.spotlight.textures.sec");
				this.drawHoveringText(this.font.listFormattedStringToWidth(TextFormatting.GREEN + text, (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
			}
			for (GuiButton button : this.buttons) {
				if (button.isMouseOver()) {
					String text = "";
					switch (button.id) {
					case 19:
						text = I18n.format("tutorial.spotlight.back");
						break;
					case 20:
						text = I18n.format("tutorial.spotlight.help");
						break;
					}
					if (!text.isEmpty()) {
						this.drawHoveringText(this.font.listFormattedStringToWidth(TextFormatting.GREEN + text, (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
					}
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		this.mc.getTextureManager().bindTexture(tsmIcons);
		this.drawTexturedModalRect(x + 39, y + 79, 238, 18, 18, 18);
		this.drawTexturedModalRect(x + 119, y + 79, 238, 36, 18, 18);
		this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.textures")), x - 30, y - 35, 0xffffff);
	}
}