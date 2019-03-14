package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

// TODO still useful ?
public class PacketOpenGui
{
    public int x, y, z, id;

    public PacketOpenGui(int x, int y, int z, int id)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }


	public static void encode(PacketOpenGui packet, PacketBuffer buffer) {
		buffer.writeInt(packet.x);
		buffer.writeInt(packet.y);
		buffer.writeInt(packet.z);
        buffer.writeInt(packet.id);
    }
	
	public static PacketOpenGui decode(PacketBuffer buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        int id = buffer.readInt();
        return new PacketOpenGui(x, y, z, id);
    }


	public static void handle(PacketOpenGui packet, Supplier<NetworkEvent.Context> ctx) {
//        @Override
//        public IMessage onMessage(PacketOpenGui message, MessageContext ctx)
//        {
//            ctx.getServerHandler().player.openGui(TheSpotLightMod.MODID, message.id, ctx.getServerHandler().player.world, message.x, message.y, message.z);
//            return null;
//        }
        ctx.get().setPacketHandled(true);
    }
}