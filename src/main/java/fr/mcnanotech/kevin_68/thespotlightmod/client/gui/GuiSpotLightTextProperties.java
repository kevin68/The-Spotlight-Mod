package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.ButtonToggle;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.ButtonToggleHelp;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.IHelpButton;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.TSMButton;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.TSMButtonSlider;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class GuiSpotLightTextProperties extends ContainerScreen<ContainerSpotLight> {
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

	private PlayerInventory invPlayer;
	private TileEntitySpotLight tile;
	private TSMButtonSlider sliderTranslateSpeed;
	private ButtonToggle buttonBold, buttonStrike, buttonUnderline, buttonItalic, buttonObfuscated, buttonShadow, buttonTranslating, buttonReverseTranslating, button3D;

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
		this.addButton(new TSMButtonSlider(x - 50, y - 20, 130, 20, I18n.format("container.spotlight.textheight"), "", -100, 100, this.tile.getShort(EnumTSMProperty.TEXT_HEIGHT), false, true, b -> {
			buttonBold.toggle();
			tile.setProperty(EnumTSMProperty.TEXT_BOLD, buttonBold.isActive());
		}, slider -> {
			this.tile.setProperty(EnumTSMProperty.TEXT_HEIGHT, (short) (slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.textprops.height")));
		this.addButton(new TSMButtonSlider(x + 90, y - 20, 130, 20, I18n.format("container.spotlight.textscale"), "", 0, 100, this.tile.getShort(EnumTSMProperty.TEXT_SCALE), false, true, b -> {
			buttonStrike.toggle();
			tile.setProperty(EnumTSMProperty.TEXT_STRIKE, buttonStrike.isActive());
		}, slider -> {
			this.tile.setProperty(EnumTSMProperty.TEXT_SCALE, (short) (slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.textprops.scale")));
		this.addButton(this.buttonBold = new ButtonToggle(x - 50, y + 3, 130, 20, I18n.format("container.spotlight.bold"), this.tile.getBoolean(EnumTSMProperty.TEXT_BOLD), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_BOLD, buttonBold.isActive());
		}, I18n.format("tutorial.spotlight.textprops.bold")));
		this.addButton(this.buttonStrike = new ButtonToggle(x + 90, y + 3, 130, 20, I18n.format("container.spotlight.strike"), this.tile.getBoolean(EnumTSMProperty.TEXT_STRIKE), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_STRIKE, buttonStrike.isActive());
		}, I18n.format("tutorial.spotlight.textprops.strike")));
		this.addButton(this.buttonUnderline = new ButtonToggle(x - 50, y + 26, 130, 20, I18n.format("container.spotlight.underline"), this.tile.getBoolean(EnumTSMProperty.TEXT_UNDERLINE), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_UNDERLINE, buttonUnderline.isActive());
		}, I18n.format("tutorial.spotlight.textprops.underline")));
		this.addButton(this.buttonItalic = new ButtonToggle(x + 90, y + 26, 130, 20, I18n.format("container.spotlight.italic"), this.tile.getBoolean(EnumTSMProperty.TEXT_ITALIC), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_ITALIC, buttonItalic.isActive());
		}, I18n.format("tutorial.spotlight.textprops.italic")));
		this.addButton(this.buttonObfuscated = new ButtonToggle(x - 50, y + 49, 130, 20, I18n.format("container.spotlight.obfuscated"), this.tile.getBoolean(EnumTSMProperty.TEXT_OBFUSCATED), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_OBFUSCATED, buttonObfuscated.isActive());
		}, I18n.format("tutorial.spotlight.textprops.obfuscated")));
		this.addButton(this.buttonShadow = new ButtonToggle(x + 90, y + 49, 130, 20, I18n.format("container.spotlight.shadow"), this.tile.getBoolean(EnumTSMProperty.TEXT_SHADOW), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_SHADOW, buttonShadow.isActive());
		}, I18n.format("tutorial.spotlight.textprops.shadow")));
		this.addButton(this.buttonTranslating = new ButtonToggle(x - 50, y + 72, 130, 20, I18n.format("container.spotlight.translate"), this.tile.getBoolean(EnumTSMProperty.TEXT_TRANSLATING), b -> {
			buttonTranslating.toggle();
			tile.setProperty(EnumTSMProperty.TEXT_TRANSLATING, buttonTranslating.isActive());
			sliderTranslateSpeed.active = buttonTranslating.isActive();
			buttonReverseTranslating.active = buttonTranslating.isActive();
		}, ""));  //TODO: missing help text?
		this.addButton(this.sliderTranslateSpeed = new TSMButtonSlider(x + 90, y + 72, 130, 20, I18n.format("container.spotlight.translatespeed"), "", 0, 100, this.tile.getShort(EnumTSMProperty.TEXT_TRANSLATE_SPEED), false, true, b -> {}, slider -> {
			this.tile.setProperty(EnumTSMProperty.TEXT_TRANSLATE_SPEED, (short) (slider.getValueInt()));
		}, "")); //TODO: missing help text?
		this.addButton(this.buttonReverseTranslating = new ButtonToggle(x - 50, y + 95, 130, 20, I18n.format("container.spotlight.reversetranslate"), this.tile.getBoolean(EnumTSMProperty.TEXT_T_REVERSE), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_T_REVERSE, buttonReverseTranslating.isActive());
		}, ""));  //TODO: missing help text?

		this.addButton(this.button3D = new ButtonToggle(x + 90, y + 95, 130, 20, I18n.format("container.spotlight.text3d"), this.tile.getBoolean(EnumTSMProperty.TEXT_3D), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_3D, button3D.isActive());
			buttonBold.active = !button3D.isActive();
			buttonStrike.active = !button3D.isActive();
			buttonUnderline.active = !button3D.isActive();
			buttonItalic.active = !button3D.isActive();
			buttonObfuscated.active = !button3D.isActive();
			buttonShadow.active = !button3D.isActive();
		}, ""));  //TODO: missing help text?

		this.sliderTranslateSpeed.active = this.buttonTranslating.isActive();
		this.buttonReverseTranslating.active = this.buttonTranslating.isActive();

		this.buttonBold.active = !this.button3D.isActive();
		this.buttonStrike.active = !this.button3D.isActive();
		this.buttonUnderline.active = !this.button3D.isActive();
		this.buttonItalic.active = !this.button3D.isActive();
		this.buttonObfuscated.active = !this.button3D.isActive();
		this.buttonShadow.active = !this.button3D.isActive();

		this.addButton(new TSMButton(x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back"), b -> {
			minecraft.displayGuiScreen(new GuiSpotLight(container, invPlayer, title));
		}, I18n.format("tutorial.spotlight.back")));
		this.addButton(new ButtonToggleHelp(x + 180, y + 140, 20, 20, tile));
	}

	@Override
	public void onClose() {
		TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos(), TSMJsonManager.getDataFromTile(this.tile).toString()));
		super.onClose();
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
		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		this.blit(x, y + 114, 69, 81, this.xSize, 52);
		this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.beamspecs")), x - 30, y - 35, 0xffffff);
	}
}
