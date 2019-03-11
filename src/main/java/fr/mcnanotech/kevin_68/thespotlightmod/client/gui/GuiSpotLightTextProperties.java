package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;

public class GuiSpotLightTextProperties extends GuiContainer implements ISlider
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    private InventoryPlayer invPlayer;
    private TileEntitySpotLight tile;
    private World world;
    private GuiSlider sliderTranslateSpeed;
    private GuiBooleanButton buttonBold, buttonStrike, buttonUnderline, buttonItalic, buttonObfuscated, buttonShadow, buttonTranslating, buttonReverseTranslating, buttonHelp, button3D;

    public GuiSpotLightTextProperties(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
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
        this.buttonList.add(new GuiSlider(0, x - 50, y - 20, 130, 20, I18n.format("container.spotlight.textheight"), "", -100, 100, this.tile.getShort(EnumTSMProperty.TEXT_HEIGHT), false, true, this));
        this.buttonList.add(new GuiSlider(1, x + 90, y - 20, 130, 20, I18n.format("container.spotlight.textscale"), "", 0, 100, this.tile.getShort(EnumTSMProperty.TEXT_SCALE), false, true, this));
        this.buttonList.add(this.buttonBold = new GuiBooleanButton(2, x - 50, y + 3, 130, 20, I18n.format("container.spotlight.bold"), this.tile.getBoolean(EnumTSMProperty.TEXT_BOLD)));
        this.buttonList.add(this.buttonStrike = new GuiBooleanButton(3, x + 90, y + 3, 130, 20, I18n.format("container.spotlight.strike"), this.tile.getBoolean(EnumTSMProperty.TEXT_STRIKE)));
        this.buttonList.add(this.buttonUnderline = new GuiBooleanButton(4, x - 50, y + 26, 130, 20, I18n.format("container.spotlight.underline"), this.tile.getBoolean(EnumTSMProperty.TEXT_UNDERLINE)));
        this.buttonList.add(this.buttonItalic = new GuiBooleanButton(5, x + 90, y + 26, 130, 20, I18n.format("container.spotlight.italic"), this.tile.getBoolean(EnumTSMProperty.TEXT_ITALIC)));
        this.buttonList.add(this.buttonObfuscated = new GuiBooleanButton(6, x - 50, y + 49, 130, 20, I18n.format("container.spotlight.obfuscated"), this.tile.getBoolean(EnumTSMProperty.TEXT_OBFUSCATED)));
        this.buttonList.add(this.buttonShadow = new GuiBooleanButton(7, x + 90, y + 49, 130, 20, I18n.format("container.spotlight.shadow"), this.tile.getBoolean(EnumTSMProperty.TEXT_SHADOW)));
        this.buttonList.add(this.buttonTranslating = new GuiBooleanButton(8, x - 50, y + 72, 130, 20, I18n.format("container.spotlight.translate"), this.tile.getBoolean(EnumTSMProperty.TEXT_TRANSLATING)));
        this.buttonList.add(this.sliderTranslateSpeed = new GuiSlider(9, x + 90, y + 72, 130, 20, I18n.format("container.spotlight.translatespeed"), "", 0, 100, this.tile.getShort(EnumTSMProperty.TEXT_TRANSLATE_SPEED), false, true, this));
        this.buttonList.add(this.buttonReverseTranslating = new GuiBooleanButton(10, x - 50, y + 95, 130, 20, I18n.format("container.spotlight.reversetranslate"), this.tile.getBoolean(EnumTSMProperty.TEXT_T_REVERSE)));
        this.buttonList.add(this.button3D = new GuiBooleanButton(11, x + 90, y + 95, 130, 20, I18n.format("container.spotlight.text3d"), this.tile.getBoolean(EnumTSMProperty.TEXT_3D)));

        this.sliderTranslateSpeed.enabled = this.buttonTranslating.isActive();
        this.buttonReverseTranslating.enabled = this.buttonTranslating.isActive();

        this.buttonBold.enabled = !this.button3D.isActive();
        this.buttonStrike.enabled = !this.button3D.isActive();
        this.buttonUnderline.enabled = !this.button3D.isActive();
        this.buttonItalic.enabled = !this.button3D.isActive();
        this.buttonObfuscated.enabled = !this.button3D.isActive();
        this.buttonShadow.enabled = !this.button3D.isActive();

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
            case 2:
                this.buttonBold.toggle();
                this.tile.setProperty(EnumTSMProperty.TEXT_BOLD, this.buttonBold.isActive());
                break;
            case 3:
                this.buttonStrike.toggle();
                this.tile.setProperty(EnumTSMProperty.TEXT_STRIKE, this.buttonStrike.isActive());
                break;
            case 4:
                this.buttonUnderline.toggle();
                this.tile.setProperty(EnumTSMProperty.TEXT_UNDERLINE, this.buttonUnderline.isActive());
                break;
            case 5:
                this.buttonItalic.toggle();
                this.tile.setProperty(EnumTSMProperty.TEXT_ITALIC, this.buttonItalic.isActive());
                break;
            case 6:
                this.buttonObfuscated.toggle();
                this.tile.setProperty(EnumTSMProperty.TEXT_OBFUSCATED, this.buttonObfuscated.isActive());
                break;
            case 7:
                this.buttonShadow.toggle();
                this.tile.setProperty(EnumTSMProperty.TEXT_SHADOW, this.buttonShadow.isActive());
                break;
            case 8:
                this.buttonTranslating.toggle();
                this.tile.setProperty(EnumTSMProperty.TEXT_TRANSLATING, this.buttonTranslating.isActive());
                this.sliderTranslateSpeed.enabled = this.buttonTranslating.isActive();
                this.buttonReverseTranslating.enabled = this.buttonTranslating.isActive();
                break;
            case 10:
                this.buttonReverseTranslating.toggle();
                this.tile.setProperty(EnumTSMProperty.TEXT_T_REVERSE, this.buttonReverseTranslating.isActive());
                break;
            case 11:
                this.button3D.toggle();
                this.tile.setProperty(EnumTSMProperty.TEXT_3D, this.button3D.isActive());
                this.buttonBold.enabled = !this.button3D.isActive();
                this.buttonStrike.enabled = !this.button3D.isActive();
                this.buttonUnderline.enabled = !this.button3D.isActive();
                this.buttonItalic.enabled = !this.button3D.isActive();
                this.buttonObfuscated.enabled = !this.button3D.isActive();
                this.buttonShadow.enabled = !this.button3D.isActive();
                break;
            case 19:
                this.mc.displayGuiScreen(new GuiSpotLight(this.invPlayer, this.tile, this.world));
                break;
            case 20:
                this.buttonHelp.toggle();
                this.tile.helpMode = this.buttonHelp.isActive();
                break;
        }
    }
    
    @Override
    public void onChangeSliderValue(GuiSlider slider)
    {
        switch(slider.id)
        {
            case 0:
                this.tile.setProperty(EnumTSMProperty.TEXT_HEIGHT, (short)(slider.getValueInt()));
                break;
            case 1:
                this.tile.setProperty(EnumTSMProperty.TEXT_SCALE, (short)(slider.getValueInt()));
                break;
            case 9:
                this.tile.setProperty(EnumTSMProperty.TEXT_TRANSLATE_SPEED, (short)(slider.getValueInt()));
                break;
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
                    switch(button.id)
                    {
                        case 0:
                            text = I18n.format("tutorial.spotlight.textprops.height");
                            break;
                        case 1:
                            text = I18n.format("tutorial.spotlight.textprops.scale");
                            break;
                        case 2:
                            text = I18n.format("tutorial.spotlight.textprops.bold");
                            break;
                        case 3:
                            text = I18n.format("tutorial.spotlight.textprops.strike");
                            break;
                        case 4:
                            text = I18n.format("tutorial.spotlight.textprops.underline");
                            break;
                        case 5:
                            text = I18n.format("tutorial.spotlight.textprops.italic");
                            break;
                        case 6:
                            text = I18n.format("tutorial.spotlight.textprops.obfuscated");
                            break;
                        case 7:
                            text = I18n.format("tutorial.spotlight.textprops.shadow");
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