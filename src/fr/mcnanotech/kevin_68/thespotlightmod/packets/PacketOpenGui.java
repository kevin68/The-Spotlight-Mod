package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenGui implements IMessage
{
    public int x, y, z, id;

    public PacketOpenGui()
    {}

    public PacketOpenGui(int x, int y, int z, int id)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.id);
    }

    public static class Handler implements IMessageHandler<PacketOpenGui, IMessage>
    {
        @Override
        public IMessage onMessage(PacketOpenGui message, MessageContext ctx)
        {
            ctx.getServerHandler().playerEntity.openGui(TheSpotLightMod.MODID, message.id, ctx.getServerHandler().playerEntity.worldObj, message.x, message.y, message.z);
            return null;
        }
    }
}