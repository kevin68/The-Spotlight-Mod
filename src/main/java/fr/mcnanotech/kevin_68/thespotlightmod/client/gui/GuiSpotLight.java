package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketLock;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class GuiSpotLight extends ContainerScreen<ContainerSpotLight> {
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/spotlight.png");
    protected static final ResourceLocation tsmIcons = new ResourceLocation(TheSpotLightMod.MOD_ID + ":textures/gui/icons.png");

    public PlayerInventory invPlayer;
    public TileEntitySpotLight tile;
    public Button buttonTextures, buttonColors, buttonAngle, buttonBeamSpecs;
    public ButtonToggle buttonMode, buttonHelp, buttonRedstone, buttonLock;


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

        this.buttonColors = this.addButton(new Button(x + 5, y + 20, 80, 20, this.tile.isBeam ? I18n.format("container.spotlight.color") : I18n.format("container.spotlight.textcolor"), b -> {
            if (buttonMode.isActive()) {
                Minecraft.getInstance().displayGuiScreen(new GuiSpotLightBeamColor(container, invPlayer, title));
            } else {
                Minecraft.getInstance().displayGuiScreen(new GuiSpotLightTextColor(container, invPlayer, title));
            }
        }));
        this.buttonColors.active = !this.tile.timelineEnabled;

        this.buttonAngle = this.addButton(new Button(x + 5, y + 43, 80, 20, I18n.format("container.spotlight.angle"), b -> {
            if (buttonMode.isActive()) {
                Minecraft.getInstance().displayGuiScreen(new GuiSpotLightBeamAngles(container, invPlayer, title));
            } else {
                Minecraft.getInstance().displayGuiScreen(new GuiSpotLightTextAngles(container, invPlayer, title));
            }
        }));
        this.buttonAngle.active = !this.tile.timelineEnabled;

        this.buttonBeamSpecs = this.addButton(new Button(x + 5, y + 66, 80, 20, I18n.format("container.spotlight.beamspecs"), b -> {
            if (buttonMode.isActive()) {
                Minecraft.getInstance().displayGuiScreen(new GuiSpotLightBeamProperties(container, invPlayer, title));
            } else {
                Minecraft.getInstance().displayGuiScreen(new GuiSpotLightTextProperties(container, invPlayer, title));
            }
        }));
        this.buttonBeamSpecs.active = !this.tile.timelineEnabled;

        this.buttonTextures = this.addButton(new Button(x + 5, y + 89, 80, 20, I18n.format("container.spotlight.textures"), b -> {
            Minecraft.getInstance().displayGuiScreen(new GuiSpotLightBeamTextures(container, invPlayer, title));
        }));
        this.buttonMode = this.addButton(new ButtonToggle(x + 5, y + 112, 80, 20, "", this.tile.isBeam, b -> {
            // buttonMode.toggle();
            tile.isBeam = buttonMode.isActive();
            buttonTextures.active = buttonMode.isActive();
            buttonColors.setMessage(tile.isBeam ? I18n.format("container.spotlight.color") : I18n.format("container.spotlight.textcolor"));
        }));
        this.buttonMode.enabled = !this.tile.timelineEnabled;
        this.buttonMode.setTexts(I18n.format("container.spotlight.modebeam"), I18n.format("container.spotlight.modetext"));
        this.buttonMode.shouldNotChangeTextColor(true);
        this.buttonMode.shouldChangeTextureOnToggle(false);

        this.addButton(new Button(x + 90, y + 20, 80, 20, I18n.format("container.spotlight.timeline"), b -> {
            Minecraft.getInstance().displayGuiScreen(new GuiSpotlightTimeline(container, invPlayer, title));
        }));
        this.addButton(this.buttonLock = new ButtonToggle(x + 180, y + 65, 20, 20, "", this.tile.locked, b -> {
            TSMNetwork.CHANNEL.sendToServer(new PacketLock(tile.getPos(), buttonLock.isActive(), Minecraft.getInstance().player.getGameProfile().getId()));
        }));
        this.addButton(this.buttonRedstone = new ButtonToggle(x + 180, y + 90, 20, 20, "", this.tile.redstone, b -> {
            // buttonRedstone.toggle();
            tile.redstone = buttonRedstone.isActive();
        }));
        this.addButton(new Button(x + 180, y + 115, 20, 20, "", b -> {
            Minecraft.getInstance().displayGuiScreen(new GuiSpotLightConfig(container, invPlayer, title));
        }));
        this.addButton(this.buttonHelp = new ButtonToggle(x + 180, y + 140, 20, 20, "?", this.tile.helpMode, b -> {
            buttonHelp.toggle();
            tile.helpMode = buttonHelp.isActive();
        }));
        this.buttonTextures.enabled = !this.tile.timelineEnabled && this.buttonMode.isActive();
    }

    @Override
    public void onClose() {
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos(), TSMJsonManager.getDataFromTile(this.tile).toString()));
        super.onClose();
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
        if (this.buttonHelp.isActive()) {
            for (Button button : this.buttons) {
                if (button.isMouseOver()) {
                    String text = "";
                    boolean isBeam = this.tile.isBeam;
                    switch (button.id) {
                    case 0:
                        text = isBeam ? I18n.format("tutorial.spotlight.colors") : I18n.format("tutorial.spotlight.textcolors");
                        break;
                    case 1:
                        text = isBeam ? I18n.format("tutorial.spotlight.beamangle") : I18n.format("tutorial.spotlight.textangle");
                        break;
                    case 2:
                        text = isBeam ? I18n.format("tutorial.spotlight.beamprops") : I18n.format("tutorial.spotlight.textprops");
                        break;
                    case 3:
                        text = I18n.format("tutorial.spotlight.texture");
                        break;
                    case 4:
                        text = I18n.format("tutorial.spotlight.mode");
                        break;
                    case 5:
                        text = I18n.format("tutorial.spotlight.timeline");
                        break;
                    case 18:
                        text = I18n.format("tutorial.spotlight.redstone");
                        break;
                    case 19:
                        text = I18n.format("tutorial.spotlight.config");
                        break;
                    case 20:
                        text = I18n.format("tutorial.spotlight.help");
                        break;
                    }
                    if (!text.isEmpty()) {
                        this.drawHoveringText(this.font.listFormattedStringToWidth(TextFormatting.GREEN + text, (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
                    }
                }
            }
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
