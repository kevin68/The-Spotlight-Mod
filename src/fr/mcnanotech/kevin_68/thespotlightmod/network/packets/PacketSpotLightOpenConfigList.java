package fr.mcnanotech.kevin_68.thespotlightmod.network.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.Constants.NBT;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightLoadConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.BaseListEntry;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.ConfigEntry;
import fr.minecraftforgefrance.ffmtlibs.network.FFMTPacket;

public class PacketSpotLightOpenConfigList extends FFMTPacket
{
    public int x, y, z;
    public ArrayList<BaseListEntry> list;

    public PacketSpotLightOpenConfigList()
    {

    }

    public PacketSpotLightOpenConfigList(int x, int y, int z, ArrayList<BaseListEntry> list)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.list = list;
    }

    @Override
    public int getDiscriminator()
    {
        return 6;
    }

    @Override
    public void writeData(ByteBuf buffer) throws IOException
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        if(list != null)
        {
            buffer.writeBoolean(true);
            buffer.writeInt(list.size());
            for(int i = 0; i < list.size(); i++)
            {
                ByteBufUtils.writeUTF8String(buffer, ((ConfigEntry)list.get(i)).getName());
                buffer.writeInt(((ConfigEntry)list.get(i)).getTxtColor());
                buffer.writeInt(((ConfigEntry)list.get(i)).getId());
            }
        }
        else
        {
            buffer.writeBoolean(false);
        }
    }

    @Override
    public void readData(ByteBuf buffer)
    {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        if(buffer.readBoolean())
        {
            ArrayList<BaseListEntry> tempList = new ArrayList<BaseListEntry>();
            int size = buffer.readInt();
            for(int i = 0; i < size; i++)
            {
                String name = ByteBufUtils.readUTF8String(buffer);
                int color = buffer.readInt();
                int id = buffer.readInt();
                tempList.add(new ConfigEntry(name, id, color));
            }
            list = tempList;
        }
        else
        {
            list = null;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer player)
    {
        TileEntitySpotLight te = (TileEntitySpotLight)player.worldObj.getTileEntity(x, y, z);
        if(!list.isEmpty())
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiSpotLightLoadConfig(player.inventory, te, player.worldObj, list));
        }
        else
        {
            player.addChatMessage(new ChatComponentTranslation("container.spotlight.noconfig"));
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        TileEntitySpotLight te = (TileEntitySpotLight)player.worldObj.getTileEntity(x, y, z);
        ArrayList<BaseListEntry> templist = new ArrayList<BaseListEntry>();

        ItemStack stack = te.getStackInSlot(0).copy();
        if(stack.hasTagCompound())
        {
            System.out.println("Tag");
            if(stack.getTagCompound().hasKey("TSMConfigs"))
            {
                System.out.println("TSMConfigs");
                NBTTagList tagList = stack.getTagCompound().getTagList("TSMConfigs", NBT.TAG_LIST);
                System.out.println(tagList.tagCount());
                System.out.println(tagList.getCompoundTagAt(0).getString("ConfigName"));
                for(int i = 0; i < tagList.tagCount(); i++)
                {
                    System.out.println(tagList.getCompoundTagAt(i).getString("ConfigName"));
                    templist.add(new ConfigEntry(tagList.getCompoundTagAt(i).getString("ConfigName"), i + 1, (tagList.getCompoundTagAt(i).getByte("Red") & 0xFF * 65536) + (tagList.getCompoundTagAt(i).getByte("Green") & 0xFF * 256) + (tagList.getCompoundTagAt(i).getByte("Blue") & 0xFF)));
                }
            }
        }
        if(!templist.isEmpty())
        {
            PacketSender.sendSpotLightPacketConfig(te, false, templist, (EntityPlayerMP)player);
        }
        else
        {
            player.addChatMessage(new ChatComponentTranslation("container.spotlight.noconfig"));
        }
    }
}