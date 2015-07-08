package fr.mcnanotech.kevin_68.thespotlightmod.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.BaseListEntry;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;
import fr.minecraftforgefrance.ffmtlibs.entity.EggColor;

public class GuiList
{
    private ArrayList<ButtonEntry[]> arrList = new ArrayList();
    private int xS, yS, xE, yE, currentPageDiplayed, height, heightWithoutButton, numberOfLine, numberOfPage, width;
    private GuiButton next, prev;
    private ArrayList<BaseListEntry> list;
    private GuiListBase gui;
    private GuiButtonList lastSelected;

    public GuiList(GuiListBase gui, ArrayList<BaseListEntry> list, int xStart, int yStart, int xEnd, int yEnd)
    {
        this.gui = gui;
        this.list = list;
        this.xS = xStart;
        this.yS = yStart;
        this.xE = xEnd;
        this.yE = yEnd;

        this.height = yEnd - yStart;
        this.heightWithoutButton = this.height - 22;
        this.numberOfLine = (this.heightWithoutButton - (this.heightWithoutButton % 15)) / 15;
        this.width = xEnd - xStart;

        int l = 0;
        int currentPage = 0;
        ButtonEntry[] entry = new ButtonEntry[this.numberOfLine + 1];
        for(int k = 0; k < list.size(); k++)
        {
            int color = list.get(k).getTxtColor();
            if(l < this.numberOfLine)
            {
                entry[l] = new ButtonEntry(k + 5, list.get(k).getName(), xStart, yStart + (15 * l), (this.width / 2) - 2, 14, xStart + 2 + (this.width / 2), color);
                l++;
            }
            else
            {
                this.arrList.add(currentPage, entry);
                entry = new ButtonEntry[this.numberOfLine];
                currentPage++;
                l = 0;
                entry[l] = new ButtonEntry(k + 5, list.get(k).getName(), xStart, yStart + (15 * l), (this.width / 2) - 2, 14, xStart + 2 + (this.width / 2), color);
                l++;
            }

            if(k == list.size() - 1)
            {
                this.arrList.add(currentPage, entry);
            }
        }

        this.numberOfPage = this.arrList.size() - 1;
    }

    protected void actionPerformed(GuiButton guibutton, List buttonList)
    {
        switch(guibutton.id)
        {
            case 3:
            {
                if(this.currentPageDiplayed != this.numberOfPage)
                {
                    this.currentPageDiplayed += 1;
                }
                this.updatePage();
                this.updateList(buttonList);
                break;
            }
            case 4:
            {
                if(this.currentPageDiplayed != 0)
                {
                    this.currentPageDiplayed += -1;
                }
                this.updatePage();
                this.updateList(buttonList);
                break;
            }
            default:
            {
                if(this.lastSelected != null)
                {
                    this.lastSelected.selected = false;
                }
                this.gui.setSelected(this.list.get(guibutton.id - 5));
                if(guibutton instanceof GuiButtonList)
                {
                    GuiButtonList button = (GuiButtonList)guibutton;
                    this.lastSelected = button;
                    button.selected = true;
                }
            }
        }
    }

