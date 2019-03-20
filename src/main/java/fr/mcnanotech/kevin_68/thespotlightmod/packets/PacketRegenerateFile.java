package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRegenerateFile {
    private BlockPos pos;

    public PacketRegenerateFile(BlockPos pos) {
        this.pos = pos;
    }

    public static PacketRegenerateFile decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        return new PacketRegenerateFile(pos);
    }

    public static void encode(PacketRegenerateFile packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
    }

    public static void handle(PacketRegenerateFile packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TSMJsonManager.deleteFile(ctx.get().getSender().world, packet.pos);
            TSMJsonManager.generateNewFiles(ctx.get().getSender().world, packet.pos);
        });
        ctx.get().setPacketHandled(true);
    }
}
