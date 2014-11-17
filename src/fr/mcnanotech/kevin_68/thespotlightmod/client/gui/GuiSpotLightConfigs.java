package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLightSlotConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.items.TSMItems;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;

public class GuiSpotLightConfigs extends GuiContainer
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");
    protected static final ResourceLocation icons = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tileSpotLight;
    public World world;
    public GuiButton save, load, delete;

    public GuiSpotLightConfigs(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
    {
        super(new ContainerSpotLightSlotConfig(tileEntity, playerInventory, wrld));
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

        this.buttonList.add(new GuiButton(0, x + 38, y + 112, 100, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(save = new GuiButton(1, x + 5, y + 20, 166, 20, I18n.format("container.spotlight.save")));
        this.buttonList.add(load = new GuiButton(2, x + 5, y + 43, 166, 20, I18n.format("container.spotlight.load")));
        this.buttonList.add(delete = new GuiButton(3, x + 5, y + 66, 166, 20, I18n.format("container.spotlight.delete")));
        this.buttonList.add(new GuiButton(4, x + 5, y + 89, 166, 20, I18n.format("container.spotlight.reset")));
        save.enabled = false;
        load.enabled = false;
        delete.enabled = false;
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch(guibutton.id)
        {
            case 0:
            {
                this.mc.displayGuiScreen(new GuiSpotLight(invPlayer, tileSpotLight, world));
                break;
            }
            case 1:
            {
                this.mc.displayGuiScreen(new GuiSpotLightSaveConfig(invPlayer, tileSpotLight, world));
                break;
            }
            case 2:
            {
                PacketSender.sendSpotLightPacketConfig(tileSpotLight, true, null, null, 0);
                break;
            }
            case 3:
            {
                PacketSender.sendSpotLightPacketConfig(tileSpotLight, true, null, null, 1);
                break;
            }
            case 4:
            {
                PacketSender.sendSpotLightPacketByte(tileSpotLight, (byte)100, (byte)0);
            }
        }
    }

    @Override
    public void updateScreen()
    {
        this.save.enabled = this.tileSpotLight.getStackInSlot(0) != null && this.tileSpotLight.getStackInSlot(0).getItem() == TSMItems.configSaver;
        this.load.enabled = this.save.enabled;
        this.delete.enabled = this.save.enabled;

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.mc.renderEngine.bindTexture(texture);
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        this.mc.renderEngine.bindTexture(icons);
        this.drawTexturedModalRect(x + 7, y + 113, 238, 0, 18, 18);
        this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.configs")), x + 5, y + 8, 4210752);
    }
}