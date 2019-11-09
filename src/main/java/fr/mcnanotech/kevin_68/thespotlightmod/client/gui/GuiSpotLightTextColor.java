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
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class GuiSpotLightTextColor extends ContainerScreen<ContainerSpotLight> implements IGuiEventListener {
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

    private PlayerInventory invPlayer;
    private TileEntitySpotLight tile;
    private TextFieldWidget textField;

    public GuiSpotLightTextColor(ContainerSpotLight container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.invPlayer = playerInventory;
        this.tile = container.getSpotlight();
    }

    @Override
    public void init() {
        super.init();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.addButton(new TSMButtonSlider(x - 40, y + 24, 256, 20, TextFormatting.RED + I18n.format("container.spotlight.red"), "", 0, 255, this.tile.getShort(EnumTSMProperty.TEXT_RED), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.TEXT_RED, (short) (slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.textcolors.red")));
        this.addButton(new TSMButtonSlider(x - 40, y + 46, 256, 20, TextFormatting.GREEN + I18n.format("container.spotlight.green"), "", 0, 255, this.tile.getShort(EnumTSMProperty.TEXT_GREEN), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.TEXT_GREEN, (short) (slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.textcolors.green")));
        this.addButton(new TSMButtonSlider(x - 40, y + 68, 256, 20, TextFormatting.BLUE + I18n.format("container.spotlight.blue"), "", 0, 255, this.tile.getShort(EnumTSMProperty.TEXT_BLUE), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.TEXT_BLUE, (short) (slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.textcolors.blue")));

        this.textField = new TextFieldWidget(this.font, x - 40, y, 256, 12, "");
        this.textField.setTextColor((this.tile.getShort(EnumTSMProperty.TEXT_RED) * 65536) + (this.tile.getShort(EnumTSMProperty.TEXT_GREEN) * 256) + this.tile.getShort(EnumTSMProperty.TEXT_BLUE));
        this.textField.setEnableBackgroundDrawing(true);
        this.textField.setMaxStringLength(40);
        this.textField.setEnabled(true);
        this.textField.setText(this.tile.getString(EnumTSMProperty.TEXT));
        this.children.add(this.textField);

        this.addButton(new TSMButton(x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back"), b -> {
            minecraft.displayGuiScreen(new GuiSpotLight(container, invPlayer, title));
        }, I18n.format("tutorial.spotlight.back")));
        this.addButton(new ButtonToggleHelp(x + 180, y + 140, 20, 20, tile));
    }

    @Override
    public void onClose() {
        super.onClose();
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos(), TSMJsonManager.getDataFromTile(this.tile).toString()));
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialRenderTick) {
        super.render(mouseX, mouseY, partialRenderTick);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.textField.render(mouseX, mouseY, partialRenderTick);

        if (this.tile.helpMode) {
            if (this.textField.isMouseOver(mouseX, mouseY)) {
                this.renderTooltip(this.font.listFormattedStringToWidth(TextFormatting.GREEN + I18n.format("tutorial.spotlight.textcolors.text"), (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
            }

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
        this.minecraft.getTextureManager().bindTexture(texture);
        this.blit(x, y + 114, 69, 81, this.xSize, 52);
        this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.color")), x - 30, y - 35, 0xffffff);
    }

    @Override
    public boolean charTyped(char c, int i) {
        if (this.textField.charTyped(c, i) && !this.textField.getText().isEmpty()) {
            this.tile.setProperty(EnumTSMProperty.TEXT, this.textField.getText());
            return true;
        } else {
           return false;
        }
    }
}
