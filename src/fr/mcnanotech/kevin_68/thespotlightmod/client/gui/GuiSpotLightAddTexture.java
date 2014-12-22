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

public class GuiSpotLightAddTexture extends GuiContainer
{
	protected static final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID + ":textures/gui/spotlight.png");

	public InventoryPlayer invPlayer;
	public TileEntitySpotLight tileSpotLight;
	public World world;
	public GuiTextField nameField;
	public GuiTextField pathField;
	public GuiTextField delNameField;
	public String name, path, delname;
	public GuiBooleanButton helpButton;

	public GuiSpotLightAddTexture(InventoryPlayer playerInventory, TileEntitySpotLight tileEntity, World wrld)
	{
		super(new ContainerSpotLight(tileEntity, playerInventory, wrld, 8));
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

		Keyboard.enableRepeatEvents(true);
		nameField = new GuiTextField(3, fontRendererObj, x + 6, y + 20, 160, 12);
		nameField.setDisabledTextColour(-1);
		nameField.setEnableBackgroundDrawing(true);
		nameField.setMaxStringLength(40);
		nameField.setEnabled(true);
		nameField.setText("");

		pathField = new GuiTextField(4, fontRendererObj, x + 6, y + 50, 160, 12);
		pathField.setDisabledTextColour(-1);
		pathField.setEnableBackgroundDrawing(true);
		pathField.setMaxStringLength(60);
		pathField.setEnabled(true);
		pathField.setText("");

		delNameField = new GuiTextField(5, fontRendererObj, x + 6, y + 80, 160, 12);
		delNameField.setDisabledTextColour(-1);
		delNameField.setEnableBackgroundDrawing(true);
		delNameField.setMaxStringLength(60);
		delNameField.setEnabled(true);
		delNameField.setText("");

		buttonList.add(new GuiButton(0, x + 6, y + 117, 78, 20, I18n.format("container.spotlight.back")));
		buttonList.add(new GuiButton(1, x + 91, y + 95, 78, 20, I18n.format("container.spotlight.add")));
		buttonList.add(new GuiButton(2, x + 91, y + 117, 78, 20, I18n.format("container.spotlight.delete")));
		buttonList.add(helpButton = new GuiBooleanButton(20, x + 180, y + 140, 20, 20, "?", false));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		switch(guibutton.id)
		{
		case 0:
		{
			mc.displayGuiScreen(new GuiSpotLight(invPlayer, tileSpotLight, world));
			break;
		}
		case 1:
		{
			UtilSpotLight.setTextures(name, path);
			mc.displayGuiScreen(new GuiSpotLight(invPlayer, tileSpotLight, world));
			break;
		}
		case 2:
		{
			UtilSpotLight.deleteTexure(delname);
			break;
		}
		case 20:
		{
			helpButton.toggle();
			break;
		}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialRenderTick)
	{
		super.drawScreen(mouseX, mouseY, partialRenderTick);
		GL11.glDisable(GL11.GL_LIGHTING);
		nameField.drawTextBox();
		pathField.drawTextBox();
		delNameField.drawTextBox();
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawString(fontRendererObj, I18n.format("container.spotlight.texname") + " : ", x + 10, y + 10, 0xffffff);
		drawString(fontRendererObj, I18n.format("container.spotlight.texpath") + " : ", x + 10, y + 40, 0xffffff);
		drawString(fontRendererObj, I18n.format("container.spotlight.delname") + " : ", x + 10, y + 70, 0xffffff);
		if(helpButton.getIsActive())
		{
			boolean reversed = mouseX > width / 2;
			ArrayList<String> list = new ArrayList<String>();

			if(mouseX > x + 6 && mouseX < x + 166)
			{
				if(mouseY > y + 20 && mouseY < y + 32)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.more.name"), mouseX, width, reversed);
				}

				if(mouseY > y + 50 && mouseY < y + 62)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.more.path"), mouseX, width, reversed);
				}

				if(mouseY > y + 80 && mouseY < y + 92)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.more.delname"), mouseX, width, reversed);
				}
			}

			if(mouseY > y + 117 && mouseY < y + 137)
			{
				if(mouseX > x + 6 && mouseX < x + 84)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.back"), mouseX, width, reversed);
				}

				if(mouseX > x + 91 && mouseX < x + 169)
				{
					list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.more.delete"), mouseX, width, reversed);
				}
			}

			if(mouseX > x + 91 && mouseX < x + 169 && mouseY > y + 95 && mouseY < y + 115)
			{
				list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.more.add"), mouseX, width, reversed);
			}

			if(mouseX > x + 180 && mouseX < x + 200 && mouseY > y + 140 && mouseY < y + 160)
			{
				list = UtilSpotLight.formatedText(fontRendererObj, I18n.format("tutorial.spotlight.help"), mouseX, width, reversed);
			}

			if(list.size() > 0 && (list.get(list.size() - 1) == " " || list.get(list.size() - 1).isEmpty()))
			{
				list.remove(list.size() - 1);
			}
			GuiHelper.drawHoveringText(list, mouseX, mouseY, fontRendererObj, reversed ? 0 : 200000, height, 0x00ff00);
		}
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void keyTyped(char chr, int chrValue) throws IOException
	{
		if(nameField.textboxKeyTyped(chr, chrValue))
		{
			name = nameField.getText();
		}
		else if(pathField.textboxKeyTyped(chr, chrValue))
		{
			path = pathField.getText();
		}
		else if(delNameField.textboxKeyTyped(chr, chrValue))
		{
			delname = delNameField.getText();
		}
		else
		{
			super.keyTyped(chr, chrValue);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		nameField.mouseClicked(mouseX, mouseY, mouseButton);
		pathField.mouseClicked(mouseX, mouseY, mouseButton);
		delNameField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}