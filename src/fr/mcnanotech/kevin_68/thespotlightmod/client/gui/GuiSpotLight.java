package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketLock;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketOpenGui;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;

public class GuiSpotLight extends GuiContainer
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");
    protected static final ResourceLocation icons = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tile;
    public World world;
    public GuiButton buttonTextures, buttonColors, buttonAngle, buttonBeamSpecs;
    public GuiBooleanButton buttonMode, buttonHelp, buttonRedstone, buttonLock;

    public GuiSpotLight(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
    {
        super(new ContainerSpotLight(tileEntity, playerInventory, wrld, 8, true));
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

        this.buttonList.add(this.buttonColors = new GuiButton(0, x + 5, y + 20, 80, 20, this.tile.isBeam ? I18n.format("container.spotlight.color") : I18n.format("container.spotlight.textcolor")));
        this.buttonColors.enabled = !this.tile.timelineEnabled;
        this.buttonList.add(this.buttonAngle = new GuiButton(1, x + 5, y + 43, 80, 20, I18n.format("container.spotlight.angle")));
        this.buttonAngle.enabled = !this.tile.timelineEnabled;
        this.buttonList.add(this.buttonBeamSpecs = new GuiButton(2, x + 5, y + 66, 80, 20, I18n.format("container.spotlight.beamspecs")));
        this.buttonBeamSpecs.enabled = !this.tile.timelineEnabled;
        this.buttonList.add(this.buttonTextures = new GuiButton(3, x + 5, y + 89, 80, 20, I18n.format("container.spotlight.textures")));
        this.buttonList.add(this.buttonMode = new GuiBooleanButton(4, x + 5, y + 112, 80, 20, "", this.tile.isBeam));
        this.buttonMode.enabled = !this.tile.timelineEnabled;
        this.buttonMode.setTexts(I18n.format("container.spotlight.modebeam"), I18n.format("container.spotlight.modetext"));
        this.buttonMode.shouldNotChangeTextColor(true);
        this.buttonMode.shouldChangeTextureOnToggle(false);
        this.buttonList.add(new GuiButton(5, x + 90, y + 20, 80, 20, I18n.format("container.spotlight.timeline")));
        this.buttonList.add(this.buttonLock = new GuiBooleanButton(17, x + 180, y + 65, 20, 20, "", this.tile.locked));
        this.buttonLock.setTexts("CADENA FERM�", "CADENA OUVERT");//TODO remove, change with texture
        this.buttonList.add(this.buttonRedstone = new GuiBooleanButton(18, x + 180, y + 90, 20, 20, "", this.tile.redstone));
        this.buttonList.add(new GuiButton(19, x + 180, y + 115, 20, 20, ""));
        this.buttonList.add(this.buttonHelp = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
        this.buttonTextures.enabled = !this.tile.timelineEnabled && this.buttonMode.isActive();
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
        case 0:
        {
            if(this.buttonMode.isActive())
            {
                TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 4));
            }
            else
            {
                this.mc.displayGuiScreen(new GuiSpotLightTextColor(this.invPlayer, this.tile, this.world));
            }
            break;
        }
        case 1:
        {
            if(this.buttonMode.isActive())
            {
                this.mc.displayGuiScreen(new GuiSpotLightBeamAngles(this.invPlayer, this.tile, this.world));
            }
            else
            {
                this.mc.displayGuiScreen(new GuiSpotLightTextAngles(this.invPlayer, this.tile, this.world));
            }
            break;
        }
        case 2:
        {
            if(this.buttonMode.isActive())
            {
                this.mc.displayGuiScreen(new GuiSpotLightBeamProperties(this.invPlayer, this.tile, this.world));
            }
            else
            {
                this.mc.displayGuiScreen(new GuiSpotLightTextProperties(this.invPlayer, this.tile, this.world));
            }
            break;
        }
        case 3:
        {
            TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 1));
            break;
        }
        case 4:
        {
            this.buttonMode.toggle();
            this.tile.isBeam = this.buttonMode.isActive();
            this.buttonTextures.enabled = this.buttonMode.isActive();
            this.buttonColors.displayString = this.tile.isBeam ? I18n.format("container.spotlight.color") : I18n.format("container.spotlight.textcolor");
            break;
        }
        case 5:
        {
            TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 3));
            break;
        }
        case 17:
        {
            this.buttonLock.toggle();
            TheSpotLightMod.network.sendToServer(new PacketLock(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), this.buttonLock.isActive(), this.mc.thePlayer.getGameProfile().getId().toString()));
            break;
        }
        case 18:
        {
            this.buttonRedstone.toggle();
            this.tile.redstone = this.buttonRedstone.isActive();
            break;
        }
        case 19:
        {
            TheSpotLightMod.network.sendToServer(new PacketOpenGui(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ(), 2));
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
    public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
    {
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        super.drawScreen(mouseX, mouseY, partialRenderTick);
        this.mc.renderEngine.bindTexture(icons);
        if(!this.buttonRedstone.isActive())
        {
            GlStateManager.color(0.3F, 0.2F, 0.2F);
        }
        this.drawTexturedModalRect(x + 184, y + 94, 6, 104, 12, 11);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(x + 183, y + 118, 20, 104, 13, 13);
        if(this.buttonHelp.isActive())
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
