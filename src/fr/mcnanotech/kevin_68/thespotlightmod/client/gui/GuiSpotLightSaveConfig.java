package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiBooleanButton;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;

public class GuiSpotLightSaveConfig extends GuiContainer
{
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");

	public InventoryPlayer invPlayer;
	public TileEntitySpotLight tileSpotLight;
	public World world;
	public GuiTextField txtField;
	private GuiBooleanButton helpButton;
	private GuiButton createButton;

	public GuiSpotLightSaveConfig(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
	{
		super(new ContainerSpotLight(tileEntity, playerInventory, wrld, 8));
		this.invPlayer = playerInventory;
		this.tileSpotLight = tileEntity;
		this.world = wrld;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		Keyboard.enableRepeatEvents(true);
		this.txtField = new GuiTextField(2, this.fontRendererObj, x + 5, y + 20, 166, 12);
		this.txtField.setEnableBackgroundDrawing(true);
		this.txtField.setMaxStringLength(40);
		this.txtField.setEnabled(true);

		this.buttonList.add(new GuiButton(0, x + 5, y + 112, 80, 20, I18n.format("container.spotlight.back")));
		this.buttonList.add(this.createButton = new GuiButton(1, x + 90, y + 112, 80, 20, I18n.format("container.spotlight.create")));
		this.buttonList.add(this.helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
		this.createButton.enabled = false;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 0:
		{
			this.mc.displayGuiScreen(new GuiSpotLightConfigs(this.invPlayer, this.tileSpotLight, this.world));
			break;
		}
		case 1:
		{
			if(this.createButton.enabled)
			{
//				PacketSender.send(EnumLaserInformations.COMMANDCREATECONFIG, this.txtField.getText());
				this.mc.displayGuiScreen(new GuiSpotLightConfigs(this.invPlayer, this.tileSpotLight, this.world));
			}
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
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		this.fontRendererObj.drawString(I18n.format("container.spotlight.desc", I18n.format("container.spotlight.save")), x + 5, y + 8, 4210752);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
	{
		super.drawScreen(mouseX, mouseY, partialRenderTick);
		GL11.glDisable(GL11.GL_LIGHTING);
		this.txtField.drawTextBox();

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		if(this.helpButton.isActive())
		{
			boolean reversed = mouseX > this.width / 2;
			ArrayList<String> list = new ArrayList<String>();

			if(mouseY > y + 20 && mouseY < y + 32 && mouseX > x + 5 && mouseX < x + 171)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.configs.save.name"), mouseX, this.width, reversed);
			}

			if(mouseY > y + 117 && mouseY < y + 137)
			{
				if(mouseX > x + 6 && mouseX < x + 84)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.back"), mouseX, this.width, reversed);
				}

				if(mouseX > x + 90 && mouseX < x + 170)
				{
					list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.configs.save.create"), mouseX, this.width, reversed);
				}
			}

			if(mouseX > x + 180 && mouseX < x + 200 && mouseY > y + 140 && mouseY < y + 160)
			{
				list = UtilSpotLight.formatedText(this.fontRendererObj, I18n.format("tutorial.spotlight.help"), mouseX, this.width, reversed);
			}

			if(list.size() > 0 && (list.get(list.size() - 1) == " " || list.get(list.size() - 1).isEmpty()))
			{
				list.remove(list.size() - 1);
			}
			GuiHelper.drawHoveringText(list, mouseX, mouseY, this.fontRendererObj, reversed ? 0 : 200000, this.height, 0x00ff00);
		}
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void keyTyped(char chr, int chrValue)
	{
		this.txtField.textboxKeyTyped(chr, chrValue);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.txtField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen()
	{
		this.createButton.enabled = this.txtField.getText().length() > 0;
	}

}