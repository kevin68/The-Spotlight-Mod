package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;

public class GuiSpotLightBeamAngles extends ContainerScreen<ContainerSpotLight> {
	protected static final ResourceLocation TEXTURE = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

	public PlayerInventory invPlayer;
	public TileEntitySpotLight tile;

	private ButtonToggle buttonX, buttonY, buttonZ, buttonAR, buttonRR, buttonHelp;
	private GuiSlider sliderAngle, sliderSpeed;

	public GuiSpotLightBeamAngles(ContainerSpotLight container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.invPlayer = playerInventory;
		this.tile = container.getSpotlight();
	}

	@Override
	public void init() {
		super.init();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		this.addButton(this.buttonX = new ButtonToggle(x - 50, y + 110, 20, 20, "X", true, b -> {
			if (!buttonX.isActive()) {
				buttonX.toggle();
				if (buttonY.isActive()) {
					buttonY.toggle();
				}
				if (buttonZ.isActive()) {
					buttonZ.toggle();
				}
				sliderAngle.sliderValue = tile.getShort(EnumTSMProperty.BEAM_ANGLE_X);
				sliderAngle.dispString = I18n.format("container.spotlight.angleval", "X");
				sliderAngle.updateSlider();
				sliderSpeed.sliderValue = tile.getShort(EnumTSMProperty.BEAM_R_SPEED_X);
				sliderSpeed.dispString = I18n.format("container.spotlight.angleval", "X");
				sliderSpeed.updateSlider();
				if (buttonAR.isActive() != tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_X)) {
					buttonAR.toggle();
				}
				sliderAngle.enabled = !buttonAR.isActive();
				sliderSpeed.enabled = buttonAR.isActive();
				buttonAR.setTexts(I18n.format("container.spotlight.rotate", "X", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "X", I18n.format("container.spotlight.off")));
				if (buttonRR.isActive() != tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_X)) {
					buttonRR.toggle();
				}
				buttonRR.enabled = buttonAR.isActive();
				buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "X", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "X", I18n.format("container.spotlight.off")));
				TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(tile.getPos(), TSMJsonManager.getDataFromTile(tile).toString()));
			}
		}));
		this.addButton(this.buttonY = new ButtonToggle(x - 25, y + 110, 20, 20, "Y", false, b -> {
			if (!buttonY.isActive()) {
				buttonY.toggle();
				if (buttonX.isActive()) {
					buttonX.toggle();
				}
				if (buttonZ.isActive()) {
					buttonZ.toggle();
				}
				sliderAngle.sliderValue = tile.getShort(EnumTSMProperty.BEAM_ANGLE_Y);
				sliderAngle.dispString = I18n.format("container.spotlight.angleval", "Y");
				sliderAngle.updateSlider();
				sliderSpeed.sliderValue = tile.getShort(EnumTSMProperty.BEAM_R_SPEED_Y);
				sliderSpeed.dispString = I18n.format("container.spotlight.angleval", "Y");
				sliderSpeed.updateSlider();
				if (buttonAR.isActive() != tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_Y)) {
					buttonAR.toggle();
				}
				sliderAngle.enabled = !buttonAR.isActive();
				sliderSpeed.enabled = buttonAR.isActive();
				buttonAR.setTexts(I18n.format("container.spotlight.rotate", "Y", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "Y", I18n.format("container.spotlight.off")));
				if (buttonRR.isActive() != tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_Y)) {
					buttonRR.toggle();
				}
				buttonRR.enabled = buttonAR.isActive();
				buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "Y", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "Y", I18n.format("container.spotlight.off")));
				TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(tile.getPos(), TSMJsonManager.getDataFromTile(tile).toString()));
			}
		}));
		this.addButton(this.buttonZ = new ButtonToggle(x, y + 110, 20, 20, "Z", false, b -> {
			if (!buttonZ.isActive()) {
				buttonZ.toggle();
				if (buttonX.isActive()) {
					buttonX.toggle();
				}
				if (buttonY.isActive()) {
					buttonY.toggle();
				}
				sliderAngle.sliderValue = tile.getShort(EnumTSMProperty.BEAM_ANGLE_Z);
				sliderAngle.dispString = I18n.format("container.spotlight.angleval", "Z");
				sliderAngle.updateSlider();
				sliderSpeed.sliderValue = tile.getShort(EnumTSMProperty.BEAM_R_SPEED_Z);
				sliderSpeed.dispString = I18n.format("container.spotlight.angleval", "Z");
				sliderSpeed.updateSlider();
				if (buttonAR.isActive() != tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_Z)) {
					buttonAR.toggle();
				}
				sliderAngle.enabled = !buttonAR.isActive();
				sliderSpeed.enabled = buttonAR.isActive();
				buttonAR.setTexts(I18n.format("container.spotlight.rotate", "Z", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "Z", I18n.format("container.spotlight.off")));
				if (buttonRR.isActive() != tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_Z)) {
					buttonRR.toggle();
				}
				buttonRR.enabled = buttonAR.isActive();
				buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "Z", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "Z", I18n.format("container.spotlight.off")));
				TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(tile.getPos(), TSMJsonManager.getDataFromTile(tile).toString()));
			}
		}));
		this.addButton(this.sliderAngle = new GuiSlider(x - 50, y - 20, 270, 20, I18n.format("container.spotlight.angleval", "X"), "", 0, 360, this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_X), false, true, b -> {}, slider -> {
			if (this.buttonX.isActive()) {
				this.tile.setProperty(EnumTSMProperty.BEAM_ANGLE_X, (short) slider.getValueInt());
			} else if (this.buttonY.isActive()) {
				this.tile.setProperty(EnumTSMProperty.BEAM_ANGLE_Y, (short) slider.getValueInt());
			} else if (this.buttonZ.isActive()) {
				this.tile.setProperty(EnumTSMProperty.BEAM_ANGLE_Z, (short) slider.getValueInt());
			}
		}));
		this.addButton(this.buttonAR = new ButtonToggle(x - 50, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_X), b -> {
			buttonAR.toggle();
			if (buttonX.isActive()) {
				tile.setProperty(EnumTSMProperty.BEAM_R_AUTO_X, buttonAR.isActive());
			} else if (buttonY.isActive()) {
				tile.setProperty(EnumTSMProperty.BEAM_R_AUTO_Y, buttonAR.isActive());
			} else if (buttonZ.isActive()) {
				tile.setProperty(EnumTSMProperty.BEAM_R_AUTO_Z, buttonAR.isActive());
			}
			sliderAngle.enabled = !buttonAR.isActive();
			sliderSpeed.enabled = buttonAR.isActive();
			buttonRR.enabled = buttonAR.isActive();
		}));
		this.buttonAR.setTexts(I18n.format("container.spotlight.rotate", "X", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "X", I18n.format("container.spotlight.off")));
		this.sliderAngle.enabled = !this.buttonAR.isActive();
		this.addButton(this.buttonRR = new ButtonToggle(x + 90, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_X), b -> {
			buttonRR.toggle();
			if (buttonX.isActive()) {
				tile.setProperty(EnumTSMProperty.BEAM_R_REVERSE_X, buttonRR.isActive());
			} else if (buttonY.isActive()) {
				tile.setProperty(EnumTSMProperty.BEAM_R_REVERSE_Y, buttonRR.isActive());
			} else if (buttonZ.isActive()) {
				tile.setProperty(EnumTSMProperty.BEAM_R_REVERSE_Z, buttonRR.isActive());
			}
		}));
		this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "X", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "X", I18n.format("container.spotlight.off")));
		this.buttonRR.enabled = this.buttonAR.enabled;
		this.addButton(this.sliderSpeed = new GuiSlider(x - 50, y + 30, 270, 20, I18n.format("container.spotlight.rotationspeed", "X"), "", 0, 200, this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_X), false, true, b -> {}, slider -> {
			if (this.buttonX.isActive()) {
				this.tile.setProperty(EnumTSMProperty.BEAM_R_SPEED_X, (short) slider.getValueInt());
			} else if (this.buttonY.isActive()) {
				this.tile.setProperty(EnumTSMProperty.BEAM_R_SPEED_Y, (short) slider.getValueInt());
			} else if (this.buttonZ.isActive()) {
				this.tile.setProperty(EnumTSMProperty.BEAM_R_SPEED_Z, (short) slider.getValueInt());
			}
		}));
		this.sliderSpeed.enabled = this.buttonAR.isActive();

		this.addButton(new Button(x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back"), b -> {
			Minecraft.getInstance().displayGuiScreen(new GuiSpotLight(container, invPlayer, title));
		}));
		this.addButton(this.buttonHelp = new ButtonToggle(x + 180, y + 140, 20, 20, "?", this.tile.helpMode, b -> {
			//buttonHelp.toggle();
			tile.helpMode = buttonHelp.isActive();
		}));
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
			for (Button button : this.buttons) {
				if (button.isMouseOver()) {
					String text = "";
					switch (button.id) {
					case 0:
						text = I18n.format("tutorial.spotlight.angles.x");
						break;
					case 1:
						text = I18n.format("tutorial.spotlight.angles.y");
						break;
					case 2:
						text = I18n.format("tutorial.spotlight.angles.z");
						break;
					case 3:
						text = I18n.format("tutorial.spotlight.angles.angle");
						break;
					case 4:
						text = I18n.format("tutorial.spotlight.angles.autorotate");
						break;
					case 5:
						text = I18n.format("tutorial.spotlight.angles.reverserotation");
						break;
					case 6:
						text = I18n.format("tutorial.spotlight.angles.rotationspeed");
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
		Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
		this.blit(x, y + 114, 69, 81, this.xSize, 52);
		this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.angle")), x - 30, y - 35, 0xffffff);
	}
}
