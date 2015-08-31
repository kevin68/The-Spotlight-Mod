package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLight;
import fr.minecraftforgefrance.ffmtlibs.client.gui.GuiHelper;

public class TSMUtils
{
    public static ArrayList formatedText(FontRenderer font, String str, int mouseX, int width, boolean reversed)
    {
        int maxSize = reversed ? mouseX - 70 : width - mouseX - 70;
        ArrayList list = new ArrayList();
        if(font.getStringWidth(str) >= maxSize)
        {
            String[] words = str.split(" ");
            String cutted = "";
            int i = 0;
            while(font.getStringWidth(cutted) <= maxSize)
            {
                cutted += words[i] + " ";
                i++;
                if(i >= words.length)
                {
                    break;
                }
            }
            if(font.getStringWidth(cutted) >= maxSize)
            {
                cutted.substring(0, cutted.length() - words[i - 1].length());
            }
            if(font.getStringWidth(cutted) >= maxSize)
            {
                if(i > 1)
                {
                    cutted.substring(0, cutted.length() - words[i - 2].length());
                }
            }
            list.add(cutted);
            String forNext = "";
            for(int j = i; j < words.length; j++)
            {
                forNext += words[j] + " ";
            }
            list.addAll(formatedText(font, forNext, mouseX, width, reversed));
        }
        else
        {
            list.add(str);
        }
        return list;
    }

    public static void drawTextHelper(FontRenderer font, int mouseX, int mouseY, int width, int height, List buttons, GuiScreen gui)
    {
        for(int i = 0; i < buttons.size(); i++)
        {
            GuiButton button = (GuiButton)buttons.get(i);
            if(button.isMouseOver())
            {
                ArrayList list = new ArrayList();
                if(gui instanceof GuiSpotLight)
                {
                    switch(button.id)
                    {
                    case 0:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.colors"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 1:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.TODO"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 2:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.TODO"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 20:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.help"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    }
                }
                GuiHelper.drawHoveringText(list, mouseX, mouseY, font, mouseX > width / 2 ? 0 : 200000, mouseY, 0x00ff00);
            }
        }
    }
}
