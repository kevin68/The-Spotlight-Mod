package fr.mcnanotech.kevin_68.thespotlightmod;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemConfigSaver extends Item
{
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advItemToolTip)
    {
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("TSMConfigs"))
            {
                list.add(I18n.format("item.configsaver.info"));
                for(int i = 0; i < stack.getTagCompound().getTagList("TSMConfigs", NBT.TAG_COMPOUND).tagCount(); i++)
                {
                    list.add("- " + stack.getTagCompound().getTagList("TSMConfigs", NBT.TAG_COMPOUND).getCompoundTagAt(i).getString("ConfigName"));
                }
            }
        }
    }
}