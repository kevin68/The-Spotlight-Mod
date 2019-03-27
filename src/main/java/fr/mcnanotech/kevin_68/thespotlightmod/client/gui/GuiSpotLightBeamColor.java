package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;

public class GuiSpotLightBeamColor extends GuiContainer implements ISlider
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID, "textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tile;
    public World world;
    public GuiBooleanButton buttonHelp;

    public GuiSpotLightBeamColor(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
    {
        super(new ContainerSpotLight(tileEntity, playerInventory, false));
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

        this.addButton(new GuiSlider(0, x - 40, y - 20, 256, 20, TextFormatting.RED + I18n.format("container.spotlight.red"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_RED), false, true, this));
        this.addButton(new GuiSlider(1, x - 40, y + 2, 256, 20, TextFormatting.GREEN + I18n.format("container.spotlight.green"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_GREEN), false, true, this));
        this.addButton(new GuiSlider(2, x - 40, y + 24, 256, 20, TextFormatting.BLUE + I18n.format("container.spotlight.blue"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_BLUE), false, true, this));

        this.addButton(new GuiSlider(3, x - 40, y + 68, 256, 20, TextFormatting.DARK_RED + I18n.format("container.spotlight.red"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_SEC_RED), false, true, this));
        this.addButton(new GuiSlider(4, x - 40, y + 90, 256, 20, TextFormatting.DARK_GREEN + I18n.format("container.spotlight.green"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_SEC_GREEN), false, true, this));
        this.addButton(new GuiSlider(5, x - 40, y + 112, 256, 20, TextFormatting.DARK_BLUE + I18n.format("container.spotlight.blue"), "", 0, 255, this.tile.getShort(EnumTSMProperty.BEAM_SEC_BLUE), false, true, this));

        this.addButton(new GuiSlider(6, x - 40, y + 46, 256, 20, I18n.format("container.spotlight.alpha"), "", 0, 1, this.tile.getFloat(EnumTSMProperty.BEAM_ALPHA), true, true, this));
        this.addButton(new GuiSlider(7, x - 40, y + 134, 256, 20, I18n.format("container.spotlight.alpha"),"", 0, 1, this.tile.getFloat(EnumTSMProperty.BEAM_SEC_ALPHA), true, true, this));

        this.addButton(new GuiButton(19, x + 38, y + 159, 100, 20, I18n.format("container.spotlight.back")) {
			@Override
			public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(new GuiSpotLight(invPlayer, tile, world));
			}
		});
        this.buttonHelp = this.addButton(new GuiBooleanButton(20, x + 180, y + 159, 20, 20, "?", this.tile.helpMode) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				buttonHelp.toggle();
				tile.helpMode = buttonHelp.isActive();
			}
		});
    }

    @Override
    public void onGuiClosed()
    {
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos(), TSMJsonManager.getDataFromTile(this.tile).toString()));
        super.onGuiClosed();
    }
    
    @Override
    public void onChangeSliderValue(GuiSlider slider)
    {
        switch(slider.id)
        {
            case 0:
                this.tile.setProperty(EnumTSMProperty.BEAM_RED, (short)(slider.getValueInt()));
                break;
            case 1:
                this.tile.setProperty(EnumTSMProperty.BEAM_GREEN, (short)(slider.getValueInt()));
                break;
            case 2:
                this.tile.setProperty(EnumTSMProperty.BEAM_BLUE, (short)(slider.getValueInt()));
                break;
            case 3:
                this.tile.setProperty(EnumTSMProperty.BEAM_SEC_RED, (short)(slider.getValueInt()));
                break;
            case 4:
                this.tile.setProperty(EnumTSMProperty.BEAM_SEC_GREEN, (short)(slider.getValueInt()));
                break;
            case 5:
                this.tile.setProperty(EnumTSMProperty.BEAM_SEC_BLUE, (short)(slider.getValueInt()));
                break;
            case 6:
                this.tile.setProperty(EnumTSMProperty.BEAM_ALPHA, (float)(slider.getValue()));
                break;
            case 7:
                this.tile.setProperty(EnumTSMProperty.BEAM_SEC_ALPHA, (float)(slider.getValue()));
                break;
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialRenderTick)
    {
        super.render(mouseX, mouseY, partialRenderTick);

        if(this.buttonHelp.isActive())
        {
            for(GuiButton button : this.buttons)
            {
                if(button.isMouseOver())
                {
                    String text = "";
                    switch(button.id)
                    {
                        case 0:
                            text = I18n.format("tutorial.spotlight.colors.red");
                            break;
                        case 1:
                            text = I18n.format("tutorial.spotlight.colors.green");
                            break;
                        case 2:
                            text = I18n.format("tutorial.spotlight.colors.blue");
                            break;
                        case 3:
                            text = I18n.format("tutorial.spotlight.colors.secred");
                            break;
                        case 4:
                            text = I18n.format("tutorial.spotlight.colors.secgreen");
                            break;
                        case 5:
                            text = I18n.format("tutorial.spotlight.colors.secblue");
                            break;
                        case 6:
                            text = I18n.format("tutorial.spotlight.colors.alpha");
                            break;
                        case 7:
                            text = I18n.format("tutorial.spotlight.colors.secalpha");
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
        // this.mc.renderEngine.bindTexture(texture);
        // this.drawTexturedModalRect(x, y + 114, 69, 81, this.xSize, 52);
        this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.color")), x - 30, y - 35, 0xffffff);
    }
}