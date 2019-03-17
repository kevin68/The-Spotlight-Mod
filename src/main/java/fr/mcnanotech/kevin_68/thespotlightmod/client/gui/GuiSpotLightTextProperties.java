package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;

public class GuiSpotLightTextProperties extends GuiContainer implements ISlider {
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

	private InventoryPlayer invPlayer;
	private TileEntitySpotLight tile;
	private World world;
	private GuiSlider sliderTranslateSpeed;
	private GuiBooleanButton buttonBold, buttonStrike, buttonUnderline, buttonItalic, buttonObfuscated, buttonShadow, buttonTranslating, buttonReverseTranslating, buttonHelp, button3D;

	public GuiSpotLightTextProperties(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld) {
		super(new ContainerSpotLight(tileEntity, playerInventory, true));
		this.invPlayer = playerInventory;
		this.tile = tileEntity;
		this.world = wrld;
	}

	@Override
	public void initGui() {
		super.initGui();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.addButton(new GuiSlider(0, x - 50, y - 20, 130, 20, I18n.format("container.spotlight.textheight"), "", -100, 100, this.tile.getShort(EnumTSMProperty.TEXT_HEIGHT), false, true, this) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonBold.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_BOLD, buttonBold.isActive());
			}
		});
		this.addButton(new GuiSlider(1, x + 90, y - 20, 130, 20, I18n.format("container.spotlight.textscale"), "", 0, 100, this.tile.getShort(EnumTSMProperty.TEXT_SCALE), false, true, this) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonStrike.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_STRIKE, buttonStrike.isActive());
			}
		});
		this.addButton(this.buttonBold = new GuiBooleanButton(2, x - 50, y + 3, 130, 20, I18n.format("container.spotlight.bold"), this.tile.getBoolean(EnumTSMProperty.TEXT_BOLD)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonBold.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_BOLD, buttonBold.isActive());
			}
		});
		this.addButton(this.buttonStrike = new GuiBooleanButton(3, x + 90, y + 3, 130, 20, I18n.format("container.spotlight.strike"), this.tile.getBoolean(EnumTSMProperty.TEXT_STRIKE)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonStrike.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_STRIKE, buttonStrike.isActive());
			}
		});
		this.addButton(this.buttonUnderline = new GuiBooleanButton(4, x - 50, y + 26, 130, 20, I18n.format("container.spotlight.underline"), this.tile.getBoolean(EnumTSMProperty.TEXT_UNDERLINE)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonUnderline.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_UNDERLINE, buttonUnderline.isActive());
			}
		});
		this.addButton(this.buttonItalic = new GuiBooleanButton(5, x + 90, y + 26, 130, 20, I18n.format("container.spotlight.italic"), this.tile.getBoolean(EnumTSMProperty.TEXT_ITALIC)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonItalic.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_ITALIC, buttonItalic.isActive());
			}
		});
		this.addButton(this.buttonObfuscated = new GuiBooleanButton(6, x - 50, y + 49, 130, 20, I18n.format("container.spotlight.obfuscated"), this.tile.getBoolean(EnumTSMProperty.TEXT_OBFUSCATED)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonObfuscated.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_OBFUSCATED, buttonObfuscated.isActive());
			}
		});
		this.addButton(this.buttonShadow = new GuiBooleanButton(7, x + 90, y + 49, 130, 20, I18n.format("container.spotlight.shadow"), this.tile.getBoolean(EnumTSMProperty.TEXT_SHADOW)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonShadow.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_SHADOW, buttonShadow.isActive());
			}
		});
		this.addButton(this.buttonTranslating = new GuiBooleanButton(8, x - 50, y + 72, 130, 20, I18n.format("container.spotlight.translate"), this.tile.getBoolean(EnumTSMProperty.TEXT_TRANSLATING)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonTranslating.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_TRANSLATING, buttonTranslating.isActive());
				sliderTranslateSpeed.enabled = buttonTranslating.isActive();
				buttonReverseTranslating.enabled = buttonTranslating.isActive();
			}
		});
		this.addButton(this.sliderTranslateSpeed = new GuiSlider(9, x + 90, y + 72, 130, 20, I18n.format("container.spotlight.translatespeed"), "", 0, 100, this.tile.getShort(EnumTSMProperty.TEXT_TRANSLATE_SPEED), false, true, this));
		this.addButton(this.buttonReverseTranslating = new GuiBooleanButton(10, x - 50, y + 95, 130, 20, I18n.format("container.spotlight.reversetranslate"), this.tile.getBoolean(EnumTSMProperty.TEXT_T_REVERSE)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonReverseTranslating.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_T_REVERSE, buttonReverseTranslating.isActive());
			}
		});
		this.addButton(this.button3D = new GuiBooleanButton(11, x + 90, y + 95, 130, 20, I18n.format("container.spotlight.text3d"), this.tile.getBoolean(EnumTSMProperty.TEXT_3D)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				button3D.toggle();
				tile.setProperty(EnumTSMProperty.TEXT_3D, button3D.isActive());
				buttonBold.enabled = !button3D.isActive();
				buttonStrike.enabled = !button3D.isActive();
				buttonUnderline.enabled = !button3D.isActive();
				buttonItalic.enabled = !button3D.isActive();
				buttonObfuscated.enabled = !button3D.isActive();
				buttonShadow.enabled = !button3D.isActive();
			}
		});

		this.sliderTranslateSpeed.enabled = this.buttonTranslating.isActive();
		this.buttonReverseTranslating.enabled = this.buttonTranslating.isActive();

		this.buttonBold.enabled = !this.button3D.isActive();
		this.buttonStrike.enabled = !this.button3D.isActive();
		this.buttonUnderline.enabled = !this.button3D.isActive();
		this.buttonItalic.enabled = !this.button3D.isActive();
		this.buttonObfuscated.enabled = !this.button3D.isActive();
		this.buttonShadow.enabled = !this.button3D.isActive();

		this.addButton(new GuiButton(19, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				mc.displayGuiScreen(new GuiSpotLight(invPlayer, tile, world));
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
		TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimension, TSMJsonManager.getDataFromTile(this.tile).toString()));
		super.onGuiClosed();
	}

	@Override
	public void onChangeSliderValue(GuiSlider slider) {
		switch (slider.id) {
		case 0:
			this.tile.setProperty(EnumTSMProperty.TEXT_HEIGHT, (short) (slider.getValueInt()));
			break;
		case 1:
			this.tile.setProperty(EnumTSMProperty.TEXT_SCALE, (short) (slider.getValueInt()));
			break;
		case 9:
			this.tile.setProperty(EnumTSMProperty.TEXT_TRANSLATE_SPEED, (short) (slider.getValueInt()));
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
		this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
		this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.beamspecs")), x - 30, y - 35, 0xffffff);
	}
}