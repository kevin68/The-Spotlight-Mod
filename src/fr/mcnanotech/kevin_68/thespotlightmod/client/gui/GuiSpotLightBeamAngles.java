package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

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

public class GuiSpotLightBeamAngles extends GuiContainer implements ISliderButton
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tile;
    public World world;

    private GuiBooleanButton buttonX, buttonY, buttonZ, buttonAR, buttonRR, buttonHelp;
    private GuiSliderButton sliderAngle, sliderSpeed;

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
        this.buttonList.add(this.sliderAngle = new GuiSliderButton(this, 3, x - 50, y - 20, 270, 20, I18n.format("container.spotlight.angleval", "X", this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_X)), this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_X) / 360.0F));
        this.sliderAngle.shouldResetOnEnd(true);
        this.buttonList.add(this.buttonAR = new GuiBooleanButton(4, x - 50, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_R_AUTO_X)));
        this.buttonAR.setTexts(I18n.format("container.spotlight.rotate", "X", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "X", I18n.format("container.spotlight.off")));
        this.sliderAngle.enabled = !this.buttonAR.isActive();
        this.buttonList.add(this.buttonRR = new GuiBooleanButton(5, x + 90, y + 5, 130, 20, "", this.tile.getBoolean(EnumTSMProperty.BEAM_R_REVERSE_X)));
        this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "X", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "X", I18n.format("container.spotlight.off")));
        this.buttonRR.enabled = this.buttonAR.enabled;
        this.buttonList.add(this.sliderSpeed = new GuiSliderButton(this, 6, x - 50, y + 30, 270, 20, I18n.format("container.spotlight.rotationspeed", "X", this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_X)), this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_X) / 200.0F));
        this.sliderSpeed.enabled = this.buttonAR.isActive();

        this.buttonList.add(new GuiButton(19, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(this.buttonHelp = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", this.tile.helpMode));
    }

    @Override
    public void onGuiClosed()
    {
        TheSpotLightMod.network.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimensionID, TSMJsonManager.getDataFromTile(this.tile).toString()));
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
                    this.sliderAngle.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_X) / 360.0F;
                    this.sliderAngle.displayString = getSliderName(3, this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_X) / 360.0F);
                    this.sliderSpeed.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_X) / 200.0F;
                    this.sliderSpeed.displayString = getSliderName(6, this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_X) / 200.0F);
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
                    TheSpotLightMod.network.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimensionID, TSMJsonManager.getDataFromTile(this.tile).toString()));
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
                    this.sliderAngle.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_Y) / 360.0F;
                    this.sliderAngle.displayString = getSliderName(3, this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_Y) / 360.0F);
                    this.sliderSpeed.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_Y) / 200.0F;
                    this.sliderSpeed.displayString = getSliderName(6, this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_Y) / 200.0F);
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
                    TheSpotLightMod.network.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimensionID, TSMJsonManager.getDataFromTile(this.tile).toString()));
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
                    this.sliderAngle.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_Z) / 360.0F;
                    this.sliderAngle.displayString = getSliderName(3, this.tile.getShort(EnumTSMProperty.BEAM_ANGLE_Z) / 360.0F);
                    this.sliderSpeed.sliderValue = this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_Z) / 200.0F;
                    this.sliderSpeed.displayString = getSliderName(6, this.tile.getShort(EnumTSMProperty.BEAM_R_SPEED_Z) / 200.0F);
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
                    TheSpotLightMod.network.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimensionID, TSMJsonManager.getDataFromTile(this.tile).toString()));
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
    public void handlerSliderAction(int sliderId, float sliderValue)
    {
        switch(sliderId)
        {
            case 3:
            {
                if(this.buttonX.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_ANGLE_X, (short)(sliderValue * 360.0F));
                }
                else if(this.buttonY.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_ANGLE_Y, (short)(sliderValue * 360.0F));
                }
                else if(this.buttonZ.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_ANGLE_Z, (short)(sliderValue * 360.0F));
                }
                break;
            }
            case 6:
            {
                if(this.buttonX.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_SPEED_X, (short)(sliderValue * 200));
                }
                else if(this.buttonY.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_SPEED_Y, (short)(sliderValue * 200));
                }
                else if(this.buttonZ.isActive())
                {
                    this.tile.setProperty(EnumTSMProperty.BEAM_R_SPEED_Z, (short)(sliderValue * 200));
                }
                break;
            }
        }
    }

    @Override
    public String getSliderName(int sliderId, float sliderValue)
    {
        String name = "";
        switch(sliderId)
        {
            case 3:
            {
                if(this.buttonX.isActive())
                {
                    name = I18n.format("container.spotlight.angleval", "X", (short)(sliderValue * 360));
                }
                else if(this.buttonY.isActive())
                {
                    name = I18n.format("container.spotlight.angleval", "Y", (short)(sliderValue * 360));
                }
                else if(this.buttonZ.isActive())
                {
                    name = I18n.format("container.spotlight.angleval", "Z", (short)(sliderValue * 360));
                }
                break;
            }
            case 6:
            {
                if(this.buttonX.isActive())
                {
                    name = I18n.format("container.spotlight.rotationspeed", "X", (short)(sliderValue * 200));
                }
                else if(this.buttonY.isActive())
                {
                    name = I18n.format("container.spotlight.rotationspeed", "Y", (short)(sliderValue * 200));
                }
                else if(this.buttonZ.isActive())
                {
                    name = I18n.format("container.spotlight.rotationspeed", "Z", (short)(sliderValue * 200));
                }
                break;
            }
        }
        return name;
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
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.mc.renderEngine.bindTexture(texture);
        this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
        this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.angle")), x - 30, y - 35, 0xffffff);
    }
}