package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiSliderButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.ISliderButton;

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
        super(new ContainerSpotLight(tileEntity, playerInventory, wrld, 8));
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

        this.buttonList.add(this.buttonX = new GuiBooleanButton(0, x - 50, y + 110, 20, 20, "X"));
        this.buttonX.setActive(true);
        this.buttonList.add(this.buttonY = new GuiBooleanButton(1, x - 25, y + 110, 20, 20, "Y"));
        this.buttonX.setActive(false);
        this.buttonList.add(this.buttonZ = new GuiBooleanButton(2, x, y + 110, 20, 20, "Z"));
        this.buttonX.setActive(false);
        this.buttonList.add(this.sliderAngle = new GuiSliderButton(this, 3, x - 50, y - 20, 270, 20, I18n.format("container.spotlight.angle") + " X: " + this.tile.beamAngleX, this.tile.beamAngleX / 360.0F));
        this.buttonList.add(this.buttonAR = new GuiBooleanButton(4, x - 50, y + 5, 130, 20, ""));
        this.buttonAR.setActive(this.tile.beamAutoRotateX);
        this.buttonAR.setTexts(I18n.format("container.spotlight.rotate") + " X: " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotate") + " X: " + I18n.format("container.spotlight.off"));
        this.sliderAngle.enabled = !this.buttonAR.isActive();
        this.buttonList.add(this.buttonRR = new GuiBooleanButton(5, x + 90, y + 5, 130, 20, ""));
        this.buttonRR.setActive(this.tile.beamReverseRotateX);
        this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse") + " X: " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotationreverse") + " X: " + I18n.format("container.spotlight.off"));
        this.buttonRR.enabled = this.buttonAR.enabled;
        this.buttonList.add(this.sliderSpeed = new GuiSliderButton(this, 6, x - 50, y + 30, 270, 20, I18n.format("container.spotlight.rotationspeed") + " X: " + this.tile.beamRotationSpeedX, this.tile.beamRotationSpeedX / 200.0F));
        this.sliderSpeed.enabled = this.buttonAR.isActive();

        this.buttonList.add(new GuiButton(19, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(this.buttonHelp = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?"));
        this.buttonHelp.setActive(false);
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
                this.sliderAngle.sliderValue = this.tile.beamAngleX / 360.0F;
                this.sliderAngle.displayString = getSliderName(3, this.tile.beamAngleX / 360.0F);
                this.sliderSpeed.sliderValue = this.tile.beamRotationSpeedX / 200.0F;
                this.sliderSpeed.displayString = getSliderName(6, this.tile.beamRotationSpeedX / 200.0F);
                if(this.buttonAR.isActive() != this.tile.beamAutoRotateX)
                {
                    this.buttonAR.toggle();
                }
                this.sliderAngle.enabled = !this.buttonAR.isActive();
                this.sliderSpeed.enabled = this.buttonAR.isActive();
                this.buttonAR.setTexts(I18n.format("container.spotlight.rotate") + " X: " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotate") + " X: " + I18n.format("container.spotlight.off"));
                if(this.buttonRR.isActive() != this.tile.beamReverseRotateX)
                {
                    this.buttonRR.toggle();
                }
                this.buttonRR.enabled = this.buttonAR.isActive();
                this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse") + " X: " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotationreverse") + " X: " + I18n.format("container.spotlight.off"));
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
                this.sliderAngle.sliderValue = this.tile.beamAngleY / 360.0F;
                this.sliderAngle.displayString = getSliderName(3, this.tile.beamAngleY / 360.0F);
                this.sliderSpeed.sliderValue = this.tile.beamRotationSpeedY / 200.0F;
                this.sliderSpeed.displayString = getSliderName(6, this.tile.beamRotationSpeedY / 200.0F);
                if(this.buttonAR.isActive() != this.tile.beamAutoRotateY)
                {
                    this.buttonAR.toggle();
                }
                this.sliderAngle.enabled = !this.buttonAR.isActive();
                this.sliderSpeed.enabled = this.buttonAR.isActive();
                this.buttonAR.setTexts(I18n.format("container.spotlight.rotate") + " Y: " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotate") + " Y: " + I18n.format("container.spotlight.off"));
                if(this.buttonRR.isActive() != this.tile.beamReverseRotateY)
                {
                    this.buttonRR.toggle();
                }
                this.buttonRR.enabled = this.buttonAR.isActive();
                this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse") + " Y: " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotationreverse") + " Y: " + I18n.format("container.spotlight.off"));
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
                this.sliderAngle.sliderValue = this.tile.beamAngleZ / 360.0F;
                this.sliderAngle.displayString = getSliderName(3, this.tile.beamAngleZ / 360.0F);
                this.sliderSpeed.sliderValue = this.tile.beamRotationSpeedZ / 200.0F;
                this.sliderSpeed.displayString = getSliderName(6, this.tile.beamRotationSpeedZ / 200.0F);
                if(this.buttonAR.isActive() != this.tile.beamAutoRotateZ)
                {
                    this.buttonAR.toggle();
                }
                this.sliderAngle.enabled = !this.buttonAR.isActive();
                this.sliderSpeed.enabled = this.buttonAR.isActive();
                this.buttonAR.setTexts(I18n.format("container.spotlight.rotate") + " Z: " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotate") + " Z: " + I18n.format("container.spotlight.off"));
                if(this.buttonRR.isActive() != this.tile.beamReverseRotateZ)
                {
                    this.buttonRR.toggle();
                }
                this.buttonRR.enabled = this.buttonAR.isActive();
                this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse") + " Z: " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotationreverse") + " Z: " + I18n.format("container.spotlight.off"));
                TheSpotLightMod.network.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimensionID, TSMJsonManager.getDataFromTile(this.tile).toString()));
            }
            break;
        }
        case 4:
        {
            this.buttonAR.toggle();
            if(this.buttonX.isActive())
            {
                this.tile.beamAutoRotateX = this.buttonAR.isActive();
            }
            else if(this.buttonY.isActive())
            {
                this.tile.beamAutoRotateY = this.buttonAR.isActive();
            }
            else if(this.buttonZ.isActive())
            {
                this.tile.beamAutoRotateZ = this.buttonAR.isActive();
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
                this.tile.beamReverseRotateX = this.buttonRR.isActive();
            }
            else if(this.buttonY.isActive())
            {
                this.tile.beamReverseRotateY = this.buttonRR.isActive();
            }
            else if(this.buttonZ.isActive())
            {
                this.tile.beamReverseRotateZ = this.buttonRR.isActive();
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
                this.tile.beamAngleX = (short)(sliderValue * 360.0F);
            }
            else if(this.buttonY.isActive())
            {
                this.tile.beamAngleY = (short)(sliderValue * 360.0F);
            }
            else if(this.buttonZ.isActive())
            {
                this.tile.beamAngleZ = (short)(sliderValue * 360.0F);
            }
            break;
        }
        case 6:
        {
            if(this.buttonX.isActive())
            {
                this.tile.beamRotationSpeedX = (short)(sliderValue * 200);
            }
            else if(this.buttonY.isActive())
            {
                this.tile.beamRotationSpeedY = (short)(sliderValue * 200);
            }
            else if(this.buttonZ.isActive())
            {
                this.tile.beamRotationSpeedZ = (short)(sliderValue * 200);
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
                name = I18n.format("container.spotlight.angle") + " X: " + (short)(sliderValue * 360);
            }
            else if(this.buttonY.isActive())
            {
                name = I18n.format("container.spotlight.angle") + " Y: " + (short)(sliderValue * 360);
            }
            else if(this.buttonZ.isActive())
            {
                name = I18n.format("container.spotlight.angle") + " Z: " + (short)(sliderValue * 360);
            }
            break;
        }
        case 6:
        {
            if(this.buttonX.isActive())
            {
                name = I18n.format("container.spotlight.rotationspeed") + " X: " + (short)(sliderValue * 200);
            }
            else if(this.buttonY.isActive())
            {
                name = I18n.format("container.spotlight.rotationspeed") + " Y: " + (short)(sliderValue * 200);
            }
            else if(this.buttonZ.isActive())
            {
                name = I18n.format("container.spotlight.rotationspeed") + " Z: " + (short)(sliderValue * 200);
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
            TSMUtils.drawTextHelper(this.fontRendererObj, mouseX, mouseY, this.width, this.height, this.buttonList, this);
            // boolean reversed = mouseX > this.width / 2;
            // ArrayList<String> list = new ArrayList<String>();
            //
            // if(mouseX > x - 40 && mouseX < x + 216)
            // {
            // if(mouseY > y - 20 && mouseY < y)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.props.angle1"), mouseX,
            // this.width,
            // reversed);
            // }
            // if(mouseY > y + 2 && mouseY < y + 22)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.props.angle2"), mouseX,
            // this.width,
            // reversed);
            // }
            // if(mouseY > y + 68 && mouseY < y + 88)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.props.height"), mouseX,
            // this.width,
            // reversed);
            // }
            // }
            //
            // if(mouseX > x - 40 && mouseX < x + 87)
            // {
            // if(mouseY > y + 24 && mouseY < y + 44)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.props.autorotate"), mouseX,
            // this.width, reversed);
            // }
            // if(mouseY > y + 46 && mouseY < y + 66)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.props.rotationspeed"), mouseX,
            // this.width, reversed);
            // }
            // if(mouseY > y + 90 && mouseY < y + 110)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.props.mainlasersize"), mouseX,
            // this.width, reversed);
            // }
            //
            // if(mouseY > y + 113 && mouseY < y + 133 && mouseX < x + 30)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.props.simple"), mouseX,
            // this.width,
            // reversed);
            // }
            // }
            //
            // if(mouseX > x + 90 && mouseX < x + 216)
            // {
            // if(mouseY > y + 24 && mouseY < y + 44)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.props.reverserotate"), mouseX,
            // this.width, reversed);
            // }
            // if(mouseY > y + 46 && mouseY < y + 66)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.props.seclaserswitch"), mouseX,
            // this.width, reversed);
            // }
            // if(mouseY > y + 90 && mouseY < y + 110)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.props.seclasersize"), mouseX,
            // this.width, reversed);
            // }
            //
            // if(mouseY > y + 113 && mouseY < y + 133 && mouseX > x + 146)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.props.axe"), mouseX, this.width,
            // reversed);
            // }
            // }
            //
            // if(mouseX > x + 38 && mouseX < x + 138 && mouseY > y + 117 &&
            // mouseY
            // < y + 137)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.back"), mouseX, this.width,
            // reversed);
            // }
            //
            // if(mouseX > x + 180 && mouseX < x + 200 && mouseY > y + 140 &&
            // mouseY
            // < y + 160)
            // {
            // list = UtilSpotLight.formatedText(this.fontRendererObj,
            // I18n.format("tutorial.spotlight.help"), mouseX, this.width,
            // reversed);
            // }
            //
            // if(list.size() > 0 && (list.get(list.size() - 1) == " " ||
            // list.get(list.size() - 1).isEmpty()))
            // {
            // list.remove(list.size() - 1);
            // }
            // GuiHelper.drawHoveringText(list, mouseX, mouseY,
            // this.fontRendererObj, reversed ? 0 : 200000, this.height,
            // 0x00ff00);
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
        this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.angle")), x - 30, y - 35, 0xffffff);
    }
}