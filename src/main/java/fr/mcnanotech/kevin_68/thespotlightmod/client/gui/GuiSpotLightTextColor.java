package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;

public class GuiSpotLightTextColor extends GuiContainer implements ISlider
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
        this.buttonList.add(new GuiSlider(0, x - 40, y + 24, 256, 20, TextFormatting.RED + I18n.format("container.spotlight.red"), "", 0, 255, this.tile.getShort(EnumTSMProperty.TEXT_RED), false, true, this));
        this.buttonList.add(new GuiSlider(1, x - 40, y + 46, 256, 20, TextFormatting.GREEN + I18n.format("container.spotlight.green"), "", 0, 255, this.tile.getShort(EnumTSMProperty.TEXT_GREEN), false, true, this));
        this.buttonList.add(new GuiSlider(2, x - 40, y + 68, 256, 20, TextFormatting.BLUE + I18n.format("container.spotlight.blue"), "", 0, 255, this.tile.getShort(EnumTSMProperty.TEXT_BLUE), false, true, this));

        Keyboard.enableRepeatEvents(true);
        this.textField = new GuiTextField(3, this.fontRenderer, x - 40, y, 256, 12);
        this.textField.setTextColor((this.tile.getShort(EnumTSMProperty.TEXT_RED) * 65536) + (this.tile.getShort(EnumTSMProperty.TEXT_GREEN) * 256) + this.tile.getShort(EnumTSMProperty.TEXT_BLUE));
        this.textField.setEnableBackgroundDrawing(true);
        this.textField.setMaxStringLength(40);
        this.textField.setEnabled(true);
        this.textField.setText(this.tile.getString(EnumTSMProperty.TEXT));

        this.buttonList.add(new GuiButton(19, x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(this.buttonHelp = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", this.tile.helpMode));
    }

    @Override
    public void onGuiClosed()
    {
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.tile.dimension, TSMJsonManager.getDataFromTile(this.tile).toString()));
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
    public void onChangeSliderValue(GuiSlider slider)
    {
        switch(slider.id)
        {
            case 0:
                this.tile.setProperty(EnumTSMProperty.TEXT_RED, (short)(slider.getValueInt()));
                break;
            case 1:
                this.tile.setProperty(EnumTSMProperty.TEXT_GREEN, (short)(slider.getValueInt()));
                break;
            case 2:
                this.tile.setProperty(EnumTSMProperty.TEXT_BLUE, (short)(slider.getValueInt()));
                break;
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialRenderTick)
    {
        super.render(mouseX, mouseY, partialRenderTick);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.textField.drawTextBox();
        if(this.buttonHelp.isActive())
        {
            if(mouseX >= this.textField.x && mouseX < this.textField.x + this.textField.width && mouseY >= this.textField.y && mouseY < this.textField.y + this.textField.height)
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
        this.mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
        this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.color")), x - 30, y - 35, 0xffffff);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if(this.textField.textboxKeyTyped(typedChar, keyCode))
        {
            this.tile.setProperty(EnumTSMProperty.TEXT, this.textField.getText());
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