package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLightConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMUtils;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;

public class GuiSpotLightConfig extends GuiContainer
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");
    protected static final ResourceLocation icons = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tile;
    public World world;
    private GuiBooleanButton buttonHelp;

    public GuiSpotLightConfig(InventoryPlayer inventory, TileEntitySpotLight tile, World world)
    {
        super(new ContainerSpotLightConfig(tile, inventory));
        this.invPlayer = inventory;
        this.tile = tile;
        this.world = world;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
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
    public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
    {
        super.drawScreen(mouseX, mouseY, partialRenderTick);
        if(this.buttonHelp.isActive())
        {
            TSMUtils.drawTextHelper(this.fontRendererObj, mouseX, mouseY, this.width, this.height, this.buttonList, this);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.mc.renderEngine.bindTexture(texture);
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        this.mc.renderEngine.bindTexture(icons);
        this.drawTexturedModalRect(x + 39, y + 79, 220, 0, 18, 18);
        this.drawTexturedModalRect(x + 79, y + 79, 220, 0, 18, 18);
        this.drawTexturedModalRect(x + 39, y + 29, 238, 0, 18, 18);
        this.drawTexturedModalRect(x + 79, y + 29, 220, 0, 18, 18);
        this.drawTexturedModalRect(x + 119, y + 29, 220, 0, 18, 18);
        this.drawTexturedModalRect(x + 119, y + 79, 238, 0, 18, 18);
        this.drawTexturedModalRect(x + 122, y + 49, 213, 21, 13, 26);
        this.drawTexturedModalRect(x + 82, y + 49, 213, 21, 13, 26);
        this.drawTexturedModalRect(x + 42, y + 49, 213, 21, 13, 26);
        this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.config")), x - 30, y - 35, 0xffffff);
    }
}