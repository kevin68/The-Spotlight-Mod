package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTimeline implements IMessage
{
    public int x, y, z;
    public boolean enabled;

    public PacketTimeline()
    {}

    public PacketTimeline(int x, int y, int z, boolean enabled)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.enabled = enabled;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.enabled = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeBoolean(this.enabled);
    }

    public static class Handler implements IMessageHandler<PacketTimeline, IMessage>
    {
        @Override
        public IMessage onMessage(PacketTimeline message, MessageContext ctx)
        {
            TileEntitySpotLight tile = (TileEntitySpotLight)ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
            tile.timelineEnabled = message.enabled;
            tile.markForUpdate();
            return null;
        }
    }
}