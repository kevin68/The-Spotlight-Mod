package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightBeamAngles;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightBeamProperties;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightBeamColor;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightBeamTextures;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightTextAngles;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightTextColor;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightTextProperties;
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
        if(gui instanceof GuiSpotLightTextColor)
        {
            ArrayList list = new ArrayList();
            boolean flag = mouseX >= ((GuiSpotLightTextColor)gui).textField.xPosition && mouseX < ((GuiSpotLightTextColor)gui).textField.xPosition + ((GuiSpotLightTextColor)gui).textField.width && mouseY >= ((GuiSpotLightTextColor)gui).textField.yPosition && mouseY < ((GuiSpotLightTextColor)gui).textField.yPosition + ((GuiSpotLightTextColor)gui).textField.height;

            if(flag)
            {
                list = formatedText(font, I18n.format("tutorial.spotlight.textcolors.text"), mouseX, width, mouseX > width / 2);
            }
            GuiHelper.drawHoveringText(list, mouseX, mouseY, font, mouseX > width / 2 ? 0 : 200000, mouseY, 0x00ff00);
        }

        if(gui instanceof GuiSpotLightBeamTextures)
        {
            GuiSpotLightBeamTextures g = (GuiSpotLightBeamTextures)gui;
            Slot s = g.getSlotUnderMouse();
            if(s != null)
            {
                ArrayList list = new ArrayList();
                switch(s.slotNumber)
                {
                case 0:
                {
                    list = formatedText(font, I18n.format("tutorial.spotlight.textures.main"), mouseX, width, mouseX > width / 2);
                    break;
                }
                case 1:
                {
                    list = formatedText(font, I18n.format("tutorial.spotlight.textures.sec"), mouseX, width, mouseX > width / 2);
                    break;
                }
                }
                GuiHelper.drawHoveringText(list, mouseX, mouseY, font, mouseX > width / 2 ? 0 : 200000, mouseY, 0x00ff00);
                return;
            }
        }
        else if(gui instanceof GuiSpotLightConfig)
        {
            GuiSpotLightConfig g = (GuiSpotLightConfig)gui;
            Slot s = g.getSlotUnderMouse();
            if(s != null)
            {
                ArrayList list = new ArrayList();
                switch(s.slotNumber)
                {
                case 0:
                {
                    list = formatedText(font, I18n.format("tutorial.spotlight.config.tosave"), mouseX, width, mouseX > width / 2);
                    break;
                }
                case 1:
                {
                    list = formatedText(font, I18n.format("tutorial.spotlight.config.fromsave"), mouseX, width, mouseX > width / 2);
                    break;
                }
                case 2:
                {
                    list = formatedText(font, I18n.format("tutorial.spotlight.config.toload"), mouseX, width, mouseX > width / 2);
                    break;
                }
                case 3:
                {
                    list = formatedText(font, I18n.format("tutorial.spotlight.config.fromload"), mouseX, width, mouseX > width / 2);
                    break;
                }
                case 4:
                {
                    list = formatedText(font, I18n.format("tutorial.spotlight.config.toclean"), mouseX, width, mouseX > width / 2);
                    break;
                }
                case 5:
                {
                    list = formatedText(font, I18n.format("tutorial.spotlight.config.fromclean"), mouseX, width, mouseX > width / 2);
                    break;
                }
                }
                GuiHelper.drawHoveringText(list, mouseX, mouseY, font, mouseX > width / 2 ? 0 : 200000, mouseY, 0x00ff00);
                return;
            }
        }

        for(int i = 0; i < buttons.size(); i++)
        {
            GuiButton button = (GuiButton)buttons.get(i);
            if(button.isMouseOver())
            {
                ArrayList list = new ArrayList();
                if(gui instanceof GuiSpotLight)
                {
                    boolean isBeam = ((GuiSpotLight)gui).tile.isBeam;
                    switch(button.id)
                    {
                    case 0:
                    {
                        list = formatedText(font, isBeam ? I18n.format("tutorial.spotlight.colors") : I18n.format("tutorial.spotlight.textcolors"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 1:
                    {
                        list = formatedText(font, isBeam ? I18n.format("tutorial.spotlight.beamangle") : I18n.format("tutorial.spotlight.textangle"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 2:
                    {
                        list = formatedText(font, isBeam ? I18n.format("tutorial.spotlight.beamprops") : I18n.format("tutorial.spotlight.textprops"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 3:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.texture"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 4:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.mode"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 18:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.redstone"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 19:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.config"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 20:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.help"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    }
                }
                else if(gui instanceof GuiSpotLightBeamColor)
                {
                    switch(button.id)
                    {
                    case 0:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.colors.red"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 1:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.colors.green"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 2:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.colors.blue"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 3:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.colors.secred"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 4:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.colors.secgreen"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 5:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.colors.secblue"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 19:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.back"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 20:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.help"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    }
                }
                else if(gui instanceof GuiSpotLightBeamAngles)
                {
                    switch(button.id)
                    {
                    case 0:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.angles.x"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 1:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.angles.y"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 2:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.angles.z"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 3:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.angles.angle"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 4:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.angles.autorotate"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 5:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.angles.reverserotation"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 6:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.angles.rotationspeed"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 19:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.back"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 20:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.help"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    }

                }
                else if(gui instanceof GuiSpotLightBeamProperties)
                {
                    switch(button.id)
                    {
                    case 0:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.beamprops.mainsize"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 1:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.beamprops.secsize"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 2:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.beamprops.secbeam"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 3:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.beamprops.double"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 4:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.beamprops.height"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 5:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.beamprops.sides"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 19:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.back"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 20:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.help"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    }
                }
                else if(gui instanceof GuiSpotLightBeamTextures)
                {
                    switch(button.id)
                    {
                    case 19:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.back"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 20:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.help"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    }
                }
                else if(gui instanceof GuiSpotLightConfig)
                {
                    switch(button.id)
                    {
                    case 19:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.back"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 20:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.help"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    }
                }
                else if(gui instanceof GuiSpotLightTextColor)
                {
                    switch(button.id)
                    {
                    case 0:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.textcolors.red"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 1:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.textcolors.green"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 2:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.textcolors.blue"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 19:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.back"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 20:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.help"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    }
                }
                else if(gui instanceof GuiSpotLightTextAngles)
                {
                    switch(button.id)
                    {
                    case 3:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.textangles.angle"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 4:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.textangles.autorotate"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 5:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.textangles.reverserotation"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 6:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.textangles.rotationspeed"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 19:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.back"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 20:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.help"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    }
                }
                else if (gui instanceof GuiSpotLightTextProperties)
                {
                    switch(button.id)
                    {
                    case 0:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.textprops.height"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    case 1:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.textprops.scale"), mouseX, width, mouseX > width / 2);
                        break;
                    }
                    
                    case 19:
                    {
                        list = formatedText(font, I18n.format("tutorial.spotlight.back"), mouseX, width, mouseX > width / 2);
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
    
    public static TSMKey createKey(short time, TileEntitySpotLight tile)
    {
        return new TSMKey(time, tile.beamRed, tile.beamGreen, tile.beamBlue);//TODO fill
    }
}
