package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.io.IOException;
import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
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
            String data = TSMJsonManager.getTlDataFromJson(ctx.get().getSender().getServerWorld(), packet.pos);
            try {
                TSMNetwork.CHANNEL.reply(new PacketTLData(packet.pos, TSMJsonManager.compress(data == null ? "null" : data)), ctx.get());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}