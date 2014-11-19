package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.BaseListEntry;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.ConfigEntry;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;

public class GuiSpotLightDeleteConfig extends GuiContainer implements GuiListBase
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tileSpotLight;
    public World world;
    private GuiList gList;
    private ArrayList<BaseListEntry> list = new ArrayList<BaseListEntry>();
    private ConfigEntry selected;
    private GuiButton deleteButton;
    private GuiBooleanButton helpButton;

    public GuiSpotLightDeleteConfig(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld, ArrayList<BaseListEntry> list)
    {
        super(new ContainerSpotLight(tileEntity, playerInventory, wrld, 8));
        invPlayer = playerInventory;
        tileSpotLight = tileEntity;
        world = wrld;
        this.list = list;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.buttonList.add(0, new GuiButton(0, x + 6, y + 117, 78, 20, I18n.format("container.spotlight.back")));
        this.buttonList.add(1, deleteButton = new GuiButton(1, x + 90, y + 117, 78, 20, EnumChatFormatting.RED + I18n.format("container.spotlight.delete")));
        this.buttonList.add(2, helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
        deleteButton.enabled = false;

        gList = new GuiList(this, list, x + 6, y + 17, x + 169, y + 115);
        gList.addButton(buttonList);
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
                if(deleteButton.enabled)
                {
                    PacketSender.sendSpotLightPacket(tileSpotLight, 52, selected.getId());
                }
                break;
            }
            case 20:
            {
                this.helpButton.toggle();
                break;
            }
            default:
            {
                this.gList.actionPerformed(guibutton, this.buttonList);
            }
        }
    }

    @Override
    public void setSelected(BaseListEntry entry)
    {
        if(entry instanceof ConfigEntry)
        {
            ConfigEntry ent = (ConfigEntry)entry;
            this.selected = ent;
            PacketSender.sendSpotLightPacket(tileSpotLight, 51, ent.getId());
        }
    }

    @Override
    public void updateScreen()
    {
        deleteButton.enabled = this.selected != null;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
    {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        super.drawScreen(mouseX, mouseY, partialRenderTick);
        this.gList.drawScreen(x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.mc.renderEngine.bindTexture(texture);
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.delete")), x + 5, y + 8, 4210752);
    }
}