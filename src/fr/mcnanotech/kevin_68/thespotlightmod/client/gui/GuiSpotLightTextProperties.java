package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiSliderButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.ISliderButton;

public class GuiSpotLightTextProperties extends GuiContainer implements ISliderButton
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    private InventoryPlayer invPlayer;
    private TileEntitySpotLight tile;
    private World world;
    private GuiBooleanButton buttonHelp;
    
    public GuiSpotLightTextProperties(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
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
        this.buttonList.add(new GuiSliderButton(this, 0, x - 50, y - 20, 130, 20, I18n.format("container.spotlight.textheight") + " : " + this.tile.textHeight, this.tile.textHeight / 100.0F));
        this.buttonList.add(new GuiSliderButton(this, 1, x + 90, y - 20, 130, 20, I18n.format("container.spotlight.textscale") + " : " + this.tile.textScale, this.tile.textScale / 100.0F));

        this.buttonList.add(new GuiButton(19, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(this.buttonHelp = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
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
        case 0:
        {
            this.tile.textHeight = (short)(sliderValue * 100);
            break;
        }
        case 1:
        {
            this.tile.textScale = (short)(sliderValue * 100);
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
            name = I18n.format("container.spotlight.textheight") + " : " + (short)(sliderValue * 100);
            break;
        }
        case 1:
        {
            name = I18n.format("container.spotlight.textscale") + " : " + (short)(sliderValue * 100);
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
        this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.beamspecs")), x - 30, y - 35, 0xffffff);
    }
}