    public void drawScreen(int x, int y)
    {
        GuiHelper.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, (this.currentPageDiplayed + 1) + "/" + (this.numberOfPage + 1), x + (this.width / 2) + 6, y + (this.height) + 5, EggColor.GRAY);
    }

    public void addButton(List buttonList)
    {
        buttonList.add(3, this.next = new GuiButton(2, this.xE - 20, this.yE - 20, 20, 20, ">"));
        buttonList.add(4, this.prev = new GuiButton(3, this.xS, this.yE - 20, 20, 20, "<"));
        this.updatePage();
        this.updateList(buttonList);
    }

    public void updatePage()
    {
        if(this.currentPageDiplayed == 0)
        {
            this.prev.enabled = false;
            if(this.numberOfPage < 2)
            {
                this.next.enabled = false;
            }
            else
            {
                this.next.enabled = true;
            }
        }
        else
        {
            this.prev.enabled = true;
            if(this.currentPageDiplayed == this.numberOfPage)
            {
                this.next.enabled = false;
            }
            else
            {
                this.next.enabled = true;
            }
        }
    }

    public void updateList(List buttonList)
    {
        ButtonEntry[] entryLeft = this.arrList.get(this.currentPageDiplayed);
        this.clearList(buttonList);
        for(int i = 0; i < entryLeft.length; i++)
        {
            if(entryLeft[i] != null)
            {
                buttonList.add(new GuiButtonList(entryLeft[i], false));
            }
        }

        if(this.currentPageDiplayed < this.numberOfPage)
        {
            ButtonEntry[] entryRight = this.arrList.get(this.currentPageDiplayed + 1);
            for(int j = 0; j < entryRight.length; j++)
            {
                if(entryRight[j] != null)
                {
                    buttonList.add(new GuiButtonList(entryRight[j], true));
                }
            }
        }

        if(this.lastSelected != null)
        {
            for(int k = 0; k < buttonList.size(); k++)
            {
                if(buttonList.get(k) instanceof GuiButtonList)
                {
                    GuiButtonList button = (GuiButtonList)buttonList.get(k);
                    if(button.entry.getName() == this.lastSelected.entry.getName() && button.entry.x == this.lastSelected.entry.x && button.entry.xR == this.lastSelected.entry.xR && button.entry.y == this.lastSelected.entry.y)
                    {
                        button.selected = true;
                        this.lastSelected = button;
                    }
                }
            }
        }
    }

    public void clearList(List buttonList)
    {
        ArrayList keepList = new ArrayList();
        for(int i = 0; i < 5; i++)
        {
            keepList.add(i, buttonList.get(i));
        }
        buttonList.clear();
        for(int i = 0; i < 5; i++)
        {
            buttonList.add(i, keepList.get(i));
        }
    }

    public void setSelected(List buttonList, int id)
    {
        GuiButton guibutton = (GuiButton)buttonList.get(id);
        if(guibutton.id > 4)
        {
            if(this.lastSelected != null)
            {
                this.lastSelected.selected = false;
            }
            this.gui.setSelected(this.list.get(guibutton.id - 5));
            if(guibutton instanceof GuiButtonList)
            {
                GuiButtonList button = (GuiButtonList)guibutton;
                this.lastSelected = button;
                button.selected = true;
            }
            this.updateList(buttonList);
        }
    }

    public class ButtonEntry extends BaseListEntry
    {
        public int id, x, y, width, height, xR;

        public ButtonEntry(int id, String name, int x, int y, int width, int height, int xR, int color)
        {
            super(name, color);
            this.id = id;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.xR = xR;
        }
    }

    public class GuiButtonList extends GuiButton
    {
        private final ResourceLocation texture = new ResourceLocation(TheSpotLightMod.MODID.toLowerCase(), "textures/gui/icons.png");

        public boolean selected;
        public ButtonEntry entry;

        public GuiButtonList(ButtonEntry entry, boolean isRight)
        {
            super(entry.id, isRight ? entry.xR : entry.x, entry.y, entry.width, entry.height, entry.getName());
            this.entry = entry;
        }

        @Override
        public int getHoverState(boolean p_146114_1_)
        {
            byte b0 = 1;

            if(!this.enabled)
            {
                b0 = 0;
            }
            else if(p_146114_1_)
            {
                b0 = 2;
            }
            else if(this.selected)
            {
                b0 = 3;
            }

            return b0;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY)
        {
            if(this.visible)
            {
                FontRenderer fontrenderer = mc.fontRendererObj;
                mc.getTextureManager().bindTexture(this.texture);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int k = this.getHoverState(this.hovered);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 2 + k * 14, this.width / 2, this.height);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 2 + k * 14, this.width / 2, this.height);
                this.mouseDragged(mc, mouseX, mouseY);
                String txt;
                if(this.displayString.length() * 5 < this.width)
                {
                    txt = this.displayString;
                }
                else
                {
                    txt = String.valueOf(this.displayString.subSequence(0, this.width / 4 - 10)) + "...";
                }
                this.drawCenteredString(fontrenderer, txt, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, this.entry.getTxtColor());

                if(k == 2 && !(this.displayString.length() * 5 < this.width))
                {
                    this.renderOverlayText(mouseX, mouseY, this.displayString, this.entry.getTxtColor());
                }
            }
        }

        protected void renderOverlayText(int mouseX, int mouseY, String txt, int color)
        {
            ArrayList<String> list = new ArrayList();
            list.add(0, txt);
            GuiHelper.drawHoveringText(list, mouseX, mouseY, Minecraft.getMinecraft().fontRendererObj, 166, 176, color);
        }
    }
}