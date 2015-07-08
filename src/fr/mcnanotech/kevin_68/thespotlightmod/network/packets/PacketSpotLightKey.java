package fr.mcnanotech.kevin_68.thespotlightmod.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.SpotLightEntry;

public class PacketSpotLightKey implements IMessage
{
    public int x, y, z;
    public int index;
    public NBTTagCompound tag = new NBTTagCompound();

    public PacketSpotLightKey()
    {

    }

    public PacketSpotLightKey(int x, int y, int z, int index, SpotLightEntry entry)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.index = index;
        entry.writeToNBT(this.tag);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.index = buf.readInt();
        this.tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.index);
        ByteBufUtils.writeTag(buf, this.tag);

    }

    public static class Handler implements IMessageHandler<PacketSpotLightKey, IMessage>
    {
        @Override
        public IMessage onMessage(PacketSpotLightKey message, MessageContext ctx)
        {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            TileEntity tile = world.getTileEntity(new BlockPos(message.x, message.y, message.z));

            if(tile instanceof TileEntitySpotLight)
            {
                TileEntitySpotLight te = (TileEntitySpotLight)tile;
                SpotLightEntry entry = SpotLightEntry.loadSpotLightEntryFromNBT(message.tag);
                te.setKey(message.index, entry);
            }
            return null;
        }
    }
}