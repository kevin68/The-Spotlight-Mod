package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRequestTLData {
    public int x, y, z;

    public PacketRequestTLData(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static PacketRequestTLData decode(PacketBuffer buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        return new PacketRequestTLData(x, y, z);
    }

    public static void encode(PacketRequestTLData packet, PacketBuffer buffer) {
        buffer.writeInt(packet.x);
        buffer.writeInt(packet.y);
        buffer.writeInt(packet.z);
    }

    public static void handle(PacketRequestTLData packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DimensionType dim = ctx.get().getSender().dimension;
            String data = TSMJsonManager.getTlDataFromJson(dim, new BlockPos(packet.x, packet.y, packet.z));
            TSMNetwork.CHANNEL.reply(new PacketTLData(packet.x, packet.y, packet.z, data == null ? "null" : data), ctx.get());
        });
        ctx.get().setPacketHandled(true);
    }
}