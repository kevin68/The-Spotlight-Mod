package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;

public class GuiSpotLightSaveConfig extends GuiContainer
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tileSpotLight;
    public World world;
    public GuiTextField txtField;

    public GuiSpotLightSaveConfig(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
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

        Keyboard.enableRepeatEvents(true);
        this.txtField = new GuiTextField(this.fontRendererObj, x + 5, y + 20, 166, 12);
        this.txtField.setEnableBackgroundDrawing(true);
        this.txtField.setMaxStringLength(40);
        this.txtField.setEnabled(true);

        this.buttonList.add(new GuiButton(0, x + 5, y + 112, 80, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(new GuiButton(1, x + 90, y + 112, 80, 20, I18n.format("container.spotlight.create")));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch(guibutton.id)
        {
            case 0:
            {
                this.mc.displayGuiScreen(new GuiSpotLightConfigs(invPlayer, tileSpotLight, world));
                break;
            }
            case 1:
            {
                PacketSender.sendSpotLightPacket(tileSpotLight, 50, txtField.getText());
                this.mc.displayGuiScreen(new GuiSpotLightConfigs(invPlayer, tileSpotLight, world));
                break;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.mc.renderEngine.bindTexture(texture);
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.txtField.drawTextBox();
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        if(this.txtField.textboxKeyTyped(par1, par2))
        {}
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.txtField.mouseClicked(par1, par2, par3);
    }
}