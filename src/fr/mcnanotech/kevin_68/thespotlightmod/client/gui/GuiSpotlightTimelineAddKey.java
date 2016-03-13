package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketOpenGui;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiSliderButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.ISliderButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiSpotlightTimelineAddKey extends GuiContainer implements ISliderButton
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");

    protected InventoryPlayer invPlayer;
    protected TileEntitySpotLight tile;
    protected World world;
    private GuiBooleanButton buttonHelp;
    private short time;

    public GuiSpotlightTimelineAddKey(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World world)
    {
        super(new ContainerSpotLight(tileEntity, playerInventory, world, 8, true));
        this.invPlayer = playerInventory;
        this.tile = tileEntity;
        this.world = world;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiSliderButton(this, 0, x + 3, y + 20, 170, 20, I18n.format("container.spotlight.time", "0.0"), 0));
        this.buttonList.add(new GuiButton(1, x + 13, y + 115, 150, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(new GuiButton(2, x + 13, y + 90, 150, 20, I18n.format("container.spotlight.createkey")));
        this.buttonList.add(this.buttonHelp = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
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
        if(guibutton.id == 1)
        {
            TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 3));
        }
        else if(guibutton.id == 2)
        {
            if(this.tile.getKey(this.time) != null)
            {
                this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("container.spotlight.askoverwrite"), "", I18n.format("container.spotlight.overwrite"), I18n.format("container.spotlight.cancel"), 2));
            }
            else
            {
                this.tile.setKey(this.time, TSMUtils.createKey(this.time, this.tile));
                TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 3));
            }
        }
        else if(guibutton.id == 20)
        {
            this.buttonHelp.toggle();
        }
    }

    @Override
    public void confirmClicked(boolean result, int id)
    {
        if(id == 2)
        {
            if(result)
            {
                this.tile.setKey(this.time, TSMUtils.createKey(this.time, this.tile));
                TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 3));
            }
            else
            {
                this.mc.displayGuiScreen(this);
            }
        }
    }

    @Override
    public void handlerSliderAction(int sliderId, float sliderValue)
    {
        if(sliderId == 0)
        {
            this.time = (short)(sliderValue * 119);
        }
    }

    @Override
    public String getSliderName(int sliderId, float sliderValue)
    {
        String name = "";
        if(sliderId == 0)
        {
            name = I18n.format("container.spotlight.time", (((int)((sliderValue * 119) / 0.2F))) / 10.0F);
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
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.mc.renderEngine.bindTexture(texture);
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.addKey")), x + 5, y + 8, 4210752);
    }
}