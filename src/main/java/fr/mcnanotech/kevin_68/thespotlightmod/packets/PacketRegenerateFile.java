package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRegenerateFile {
    public BlockPos pos;
    public DimensionType dimType;

    public PacketRegenerateFile(BlockPos pos, DimensionType dimType) {
        this.pos = pos;
        this.dimType = dimType;
    }

    public static PacketRegenerateFile decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        DimensionType dimType = DimensionType.getById(buffer.readInt());
        return new PacketRegenerateFile(pos, dimType);
    }

    public static void encode(PacketRegenerateFile packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeInt(packet.dimType.getId());
    }

    public static void handle(PacketRegenerateFile packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TSMJsonManager.deleteFile(packet.dimType, packet.pos);
            TSMJsonManager.generateNewFiles(packet.dimType, packet.pos);
        });
        ctx.get().setPacketHandled(true);
    }
}
