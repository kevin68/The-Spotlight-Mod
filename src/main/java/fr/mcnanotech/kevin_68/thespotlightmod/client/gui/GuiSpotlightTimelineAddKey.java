package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.ButtonToggleHelp;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.IHelpButton;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.TSMButton;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.TSMButtonSlider;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiSpotlightTimelineAddKey extends ContainerScreen<ContainerSpotLight> {
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/spotlight.png");

	protected PlayerInventory invPlayer;
	protected TileEntitySpotLight tile;
	private short time;

	public GuiSpotlightTimelineAddKey(ContainerSpotLight container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
		this.invPlayer = playerInventory;
		this.tile = container.getSpotlight();
	}

	@Override
	public void init() {
		super.init();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.addButton(new TSMButtonSlider(x + 3, y + 20, 170, 20, I18n.format("container.spotlight.time"), "", 0, 119, 0, false, true, b -> {}, slider -> {
			this.time = (short)slider.getValueInt();
		}, I18n.format("tutorial.spotlight.addkey.time")));
		this.addButton(new TSMButton(x + 13, y + 115, 150, 20, I18n.format("container.spotlight.back"), b -> {
			minecraft.displayGuiScreen(new GuiSpotlightTimeline(container, invPlayer, title));
		}, I18n.format("tutorial.spotlight.back")));
		this.addButton(new TSMButton(x + 13, y + 90, 150, 20, I18n.format("container.spotlight.createkey"), b -> {
			if (tile.getKey(time) != null) {
				minecraft.displayGuiScreen(new ConfirmScreen((confirmed) -> {
					if (confirmed) {
						tile.setKey(time, TSMUtils.createKey(time, tile));
						minecraft.displayGuiScreen(new GuiSpotlightTimeline(container, invPlayer, title));
					} else {
						minecraft.displayGuiScreen(GuiSpotlightTimelineAddKey.this);
					}
				}, new TranslationTextComponent("container.spotlight.askoverwrite"), new StringTextComponent(""), I18n.format("container.spotlight.overwrite"), I18n.format("container.spotlight.cancel")));
			} else {
				tile.setKey(time, TSMUtils.createKey(time, tile));
				minecraft.displayGuiScreen(new GuiSpotlightTimeline(container, invPlayer, title));
			}
		}, I18n.format("tutorial.spotlight.addkey.create")));
		this.addButton(new ButtonToggleHelp(x + 180, y + 140, 20, 20, tile));
	}

	@Override
	public void removed() {
		TSMNetwork.CHANNEL.sendToServer(new PacketUpdateTLData(this.tile.getPos(), TSMJsonManager.getTlDataFromTile(this.tile).toString()));
		super.removed();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialRenderTick) {
		super.render(mouseX, mouseY, partialRenderTick);

		if (this.tile.helpMode) {
			this.buttons.stream().filter(b -> b.isMouseOver(mouseX, mouseY) && b instanceof IHelpButton).findFirst().ifPresent(b -> {
				this.renderTooltip(this.font.listFormattedStringToWidth(TextFormatting.GREEN + ((IHelpButton)b).getHelpMessage(), (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
			});
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		minecraft.getTextureManager().bindTexture(texture);
		this.blit(x, y, 0, 0, this.xSize, this.ySize);
		this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.addKey")), x + 5, y + 8, 4210752);
	}
}
