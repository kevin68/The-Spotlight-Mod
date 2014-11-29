package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.util.ArrayList;

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
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;

public class GuiSpotLightConfigs extends GuiContainer
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");
    protected static final ResourceLocation icons = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tileSpotLight;
    public World world;
    public GuiButton save, load, delete;
    private GuiBooleanButton helpButton;

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
        this.buttonList.add(helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
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
    public void updateScreen()
    {
        this.save.enabled = this.tileSpotLight.getStackInSlot(0) != null && this.tileSpotLight.getStackInSlot(0).getItem() == TSMItems.configSaver;
        this.load.enabled = this.save.enabled;
        this.delete.enabled = this.save.enabled;

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
    {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        super.drawScreen(mouseX, mouseY, partialRenderTick);

        if(helpButton.getIsActive())
        {
            boolean reversed = mouseX > width / 2;
            ArrayList<String> list = new ArrayList<String>();

            if(mouseX > x + 5 && mouseX < x + 171)
            {
                if(mouseY > y + 20 && mouseY < y + 40)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.configs.save"), mouseX, width, reversed);
                }

                if(mouseY > y + 43 && mouseY < y + 63)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.configs.load"), mouseX, width, reversed);
                }

                if(mouseY > y + 66 && mouseY < y + 86)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.configs.delete"), mouseX, width, reversed);
                }

                if(mouseY > y + 89 && mouseY < y + 109)
                {
                    list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.configs.reset"), mouseX, width, reversed);
                }
            }

            if(mouseX > x + 5 && mouseX < x + 85 && mouseY > y + 112 && mouseY < y + 132 && mouseX < x + 25)
            {
                list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.configslot"), mouseX, width, reversed);
            }

            if(mouseX > x + 38 && mouseX < x + 138 && mouseY > y + 117 && mouseY < y + 137)
            {
                list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.back"), mouseX, width, reversed);
            }

            if(mouseX > x + 180 && mouseX < x + 200 && mouseY > y + 140 && mouseY < y + 160)
            {
                list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.help"), mouseX, width, reversed);
            }

            if(list.size() > 0 && (list.get(list.size() - 1) == " " || list.get(list.size() - 1).isEmpty()))
            {
                list.remove(list.size() - 1);
            }
            GuiHelper.drawHoveringText(list, mouseX, mouseY, this.fontRendererObj, reversed ? 0 : 200000, height, 0x00ff00);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseYS)
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