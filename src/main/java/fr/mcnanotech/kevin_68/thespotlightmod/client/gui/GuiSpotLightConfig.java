package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.ButtonToggleHelp;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.IHelpButton;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.TSMButton;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class GuiSpotLightConfig extends ContainerScreen<ContainerSpotLight>
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/spotlight.png");
    protected static final ResourceLocation TSM_ICONS = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

    public PlayerInventory invPlayer;
    public TileEntitySpotLight tile;

    public GuiSpotLightConfig(ContainerSpotLight container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        container.showConfigSlot(true);
        this.invPlayer = playerInventory;
        this.tile = container.getSpotlight();
    }

    @Override
    public void init() {
        super.init();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.addButton(new TSMButton(x + 38, y + 117, 100, 20, I18n.format("container.spotlight.back"), b -> {
            minecraft.displayGuiScreen(new GuiSpotLight(container, invPlayer, title));
		}, I18n.format("tutorial.spotlight.back")));
        this.addButton(new ButtonToggleHelp(x + 180, y + 140, 20, 20, tile));
    }

    @Override
    public void removed() {
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos(), TSMJsonManager.getDataFromTile(this.tile).toString()));
        super.removed();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialRenderTick) {
        super.render(mouseX, mouseY, partialRenderTick);
        if (this.tile.helpMode) {
            Slot s = this.getSlotUnderMouse();
            if(s != null)
            {
                String text = "";
                switch(s.slotNumber)
                {
                    case 0:
                        text = I18n.format("tutorial.spotlight.config.tosave");
                        break;
                    case 1:
                        text = I18n.format("tutorial.spotlight.config.fromsave");
                        break;
                    case 2:
                        text = I18n.format("tutorial.spotlight.config.toload");
                        break;
                    case 3:
                        text = I18n.format("tutorial.spotlight.config.fromload");
                        break;
                    case 4:
                        text = I18n.format("tutorial.spotlight.config.toclean");
                        break;
                    case 5:
                        text = I18n.format("tutorial.spotlight.config.fromclean");
                        break;
                }
                if(!text.isEmpty())
                {
                    this.renderTooltip(this.font.listFormattedStringToWidth(TextFormatting.GREEN + text, (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);  
                }
            }

			this.buttons.stream().filter(b -> b.isMouseOver(mouseX, mouseY) && b instanceof IHelpButton).findFirst().ifPresent(b -> {
				this.renderTooltip(this.font.listFormattedStringToWidth(TextFormatting.GREEN + ((IHelpButton)b).getHelpMessage(), (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
			});
		}
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        this.blit(x, y, 0, 0, this.xSize, this.ySize);
        Minecraft.getInstance().getTextureManager().bindTexture(TSM_ICONS);
        this.blit(x + 39, y + 79, 220, 0, 18, 18);
        this.blit(x + 79, y + 79, 220, 0, 18, 18);
        this.blit(x + 39, y + 29, 238, 0, 18, 18);
        this.blit(x + 79, y + 29, 220, 0, 18, 18);
        this.blit(x + 119, y + 29, 220, 0, 18, 18);
        this.blit(x + 119, y + 79, 238, 0, 18, 18);
        this.blit(x + 122, y + 49, 213, 21, 13, 26);
        this.blit(x + 82, y + 49, 213, 21, 13, 26);
        this.blit(x + 42, y + 49, 213, 21, 13, 26);
        this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.config")), x - 30, y - 35, 0xffffff);
    }
}
