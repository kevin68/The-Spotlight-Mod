package fr.mcnanotech.kevin_68.thespotlightmod.network.packets;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateData implements IMessage
{
    public int x, y, z, dimID;
    public String newData;

    public PacketUpdateData()
    {}

    public PacketUpdateData(int x, int y, int z, int dimID, String newData)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimID = dimID;
        this.newData = newData;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.dimID = buf.readInt();
        this.newData = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.dimID);
        ByteBufUtils.writeUTF8String(buf, this.newData);
    }

    public static class Handler implements IMessageHandler<PacketUpdateData, IMessage>
    {
        @Override
        public IMessage onMessage(PacketUpdateData message, MessageContext ctx)
        {
            TSMJsonManager.updateJsonData(message.dimID, new BlockPos(message.x, message.y, message.z), message.newData);
            TheSpotLightMod.network.sendToAll(new PacketData(message.x, message.y, message.z, message.newData));
            return null;
        }
    }
}