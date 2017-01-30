package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestData implements IMessage
{
    public int x, y, z;

    public PacketRequestData()
    {}

    public PacketRequestData(int x, int y, int z)
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

    public static class Handler implements IMessageHandler<PacketRequestData, IMessage>
    {
        @Override
        public IMessage onMessage(PacketRequestData message, MessageContext ctx)
        {
            int dimId = ctx.getServerHandler().player.dimension;
            String data = TSMJsonManager.getDataFromJson(dimId, new BlockPos(message.x, message.y, message.z));
            return new PacketData(message.x, message.y, message.z, data == null ? "null" : data);
        }
    }
}