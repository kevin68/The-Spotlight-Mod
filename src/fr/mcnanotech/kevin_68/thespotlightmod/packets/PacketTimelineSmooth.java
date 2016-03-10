package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTimelineSmooth implements IMessage
{
    public int x, y, z;
    public boolean smooth;

    public PacketTimelineSmooth()
    {}

    public PacketTimelineSmooth(int x, int y, int z, boolean smooth)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.smooth = smooth;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.smooth = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeBoolean(this.smooth);
    }

    public static class Handler implements IMessageHandler<PacketTimelineSmooth, IMessage>
    {
        @Override
        public IMessage onMessage(PacketTimelineSmooth message, MessageContext ctx)
        {
            TileEntitySpotLight tile = (TileEntitySpotLight)ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
            tile.timelineSmooth = message.smooth;
            tile.markForUpdate();
            return null;
        }
    }
}