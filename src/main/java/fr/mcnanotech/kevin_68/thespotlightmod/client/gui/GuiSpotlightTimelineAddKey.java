package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;

public class GuiSpotlightTimelineAddKey extends ContainerScreen<ContainerSpotLight> {
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/spotlight.png");

	protected PlayerInventory invPlayer;
	protected TileEntitySpotLight tile;
	private ButtonToggle buttonHelp;
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
		this.addButton(new GuiSlider(x + 3, y + 20, 170, 20, I18n.format("container.spotlight.time"), "", 0, 119, 0, false, true, b -> {}, slider -> {
			this.time = (short)slider.getValueInt();
		}));
		this.addButton(new Button(x + 13, y + 115, 150, 20, I18n.format("container.spotlight.back"), b -> {
            Minecraft.getInstance().displayGuiScreen(new GuiSpotlightTimeline(container, invPlayer, title));
		}));
		this.addButton(new Button(x + 13, y + 90, 150, 20, I18n.format("container.spotlight.createkey"), b -> {
			if (tile.getKey(time) != null) {
				Minecraft.getInstance().displayGuiScreen(new GuiYesNo((confirmed, id) -> {
					if (confirmed) {
						tile.setKey(time, TSMUtils.createKey(time, tile));
						Minecraft.getInstance().displayGuiScreen(new GuiSpotlightTimeline(container, invPlayer, title));
					} else {
						Minecraft.getInstance().displayGuiScreen(GuiSpotlightTimelineAddKey.this);
					}
				}, I18n.format("container.spotlight.askoverwrite"), "", I18n.format("container.spotlight.overwrite"), I18n.format("container.spotlight.cancel"), 2));
			} else {
				tile.setKey(time, TSMUtils.createKey(time, tile));
				Minecraft.getInstance().displayGuiScreen(new GuiSpotlightTimeline(container, invPlayer, title));
			}
		}));
		this.addButton(this.buttonHelp = new ButtonToggle(x + 180, y + 140, 20, 20, "?", this.tile.helpMode, b -> {
			tile.helpMode = buttonHelp.isActive();
		}));
	}

	@Override
	public void onClose() {
		TSMNetwork.CHANNEL.sendToServer(new PacketUpdateTLData(this.tile.getPos(), TSMJsonManager.getTlDataFromTile(this.tile).toString()));
		super.onClose();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialRenderTick) {
		super.render(mouseX, mouseY, partialRenderTick);

		if (this.buttonHelp.isActive()) {
			for (Button button : this.buttons) {
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
						this.drawHoveringText(this.font.listFormattedStringToWidth(TextFormatting.GREEN + text, (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
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
		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		this.blit(x, y, 0, 0, this.xSize, this.ySize);
		this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.addKey")), x + 5, y + 8, 4210752);
	}
}
