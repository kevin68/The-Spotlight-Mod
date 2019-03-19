package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTimelineSmooth {

    public BlockPos pos;
    public boolean smooth;

    public PacketTimelineSmooth(BlockPos pos, boolean smooth) {
        this.pos = pos;
        this.smooth = smooth;
    }

    public static PacketTimelineSmooth decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        boolean smooth = buffer.readBoolean();
        return new PacketTimelineSmooth(pos, smooth);
    }

    public static void encode(PacketTimelineSmooth packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeBoolean(packet.smooth);
    }
    
    public static void handle(PacketTimelineSmooth packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntitySpotLight tile = (TileEntitySpotLight)ctx.get().getSender().world.getTileEntity(packet.pos);
            tile.timelineSmooth = packet.smooth;
            tile.markForUpdate();
        });
        ctx.get().setPacketHandled(true);
    }
}