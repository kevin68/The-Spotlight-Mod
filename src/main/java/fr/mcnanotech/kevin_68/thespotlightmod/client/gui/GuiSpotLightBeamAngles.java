package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;

public class GuiSpotLightBeamAngles extends GuiContainer implements ISlider
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tile;
    public World world;

    private GuiBooleanButton buttonX, buttonY, buttonZ, buttonAR, buttonRR, buttonHelp;
    private GuiSlider sliderAngle, sliderSpeed;

    public GuiSpotLightBeamAngles(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
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

        this.buttonList.add(this.buttonX = new GuiBooleanButton(0, x - 50, y + 110, 20, 20, "X", true));
        this.buttonList.add(this.buttonY = new GuiBooleanButton(1, x - 25, y + 110, 20, 20, "Y", false));
        this.buttonList.add(this.buttonZ = new GuiBooleanButton(2, x, y + 110, 20, 20, "Z", false));
        this.buttonList.add(this.sliderAngle = new GuiSlider(3, x - 50, y - 20, 270, 20, I18n.format("container.spotlight.angleval", "X"), "", 0, 360, this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_X), false, true, this));
        this.buttonList.add(this.buttonAR = new GuiBooleanButton(4, x - 50, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_X)));
        this.buttonAR.setTexts(I18n.format("container.spotlight.rotate", "X", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "X", I18n.format("container.spotlight.off")));
        this.sliderAngle.enabled = !this.buttonAR.isActive();
        this.buttonList.add(this.buttonRR = new GuiBooleanButton(5, x + 90, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_X)));
        this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "X", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "X", I18n.format("container.spotlight.off")));
        this.buttonRR.enabled = this.buttonAR.enabled;
        this.buttonList.add(this.sliderSpeed = new GuiSlider(6, x - 50, y + 30, 270, 20, I18n.format("container.spotlight.rotationspeed", "X"), "", 0, 200, this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_X), false, true, this));
        this.sliderSpeed.enabled = this.buttonAR.isActive();

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
            case 0:
            {
                if(!this.buttonX.isActive())
                {
                    this.buttonX.toggle();
                    if(this.buttonY.isActive())
                    {
                        this.buttonY.toggle();
                    }
                    if(this.buttonZ.isActive())
                    {
                        this.buttonZ.toggle();
                    }
                    this.sliderAngle.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_X);
                    this.sliderAngle.dispString = I18n.format("container.spotlight.angleval", "X");
                    this.sliderAngle.updateSlider();
                    this.sliderSpeed.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_X);
                    this.sliderSpeed.dispString = I18n.format("container.spotlight.angleval", "X");
                    this.sliderSpeed.updateSlider();
                    if(this.buttonAR.isActive() != this.tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_X))
                    {
                        this.buttonAR.toggle();
                    }
                    this.sliderAngle.enabled = !this.buttonAR.isActive();
                    this.sliderSpeed.enabled = this.buttonAR.isActive();
                    this.buttonAR.setTexts(I18n.format("container.spotlight.rotate", "X", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "X", I18n.format("container.spotlight.off")));
                    if(this.buttonRR.isActive() != this.tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_X))
                    {
                        this.buttonRR.toggle();
                    }
                    this.buttonRR.enabled = this.buttonAR.isActive();
                    this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "X", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "X", I18n.format("container.spotlight.off")));
                    TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimension, TSMJsonManager.getDataFromTile(this.tile).toString()));
                }
                break;
            }
            case 1:
            {
                if(!this.buttonY.isActive())
                {
                    this.buttonY.toggle();
                    if(this.buttonX.isActive())
                    {
                        this.buttonX.toggle();
                    }
                    if(this.buttonZ.isActive())
                    {
                        this.buttonZ.toggle();
                    }
                    this.sliderAngle.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_Y);
                    this.sliderAngle.dispString = I18n.format("container.spotlight.angleval", "Y");
                    this.sliderAngle.updateSlider();
                    this.sliderSpeed.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_Y);
                    this.sliderSpeed.dispString = I18n.format("container.spotlight.angleval", "Y");
                    this.sliderSpeed.updateSlider();
                    if(this.buttonAR.isActive() != this.tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_Y))
                    {
                        this.buttonAR.toggle();
                    }
                    this.sliderAngle.enabled = !this.buttonAR.isActive();
                    this.sliderSpeed.enabled = this.buttonAR.isActive();
                    this.buttonAR.setTexts(I18n.format("container.spotlight.rotate", "Y", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "Y", I18n.format("container.spotlight.off")));
                    if(this.buttonRR.isActive() != this.tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_Y))
                    {
                        this.buttonRR.toggle();
                    }
                    this.buttonRR.enabled = this.buttonAR.isActive();
                    this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "Y", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "Y", I18n.format("container.spotlight.off")));
                    TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimension, TSMJsonManager.getDataFromTile(this.tile).toString()));
                }
                break;
            }
            case 2:
            {
                if(!this.buttonZ.isActive())
                {
                    this.buttonZ.toggle();
                    if(this.buttonX.isActive())
                    {
                        this.buttonX.toggle();
                    }
                    if(this.buttonY.isActive())
                    {
                        this.buttonY.toggle();
                    }
                    this.sliderAngle.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_Z);
                    this.sliderAngle.dispString = I18n.format("container.spotlight.angleval", "Z");
                    this.sliderAngle.updateSlider();
                    this.sliderSpeed.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_Z);
                    this.sliderSpeed.dispString = I18n.format("container.spotlight.angleval", "Z");
                    this.sliderSpeed.updateSlider();
                    if(this.buttonAR.isActive() != this.tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_Z))
                    {
                        this.buttonAR.toggle();
                    }
                    this.sliderAngle.enabled = !this.buttonAR.isActive();
                    this.sliderSpeed.enabled = this.buttonAR.isActive();
                    this.buttonAR.setTexts(I18n.format("container.spotlight.rotate", "Z", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "Z", I18n.format("container.spotlight.off")));
                    if(this.buttonRR.isActive() != this.tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_Z))
                    {
                        this.buttonRR.toggle();
                    }
                    this.buttonRR.enabled = this.buttonAR.isActive();
                    this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "Z", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "Z", I18n.format("container.spotlight.off")));
                    TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimension, TSMJsonManager.getDataFromTile(this.tile).toString()));
                }
                break;
            }
            case 4:
            {
                this.buttonAR.toggle();
                if(this.buttonX.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_AUTO_X, this.buttonAR.isActive());
                }
                else if(this.buttonY.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_AUTO_Y, this.buttonAR.isActive());
                }
                else if(this.buttonZ.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_AUTO_Z, this.buttonAR.isActive());
                }
                this.sliderAngle.enabled = !this.buttonAR.isActive();
                this.sliderSpeed.enabled = this.buttonAR.isActive();
                this.buttonRR.enabled = this.buttonAR.isActive();
                break;
            }
            case 5:
            {
                this.buttonRR.toggle();
                if(this.buttonX.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_REVERSE_X, this.buttonRR.isActive());
                }
                else if(this.buttonY.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_REVERSE_Y, this.buttonRR.isActive());
                }
                else if(this.buttonZ.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_REVERSE_Z, this.buttonRR.isActive());
                }
                break;
            }
            case 19:
            {
                this.mc.displayGuiScreen(new GuiSpotLight(this.invPlayer, this.tile, this.world));
                break;
            }
            case 20:
            {
                this.buttonHelp.toggle();
                this.tile.helpMode = this.buttonHelp.isActive();
                break;
            }
        }
    }

    @Override
    public void onChangeSliderValue(GuiSlider slider)
    {
        switch(slider.id)
        {
            case 3:
            {
                if(this.buttonX.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_ANGLE_X, (short)slider.getValueInt());
                }
                else if(this.buttonY.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_ANGLE_Y, (short)slider.getValueInt());
                }
                else if(this.buttonZ.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_ANGLE_Z, (short)slider.getValueInt());
                }
                break;
            }
            case 6:
            {
                if(this.buttonX.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_SPEED_X, (short)slider.getValueInt());
                }
                else if(this.buttonY.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_SPEED_Y, (short)slider.getValueInt());
                }
                else if(this.buttonZ.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_SPEED_Z, (short)slider.getValueInt());
                }
                break;
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialRenderTick)
    {

        super.render(mouseX, mouseY, partialRenderTick);

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
                            text = I18n.format("tutorial.spotlight.angles.x");
                            break;
                        case 1:
                            text = I18n.format("tutorial.spotlight.angles.y");
                            break;
                        case 2:
                            text = I18n.format("tutorial.spotlight.angles.z");
                            break;
                        case 3:
                            text = I18n.format("tutorial.spotlight.angles.angle");
                            break;
                        case 4:
                            text = I18n.format("tutorial.spotlight.angles.autorotate");
                            break;
                        case 5:
                            text = I18n.format("tutorial.spotlight.angles.reverserotation");
                            break;
                        case 6:
                            text = I18n.format("tutorial.spotlight.angles.rotationspeed");
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
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
        this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.angle")), x - 30, y - 35, 0xffffff);
    }
}