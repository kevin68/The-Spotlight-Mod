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
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;

public class GuiSpotLightBeamProperties extends GuiContainer implements ISlider
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    private InventoryPlayer invPlayer;
    private TileEntitySpotLight tile;
    private World world;
    private GuiBooleanButton buttonHelp;
    private GuiSlider sliderSecBeamSize;
    private GuiBooleanButton buttonSecBeamEnabled, buttonDoubleBeam;

    public GuiSpotLightBeamProperties(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
    {
        super(new ContainerSpotLight(tileEntity, playerInventory, true));
        this.invPlayer = playerInventory;
        this.tile = tileEntity;
        this.world = wrld;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.addButton(new GuiSlider(0, x - 50, y - 20, 130, 20, I18n.format("container.spotlight.sizeMain"), "", 0, 100, this.tile.getShort(EnumTSMProperty.BEAM_SIZE), false, true, this));
        this.sliderSecBeamSize = this.addButton(new GuiSlider(1, x + 90, y - 20, 130, 20, I18n.format("container.spotlight.sizeSec"), "", 0, 100, this.tile.getShort(EnumTSMProperty.BEAM_SEC_SIZE), false, true, this));
        this.buttonSecBeamEnabled = this.addButton(new GuiBooleanButton(2, x - 50, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_SEC_ENABLED)) {
			@Override
			public void onClick(double mouseX, double mouseY) {
                buttonSecBeamEnabled.toggle();
                sliderSecBeamSize.enabled = buttonSecBeamEnabled.isActive();
                tile.setProperty(EnumTSMProperty.BEAM_SEC_ENABLED, buttonSecBeamEnabled.isActive());
			}
		});
        this.buttonSecBeamEnabled.setTexts(I18n.format("container.spotlight.secondlazer", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.secondlazer", I18n.format("container.spotlight.off")));
        this.buttonDoubleBeam = this.addButton(new GuiBooleanButton(3, x + 90, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_DOUBLE)));
        this.buttonDoubleBeam.setTexts(I18n.format("container.spotlight.double"), I18n.format("container.spotlight.simple"));
        this.addButton(new GuiSlider(4, x - 50, y + 30, 270, 20, I18n.format("container.spotlight.laserHeight"), "", 0, 512, this.tile.getShort(EnumTSMProperty.BEAM_HEIGHT), false, true, this));
        this.addButton(new GuiSlider(5, x - 50, y + 55, 130, 20, I18n.format("container.spotlight.sides"), "", 2, 50, this.tile.getShort(EnumTSMProperty.BEAM_SIDE) + 2, false, true, this));
        this.addButton(new GuiSlider(6, x + 90, y + 55, 130, 20, I18n.format("container.spotlight.beamspeed"), "", -2, 2, this.tile.getFloat(EnumTSMProperty.BEAM_SPEED) - 2.0D, true, true, this));
        this.buttonDoubleBeam.shouldNotChangeTextColor(true);
        this.sliderSecBeamSize.enabled = this.buttonSecBeamEnabled.isActive();

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
    public void onGuiClosed()
    {
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos(), TSMJsonManager.getDataFromTile(this.tile).toString()));
        super.onGuiClosed();
    }
    
    @Override
    public void onChangeSliderValue(GuiSlider slider)
    {
        switch(slider.id)
        {
            case 0:
            {
                this.tile.setProperty(EnumTSMProperty.BEAM_SIZE, (short)(slider.getValueInt()));
                break;
            }
            case 1:
            {
                this.tile.setProperty(EnumTSMProperty.BEAM_SEC_SIZE, (short)(slider.getValueInt()));
                break;
            }
            case 4:
            {
                this.tile.setProperty(EnumTSMProperty.BEAM_HEIGHT, (short)(slider.getValueInt()));
                break;
            }
            case 5:
            {
                this.tile.setProperty(EnumTSMProperty.BEAM_SIDE, (short)(slider.getValueInt() - 2));
                break;
            }
            case 6:
            {
                this.tile.setProperty(EnumTSMProperty.BEAM_SPEED, TSMUtils.round((float)(slider.getValue() + 2.0D), 2));
            }
        }        
    }

    @Override
    public void render(int mouseX, int mouseY, float partialRenderTick)
    {
        super.render(mouseX, mouseY, partialRenderTick);

        if(this.buttonHelp.isActive())
        {
            for(GuiButton button : this.buttons)
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
                        this.drawHoveringText(this.fontRenderer.listFormattedStringToWidth(TextFormatting.GREEN + text, (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
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
        this.mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
        this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.beamspecs")), x - 30, y - 35, 0xffffff);
    }
}