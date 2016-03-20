package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

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
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiSpotLightTextProperties extends GuiContainer implements ISliderButton
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    private InventoryPlayer invPlayer;
    private TileEntitySpotLight tile;
    private World world;
    private GuiSliderButton sliderTranslateSpeed;
    private GuiBooleanButton buttonBold, buttonStrike, buttonUnderline, buttonItalic, buttonObfuscated, buttonShadow, buttonTranslating, buttonReverseTranslating, buttonHelp, button3D;

    public GuiSpotLightTextProperties(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
    {
        super(new ContainerSpotLight(tileEntity, playerInventory, 8, true));
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
        this.buttonList.add(new GuiSliderButton(this, 0, x - 50, y - 20, 130, 20, I18n.format("container.spotlight.textheight", this.tile.textHeight-100), this.tile.textHeight / 200.0F));
        this.buttonList.add(new GuiSliderButton(this, 1, x + 90, y - 20, 130, 20, I18n.format("container.spotlight.textscale", this.tile.textScale), this.tile.textScale / 100.0F));
        this.buttonList.add(this.buttonBold = new GuiBooleanButton(2, x - 50, y + 3, 130, 20, I18n.format("container.spotlight.bold"), this.tile.textBold));
        this.buttonList.add(this.buttonStrike = new GuiBooleanButton(3, x + 90, y + 3, 130, 20, I18n.format("container.spotlight.strike"), this.tile.textStrike));
        this.buttonList.add(this.buttonUnderline = new GuiBooleanButton(4, x - 50, y + 26, 130, 20, I18n.format("container.spotlight.underline"), this.tile.textUnderline));
        this.buttonList.add(this.buttonItalic = new GuiBooleanButton(5, x + 90, y + 26, 130, 20, I18n.format("container.spotlight.italic"), this.tile.textItalic));
        this.buttonList.add(this.buttonObfuscated = new GuiBooleanButton(6, x - 50, y + 49, 130, 20, I18n.format("container.spotlight.obfuscated"), this.tile.textObfuscated));
        this.buttonList.add(this.buttonShadow = new GuiBooleanButton(7, x + 90, y + 49, 130, 20, I18n.format("container.spotlight.shadow"), this.tile.textShadow));
        this.buttonList.add(this.buttonTranslating = new GuiBooleanButton(8, x - 50, y + 72, 130, 20, I18n.format("container.spotlight.translate"), this.tile.textTranslating));
        this.buttonList.add(this.sliderTranslateSpeed = new GuiSliderButton(this, 9, x + 90, y + 72, 130, 20, I18n.format("container.spotlight.translatespeed", this.tile.textTranslateSpeed), this.tile.textTranslateSpeed / 100.0F));
        this.buttonList.add(this.buttonReverseTranslating = new GuiBooleanButton(10, x - 50, y + 95, 130, 20, I18n.format("container.spotlight.reversetranslate"), this.tile.textReverseTranslating));
        this.buttonList.add(this.button3D = new GuiBooleanButton(11, x + 90, y + 95, 130, 20, I18n.format("container.spotlight.text3d"), this.tile.text3D));

        this.sliderTranslateSpeed.enabled = this.buttonTranslating.isActive();
        this.buttonReverseTranslating.enabled = this.buttonTranslating.isActive();

        this.buttonBold.enabled = !this.button3D.isActive();
        this.buttonStrike.enabled = !this.button3D.isActive();
        this.buttonUnderline.enabled = !this.button3D.isActive();
        this.buttonItalic.enabled = !this.button3D.isActive();
        this.buttonObfuscated.enabled = !this.button3D.isActive();
        this.buttonShadow.enabled = !this.button3D.isActive();

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
        case 2:
        {
            this.buttonBold.toggle();
            this.tile.textBold = this.buttonBold.isActive();
            break;
        }
        case 3:
        {
            this.buttonStrike.toggle();
            this.tile.textStrike = this.buttonStrike.isActive();
            break;
        }
        case 4:
        {
            this.buttonUnderline.toggle();
            this.tile.textUnderline = this.buttonUnderline.isActive();
            break;
        }
        case 5:
        {
            this.buttonItalic.toggle();
            this.tile.textItalic = this.buttonItalic.isActive();
            break;
        }
        case 6:
        {
            this.buttonObfuscated.toggle();
            this.tile.textObfuscated = this.buttonObfuscated.isActive();
            break;
        }
        case 7:
        {
            this.buttonShadow.toggle();
            this.tile.textShadow = this.buttonShadow.isActive();
            break;
        }
        case 8:
        {
            this.buttonTranslating.toggle();
            this.tile.textTranslating = this.buttonTranslating.isActive();
            this.sliderTranslateSpeed.enabled = this.buttonTranslating.isActive();
            this.buttonReverseTranslating.enabled = this.buttonTranslating.isActive();
            break;
        }
        case 10:
        {
            this.buttonReverseTranslating.toggle();
            this.tile.textReverseTranslating = this.buttonReverseTranslating.isActive();
            break;
        }
        case 11:
        {
            this.button3D.toggle();
            this.tile.text3D = this.button3D.isActive();
            this.buttonBold.enabled = !this.button3D.isActive();
            this.buttonStrike.enabled = !this.button3D.isActive();
            this.buttonUnderline.enabled = !this.button3D.isActive();
            this.buttonItalic.enabled = !this.button3D.isActive();
            this.buttonObfuscated.enabled = !this.button3D.isActive();
            this.buttonShadow.enabled = !this.button3D.isActive();
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
        case 0:
        {
            this.tile.textHeight = (short)(sliderValue * 200);
            break;
        }
        case 1:
        {
            this.tile.textScale = (short)(sliderValue * 100);
            break;
        }
        case 9:
        {
            this.tile.textTranslateSpeed = (short)(sliderValue * 100);
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
            name = I18n.format("container.spotlight.textheight", (short)(sliderValue * 200)-100);
            break;
        }
        case 1:
        {
            name = I18n.format("container.spotlight.textscale", (short)(sliderValue * 100));
            break;
        }
        case 9:
        {
            name = I18n.format("container.spotlight.translatespeed", (short)(sliderValue * 100));
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
            TSMUtils.drawTextHelper(this.fontRendererObj, mouseX, mouseY, this.width, this.buttonList, this);
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