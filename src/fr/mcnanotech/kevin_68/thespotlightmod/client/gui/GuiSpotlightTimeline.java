package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketOpenGui;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimeline;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineDeleteKey;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineReset;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineSmooth;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class GuiSpotlightTimeline extends GuiContainer
{
    protected InventoryPlayer invPlayer;
    protected TileEntitySpotLight tile;
    protected World world;

    private short selectedKeyID = -1;
    private GuiBooleanButton buttonHelp, buttonTimelineEnabled, buttonSmooth;
    private GuiButton buttonRemove;
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight1.png");
    protected static final ResourceLocation texture2 = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight2.png");
    protected static final ResourceLocation tsmIcons = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public GuiSpotlightTimeline(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World world)
    {
        super(new ContainerSpotLight(tileEntity, playerInventory, 11, true));
        this.invPlayer = playerInventory;
        this.tile = tileEntity;
        this.world = world;
        this.xSize = 256;
        this.ySize = 256;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButton(2, x - 27, y + 184, 65, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(new GuiButton(3, x - 27, y + 69, 120, 20, I18n.format("container.spotlight.addKey")));
        this.buttonList.add(this.buttonTimelineEnabled = new GuiBooleanButton(4, x - 27, y + 157, 120, 20, "", this.tile.timelineEnabled));
        this.buttonTimelineEnabled.setTexts(I18n.format("container.spotlight.timelineval", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.timelineval", I18n.format("container.spotlight.off")));
        this.buttonList.add(this.buttonRemove = new GuiButton(5, x - 27, y + 91, 120, 20, I18n.format("container.spotlight.deleteKey")));
        this.buttonRemove.enabled = false;
        this.buttonList.add(new GuiButton(6, x - 27, y + 113, 120, 20, I18n.format("container.spotlight.resettime")));
        this.buttonList.add(this.buttonSmooth = new GuiBooleanButton(7, x - 27, y + 135, 120, 20, I18n.format("container.spotlight.smooth"), this.tile.timelineSmooth));
        this.buttonList.add(this.buttonHelp = new GuiBooleanButton(8, x + 220, y + 185, 20, 20, "?", this.tile.helpMode));
        this.buttonTimelineEnabled.enabled = this.tile.hasKey();
        for(short i = 0; i < 120; i++)
        {
            if(this.tile.getKey(i) != null)
            {
                this.buttonList.add(new GuiSpotlightTimelineKeyButton(10 + i, this.width / 2 - 149 + (int)(i * 2.5), y + 50 + i % 2 * 4));
            }
        }
    }

    @Override
    public void onGuiClosed()
    {
        TheSpotLightMod.network.sendToServer(new PacketUpdateTLData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimensionID, TSMJsonManager.getTlDataFromTile(this.tile).toString()));
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 2)
        {
            TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 0));
        }
        else if(guibutton.id == 3)
        {
            TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 5));
        }
        else if(guibutton.id == 4)
        {
            this.buttonTimelineEnabled.toggle();
            TheSpotLightMod.network.sendToServer(new PacketTimeline(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.buttonTimelineEnabled.isActive()));
        }
        else if(guibutton.id == 5)
        {
            this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("container.spotlight.askerasekey"), "", I18n.format("container.spotlight.erase"), I18n.format("container.spotlight.cancel"), 5));
        }
        else if(guibutton.id == 6)
        {
            TheSpotLightMod.network.sendToServer(new PacketTimelineReset(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ()));
        }
        else if(guibutton.id == 7)
        {
            this.buttonSmooth.toggle();
            TheSpotLightMod.network.sendToServer(new PacketTimelineSmooth(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.buttonSmooth.isActive()));
        }
        else if(guibutton.id == 8)
        {
            this.buttonHelp.toggle();
            this.tile.helpMode = this.buttonHelp.isActive();
        }
        else if(guibutton.id >= 10)
        {
            this.selectedKeyID = (short)(guibutton.id - 10);
            this.buttonRemove.enabled = true;
        }
    }

    @Override
    public void confirmClicked(boolean result, int id)
    {
        if(id == 5)
        {
            if(result)
            {
                this.tile.setKey(this.selectedKeyID, null);
                TheSpotLightMod.network.sendToServer(new PacketTimelineDeleteKey(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.selectedKeyID));
                TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 3));
            }
            else
            {
                TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 3));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
    {
        super.drawScreen(mouseX, mouseY, partialRenderTick);

        if(this.buttonHelp.isActive())
        {
            System.out.println(mouseX);
            if(mouseX > 60 && mouseY > 30 && mouseX < 363 && mouseY < 53)
            {
                this.drawHoveringText(this.fontRenderer.listFormattedStringToWidth(TextFormatting.GREEN + I18n.format("tutorial.spotlight.timeline.timeline"), (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(texture);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x - 35, y + 19, 0, 0, this.xSize, this.ySize);
        this.mc.renderEngine.bindTexture(texture2);
        this.drawTexturedModalRect(x + 135, y + 19, 0, 0, this.xSize, this.ySize);
        this.mc.renderEngine.bindTexture(tsmIcons);
        this.drawTexturedModalRect(x - 20, y + 40, 0, 59, 256, 21);
        this.drawTexturedModalRect(x + 225, y + 40, 0, 81, 57, 21);
        this.drawTexturedModalRect(x - 20 + this.tile.time / 4, y + 40, 0, 105, 1, 12);
        if(this.selectedKeyID != -1)
        {
            this.drawTexturedModalRect((int)(x - 22 + this.selectedKeyID * 2.5), y + 62, 0, 115, 5, 6);
        }
        // drawString(this.fontRendererObj, EnumTextFormatting.RED +
        // I18n.format("container.spotlight.red") + " : " +
        // ((Byte)entry.get(EnumLaserInformations.LASERRED) & 0xFF), x + 100, y
        // + 70, 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.GREEN +
        // I18n.format("container.spotlight.green") + " : " +
        // ((Byte)entry.get(EnumLaserInformations.LASERGREEN) & 0xFF), x + 100,
        // y + 80, 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.BLUE +
        // I18n.format("container.spotlight.blue") + " : " +
        // ((Byte)entry.get(EnumLaserInformations.LASERBLUE) & 0xFF), x + 100, y
        // + 90, 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.DARK_RED +
        // I18n.format("container.spotlight.red") + " : " +
        // ((Byte)entry.get(EnumLaserInformations.LASERSECRED) & 0xFF), x + 185,
        // y + 70, 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.DARK_GREEN +
        // I18n.format("container.spotlight.green") + " : " +
        // ((Byte)entry.get(EnumLaserInformations.LASERSECGREEN) & 0xFF), x +
        // 185, y + 80, 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.DARK_BLUE +
        // I18n.format("container.spotlight.blue") + " : " +
        // ((Byte)entry.get(EnumLaserInformations.LASERSECBLUE) & 0xFF), x +
        // 185, y + 90, 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.WHITE +
        // I18n.format("container.spotlight.angle") + " 1 : " +
        // entry.get(EnumLaserInformations.LASERANGLE1), x + 100, y + 100,
        // 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.WHITE +
        // I18n.format("container.spotlight.angle") + " 2 : " +
        // entry.get(EnumLaserInformations.LASERANGLE2), x + 185, y + 100,
        // 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.WHITE +
        // I18n.format("container.spotlight.rotate") + " : " +
        // ((Boolean)entry.get(EnumLaserInformations.LASERAUTOROTATE) ?
        // I18n.format("container.spotlight.true") :
        // I18n.format("container.spotlight.false")), x + 100, y + 110,
        // 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.WHITE +
        // I18n.format("container.spotlight.rotationspeed") + " : " +
        // entry.get(EnumLaserInformations.LASERROTATIONSPEED), x + 100, y +
        // 120, 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.WHITE +
        // I18n.format("container.spotlight.rotationreverse") + " : " +
        // ((Boolean)entry.get(EnumLaserInformations.LASERREVERSEROTATION) ?
        // I18n.format("container.spotlight.true") :
        // I18n.format("container.spotlight.false")), x + 100, y + 130,
        // 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.WHITE +
        // I18n.format("container.spotlight.secondlazer") + " : " +
        // ((Boolean)entry.get(EnumLaserInformations.LASERSECONDARY) ?
        // I18n.format("container.spotlight.true") :
        // I18n.format("container.spotlight.false")), x + 100, y + 140,
        // 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.WHITE +
        // I18n.format("container.spotlight.axis") + " : " +
        // ((Byte)entry.get(EnumLaserInformations.LASERDISPLAYAXE) == 0 ? "y" :
        // (Byte)entry.get(EnumLaserInformations.LASERDISPLAYAXE) == 1 ? "x" :
        // "z"), x + 100, y + 150, 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.WHITE +
        // I18n.format("container.spotlight.laserMode") + " : " +
        // ((Boolean)entry.get(EnumLaserInformations.LASERDOUBLE) ?
        // I18n.format("container.spotlight.double") :
        // I18n.format("container.spotlight.simple")), x + 100, y + 160,
        // 0xffffff);
        // drawString(this.fontRendererObj, EnumTextFormatting.WHITE +
        // I18n.format("container.spotlight.size") + " " +
        // I18n.format("container.spotlight.main") + " : " +
        // entry.get(EnumLaserInformations.LASERMAINSIZE) + " " +
        // I18n.format("container.spotlight.sec") + " : " +
        // entry.get(EnumLaserInformations.LASERSECSIZE), x + 100, y + 170,
        // 0xffffff);
        // }

        this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.timeline")), x - 25, y + 28, 4210752);
    }
}