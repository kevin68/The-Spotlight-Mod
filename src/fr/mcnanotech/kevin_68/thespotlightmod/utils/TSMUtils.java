package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import java.math.BigDecimal;
import java.util.ArrayList;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.objs.TSMKey;
import net.minecraft.client.gui.FontRenderer;

public class TSMUtils
{
    public static ArrayList<String> formatedText(FontRenderer font, String str, int mouseX, int width, boolean reversed)
    {
        int maxSize = reversed ? mouseX - 70 : width - mouseX - 70;
        ArrayList<String> list = new ArrayList<String>();
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

    public static TSMKey createKey(short time, TileEntitySpotLight tile)
    {
        return new TSMKey(time, tile.cloneProperties());
    }

    public static float round(float d, int decimalPlace)
    {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_DOWN);
        return bd.floatValue();
    }
}
