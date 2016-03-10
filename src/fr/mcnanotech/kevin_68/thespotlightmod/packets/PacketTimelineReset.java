package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTimelineReset implements IMessage
{
    public int x, y, z;

    public PacketTimelineReset()
    {}

    public PacketTimelineReset(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    public static class Handler implements IMessageHandler<PacketTimelineReset, IMessage>
    {
        @Override
        public IMessage onMessage(PacketTimelineReset message, MessageContext ctx)
        {
            TileEntitySpotLight tile = (TileEntitySpotLight)ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
            tile.time = 0;
            tile.markForUpdate();
            return null;
        }
    }
}