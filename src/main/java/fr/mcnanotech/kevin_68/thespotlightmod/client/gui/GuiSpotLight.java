package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketLock;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class GuiSpotLight extends GuiContainer {
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");
    protected static final ResourceLocation tsmIcons = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/icons.png");

    public InventoryPlayer invPlayer;
    public TileEntitySpotLight tile;
    public World world;
    public GuiButton buttonTextures, buttonColors, buttonAngle, buttonBeamSpecs;
    public GuiBooleanButton buttonMode, buttonHelp, buttonRedstone, buttonLock;

    public GuiSpotLight(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World world, Container container) {
        super(container);
        ((ContainerSpotLight)container).resetSlot();
        this.invPlayer = playerInventory;
        this.tile = tileEntity;
        this.world = world;
    }
    
    public GuiSpotLight(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World world) {
        this(playerInventory, tileEntity, world, new ContainerSpotLight(tileEntity, playerInventory));
    }

    @Override
    public void initGui() {
        super.initGui();
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        this.buttonColors = this.addButton(new GuiButton(0, x + 5, y + 20, 80, 20, this.tile.isBeam ? I18n.format("container.spotlight.color") : I18n.format("container.spotlight.textcolor")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                if (buttonMode.isActive()) {
                    mc.displayGuiScreen(new GuiSpotLightBeamColor(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
                } else {
                    mc.displayGuiScreen(new GuiSpotLightTextColor(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
                }
            }
        });
        this.buttonColors.enabled = !this.tile.timelineEnabled;

        this.buttonAngle = this.addButton(new GuiButton(1, x + 5, y + 43, 80, 20, I18n.format("container.spotlight.angle")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                if (buttonMode.isActive()) {
                    mc.displayGuiScreen(new GuiSpotLightBeamAngles(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
                } else {
                    mc.displayGuiScreen(new GuiSpotLightTextAngles(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
                }
            }
        });
        this.buttonAngle.enabled = !this.tile.timelineEnabled;

        this.buttonBeamSpecs = this.addButton(new GuiButton(2, x + 5, y + 66, 80, 20, I18n.format("container.spotlight.beamspecs")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                if (buttonMode.isActive()) {
                    mc.displayGuiScreen(new GuiSpotLightBeamProperties(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
                } else {
                    mc.displayGuiScreen(new GuiSpotLightTextProperties(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
                }
            }
        });
        this.buttonBeamSpecs.enabled = !this.tile.timelineEnabled;

        this.buttonTextures = this.addButton(new GuiButton(3, x + 5, y + 89, 80, 20, I18n.format("container.spotlight.textures")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(new GuiSpotLightBeamTextures(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
            }
        });
        this.buttonMode = this.addButton(new GuiBooleanButton(4, x + 5, y + 112, 80, 20, "", this.tile.isBeam) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                buttonMode.toggle();
                tile.isBeam = buttonMode.isActive();
                buttonTextures.enabled = buttonMode.isActive();
                buttonColors.displayString = tile.isBeam ? I18n.format("container.spotlight.color") : I18n.format("container.spotlight.textcolor");
            }
        });
        this.buttonMode.enabled = !this.tile.timelineEnabled;
        this.buttonMode.setTexts(I18n.format("container.spotlight.modebeam"), I18n.format("container.spotlight.modetext"));
        this.buttonMode.shouldNotChangeTextColor(true);
        this.buttonMode.shouldChangeTextureOnToggle(false);

        this.addButton(new GuiButton(5, x + 90, y + 20, 80, 20, I18n.format("container.spotlight.timeline")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(new GuiSpotlightTimeline(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
            }
        });
        this.addButton(this.buttonLock = new GuiBooleanButton(17, x + 180, y + 65, 20, 20, "", this.tile.locked) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                TSMNetwork.CHANNEL.sendToServer(new PacketLock(tile.getPos(), buttonLock.isActive(), mc.player.getGameProfile().getId()));
            }
        });
        this.addButton(this.buttonRedstone = new GuiBooleanButton(18, x + 180, y + 90, 20, 20, "", this.tile.redstone) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                buttonRedstone.toggle();
                tile.redstone = buttonRedstone.isActive();
            }
        });
        this.addButton(new GuiButton(19, x + 180, y + 115, 20, 20, "") {
            @Override
            public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(new GuiSpotLightConfig(invPlayer, tile, world, (ContainerSpotLight)inventorySlots));
            }
        });
        this.addButton(this.buttonHelp = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", this.tile.helpMode) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                buttonHelp.toggle();
                tile.helpMode = buttonHelp.isActive();
            }
        });
        this.buttonTextures.enabled = !this.tile.timelineEnabled && this.buttonMode.isActive();
    }

    @Override
    public void onGuiClosed() {
        TSMNetwork.CHANNEL.sendToServer(new PacketUpdateData(this.tile.getPos(), TSMJsonManager.getDataFromTile(this.tile).toString()));
        super.onGuiClosed();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialRenderTick) {
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        super.render(mouseX, mouseY, partialRenderTick);
        this.mc.getTextureManager().bindTexture(tsmIcons);
        if (!this.buttonRedstone.isActive()) {
            GlStateManager.color3f(0.3F, 0.2F, 0.2F);
        }
        this.drawTexturedModalRect(x + 184, y + 94, 6, 104, 12, 11);
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        int i = 0;
        if (this.buttonLock.isActive()) {
            i = 15;
        }
        this.drawTexturedModalRect(x + 183, y + 68, 36 + i, 104, 14, 13);
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(x + 183, y + 118, 20, 104, 13, 13);
        if (this.buttonHelp.isActive()) {
            for (GuiButton button : this.buttons) {
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
                        this.drawHoveringText(this.fontRenderer.listFormattedStringToWidth(TextFormatting.GREEN + text, (mouseX > width / 2 ? mouseX : this.width - mouseX)), mouseX, mouseY);
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
        this.mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        this.fontRenderer.drawString(I18n.format("container.spotlight.desc", "").replace("-", ""), x + 6, y + 7, 4210752);
    }
}
