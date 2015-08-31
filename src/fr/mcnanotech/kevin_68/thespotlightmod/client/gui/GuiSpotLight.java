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
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketOpenGui;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;

public class GuiSpotLight extends GuiContainer
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tile;
    public World world;
    public GuiBooleanButton timeButton, textButton, helpButton;

    public GuiSpotLight(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
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

        this.buttonList.add(new GuiButton(0, x + 5, y + 20, 80, 20, I18n.format("container.spotlight.color")));
        this.buttonList.add(new GuiButton(1, x + 5, y + 43, 80, 20, I18n.format("container.spotlight.angle")));
        this.buttonList.add(new GuiButton(2, x + 5, y + 66, 80, 20, I18n.format("container.spotlight.beamspecs")));
        this.buttonList.add(new GuiButton(3, x + 5, y + 89, 80, 20, I18n.format("container.spotlight.textures")));
        this.buttonList.add(this.helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch(guibutton.id)
        {
        case 0:
        {
            this.mc.displayGuiScreen(new GuiSpotLightColor(this.invPlayer, this.tile, this.world));
            break;
        }
        case 1:
        {
            this.mc.displayGuiScreen(new GuiSpotLightBeamAngles(this.invPlayer, this.tile, this.world));
            break;
        }
        case 2:
        {
            this.mc.displayGuiScreen(new GuiSpotLightBeamProperties(this.invPlayer, this.tile, this.world));
            break;
        }
        case 3:
        {
            TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 1));
//            this.mc.displayGuiScreen(new GuiSpotLightTextures(this.invPlayer, this.tile, this.world));
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
    public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
    {
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        super.drawScreen(mouseX, mouseY, partialRenderTick);

        if(this.helpButton.isActive())
        {
            TSMUtils.drawTextHelper(this.fontRendererObj, mouseX, mouseY, x, y, this.buttonList, this);
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
        this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", "").replace("-", ""), x + 6, y + 7, 4210752);
    }
}
