package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRegenerateFile implements IMessage
{
    public int x, y, z, dimID;

    public PacketRegenerateFile()
    {}

    public PacketRegenerateFile(int x, int y, int z, int dimID)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimID = dimID;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.dimID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.dimID);
    }

    public static class Handler implements IMessageHandler<PacketRegenerateFile, IMessage>
    {
        @Override
        public IMessage onMessage(PacketRegenerateFile message, MessageContext ctx)
        {
            TSMJsonManager.deleteFile(message.dimID, new BlockPos(message.x, message.y, message.z));
            TSMJsonManager.generateNewFiles(message.dimID, new BlockPos(message.x, message.y, message.z));
            return null;
        }
    }
}