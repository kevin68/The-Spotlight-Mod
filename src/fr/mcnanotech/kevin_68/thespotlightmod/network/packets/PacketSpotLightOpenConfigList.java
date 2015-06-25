package fr.mcnanotech.kevin_68.thespotlightmod.network.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightDeleteConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLightLoadConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.BaseListEntry;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.ConfigEntry;
import fr.minecraftforgefrance.ffmtlibs.network.FFMTPacket;

public class PacketSpotLightOpenConfigList implements IMessage
{
    public int x, y, z, type;
    public ArrayList<BaseListEntry> list;

    public PacketSpotLightOpenConfigList()
    {

    }

    public PacketSpotLightOpenConfigList(int x, int y, int z, ArrayList<BaseListEntry> list, int type)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.list = list;
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        if(buf.readBoolean())
        {
            ArrayList<BaseListEntry> tempList = new ArrayList<BaseListEntry>();
            int size = buf.readInt();
            for(int i = 0; i < size; i++)
            {
                String name = ByteBufUtils.readUTF8String(buf);
                int id = buf.readInt();
                tempList.add(new ConfigEntry(name, id));
            }
            this.list = tempList;
        }
        else
        {
            this.list = null;
        }
        this.type = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        if(this.list != null)
        {
            buf.writeBoolean(true);
            buf.writeInt(this.list.size());
            for(int i = 0; i < this.list.size(); i++)
            {
                ByteBufUtils.writeUTF8String(buf, ((ConfigEntry)this.list.get(i)).getName());
                buf.writeInt(((ConfigEntry)this.list.get(i)).getId());
            }
        }
        else
        {
            buf.writeBoolean(false);
        }
        buf.writeInt(this.type);
    }

    public static class ClientHandler implements IMessageHandler<PacketSpotLightOpenConfigList, IMessage>
    {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketSpotLightOpenConfigList message, MessageContext ctx)
        {
            EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
            TileEntitySpotLight te = (TileEntitySpotLight)player.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if(message.list != null && !message.list.isEmpty())
            {
                if(message.type == 0)
                {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiSpotLightLoadConfig(player.inventory, te, player.worldObj, message.list));
                }
                else if(message.type == 1)
                {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiSpotLightDeleteConfig(player.inventory, te, player.worldObj, message.list));
                }
                else
                {
                    player.addChatMessage(new ChatComponentTranslation("container.spotlight.fatalerror"));
                }
            }
            else
            {
                player.addChatMessage(new ChatComponentTranslation("container.spotlight.noconfig"));
            }
            return null;
        }
    }

    public static class ServerHandler implements IMessageHandler<PacketSpotLightOpenConfigList, IMessage>
    {
        @Override
        public IMessage onMessage(PacketSpotLightOpenConfigList message, MessageContext ctx)
        {
            TileEntitySpotLight te = (TileEntitySpotLight)ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
            ArrayList<BaseListEntry> templist = new ArrayList<BaseListEntry>();
            if(te.getStackInSlot(0) != null)
            {
                ItemStack stack = te.getStackInSlot(0).copy();
                if(stack.hasTagCompound())
                {
                    if(stack.getTagCompound().hasKey("TSMConfigs"))
                    {
                        NBTTagList tagList = stack.getTagCompound().getTagList("TSMConfigs", NBT.TAG_COMPOUND);
                        for(int i = 0; i < tagList.tagCount(); i++)
                        {
                            templist.add(new ConfigEntry(tagList.getCompoundTagAt(i).getString("ConfigName"), i + 1));
                        }
                    }
                }
            }
            if(!templist.isEmpty())
            {
                return new PacketSpotLightOpenConfigList(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), templist, message.type);
            }
            ctx.getServerHandler().playerEntity.addChatMessage(new ChatComponentTranslation("container.spotlight.noconfig"));
            return null;
        }
    }
}
