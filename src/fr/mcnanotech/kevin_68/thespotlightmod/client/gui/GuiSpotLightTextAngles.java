package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiSpotLightTextAngles extends GuiContainer implements ISliderButton
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tile;
    public World world;

    private GuiBooleanButton buttonAR, buttonRR, buttonHelp;
    private GuiSliderButton sliderAngle, sliderSpeed;

    public GuiSpotLightTextAngles(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
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

        this.buttonList.add(this.sliderAngle = new GuiSliderButton(this, 3, x - 50, y - 20, 270, 20, I18n.format("container.spotlight.angleval", "Y", this.tile.textAngleY), this.tile.textAngleY / 360.0F));
        this.buttonList.add(this.buttonAR = new GuiBooleanButton(4, x - 50, y + 5, 130, 20, "", this.tile.textAutoRotateY));
        this.buttonAR.setTexts(I18n.format("container.spotlight.rotate", "Y", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotate", "Y", I18n.format("container.spotlight.off")));
        this.sliderAngle.enabled = !this.buttonAR.isActive();
        this.buttonList.add(this.buttonRR = new GuiBooleanButton(5, x + 90, y + 5, 130, 20, "", this.tile.textReverseRotateY));
        this.buttonRR.setTexts(I18n.format("container.spotlight.rotationreverse", "Y", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.rotationreverse", "Y", I18n.format("container.spotlight.off")));
        this.buttonRR.enabled = this.buttonAR.enabled;
        this.buttonList.add(this.sliderSpeed = new GuiSliderButton(this, 6, x - 50, y + 30, 270, 20, I18n.format("container.spotlight.rotationspeed", "Y", this.tile.textRotationSpeedY), this.tile.textRotationSpeedY / 200.0F));
        this.sliderSpeed.enabled = this.buttonAR.isActive();

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
        case 4:
        {
            this.buttonAR.toggle();
            this.tile.textAutoRotateY = this.buttonAR.isActive();
            this.sliderAngle.enabled = !this.buttonAR.isActive();
            this.sliderSpeed.enabled = this.buttonAR.isActive();
            this.buttonRR.enabled = this.buttonAR.isActive();
            break;
        }
        case 5:
        {
            this.buttonRR.toggle();
            this.tile.textReverseRotateY = this.buttonRR.isActive();
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
        case 3:
        {
            this.tile.textAngleY = (short)(sliderValue * 360.0F);
            break;
        }
        case 6:
        {
            this.tile.textRotationSpeedY = (short)(sliderValue * 200);
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
        case 3:
        {
            name = I18n.format("container.spotlight.angleval", "Y", (short)(sliderValue * 360));
            break;
        }
        case 6:
        {
            name = I18n.format("container.spotlight.rotationspeed", "Y", (short)(sliderValue * 200));
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
            TSMUtils.drawTextHelper(this.fontRenderer, mouseX, mouseY, this.width, this.buttonList, this);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.mc.renderEngine.bindTexture(texture);
        this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
        this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.angle")), x - 30, y - 35, 0xffffff);
    }
}