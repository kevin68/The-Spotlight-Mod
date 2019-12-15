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
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiSpotLightBeamProperties extends ContainerScreen<ContainerSpotLight>
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

    private PlayerInventory invPlayer;
    private TileEntitySpotLight tile;
    private GuiSlider sliderSecBeamSize;
    private ButtonToggle buttonSecBeamEnabled, buttonDoubleBeam;

    public GuiSpotLightBeamProperties(ContainerSpotLight container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.invPlayer = playerInventory;
		this.tile = container.getSpotlight();
	}

    @Override
    public void init()
    {
        super.init();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.addButton(new TSMButtonSlider(x - 50, y - 20, 130, 20, I18n.format("container.spotlight.sizeMain"), "", 0, 100, this.tile.getShort(EnumTSMProperty.BEAM_SIZE), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SIZE, (short)(slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.beamprops.mainsize")));
        this.sliderSecBeamSize = this.addButton(new TSMButtonSlider(x + 90, y - 20, 130, 20, I18n.format("container.spotlight.sizeSec"), "", 0, 100, this.tile.getShort(EnumTSMProperty.BEAM_SEC_SIZE), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SEC_SIZE, (short)(slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.beamprops.secsize")));
        this.buttonSecBeamEnabled = this.addButton(new ButtonToggle(x - 50, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_SEC_ENABLED), b -> {
            sliderSecBeamSize.active = buttonSecBeamEnabled.isActive();
            tile.setProperty(EnumTSMProperty.BEAM_SEC_ENABLED, buttonSecBeamEnabled.isActive());
		}, I18n.format("tutorial.spotlight.beamprops.secbeam")));
        this.buttonSecBeamEnabled.setTexts(I18n.format("container.spotlight.secondlazer", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.secondlazer", I18n.format("container.spotlight.off")));
        this.buttonDoubleBeam = this.addButton(new ButtonToggle(x + 90, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_DOUBLE), b -> {
            tile.setProperty(EnumTSMProperty.BEAM_DOUBLE,buttonDoubleBeam.isActive());
        }, I18n.format("tutorial.spotlight.beamprops.double")));
        this.buttonDoubleBeam.setTexts(I18n.format("container.spotlight.double"), I18n.format("container.spotlight.simple"));
        this.addButton(new TSMButtonSlider(x - 50, y + 30, 270, 20, I18n.format("container.spotlight.laserHeight"), "", 0, 512, this.tile.getShort(EnumTSMProperty.BEAM_HEIGHT), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_HEIGHT, (short)(slider.getValueInt()));
        }, I18n.format("tutorial.spotlight.beamprops.height")));
        this.addButton(new TSMButtonSlider(x - 50, y + 55, 130, 20, I18n.format("container.spotlight.sides"), "", 2, 50, this.tile.getShort(EnumTSMProperty.BEAM_SIDE) + 2, false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SIDE, (short)(slider.getValueInt() - 2));
        }, I18n.format("tutorial.spotlight.beamprops.sides")));
        this.addButton(new TSMButtonSlider(x + 90, y + 55, 130, 20, I18n.format("container.spotlight.beamspeed"), "", -2, 2, this.tile.getFloat(EnumTSMProperty.BEAM_SPEED) - 2.0D, true, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SPEED, TSMUtils.round((float)(slider.getValue() + 2.0D), 2));
        }, "")); //TODO: missing help text?
        this.buttonDoubleBeam.shouldNotChangeTextColor(true);
        this.sliderSecBeamSize.active = this.buttonSecBeamEnabled.isActive();

        this.addButton(new TSMButton(x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back"), b -> {
            minecraft.displayGuiScreen(new GuiSpotLight(container, invPlayer, title));
		}, I18n.format("tutorial.spotlight.back")));
        this.addButton(new ButtonToggleHelp(x + 180, y + 140, 20, 20, tile));
    }

    @Override
    public void removed()
    {
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
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        this.blit(x, y + 114, 69, 81, this.xSize, 52);
        this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.beamspecs")), x - 30, y - 35, 0xffffff);
    }
}
