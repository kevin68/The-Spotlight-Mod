package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.io.IOException;
import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTLData {
    private BlockPos pos;
    private String data;

    public PacketTLData(BlockPos pos, String data) {
        this.pos = pos;
        try {
            this.data = TSMJsonManager.compress(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PacketTLData decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        String data = buffer.readString(32767);
        return new PacketTLData(pos, data);
    }

    public static void encode(PacketTLData packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeString(packet.data);
    }

    public static void handle(PacketTLData packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntitySpotLight tile = (TileEntitySpotLight) ctx.get().getSender().world.getTileEntity(packet.pos);
            try {
                tile.timelineUpdated = TSMJsonManager.updateTileTimeline(tile, TSMJsonManager.decompress(packet.data));
            } catch (IOException e) {
                e.printStackTrace();
            }
            tile.timelineUpdating = false;
        });
        ctx.get().setPacketHandled(true);
    }
}