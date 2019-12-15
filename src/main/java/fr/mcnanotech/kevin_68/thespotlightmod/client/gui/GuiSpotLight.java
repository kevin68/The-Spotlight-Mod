package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.ButtonToggle;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.ButtonToggleHelp;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.IHelpButton;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.buttons.TSMButton;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketLock;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class GuiSpotLight extends ContainerScreen<ContainerSpotLight> {
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/spotlight.png");
    protected static final ResourceLocation tsmIcons = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

    private PlayerInventory invPlayer;
    private TileEntitySpotLight tile;
    private Button buttonTextures, buttonColors, buttonAngle, buttonBeamSpecs;
    private ButtonToggle buttonMode, buttonRedstone, buttonLock;

    public GuiSpotLight(ContainerSpotLight container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
        container.resetSlot();
        this.invPlayer = playerInventory;
        this.tile = container.getSpotlight();
    }

    @Override
    public void init() {
        super.init();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        this.buttonColors = this.addButton(new TSMButton(x + 5, y + 20, 80, 20, this.tile.isBeam ? I18n.format("container.spotlight.color") : I18n.format("container.spotlight.textcolor"), b -> {
            if (buttonMode.isActive()) {
                minecraft.displayGuiScreen(new GuiSpotLightBeamColor(container, invPlayer, title));
            } else {
                minecraft.displayGuiScreen(new GuiSpotLightTextColor(container, invPlayer, title));
            }
        }, this.tile.isBeam ? I18n.format("tutorial.spotlight.colors") : I18n.format("tutorial.spotlight.textcolors")));
        this.buttonColors.active = !this.tile.timelineEnabled;

        this.buttonAngle = this.addButton(new TSMButton(x + 5, y + 43, 80, 20, I18n.format("container.spotlight.angle"), b -> {
            if (buttonMode.isActive()) {
                minecraft.displayGuiScreen(new GuiSpotLightBeamAngles(container, invPlayer, title));
            } else {
                minecraft.displayGuiScreen(new GuiSpotLightTextAngles(container, invPlayer, title));
            }
        }, this.tile.isBeam ? I18n.format("tutorial.spotlight.beamangle") : I18n.format("tutorial.spotlight.textangle")));
        this.buttonAngle.active = !this.tile.timelineEnabled;

        this.buttonBeamSpecs = this.addButton(new TSMButton(x + 5, y + 66, 80, 20, I18n.format("container.spotlight.beamspecs"), b -> {
            if (buttonMode.isActive()) {
                minecraft.displayGuiScreen(new GuiSpotLightBeamProperties(container, invPlayer, title));
            } else {
                minecraft.displayGuiScreen(new GuiSpotLightTextProperties(container, invPlayer, title));
            }
        }, this.tile.isBeam ? I18n.format("tutorial.spotlight.beamprops") : I18n.format("tutorial.spotlight.textprops")));
        this.buttonBeamSpecs.active = !this.tile.timelineEnabled;

        this.buttonTextures = this.addButton(new TSMButton(x + 5, y + 89, 80, 20, I18n.format("container.spotlight.textures"), b -> {
            minecraft.displayGuiScreen(new GuiSpotLightBeamTextures(container, invPlayer, title));
        }, I18n.format("tutorial.spotlight.texture")));
        this.buttonMode = this.addButton(new ButtonToggle(x + 5, y + 112, 80, 20, "", this.tile.isBeam, b -> {
            tile.isBeam = buttonMode.isActive();
            buttonTextures.active = buttonMode.isActive();
            buttonColors.setMessage(tile.isBeam ? I18n.format("container.spotlight.color") : I18n.format("container.spotlight.textcolor"));
        }, I18n.format("tutorial.spotlight.mode")));
        this.buttonMode.active = !this.tile.timelineEnabled;
        this.buttonMode.setTexts(I18n.format("container.spotlight.modebeam"), I18n.format("container.spotlight.modetext"));
        this.buttonMode.shouldNotChangeTextColor(true);
        this.buttonMode.shouldChangeTextureOnToggle(false);

        this.addButton(new TSMButton(x + 90, y + 20, 80, 20, I18n.format("container.spotlight.timeline"), b -> {
            minecraft.displayGuiScreen(new GuiSpotlightTimeline(container, invPlayer, title));
        }, I18n.format("tutorial.spotlight.timeline")));

        this.buttonLock = this.addButton(new ButtonToggle(x + 180, y + 65, 20, 20, "", this.tile.locked, b -> {
            TSMNetwork.CHANNEL.sendToServer(new PacketLock(tile.getPos(), buttonLock.isActive(), Minecraft.getInstance().player.getGameProfile().getId()));
        }, I18n.format("tutorial.spotlight.config")));
    
        this.buttonRedstone = this.addButton(new ButtonToggle(x + 180, y + 90, 20, 20, "", this.tile.redstone, b -> {
            tile.redstone = buttonRedstone.isActive();
        }, I18n.format("tutorial.spotlight.redstone")));

        this.addButton(new Button(x + 180, y + 115, 20, 20, "", b -> {
            Minecraft.getInstance().displayGuiScreen(new GuiSpotLightConfig(container, invPlayer, title));
        }));
        this.addButton(new ButtonToggleHelp(x + 180, y + 140, 20, 20, tile));
        this.buttonTextures.active = !this.tile.timelineEnabled && this.buttonMode.isActive();
    }

    @Override
    public void removed() {
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos(), TSMJsonManager.getDataFromTile(this.tile).toString()));
        super.removed();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialRenderTick) {
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        super.render(mouseX, mouseY, partialRenderTick);
        Minecraft.getInstance().getTextureManager().bindTexture(tsmIcons);
        if (!this.buttonRedstone.isActive()) {
            GlStateManager.color3f(0.3F, 0.2F, 0.2F);
        }
        this.blit(x + 184, y + 94, 6, 104, 12, 11);
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        int i = 0;
        if (this.buttonLock.isActive()) {
            i = 15;
        }
        this.blit(x + 183, y + 68, 36 + i, 104, 14, 13);
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        this.blit(x + 183, y + 118, 20, 104, 13, 13);

        if (this.tile.helpMode) {
			this.buttons.stream().filter(b -> b.isMouseOver(mouseX, mouseY) && b instanceof IHelpButton).findFirst().ifPresent(b -> {
				this.renderTooltip(this.font.listFormattedStringToWidth(TextFormatting.GREEN + ((IHelpButton)b).getHelpMessage(), (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
			});
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        this.blit(x, y, 0, 0, this.xSize, this.ySize);
        this.font.drawString(I18n.format("container.spotlight.desc", "").replace("-", ""), x + 6, y + 7, 4210752);
    }
}
