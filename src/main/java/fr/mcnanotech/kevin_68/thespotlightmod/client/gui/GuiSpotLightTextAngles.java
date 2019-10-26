package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;

public class GuiSpotLightTextAngles extends ContainerScreen<ContainerSpotLight> implements ISlider {
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

	public PlayerInventory invPlayer;
	public TileEntitySpotLight tile;
	public World world;

	private GuiBooleanButton buttonAR, buttonRR, buttonHelp;
	private GuiSlider sliderAngle, sliderSpeed;

	public GuiSpotLightTextAngles(PlayerInventory playerInventory, TileEntitySpotLight tileEntity, World world, ContainerSpotLight spotlightContainer) {
		super(spotlightContainer);
		this.invPlayer = playerInventory;
		this.tile = tileEntity;
		this.world = world;
	}

	@Override
	public void init() {
		super.init();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		this.addButton(this.sliderAngle = new GuiSlider(3, x - 50, y - 20, 270, 20, I18n.format("container.spotlight.angleval", "Y"), "", 0, 360, this.tile.getShort(EnumTSMProperty.TEXT_ANGLE_Y), false, true, this));
		this.addButton(this.buttonAR = new GuiBooleanButton(4, x - 50, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.TEXT_R_AUTO_Y)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonAR.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_R_AUTO_Y, buttonAR.isActive());
				sliderAngle.enabled = !buttonAR.isActive();
				sliderSpeed.enabled = buttonAR.isActive();
				buttonRR.enabled = buttonAR.isActive();
			}
		});
		this.buttonAR.setTexts(I18n.format("container.spotlight.rotate", "Y", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "Y", I18n.format("container.spotlight.off")));
		this.sliderAngle.enabled = !this.buttonAR.isActive();
		this.addButton(this.buttonRR = new GuiBooleanButton(5, x + 90, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.TEXT_R_REVERSE_Y)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonRR.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_R_REVERSE_Y, buttonRR.isActive());
			}
		});
		this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "Y", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "Y", I18n.format("container.spotlight.off")));
		this.buttonRR.enabled = this.buttonAR.enabled;
		this.addButton(this.sliderSpeed = new GuiSlider(6, x - 50, y + 30, 270, 20, I18n.format("container.spotlight.rotationspeed", "Y"), "", 0, 200, this.tile.getShort(EnumTSMProperty.TEXT_R_SPEED_Y), false, true, this));
		this.sliderSpeed.enabled = this.buttonAR.isActive();

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
	public void onChangeSliderValue(GuiSlider slider) {
		switch (slider.id) {
		case 3:
			this.tile.setProperty(EnumTSMProperty.TEXT_ANGLE_Y, (short) (slider.getValueInt()));
			break;
		case 6:
			this.tile.setProperty(EnumTSMProperty.TEXT_R_SPEED_Y, (short) (slider.getValueInt()));
			break;
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialRenderTick) {
		super.render(mouseX, mouseY, partialRenderTick);

		if (this.buttonHelp.isActive()) {
			for (GuiButton button : this.buttons) {
				if (button.isMouseOver()) {
					String text = "";
					switch (button.id) {
					case 3:
						text = I18n.format("tutorial.spotlight.textangles.angle");
						break;
					case 4:
						text = I18n.format("tutorial.spotlight.textangles.autorotate");
						break;
					case 5:
						text = I18n.format("tutorial.spotlight.textangles.reverserotation");
						break;
					case 6:
						text = I18n.format("tutorial.spotlight.textangles.rotationspeed");
						break;
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
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
		this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.angle")), x - 30, y - 35, 0xffffff);
	}
}