package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
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

public class GuiSpotLightBeamProperties extends ContainerScreen<ContainerSpotLight>
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

    private PlayerInventory invPlayer;
    private TileEntitySpotLight tile;
    private ButtonToggle buttonHelp;
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
        this.addButton(new GuiSlider(x - 50, y - 20, 130, 20, I18n.format("container.spotlight.sizeMain"), "", 0, 100, this.tile.getShort(EnumTSMProperty.BEAM_SIZE), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SIZE, (short)(slider.getValueInt()));
        }));
        this.sliderSecBeamSize = this.addButton(new GuiSlider(x + 90, y - 20, 130, 20, I18n.format("container.spotlight.sizeSec"), "", 0, 100, this.tile.getShort(EnumTSMProperty.BEAM_SEC_SIZE), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SEC_SIZE, (short)(slider.getValueInt()));
        }));
        this.buttonSecBeamEnabled = this.addButton(new ButtonToggle(x - 50, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_SEC_ENABLED), b -> {
            sliderSecBeamSize.enabled = buttonSecBeamEnabled.isActive();
            tile.setProperty(EnumTSMProperty.BEAM_SEC_ENABLED, buttonSecBeamEnabled.isActive());
		}));
        this.buttonSecBeamEnabled.setTexts(I18n.format("container.spotlight.secondlazer", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.secondlazer", I18n.format("container.spotlight.off")));
        this.buttonDoubleBeam = this.addButton(new ButtonToggle(x + 90, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_DOUBLE), b -> {
            tile.setProperty(EnumTSMProperty.BEAM_DOUBLE,buttonDoubleBeam.isActive());
        }));
        this.buttonDoubleBeam.setTexts(I18n.format("container.spotlight.double"), I18n.format("container.spotlight.simple"));
        this.addButton(new GuiSlider(x - 50, y + 30, 270, 20, I18n.format("container.spotlight.laserHeight"), "", 0, 512, this.tile.getShort(EnumTSMProperty.BEAM_HEIGHT), false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_HEIGHT, (short)(slider.getValueInt()));
        }));
        this.addButton(new GuiSlider(x - 50, y + 55, 130, 20, I18n.format("container.spotlight.sides"), "", 2, 50, this.tile.getShort(EnumTSMProperty.BEAM_SIDE) + 2, false, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SIDE, (short)(slider.getValueInt() - 2));
        }));
        this.addButton(new GuiSlider(x + 90, y + 55, 130, 20, I18n.format("container.spotlight.beamspeed"), "", -2, 2, this.tile.getFloat(EnumTSMProperty.BEAM_SPEED) - 2.0D, true, true, b -> {}, slider -> {
            this.tile.setProperty(EnumTSMProperty.BEAM_SPEED, TSMUtils.round((float)(slider.getValue() + 2.0D), 2));
        }));
        this.buttonDoubleBeam.shouldNotChangeTextColor(true);
        this.sliderSecBeamSize.enabled = this.buttonSecBeamEnabled.isActive();

        this.addButton(new Button(x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back"), b -> {
            Minecraft.getInstance().displayGuiScreen(new GuiSpotLight(container, invPlayer, title));
		}));
        this.addButton(this.buttonHelp = new ButtonToggle(x + 180, y + 140, 20, 20, "?", this.tile.helpMode, b -> {
			tile.helpMode = buttonHelp.isActive();
		}));
    }

    @Override
    public void onClose()
    {
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos(), TSMJsonManager.getDataFromTile(this.tile).toString()));
        super.onClose();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialRenderTick)
    {
        super.render(mouseX, mouseY, partialRenderTick);

        if(this.buttonHelp.isActive())
        {
            for(Button button : this.buttons)
            {
                if(button.isMouseOver())
                {
                    String text = "";
                    switch(button.id)
                    {
                        case 0:
                            text = I18n.format("tutorial.spotlight.beamprops.mainsize");
                            break;
                        case 1:
                            text = I18n.format("tutorial.spotlight.beamprops.secsize");
                            break;
                        case 2:
                            text = I18n.format("tutorial.spotlight.beamprops.secbeam");
                            break;
                        case 3:
                            text = I18n.format("tutorial.spotlight.beamprops.double");
                            break;
                        case 4:
                            text = I18n.format("tutorial.spotlight.beamprops.height");
                            break;
                        case 5:
                            text = I18n.format("tutorial.spotlight.beamprops.sides");
                            break;
                        case 19:
                            text = I18n.format("tutorial.spotlight.back");
                            break;
                        case 20:
                            text = I18n.format("tutorial.spotlight.help");
                            break;
                    }
                    if(!text.isEmpty())
                    {
                        this.drawHoveringText(this.font.listFormattedStringToWidth(TextFormatting.GREEN + text, (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
                    }
                }
            }
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
