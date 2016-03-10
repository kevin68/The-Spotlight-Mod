package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketOpenGui;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiSliderButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.ISliderButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class GuiSpotLightBeamColor extends GuiContainer implements ISliderButton
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tile;
    public World world;
    public GuiBooleanButton buttonHelp;

    public GuiSpotLightBeamColor(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
    {
        super(new ContainerSpotLight(tileEntity, playerInventory, wrld, 8, false));
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

        this.buttonList.add(new GuiSliderButton(this, 0, x - 40, y - 20, 256, 20, TextFormatting.RED + I18n.format("container.spotlight.red", this.tile.beamRed), this.tile.beamRed / 255.0F));
        this.buttonList.add(new GuiSliderButton(this, 1, x - 40, y + 2, 256, 20, TextFormatting.GREEN + I18n.format("container.spotlight.green", this.tile.beamGreen), this.tile.beamGreen / 255.0F));
        this.buttonList.add(new GuiSliderButton(this, 2, x - 40, y + 24, 256, 20, TextFormatting.BLUE + I18n.format("container.spotlight.blue", this.tile.beamBlue), this.tile.beamBlue / 255.0F));

        this.buttonList.add(new GuiSliderButton(this, 3, x - 40, y + 68, 256, 20, TextFormatting.DARK_RED + I18n.format("container.spotlight.red", this.tile.secBeamRed), this.tile.secBeamRed / 255.0F));
        this.buttonList.add(new GuiSliderButton(this, 4, x - 40, y + 90, 256, 20, TextFormatting.DARK_GREEN + I18n.format("container.spotlight.green", this.tile.secBeamGreen), this.tile.secBeamGreen / 255.0F));
        this.buttonList.add(new GuiSliderButton(this, 5, x - 40, y + 112, 256, 20, TextFormatting.DARK_BLUE + I18n.format("container.spotlight.blue", this.tile.secBeamBlue), this.tile.secBeamBlue / 255.0F));

        this.buttonList.add(new GuiSliderButton(this, 6, x - 40, y + 46, 256, 20, I18n.format("container.spotlight.alpha", this.tile.beamAlpha), this.tile.beamAlpha));
        this.buttonList.add(new GuiSliderButton(this, 7, x - 40, y + 134, 256, 20, I18n.format("container.spotlight.alpha", this.tile.secBeamAlpha), this.tile.secBeamAlpha));

        this.buttonList.add(new GuiButton(19, x + 38, y + 159, 100, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(this.buttonHelp = new GuiBooleanButton(20, x + 180, y + 159, 20, 20, "?", false));
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
            TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 0));
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
            this.tile.beamRed = (short)(sliderValue * 255.0F);
            break;
        case 1:
            this.tile.beamGreen = (short)(sliderValue * 255.0F);
            break;
        case 2:
            this.tile.beamBlue = (short)(sliderValue * 255.0F);
            break;
        case 3:
            this.tile.secBeamRed = (short)(sliderValue * 255.0F);
            break;
        case 4:
            this.tile.secBeamGreen = (short)(sliderValue * 255.0F);
            break;
        case 5:
            this.tile.secBeamBlue = (short)(sliderValue * 255.0F);
            break;
        case 6:
            this.tile.beamAlpha = ((short)(sliderValue * 1000)) / 1000.0F;
            break;
        case 7:
            this.tile.secBeamAlpha = ((short)(sliderValue * 1000)) / 1000.0F;
            break;

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
            name = TextFormatting.RED + I18n.format("container.spotlight.red", ((short)(sliderValue * 255)));
            break;
        }
        case 1:
        {
            name = TextFormatting.GREEN + I18n.format("container.spotlight.green", ((short)(sliderValue * 255)));
            break;
        }
        case 2:
        {
            name = TextFormatting.BLUE + I18n.format("container.spotlight.blue", ((short)(sliderValue * 255)));
            break;
        }
        case 3:
        {
            name = TextFormatting.DARK_RED + I18n.format("container.spotlight.red", ((short)(sliderValue * 255)));
            break;
        }
        case 4:
        {
            name = TextFormatting.DARK_GREEN + I18n.format("container.spotlight.green", ((short)(sliderValue * 255)));
            break;
        }
        case 5:
        {
            name = TextFormatting.BLUE + I18n.format("container.spotlight.blue", ((short)(sliderValue * 255)));
            break;
        }
        case 6:
        {
            name = I18n.format("container.spotlight.alpha", ((short)(sliderValue * 1000)) / 1000.0F);
            break;
        }
        case 7:
        {
            name = I18n.format("container.spotlight.alpha", ((short)(sliderValue * 1000)) / 1000.0F);
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
        // this.mc.renderEngine.bindTexture(texture);
        // this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
        this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.color")), x - 30, y - 35, 0xffffff);
    }
}