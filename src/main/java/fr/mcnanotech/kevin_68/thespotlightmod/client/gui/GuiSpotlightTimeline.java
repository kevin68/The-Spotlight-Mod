package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimeline;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineDeleteKey;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineReset;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineSmooth;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class GuiSpotlightTimeline extends GuiContainer {
    protected InventoryPlayer invPlayer;
    protected TileEntitySpotLight tile;
    protected World world;

    private short selectedKeyID = -1;
    private GuiBooleanButton buttonHelp, buttonTimelineEnabled, buttonSmooth;
    private GuiButton buttonRemove;
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight1.png");
    protected static final ResourceLocation texture2 = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight2.png");
    protected static final ResourceLocation tsmIcons = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public GuiSpotlightTimeline(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World world, ContainerSpotLight spotlightContainer) {
        super(spotlightContainer); 
        spotlightContainer.changePlayerInventoryPos(51, 186);
        this.invPlayer = playerInventory;
        this.tile = tileEntity;
        this.world = world;
        this.xSize = 256;
        this.ySize = 256;
    }

    @Override
    public void initGui() {
        super.initGui();

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.addButton(new GuiButton(2, x - 27, y + 184, 65, 20, I18n.format("container.spotlight.back")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(new GuiSpotLight(invPlayer, tile, world, inventorySlots));
            }
        });
        this.addButton(new GuiButton(3, x - 27, y + 69, 120, 20, I18n.format("container.spotlight.addKey")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(new GuiSpotlightTimelineAddKey(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
            }
        });
        this.addButton(this.buttonTimelineEnabled = new GuiBooleanButton(4, x - 27, y + 157, 120, 20, "", this.tile.timelineEnabled) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                buttonTimelineEnabled.toggle();
                TSMNetwork.CHANNEL.sendToServer(new PacketTimeline(tile.getPos(), buttonTimelineEnabled.isActive()));
            }
        });
        this.buttonTimelineEnabled.setTexts(I18n.format("container.spotlight.timelineval", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.timelineval", I18n.format("container.spotlight.off")));
        this.addButton(this.buttonRemove = new GuiButton(5, x - 27, y + 91, 120, 20, I18n.format("container.spotlight.deleteKey")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(new GuiYesNo((confirmed, id) -> {
                    if (confirmed) {
                        tile.setKey(selectedKeyID, null);
                        TSMNetwork.CHANNEL.sendToServer(new PacketTimelineDeleteKey(tile.getPos(), selectedKeyID));
                    }
                    mc.displayGuiScreen(GuiSpotlightTimeline.this);
                }, I18n.format("container.spotlight.askerasekey"), "", I18n.format("container.spotlight.erase"), I18n.format("container.spotlight.cancel"), 5));
            }
        });
        this.buttonRemove.enabled = false;
        this.addButton(new GuiButton(6, x - 27, y + 113, 120, 20, I18n.format("container.spotlight.resettime")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                TSMNetwork.CHANNEL.sendToServer(new PacketTimelineReset(tile.getPos()));
            }
        });
        this.buttonSmooth = this.addButton(new GuiBooleanButton(7, x - 27, y + 135, 120, 20, I18n.format("container.spotlight.smooth"), this.tile.timelineSmooth) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                buttonSmooth.toggle();
                TSMNetwork.CHANNEL.sendToServer(new PacketTimelineSmooth(tile.getPos(), buttonSmooth.isActive()));
            }
        });
        this.buttonHelp = this.addButton(new GuiBooleanButton(8, x + 220, y + 184, 20, 20, "?", this.tile.helpMode) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                buttonHelp.toggle();
                tile.helpMode = buttonHelp.isActive();
            }
        });
        this.buttonTimelineEnabled.enabled = this.tile.hasKey();
        for (short i = 0; i < 120; i++) {
            if (this.tile.getKey(i) != null) {
                this.addButton(new GuiSpotlightTimelineKeyButton(10 + i, this.width / 2 - 149 + (int) (i * 2.5), y + 50 + i % 2 * 4) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        selectedKeyID = (short) (this.id - 10);
                        buttonRemove.enabled = true;
                    }
                });
            }
        }
    }

    @Override
    public void onGuiClosed() {
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateTLData(this.tile.getPos(), TSMJsonManager.getTlDataFromTile(this.tile).toString()));
        super.onGuiClosed();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialRenderTick) {
        super.render(mouseX, mouseY, partialRenderTick);

        if (this.buttonHelp.isActive()) {
            if (mouseX > 64 && mouseY > 30 && mouseX < 367 && mouseY < 53) {
                this.drawHoveringText(this.fontRenderer.listFormattedStringToWidth(TextFormatting.GREEN + I18n.format("tutorial.spotlight.timeline.timeline"), (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
            }
            for (GuiButton button : this.buttons) {
                if (button.isMouseOver()) {
                    String text = "";
                    switch (button.id) {
                    case 2:
                        text = I18n.format("tutorial.spotlight.back");
                        break;
                    case 3:
                        text = I18n.format("tutorial.spotlight.timeline.addkey");
                        break;
                    case 4:
                        text = I18n.format("tutorial.spotlight.timeline.toogle");
                        break;
                    case 5:
                        text = I18n.format("tutorial.spotlight.timeline.delkey");
                        break;
                    case 6:
                        text = I18n.format("tutorial.spotlight.timeline.set0");
                        break;
                    case 7:
                        text = I18n.format("tutorial.spotlight.timeline.smooth");
                        break;
                    case 8:
                        text = I18n.format("tutorial.spotlight.help");
                        break;
                    }
                    if (!text.isEmpty()) {
                        this.drawHoveringText(this.fontRenderer.listFormattedStringToWidth(TextFormatting.GREEN + text, (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
                    }
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(texture);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x - 35, y + 19, 0, 0, this.xSize, this.ySize);
        this.mc.getTextureManager().bindTexture(texture2);
        this.drawTexturedModalRect(x + 135, y + 19, 0, 0, this.xSize, this.ySize);
        this.mc.getTextureManager().bindTexture(tsmIcons);
        this.drawTexturedModalRect(x - 20, y + 40, 0, 59, 256, 21);
        this.drawTexturedModalRect(x + 225, y + 40, 0, 81, 57, 21);
        this.drawTexturedModalRect(x - 20 + this.tile.time / 4, y + 40, 0, 105, 1, 12);
        if (this.selectedKeyID != -1) {
            this.drawTexturedModalRect((int) (x - 22 + this.selectedKeyID * 2.5), y + 62, 0, 115, 5, 6);
        }
//		drawString(this.fontRendererObj, EnumTextFormatting.RED + I18n.format("container.spotlight.red") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERRED) & 0xFF), x + 100, y + 70, 0xffffff);
//		drawString(this.fontRendererObj, EnumTextFormatting.GREEN + I18n.format("container.spotlight.green") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERGREEN) & 0xFF), x + 100, y + 80, 0xffffff);
//		drawString(this.fontRendererObj, EnumTextFormatting.BLUE + I18n.format("container.spotlight.blue") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERBLUE) & 0xFF), x + 100, y + 90, 0xffffff);
//		drawString(this.fontRendererObj, EnumTextFormatting.DARK_RED + I18n.format("container.spotlight.red") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERSECRED) & 0xFF), x + 185, y + 70, 0xffffff);
//		drawString(this.fontRendererObj, EnumTextFormatting.DARK_GREEN + I18n.format("container.spotlight.green") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERSECGREEN) & 0xFF), x + 185, y + 80, 0xffffff);
//		drawString(this.fontRendererObj, EnumTextFormatting.DARK_BLUE + I18n.format("container.spotlight.blue") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERSECBLUE) & 0xFF), x + 185, y + 90, 0xffffff);
//		drawString(this.fontRendererObj, EnumTextFormatting.WHITE + I18n.format("container.spotlight.angle") + " 1 : " + entry.get(EnumLaserInformations.LASERANGLE1), x + 100, y + 100, 0xffffff);
//		drawString(this.fontRendererObj, EnumTextFormatting.WHITE + I18n.format("container.spotlight.angle") + " 2 : " + entry.get(EnumLaserInformations.LASERANGLE2), x + 185, y + 100, 0xffffff);
//		drawString(this.fontRendererObj,
//				EnumTextFormatting.WHITE + I18n.format("container.spotlight.rotate") + " : " + ((Boolean) entry.get(EnumLaserInformations.LASERAUTOROTATE) ? I18n.format("container.spotlight.true") : I18n.format("container.spotlight.false")), x + 100,
//				y + 110, 0xffffff);
//		drawString(this.fontRendererObj, EnumTextFormatting.WHITE + I18n.format("container.spotlight.rotationspeed") + " : " + entry.get(EnumLaserInformations.LASERROTATIONSPEED), x + 100, y + 120, 0xffffff);
//		drawString(this.fontRendererObj, EnumTextFormatting.WHITE + I18n.format("container.spotlight.rotationreverse") + " : "
//				+ ((Boolean) entry.get(EnumLaserInformations.LASERREVERSEROTATION) ? I18n.format("container.spotlight.true") : I18n.format("container.spotlight.false")), x + 100, y + 130, 0xffffff);
//		drawString(this.fontRendererObj,
//				EnumTextFormatting.WHITE + I18n.format("container.spotlight.secondlazer") + " : " + ((Boolean) entry.get(EnumLaserInformations.LASERSECONDARY) ? I18n.format("container.spotlight.true") : I18n.format("container.spotlight.false")),
//				x + 100, y + 140, 0xffffff);
//		drawString(this.fontRendererObj,
//				EnumTextFormatting.WHITE + I18n.format("container.spotlight.axis") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERDISPLAYAXE) == 0 ? "y" : (Byte) entry.get(EnumLaserInformations.LASERDISPLAYAXE) == 1 ? "x" : "z"), x + 100,
//				y + 150, 0xffffff);
//		drawString(this.fontRendererObj,
//				EnumTextFormatting.WHITE + I18n.format("container.spotlight.laserMode") + " : " + ((Boolean) entry.get(EnumLaserInformations.LASERDOUBLE) ? I18n.format("container.spotlight.double") : I18n.format("container.spotlight.simple")),
//				x + 100, y + 160, 0xffffff);
//		drawString(this.fontRendererObj, EnumTextFormatting.WHITE + I18n.format("container.spotlight.size") + " " + I18n.format("container.spotlight.main") + " : " + entry.get(EnumLaserInformations.LASERMAINSIZE) + " "
//				+ I18n.format("container.spotlight.sec") + " : " + entry.get(EnumLaserInformations.LASERSECSIZE), x + 100, y + 170, 0xffffff);
        this.fontRenderer.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.timeline")), x - 25, y + 28, 4210752);
    }
}