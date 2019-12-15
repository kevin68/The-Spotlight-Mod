package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

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
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiSpotLightTextAngles extends ContainerScreen<ContainerSpotLight> {
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

	public PlayerInventory invPlayer;
	public TileEntitySpotLight tile;

	private ButtonToggle buttonAR, buttonRR;
	private GuiSlider sliderAngle, sliderSpeed;

    public GuiSpotLightTextAngles(ContainerSpotLight container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
		this.invPlayer = playerInventory;
		this.tile = container.getSpotlight();
	}

	@Override
	public void init() {
		super.init();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		this.addButton(this.sliderAngle = new TSMButtonSlider(x - 50, y - 20, 270, 20, I18n.format("container.spotlight.angleval", "Y"), "", 0, 360, this.tile.getShort(EnumTSMProperty.TEXT_ANGLE_Y), false, true, b -> {}, slider -> {
			this.tile.setProperty(EnumTSMProperty.TEXT_ANGLE_Y, (short) (slider.getValueInt()));
		}, I18n.format("tutorial.spotlight.textangles.angle")));

		this.addButton(this.buttonAR = new ButtonToggle(x - 50, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.TEXT_R_AUTO_Y), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_R_AUTO_Y, buttonAR.isActive());
			sliderAngle.active = !buttonAR.isActive();
			sliderSpeed.active = buttonAR.isActive();
			buttonRR.active = buttonAR.isActive();
		}, I18n.format("tutorial.spotlight.textangles.autorotate")));
		this.buttonAR.setTexts(I18n.format("container.spotlight.rotate", "Y", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "Y", I18n.format("container.spotlight.off")));
		this.sliderAngle.active = !this.buttonAR.isActive();

		this.addButton(this.buttonRR = new ButtonToggle(x + 90, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.TEXT_R_REVERSE_Y), b -> {
			tile.setProperty(EnumTSMProperty.TEXT_R_REVERSE_Y, buttonRR.isActive());
		}, I18n.format("tutorial.spotlight.textangles.reverserotation")));
		this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "Y", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "Y", I18n.format("container.spotlight.off")));
		this.buttonRR.active = this.buttonAR.active;

		this.addButton(this.sliderSpeed = new TSMButtonSlider(x - 50, y + 30, 270, 20, I18n.format("container.spotlight.rotationspeed", "Y"), "", 0, 200, this.tile.getShort(EnumTSMProperty.TEXT_R_SPEED_Y), false, true, b -> {}, slider -> {
			this.tile.setProperty(EnumTSMProperty.TEXT_R_SPEED_Y, (short) (slider.getValueInt()));
		}, I18n.format("tutorial.spotlight.textangles.rotationspeed")));
		this.sliderSpeed.active = this.buttonAR.isActive();

		this.addButton(new TSMButton(x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back"), b -> {
			minecraft.displayGuiScreen(new GuiSpotLight(container, invPlayer, title));
		}, I18n.format("tutorial.spotlight.back")));
		this.addButton(new ButtonToggleHelp(x + 180, y + 140, 20, 20, tile));
	}

	@Override
	public void removed() {
		TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos(), TSMJsonManager.getDataFromTile(this.tile).toString()));
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
		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		this.blit(x, y + 114, 69, 81, this.xSize, 52);
		this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.angle")), x - 30, y - 35, 0xffffff);
	}
}
