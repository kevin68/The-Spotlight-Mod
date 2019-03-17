package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

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
        this.buttonList.add(new GuiSlider(0, x - 50, y - 20, 130, 20, I18n.format("container.spotlight.sizeMain"), "", 0, 100, this.tile.getShort(EnumTSMProperty.BEAM_SIZE), false, true, this));
        this.buttonList.add(this.sliderSecBeamSize = new GuiSlider(1, x + 90, y - 20, 130, 20, I18n.format("container.spotlight.sizeSec"), "", 0, 100, this.tile.getShort(EnumTSMProperty.BEAM_SEC_SIZE), false, true, this));
        this.buttonList.add(this.buttonSecBeamEnabled = new GuiBooleanButton(2, x - 50, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_SEC_ENABLED)));
        this.buttonSecBeamEnabled.setTexts(I18n.format("container.spotlight.secondlazer", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.secondlazer", I18n.format("container.spotlight.off")));
        this.buttonList.add(this.buttonDoubleBeam = new GuiBooleanButton(3, x + 90, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_DOUBLE)));
        this.buttonDoubleBeam.setTexts(I18n.format("container.spotlight.double"), I18n.format("container.spotlight.simple"));
        this.buttonList.add(new GuiSlider(4, x - 50, y + 30, 270, 20, I18n.format("container.spotlight.laserHeight"), "", 0, 512, this.tile.getShort(EnumTSMProperty.BEAM_HEIGHT), false, true, this));
        this.buttonList.add(new GuiSlider(5, x - 50, y + 55, 130, 20, I18n.format("container.spotlight.sides"), "", 2, 50, this.tile.getShort(EnumTSMProperty.BEAM_SIDE) + 2, false, true, this));
        this.buttonList.add(new GuiSlider(6, x + 90, y + 55, 130, 20, I18n.format("container.spotlight.beamspeed"), "", -2, 2, this.tile.getFloat(EnumTSMProperty.BEAM_SPEED) - 2.0D, true, true, this));
        this.buttonDoubleBeam.shouldNotChangeTextColor(true);
        this.sliderSecBeamSize.enabled = this.buttonSecBeamEnabled.isActive();

        this.buttonList.add(new GuiButton(19, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(this.buttonHelp = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", this.tile.helpMode));
    }

    @Override
    public void onGuiClosed()
    {
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimension, TSMJsonManager.getDataFromTile(this.tile).toString()));
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch(guibutton.id)
        {
            case 2:
                this.buttonSecBeamEnabled.toggle();
                this.sliderSecBeamSize.enabled = this.buttonSecBeamEnabled.isActive();
                this.tile.setProperty(EnumTSMProperty.BEAM_SEC_ENABLED, this.buttonSecBeamEnabled.isActive());
                break;
            case 3:
                this.buttonDoubleBeam.toggle();
                this.tile.setProperty(EnumTSMProperty.BEAM_DOUBLE, this.buttonDoubleBeam.isActive());
                break;
            case 19:
                this.mc.displayGuiScreen(new GuiSpotLight(this.invPlayer, this.tile, this.world));
                break;
            case 20:
                this.buttonHelp.toggle();
                break;
        }
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
    public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
    {
        super.drawScreen(mouseX, mouseY, partialRenderTick);

        if(this.buttonHelp.isActive())
        {
            for(GuiButton button : this.buttonList)
            {
                if(button.isMouseOver())
                {
                    String text = "";
                    boolean isBeam = this.tile.isBeam;
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
        this.mc.renderEngine.bindTexture(texture);
        this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
        this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.beamspecs")), x - 30, y - 35, 0xffffff);
    }
}