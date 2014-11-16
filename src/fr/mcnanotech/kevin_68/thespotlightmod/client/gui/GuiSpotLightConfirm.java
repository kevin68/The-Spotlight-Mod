package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.SpotLightEntry;

public class GuiSpotLightConfirm extends GuiContainer
{
    protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlightconfirm.png");

    private String action;
    private String yes;
    private String no;
    private int guiopen;
    protected InventoryPlayer invPlayer;
    protected TileEntitySpotLight tileSpotLight;
    protected World world;

    public GuiSpotLightConfirm(TileEntitySpotLight tile, InventoryPlayer invplay, World world, String actionname, String yesbutton, String nobutton, int guiid)
    {
        super(new ContainerSpotLight(tile, invplay, world, 8));
        action = actionname;
        yes = yesbutton;
        no = nobutton;
        guiopen = guiid;
        invPlayer = invplay;
        tileSpotLight = tile;
        this.world = world;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.buttonList.add(new GuiButton(1, x + 13, y + 90, 150, 20, yes));
        this.buttonList.add(new GuiButton(2, x + 13, y + 115, 150, 20, no));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 1)
        {
            if(guiopen == 0)
            {
                SpotLightEntry entry = new SpotLightEntry(false, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, 0, (byte)0, false, false, (byte)0, true, (byte)0, false, (byte)0, (byte)0, 0, false, (byte)0, (byte)0, (byte)0, 0, false, false, (byte)0, (byte)0, (byte)0);
                PacketSender.sendSpotLightPacket(tileSpotLight, tileSpotLight.getLastKeySelected() & 0xFF, entry);
                this.tileSpotLight.setKey(tileSpotLight.getLastKeySelected() & 0xFF, entry);
                this.mc.displayGuiScreen(new GuiSpotLightTimeLine(invPlayer, tileSpotLight, world));
            }
            else
            {
                createKey(tileSpotLight.getCreateKeyTime() & 0xFF);
                this.mc.displayGuiScreen(new GuiSpotLightCreateKey(invPlayer, tileSpotLight, world));
            }
        }
        else
        {
            if(guiopen == 0)
            {
                this.mc.displayGuiScreen(new GuiSpotLightTimeLine(invPlayer, tileSpotLight, world));
            }
            else
            {
                this.mc.displayGuiScreen(new GuiSpotLightCreateKey(invPlayer, tileSpotLight, world));
            }
        }
    }

    public void createKey(int time)
    {
        SpotLightEntry entry = new SpotLightEntry(true, tileSpotLight.getRed(), tileSpotLight.getGreen(), tileSpotLight.getBlue(), tileSpotLight.getSecRed(), tileSpotLight.getSecGreen(), tileSpotLight.getSecBlue(), tileSpotLight.getAngle1(), tileSpotLight.getAngle2(), tileSpotLight.isAutoRotate(), tileSpotLight.isReverseRotation(), tileSpotLight.getRotationSpeed(), tileSpotLight.isSecondaryLaser(), tileSpotLight.getDisplayAxe(), tileSpotLight.isSideLaser(), tileSpotLight.getMainLaserSize(), tileSpotLight.getSecLaserSize(), tileSpotLight.getLaserHeight(), tileSpotLight.isTextEnabled(), tileSpotLight.getTxtRed(), tileSpotLight.getTxtGreen(), tileSpotLight.getTxtBlue(), tileSpotLight.getTxtAngle1(), tileSpotLight.isTxtAutoRotate(), tileSpotLight.isTxtReverseRotation(), tileSpotLight.getTxtRotationSpeed(), tileSpotLight.getTxtScale(), tileSpotLight.getTxtHeight());
        PacketSender.sendSpotLightPacket(tileSpotLight, time, entry);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.mc.renderEngine.bindTexture(texture);
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        this.drawCenteredString(this.fontRendererObj, action, width / 2, y + 10, 0xff0000);
    }
}