package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRequestTLData {
    public BlockPos pos;

    public PacketRequestTLData(BlockPos pos) {
        this.pos = pos;
    }

    public static PacketRequestTLData decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        return new PacketRequestTLData(pos);
    }

    public static void encode(PacketRequestTLData packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
    }

    public static void handle(PacketRequestTLData packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DimensionType dim = ctx.get().getSender().dimension;
            String data = TSMJsonManager.getTlDataFromJson(dim, packet.pos);
            TSMNetwork.CHANNEL.reply(new PacketTLData(packet.pos, data == null ? "null" : data), ctx.get());
        });
        ctx.get().setPacketHandled(true);
    }
}