package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class GuiSpotLightTextColor extends GuiContainer implements ISliderButton
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tile;
    public World world;
    public GuiBooleanButton buttonHelp;
    public GuiTextField textField;

    public GuiSpotLightTextColor(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
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
        this.buttonList.add(new GuiSliderButton(this, 0, x - 40, y + 24, 256, 20, TextFormatting.RED + I18n.format("container.spotlight.red", this.tile.textRed), this.tile.textRed / 255.0F));
        this.buttonList.add(new GuiSliderButton(this, 1, x - 40, y + 46, 256, 20, TextFormatting.GREEN + I18n.format("container.spotlight.green", this.tile.textGreen), this.tile.textGreen / 255.0F));
        this.buttonList.add(new GuiSliderButton(this, 2, x - 40, y + 68, 256, 20, TextFormatting.BLUE + I18n.format("container.spotlight.blue", this.tile.textBlue), this.tile.textBlue / 255.0F));

        Keyboard.enableRepeatEvents(true);
        this.textField = new GuiTextField(3, this.fontRenderer, x - 40, y, 256, 12);
        this.textField.setTextColor((this.tile.textRed * 65536) + (this.tile.textGreen * 256) + (this.tile.textBlue & 0xFF));
        this.textField.setEnableBackgroundDrawing(true);
        this.textField.setMaxStringLength(40);
        this.textField.setEnabled(true);
        this.textField.setText(this.tile.text);

        this.buttonList.add(new GuiButton(19, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(this.buttonHelp = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", this.tile.helpMode));
    }

    @Override
    public void onGuiClosed()
    {
        TheSpotLightMod.network.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimensionID, TSMJsonManager.getDataFromTile(this.tile).toString()));
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch(guibutton.id)
        {
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
    public void handlerSliderAction(int sliderId, float sliderValue)
    {
        switch(sliderId)
        {
            case 0:
                this.tile.textRed = (short)(sliderValue * 255.0F);
                break;
            case 1:
                this.tile.textGreen = (short)(sliderValue * 255.0F);
                break;
            case 2:
                this.tile.textBlue = (short)(sliderValue * 255.0F);
                break;
        }
    }

    @Override
    public String getSliderName(int sliderId, float sliderValue)
    {
        switch(sliderId)
        {
            case 0:
                return TextFormatting.RED + I18n.format("container.spotlight.red", (short)(sliderValue * 255));
            case 1:
                return TextFormatting.GREEN + I18n.format("container.spotlight.green", (short)(sliderValue * 255));
            case 2:
                return TextFormatting.BLUE + I18n.format("container.spotlight.blue", (short)(sliderValue * 255));
            default:
                return "";
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
    {
        super.drawScreen(mouseX, mouseY, partialRenderTick);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.textField.drawTextBox();
        if(this.buttonHelp.isActive())
        {
            if(mouseX >= this.textField.xPosition && mouseX < this.textField.xPosition + this.textField.width && mouseY >= this.textField.yPosition && mouseY < this.textField.yPosition + this.textField.height)
            {
                this.drawHoveringText(this.fontRenderer.listFormattedStringToWidth(TextFormatting.GREEN + I18n.format("tutorial.spotlight.textcolors.text"), (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
            }
            for(GuiButton button : this.buttonList)
            {
                if(button.isMouseOver())
                {
                    String text = "";
                    switch(button.id)
                    {
                    case 0:
                        text = I18n.format("tutorial.spotlight.textcolors.red");
                        break;
                    case 1:
                        text = I18n.format("tutorial.spotlight.textcolors.green");
                        break;
                    case 2:
                        text = I18n.format("tutorial.spotlight.textcolors.blue");
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
        this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.color")), x - 30, y - 35, 0xffffff);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if(this.textField.textboxKeyTyped(typedChar, keyCode))
        {
            this.tile.text = this.textField.getText();
        }
        else if(this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
        {
            this.mc.displayGuiScreen(new GuiSpotLight(this.invPlayer, this.tile, this.world));
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        try
        {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            this.textField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}