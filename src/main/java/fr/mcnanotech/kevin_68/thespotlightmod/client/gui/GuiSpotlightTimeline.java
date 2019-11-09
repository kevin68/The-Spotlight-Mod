package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.TSMButton;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.ButtonToggle;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.ButtonToggleHelp;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.IHelpButton;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimeline;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineDeleteKey;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineReset;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineSmooth;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiSpotlightTimeline extends ContainerScreen<ContainerSpotLight> {
    protected PlayerInventory invPlayer;
    protected TileEntitySpotLight tile;

    private short selectedKeyID = -1;
    private ButtonToggle buttonTimelineEnabled, buttonSmooth;
    private Button buttonRemove;
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/spotlight1.png");
    protected static final ResourceLocation texture2 = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/spotlight2.png");
    protected static final ResourceLocation tsmIcons = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

    public GuiSpotlightTimeline(ContainerSpotLight container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
        container.changePlayerInventoryPos(51, 186);
        this.invPlayer = playerInventory;
        this.tile = container.getSpotlight();
        this.xSize = 256;
        this.ySize = 256;
    }

    @Override
    public void init() {
        super.init();

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.addButton(new TSMButton(x - 27, y + 184, 65, 20, I18n.format("container.spotlight.back"), b -> {
            minecraft.displayGuiScreen(new GuiSpotLight(container, invPlayer, title));
        }, I18n.format("tutorial.spotlight.back")));

        this.addButton(new TSMButton(x - 27, y + 69, 120, 20, I18n.format("container.spotlight.addKey"), b -> {
            minecraft.displayGuiScreen(new GuiSpotlightTimelineAddKey(container, invPlayer, title));
        }, I18n.format("tutorial.spotlight.timeline.addkey")));

        this.buttonTimelineEnabled = this.addButton(new ButtonToggle(x - 27, y + 157, 120, 20, "", this.tile.timelineEnabled, b -> {
            TSMNetwork.CHANNEL.sendToServer(new PacketTimeline(tile.getPos(), buttonTimelineEnabled.isActive()));
        }, I18n.format("tutorial.spotlight.timeline.toogle")));
        this.buttonTimelineEnabled.setTexts(I18n.format("container.spotlight.timelineval", I18n.format("container.spotlight.on")), I18n.format("container.spotlight.timelineval", I18n.format("container.spotlight.off")));

        this.buttonRemove = this.addButton(new TSMButton(x - 27, y + 91, 120, 20, I18n.format("container.spotlight.deleteKey"), b -> {
            minecraft.displayGuiScreen(new ConfirmScreen((confirmed) -> {
                if (confirmed) {
                    tile.setKey(selectedKeyID, null);
                    TSMNetwork.CHANNEL.sendToServer(new PacketTimelineDeleteKey(tile.getPos(), selectedKeyID));
                }
                minecraft.displayGuiScreen(GuiSpotlightTimeline.this);
            }, new TranslationTextComponent("container.spotlight.askerasekey"), new StringTextComponent(""), I18n.format("container.spotlight.erase"), I18n.format("container.spotlight.cancel")));
        }, I18n.format("tutorial.spotlight.timeline.delkey")));
        this.buttonRemove.active = false;

        this.addButton(new TSMButton(x - 27, y + 113, 120, 20, I18n.format("container.spotlight.resettime"), b -> {
            TSMNetwork.CHANNEL.sendToServer(new PacketTimelineReset(tile.getPos()));
        }, I18n.format("tutorial.spotlight.timeline.set0")));

        this.buttonSmooth = this.addButton(new ButtonToggle(x - 27, y + 135, 120, 20, I18n.format("container.spotlight.smooth"), this.tile.timelineSmooth, b -> {
            //buttonSmooth.toggle();
            TSMNetwork.CHANNEL.sendToServer(new PacketTimelineSmooth(tile.getPos(), buttonSmooth.isActive()));
        }, I18n.format("tutorial.spotlight.timeline.smooth")));
        this.addButton(new ButtonToggleHelp(x + 180, y + 140, 20, 20, tile));
        this.buttonTimelineEnabled.active = this.tile.hasKey();
        for (short i = 0; i < 120; i++) {
            if (this.tile.getKey(i) != null) {
                final short keyId = i;
                this.addButton(new GuiSpotlightTimelineKeyButton(this.width / 2 - 149 + (int) (i * 2.5), y + 50 + i % 2 * 4, b -> {
                    selectedKeyID = keyId;
                    buttonRemove.active = true;
                }));
            }
        }
    }

    @Override
    public void onClose() {
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateTLData(this.tile.getPos(), TSMJsonManager.getTlDataFromTile(this.tile).toString()));
        super.onClose();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialRenderTick) {
        super.render(mouseX, mouseY, partialRenderTick);

        if (this.tile.helpMode) {
            if (mouseX > 64 && mouseY > 30 && mouseX < 367 && mouseY < 53) {
                this.renderTooltip(this.font.listFormattedStringToWidth(TextFormatting.GREEN + I18n.format("tutorial.spotlight.timeline.timeline"), (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
            }

			this.buttons.stream().filter(b -> b.isMouseOver(mouseX, mouseY) && b instanceof IHelpButton).findFirst().ifPresent(b -> {
				this.renderTooltip(this.font.listFormattedStringToWidth(TextFormatting.GREEN + ((IHelpButton)b).getHelpMessage(), (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
			});
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(x - 35, y + 19, 0, 0, this.xSize, this.ySize);
        Minecraft.getInstance().getTextureManager().bindTexture(texture2);
        this.blit(x + 135, y + 19, 0, 0, this.xSize, this.ySize);
        Minecraft.getInstance().getTextureManager().bindTexture(tsmIcons);
        this.blit(x - 20, y + 40, 0, 59, 256, 21);
        this.blit(x + 225, y + 40, 0, 81, 57, 21);
        this.blit(x - 20 + this.tile.time / 4, y + 40, 0, 105, 1, 12);
        if (this.selectedKeyID != -1) {
            this.blit((int) (x - 22 + this.selectedKeyID * 2.5), y + 62, 0, 115, 5, 6);
        }
//		drawString(this.fontObj, EnumTextFormatting.RED + I18n.format("container.spotlight.red") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERRED) & 0xFF), x + 100, y + 70, 0xffffff);
//		drawString(this.fontObj, EnumTextFormatting.GREEN + I18n.format("container.spotlight.green") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERGREEN) & 0xFF), x + 100, y + 80, 0xffffff);
//		drawString(this.fontObj, EnumTextFormatting.BLUE + I18n.format("container.spotlight.blue") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERBLUE) & 0xFF), x + 100, y + 90, 0xffffff);
//		drawString(this.fontObj, EnumTextFormatting.DARK_RED + I18n.format("container.spotlight.red") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERSECRED) & 0xFF), x + 185, y + 70, 0xffffff);
//		drawString(this.fontObj, EnumTextFormatting.DARK_GREEN + I18n.format("container.spotlight.green") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERSECGREEN) & 0xFF), x + 185, y + 80, 0xffffff);
//		drawString(this.fontObj, EnumTextFormatting.DARK_BLUE + I18n.format("container.spotlight.blue") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERSECBLUE) & 0xFF), x + 185, y + 90, 0xffffff);
//		drawString(this.fontObj, EnumTextFormatting.WHITE + I18n.format("container.spotlight.angle") + " 1 : " + entry.get(EnumLaserInformations.LASERANGLE1), x + 100, y + 100, 0xffffff);
//		drawString(this.fontObj, EnumTextFormatting.WHITE + I18n.format("container.spotlight.angle") + " 2 : " + entry.get(EnumLaserInformations.LASERANGLE2), x + 185, y + 100, 0xffffff);
//		drawString(this.fontObj,
//				EnumTextFormatting.WHITE + I18n.format("container.spotlight.rotate") + " : " + ((Boolean) entry.get(EnumLaserInformations.LASERAUTOROTATE) ? I18n.format("container.spotlight.true") : I18n.format("container.spotlight.false")), x + 100,
//				y + 110, 0xffffff);
//		drawString(this.fontObj, EnumTextFormatting.WHITE + I18n.format("container.spotlight.rotationspeed") + " : " + entry.get(EnumLaserInformations.LASERROTATIONSPEED), x + 100, y + 120, 0xffffff);
//		drawString(this.fontObj, EnumTextFormatting.WHITE + I18n.format("container.spotlight.rotationreverse") + " : "
//				+ ((Boolean) entry.get(EnumLaserInformations.LASERREVERSEROTATION) ? I18n.format("container.spotlight.true") : I18n.format("container.spotlight.false")), x + 100, y + 130, 0xffffff);
//		drawString(this.fontObj,
//				EnumTextFormatting.WHITE + I18n.format("container.spotlight.secondlazer") + " : " + ((Boolean) entry.get(EnumLaserInformations.LASERSECONDARY) ? I18n.format("container.spotlight.true") : I18n.format("container.spotlight.false")),
//				x + 100, y + 140, 0xffffff);
//		drawString(this.fontObj,
//				EnumTextFormatting.WHITE + I18n.format("container.spotlight.axis") + " : " + ((Byte) entry.get(EnumLaserInformations.LASERDISPLAYAXE) == 0 ? "y" : (Byte) entry.get(EnumLaserInformations.LASERDISPLAYAXE) == 1 ? "x" : "z"), x + 100,
//				y + 150, 0xffffff);
//		drawString(this.fontObj,
//				EnumTextFormatting.WHITE + I18n.format("container.spotlight.laserMode") + " : " + ((Boolean) entry.get(EnumLaserInformations.LASERDOUBLE) ? I18n.format("container.spotlight.double") : I18n.format("container.spotlight.simple")),
//				x + 100, y + 160, 0xffffff);
//		drawString(this.fontObj, EnumTextFormatting.WHITE + I18n.format("container.spotlight.size") + " " + I18n.format("container.spotlight.main") + " : " + entry.get(EnumLaserInformations.LASERMAINSIZE) + " "
//				+ I18n.format("container.spotlight.sec") + " : " + entry.get(EnumLaserInformations.LASERSECSIZE), x + 100, y + 170, 0xffffff);
        this.font.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.timeline")), x - 25, y + 28, 4210752);
    }
}