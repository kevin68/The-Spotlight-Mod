package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.io.IOException;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTLData implements IMessage
{
    public int x, y, z;
    public String data;

    public PacketTLData()
    {}

    public PacketTLData(int x, int y, int z, String data)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        try
        {
            this.data = TSMJsonManager.compress(data);
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
        this.data = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        ByteBufUtils.writeUTF8String(buf, this.data);
    }

    public static class Handler implements IMessageHandler<PacketTLData, IMessage>
    {
        @Override
        public IMessage onMessage(PacketTLData message, MessageContext ctx)
        {
            TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if(te instanceof TileEntitySpotLight)
            {
                TileEntitySpotLight tile = (TileEntitySpotLight)te;
                try
                {
                    tile.timelineUpdated = TSMJsonManager.updateTileTimeline(tile, TSMJsonManager.decompress(message.data));
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                tile.timelineUpdating = false;
            }
            return null;
        }
    }
}