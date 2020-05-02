package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.util.Optional;

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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiSpotLightBeamColor extends ContainerScreen<ContainerSpotLight>
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID, "textures/gui/icons.png");

    public PlayerInventory invPlayer;
    public TileEntitySpotLight tile;

    public GuiSpotLightBeamColor(ContainerSpotLight container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        container.showPlayerSlot(false);
        this.invPlayer = playerInventory;
        this.tile = container.getSpotlight();
    }

    @Override
    public void init()
    {
        super.init();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        this.addButton(new TSMButtonSlider(x - 40, y - 20, 256, 20, TextFormatting.RED + I18n.format("container.spotlight.red"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_RED), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_RED, (short)(slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.colors.red")));
        this.addButton(new TSMButtonSlider(x - 40, y + 2, 256, 20, TextFormatting.GREEN + I18n.format("container.spotlight.green"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_GREEN), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_GREEN, (short)(slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.colors.green")));
        this.addButton(new TSMButtonSlider(x - 40, y + 24, 256, 20, TextFormatting.BLUE + I18n.format("container.spotlight.blue"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_BLUE), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_BLUE, (short)(slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.colors.blue")));

        this.addButton(new TSMButtonSlider(x - 40, y + 68, 256, 20, TextFormatting.DARK_RED + I18n.format("container.spotlight.red"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_SEC_RED), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SEC_RED, (short)(slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.colors.secred")));
        this.addButton(new TSMButtonSlider(x - 40, y + 90, 256, 20, TextFormatting.DARK_GREEN + I18n.format("container.spotlight.green"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_SEC_GREEN), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SEC_GREEN, (short)(slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.colors.secgreen")));
        this.addButton(new TSMButtonSlider(x - 40, y + 112, 256, 20, TextFormatting.DARK_BLUE + I18n.format("container.spotlight.blue"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_SEC_BLUE), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SEC_BLUE, (short)(slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.colors.secblue")));

        this.addButton(new TSMButtonSlider(x - 40, y + 46, 256, 20, I18n.format("container.spotlight.alpha"), "", 0, 1, this.tile.getFloat(EnumTSMProperty.BEAM_ALPHA), true, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_ALPHA, (float)(slider.getValue()));
        }, I18n.format("tutorial.spotlight.colors.alpha")));
        this.addButton(new TSMButtonSlider(x - 40, y + 134, 256, 20, I18n.format("container.spotlight.alpha"),"", 0, 1, this.tile.getFloat(EnumTSMProperty.BEAM_SEC_ALPHA), true, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SEC_ALPHA, (float)(slider.getValue()));
        }, I18n.format("tutorial.spotlight.colors.secalpha")));

        this.addButton(new TSMButton(x + 38, y + 159, 100, 20, I18n.format("container.spotlight.back"), b -> {
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
    public void render(int mouseX, int mouseY, float partialRenderTick)
    {
        super.render(mouseX, mouseY, partialRenderTick);

        if (this.tile.helpMode) {
			this.buttons.stream().filter(b -> b.isMouseOver(mouseX, mouseY) && b instanceof IHelpButton).findFirst().ifPresent(b -> {
				this.renderTooltip(this.font.listFormattedStringToWidth(TextFormatting.GREEN + ((IHelpButton)b).getHelpMessage(), (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
			});
        }
    }

    @Override
	public boolean mouseReleased(double mouseX, double mouseY, int button)
	{
		Optional<Widget> slider = this.buttons.stream().filter(b -> (b instanceof GuiSlider && ((GuiSlider)b).dragging)).findFirst();
		if (slider.isPresent())
		{
			slider.get().onRelease(mouseX, mouseY);
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        this.blit(x, y + 114, 69, 81, this.xSize, 52);
        this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.color")), x - 30, y - 35, 0xffffff);
    }
}
