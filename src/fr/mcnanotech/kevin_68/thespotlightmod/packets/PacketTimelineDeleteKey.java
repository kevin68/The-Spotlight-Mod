package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTimelineDeleteKey implements IMessage
{
    public int x, y, z;
    public short time;

    public PacketTimelineDeleteKey()
    {}

    public PacketTimelineDeleteKey(int x, int y, int z, short time)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.time = time;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.time = buf.readShort();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeShort(this.time);
    }

    public static class Handler implements IMessageHandler<PacketTimelineDeleteKey, IMessage>
    {
        @Override
        public IMessage onMessage(PacketTimelineDeleteKey message, MessageContext ctx)
        {
            TileEntitySpotLight tile = (TileEntitySpotLight)ctx.getServerHandler().playerEntity.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
            tile.setKey(message.time, null);
            tile.markForUpdate();
            return null;
        }
    }
}