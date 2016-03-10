package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.io.IOException;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateTLData implements IMessage
{
    public int x, y, z, dimID;
    public String newData;

    public PacketUpdateTLData()
    {}

    public PacketUpdateTLData(int x, int y, int z, int dimID, String newData)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimID = dimID;
        try
        {
            this.newData = TSMJsonManager.compress(newData);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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

    public static class Handler implements IMessageHandler<PacketUpdateTLData, IMessage>
    {
        @Override
        public IMessage onMessage(PacketUpdateTLData message, MessageContext ctx)
        {
            try
            {
                TileEntitySpotLight te = (TileEntitySpotLight)ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
                te.timelineUpdated = false;
                TSMJsonManager.updateTlJsonData(message.dimID, new BlockPos(message.x, message.y, message.z), TSMJsonManager.decompress(message.newData));
                TheSpotLightMod.network.sendToAll(new PacketTLData(message.x, message.y, message.z, TSMJsonManager.decompress(message.newData)));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}