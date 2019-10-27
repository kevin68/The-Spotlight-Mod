package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;

public class GuiSpotLightTextProperties extends ContainerScreen<ContainerSpotLight> {
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

	private PlayerInventory invPlayer;
	private TileEntitySpotLight tile;
	private GuiSlider sliderTranslateSpeed;
	private ButtonToggle buttonBold, buttonStrike, buttonUnderline, buttonItalic, buttonObfuscated, buttonShadow, buttonTranslating, buttonReverseTranslating, buttonHelp, button3D;

	public GuiSpotLightTextProperties(ContainerSpotLight container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
		this.invPlayer = playerInventory;
		this.tile = container.getSpotlight();
	}

	@Override
	public void init() {
		super.init();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.addButton(new GuiSlider(x - 50, y - 20, 130, 20, I18n.format("container.spotlight.textheight"), "", -100, 100, this.tile.getShort(EnumTSMProperty.TEXT_HEIGHT), false, true, b -> {
			buttonBold.toggle();
			tile.setProperty(EnumTSMProperty.TEXT_BOLD, buttonBold.isActive());
		}, slider -> {
			this.tile.setProperty(EnumTSMProperty.TEXT_HEIGHT, (short) (slider.getValueInt()));
        }));
		this.addButton(new GuiSlider(x + 90, y - 20, 130, 20, I18n.format("container.spotlight.textscale"), "", 0, 100, this.tile.getShort(EnumTSMProperty.TEXT_SCALE), false, true, b -> {
			buttonStrike.toggle();
			tile.setProperty(EnumTSMProperty.TEXT_STRIKE, buttonStrike.isActive());
		}, slider -> {
			this.tile.setProperty(EnumTSMProperty.TEXT_SCALE, (short) (slider.getValueInt()));
        }));
		this.addButton(this.buttonBold = new ButtonToggle(x - 50, y + 3, 130, 20, I18n.format("container.spotlight.bold"), this.tile.getBoolean(EnumTSMProperty.TEXT_BOLD), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_BOLD, buttonBold.isActive());
		}));
		this.addButton(this.buttonStrike = new ButtonToggle(x + 90, y + 3, 130, 20, I18n.format("container.spotlight.strike"), this.tile.getBoolean(EnumTSMProperty.TEXT_STRIKE), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_STRIKE, buttonStrike.isActive());
		}));
		this.addButton(this.buttonUnderline = new ButtonToggle(x - 50, y + 26, 130, 20, I18n.format("container.spotlight.underline"), this.tile.getBoolean(EnumTSMProperty.TEXT_UNDERLINE), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_UNDERLINE, buttonUnderline.isActive());
		}));
		this.addButton(this.buttonItalic = new ButtonToggle(x + 90, y + 26, 130, 20, I18n.format("container.spotlight.italic"), this.tile.getBoolean(EnumTSMProperty.TEXT_ITALIC), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_ITALIC, buttonItalic.isActive());
		}));
		this.addButton(this.buttonObfuscated = new ButtonToggle(x - 50, y + 49, 130, 20, I18n.format("container.spotlight.obfuscated"), this.tile.getBoolean(EnumTSMProperty.TEXT_OBFUSCATED), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_OBFUSCATED, buttonObfuscated.isActive());
		}));
		this.addButton(this.buttonShadow = new ButtonToggle(x + 90, y + 49, 130, 20, I18n.format("container.spotlight.shadow"), this.tile.getBoolean(EnumTSMProperty.TEXT_SHADOW), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_SHADOW, buttonShadow.isActive());
		}));
		this.addButton(this.buttonTranslating = new ButtonToggle(x - 50, y + 72, 130, 20, I18n.format("container.spotlight.translate"), this.tile.getBoolean(EnumTSMProperty.TEXT_TRANSLATING), b -> {
			buttonTranslating.toggle();
			tile.setProperty(EnumTSMProperty.TEXT_TRANSLATING, buttonTranslating.isActive());
			sliderTranslateSpeed.enabled = buttonTranslating.isActive();
			buttonReverseTranslating.enabled = buttonTranslating.isActive();
		}));
		this.addButton(this.sliderTranslateSpeed = new GuiSlider(x + 90, y + 72, 130, 20, I18n.format("container.spotlight.translatespeed"), "", 0, 100, this.tile.getShort(EnumTSMProperty.TEXT_TRANSLATE_SPEED), false, true, b -> {}, slider -> {
			this.tile.setProperty(EnumTSMProperty.TEXT_TRANSLATE_SPEED, (short) (slider.getValueInt()));
		}));
		this.addButton(this.buttonReverseTranslating = new ButtonToggle(x - 50, y + 95, 130, 20, I18n.format("container.spotlight.reversetranslate"), this.tile.getBoolean(EnumTSMProperty.TEXT_T_REVERSE), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_T_REVERSE, buttonReverseTranslating.isActive());
		}));
		this.addButton(this.button3D = new ButtonToggle(x + 90, y + 95, 130, 20, I18n.format("container.spotlight.text3d"), this.tile.getBoolean(EnumTSMProperty.TEXT_3D), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_3D, button3D.isActive());
			buttonBold.enabled = !button3D.isActive();
			buttonStrike.enabled = !button3D.isActive();
			buttonUnderline.enabled = !button3D.isActive();
			buttonItalic.enabled = !button3D.isActive();
			buttonObfuscated.enabled = !button3D.isActive();
			buttonShadow.enabled = !button3D.isActive();
		}));

		this.sliderTranslateSpeed.enabled = this.buttonTranslating.isActive();
		this.buttonReverseTranslating.enabled = this.buttonTranslating.isActive();

		this.buttonBold.enabled = !this.button3D.isActive();
		this.buttonStrike.enabled = !this.button3D.isActive();
		this.buttonUnderline.enabled = !this.button3D.isActive();
		this.buttonItalic.enabled = !this.button3D.isActive();
		this.buttonObfuscated.enabled = !this.button3D.isActive();
		this.buttonShadow.enabled = !this.button3D.isActive();

		this.addButton(new Button(x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back"), b -> {
			Minecraft.getInstance().displayGuiScreen(new GuiSpotLight(container, invPlayer, title));
		}));
		this.addButton(this.buttonHelp = new ButtonToggle(x + 180, y + 140, 20, 20, "?", this.tile.helpMode, b -> {
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
						text = I18n.format("tutorial.spotlight.textprops.height");
						break;
					case 1:
						text = I18n.format("tutorial.spotlight.textprops.scale");
						break;
					case 2:
						text = I18n.format("tutorial.spotlight.textprops.bold");
						break;
					case 3:
						text = I18n.format("tutorial.spotlight.textprops.strike");
						break;
					case 4:
						text = I18n.format("tutorial.spotlight.textprops.underline");
						break;
					case 5:
						text = I18n.format("tutorial.spotlight.textprops.italic");
						break;
					case 6:
						text = I18n.format("tutorial.spotlight.textprops.obfuscated");
						break;
					case 7:
						text = I18n.format("tutorial.spotlight.textprops.shadow");
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
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		this.blit(x, y + 114, 69, 81, this.xSize, 52);
		this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.beamspecs")), x - 30, y - 35, 0xffffff);
	}
}
