package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketLock implements IMessage
{
    public int x, y, z;
    public boolean locked;
    public String uuid;

    public PacketLock()
    {}

    public PacketLock(int x, int y, int z, boolean locked, String uuid)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.locked = locked;
        this.uuid = uuid;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.locked = buf.readBoolean();
        this.uuid = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeBoolean(this.locked);
        ByteBufUtils.writeUTF8String(buf, this.uuid);
    }

    public static class Handler implements IMessageHandler<PacketLock, IMessage>
    {
        @Override
        public IMessage onMessage(PacketLock message, MessageContext ctx)
        {
            TileEntity te = ctx.getServerHandler().playerEntity.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if(te instanceof TileEntitySpotLight)
            {
                TileEntitySpotLight tile = (TileEntitySpotLight)te;
                tile.locked = message.locked;
                tile.lockerUUID = message.uuid;
                tile.markForUpdate();
            }
            return null;
        }
    }
}