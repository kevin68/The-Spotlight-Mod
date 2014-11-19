package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiContainerSliderBase;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiMultipleOptionButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiSliderForContainer;

public class GuiSpotLightBeamSpec extends GuiContainerSliderBase
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tileSpotLight;
    public World world;
    public GuiBooleanButton rotateButton, revRotaButton, secLaserButton, sideLaser;
    public GuiMultipleOptionButton axeButton;
    public GuiBooleanButton helpButton;

    public GuiSpotLightBeamSpec(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
    {
        super(new ContainerSpotLight(tileEntity, playerInventory, wrld, 8));
        invPlayer = playerInventory;
        tileSpotLight = tileEntity;
        world = wrld;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        this.buttonList.add(new GuiSliderForContainer(this, 0, x - 40, y - 20, 256, 20, I18n.format("container.spotlight.angle") + " 1 : " + tileSpotLight.getAngle1(), (tileSpotLight.getAngle1()) / 360.0F));
        this.buttonList.add(new GuiSliderForContainer(this, 1, x - 40, y + 2, 256, 20, I18n.format("container.spotlight.angle") + " 2 : " + (tileSpotLight.getAngle2() & 0xFF), (tileSpotLight.getAngle2() & 0xFF) / 180.0F));
        this.buttonList.add(rotateButton = new GuiBooleanButton(2, x - 40, y + 24, 127, 20, I18n.format("container.spotlight.rotate") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotate") + " " + I18n.format("container.spotlight.off"), tileSpotLight.isAutoRotate()));
        this.buttonList.add(revRotaButton = new GuiBooleanButton(3, x + 90, y + 24, 127, 20, I18n.format("container.spotlight.rotationreverse") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.rotationreverse") + " " + I18n.format("container.spotlight.off"), tileSpotLight.isReverseRotation()));
        this.buttonList.add(new GuiSliderForContainer(this, 4, x - 40, y + 48, 127, 20, I18n.format("container.spotlight.rotationspeed") + " : " + (tileSpotLight.getRotationSpeed() & 0xFF), (tileSpotLight.getRotationSpeed()) / 20.0F));
        this.buttonList.add(secLaserButton = new GuiBooleanButton(5, x + 90, y + 48, 127, 20, I18n.format("container.spotlight.secondlazer") + " " + I18n.format("container.spotlight.on"), I18n.format("container.spotlight.secondlazer") + " " + I18n.format("container.spotlight.off"), tileSpotLight.isSecondaryLaser()));
        this.buttonList.add(axeButton = new GuiMultipleOptionButton(7, x + 146, y + 113, 70, 20, I18n.format("container.spotlight.axis") + " : ", new String[] {"y", "x", "z"}, (tileSpotLight.getDisplayAxe() & 0xFF)));
        this.buttonList.add(new GuiSliderForContainer(this, 11, x - 40, y + 70, 256, 20, I18n.format("container.spotlight.laserHeight") + " : " + tileSpotLight.getLaserHeight(), tileSpotLight.getLaserHeight() / 512.0F));
        this.buttonList.add(sideLaser = new GuiBooleanButton(8, x - 40, y + 113, 70, 20, I18n.format("container.spotlight.double"), I18n.format("container.spotlight.simple"), tileSpotLight.isSideLaser()));
        this.buttonList.add(new GuiSliderForContainer(this, 9, x - 40, y + 92, 127, 20, I18n.format("container.spotlight.sizeMain") + " : " + (tileSpotLight.getMainLaserSize() & 0xFF), (tileSpotLight.getMainLaserSize() & 0xFF) / 100.0F));
        this.buttonList.add(new GuiSliderForContainer(this, 10, x + 89, y + 92, 127, 20, I18n.format("container.spotlight.sizeSec") + " : " + (tileSpotLight.getSecLaserSize() & 0xFF), (tileSpotLight.getSecLaserSize() & 0xFF) / 100.0F));
        this.buttonList.add(new GuiButton(6, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch(guibutton.id)
        {
            case 2:
            {
                rotateButton.toggle();
                PacketSender.sendSpotLightPacketBoolean(tileSpotLight, (byte)10, rotateButton.getIsActive());
                break;
            }
            case 3:
            {
                revRotaButton.toggle();
                PacketSender.sendSpotLightPacketBoolean(tileSpotLight, (byte)11, revRotaButton.getIsActive());
                break;
            }
            case 5:
            {
                secLaserButton.toggle();
                PacketSender.sendSpotLightPacketBoolean(tileSpotLight, (byte)13, secLaserButton.getIsActive());
                break;
            }
            case 6:
            {
                this.mc.displayGuiScreen(new GuiSpotLight(invPlayer, tileSpotLight, world));
                break;
            }
            case 7:
            {
                axeButton.next();
                PacketSender.sendSpotLightPacketByte(tileSpotLight, (byte)14, (byte)axeButton.getState());
                break;
            }
            case 8:
            {
                sideLaser.toggle();
                PacketSender.sendSpotLightPacketBoolean(tileSpotLight, (byte)15, sideLaser.getIsActive());
                break;
            }
            case 20:
            {
                this.helpButton.toggle();
                break;
            }
        }
    }

    @Override
    public void handlerSliderAction(int sliderId, float sliderValue)
    {
        switch(sliderId)
        {
            case 0:
            {
                PacketSender.sendSpotLightPacket(tileSpotLight, 8, (int)(sliderValue * 360));
                break;
            }
            case 1:
            {
                PacketSender.sendSpotLightPacketByte(tileSpotLight, (byte)9, (byte)(sliderValue * 180));
                break;
            }
            case 4:
            {
                PacketSender.sendSpotLightPacketByte(tileSpotLight, (byte)12, (byte)(sliderValue * 20));
                break;
            }
            case 9:
            {
                PacketSender.sendSpotLightPacketByte(tileSpotLight, (byte)16, (byte)(sliderValue * 100));
                break;
            }
            case 10:
            {
                PacketSender.sendSpotLightPacketByte(tileSpotLight, (byte)17, (byte)(sliderValue * 100));
                break;
            }
            case 11:
            {
                PacketSender.sendSpotLightPacket(tileSpotLight, 25, (int)(sliderValue * 512));
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
            case 0:
            {
                name = I18n.format("container.spotlight.angle") + " 1 : " + (int)(sliderValue * 360);
                break;
            }
            case 1:
            {
                name = I18n.format("container.spotlight.angle") + " 2 : " + ((byte)(sliderValue * 180) & 0xFF);
                break;
            }
            case 4:
            {
                name = I18n.format("container.spotlight.rotationspeed") + " : " + ((byte)(sliderValue * 20) & 0xFF);
                break;
            }
            case 9:
            {
                name = I18n.format("container.spotlight.sizeMain") + " : " + ((byte)(sliderValue * 100) & 0xFF);
                break;
            }
            case 10:
            {
                name = I18n.format("container.spotlight.sizeSec") + " : " + ((byte)(sliderValue * 100) & 0xFF);
                break;
            }
            case 11:
            {
                name = I18n.format("container.spotlight.laserHeight") + " : " + ((int)(sliderValue * 512));
                break;
            }
        }
        return name;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
    {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        super.drawScreen(mouseX, mouseY, partialRenderTick);

        if(helpButton.getIsActive())
        {
            boolean reversed = mouseX > width / 2;
            ArrayList<String> list = new ArrayList<String>();

            if(mouseX > x - 40 && mouseX < x + 216)
            {
                if(mouseY > y - 20 && mouseY < y)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.props.angle1"), mouseX, width, reversed);
                }
                if(mouseY > y + 2 && mouseY < y + 22)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.props.angle2"), mouseX, width, reversed);
                }
                if(mouseY > y + 68 && mouseY < y + 88)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.props.height"), mouseX, width, reversed);
                }
            }

            if(mouseX > x - 40 && mouseX < x + 87)
            {
                if(mouseY > y + 24 && mouseY < y + 44)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.props.autorotate"), mouseX, width, reversed);
                }
                if(mouseY > y + 46 && mouseY < y + 66)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.props.rotationspeed"), mouseX, width, reversed);
                }
                if(mouseY > y + 90 && mouseY < y + 110)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.props.mainlasersize"), mouseX, width, reversed);
                }

                if(mouseY > y + 113 && mouseY < y + 133 && mouseX < x + 30)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.props.simple"), mouseX, width, reversed);
                }
            }

            if(mouseX > x + 90 && mouseX < x + 216)
            {
                if(mouseY > y + 24 && mouseY < y + 44)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.props.reverserotate"), mouseX, width, reversed);
                }
                if(mouseY > y + 46 && mouseY < y + 66)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.props.seclaserswitch"), mouseX, width, reversed);
                }
                if(mouseY > y + 90 && mouseY < y + 110)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.props.seclasersize"), mouseX, width, reversed);
                }

                if(mouseY > y + 113 && mouseY < y + 133 && mouseX > x + 146)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.props.axe"), mouseX, width, reversed);
                }
            }

            if(mouseX > x + 38 && mouseX < x + 138 && mouseY > y + 117 && mouseY < y + 137)
            {
                list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.back"), mouseX, width, reversed);
            }

            if(mouseX > x + 180 && mouseX < x + 200 && mouseY > y + 140 && mouseY < y + 160)
            {
                list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.help"), mouseX, width, reversed);
            }

            if(list.size() > 0 && (list.get(list.size() - 1) == " " || list.get(list.size() - 1).isEmpty()))
            {
                list.remove(list.size() - 1);
            }
            GuiHelper.drawHoveringText(list, mouseX, mouseY, this.fontRendererObj, reversed ? 0 : 200000, height, 0x00ff00);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.mc.renderEngine.bindTexture(texture);
        this.drawTexturedModalRect(x, y + 114, 69, 81, xSize, 52);
        this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.beamspecs")), x - 30, y - 35, 0xffffff);
    }
}