package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.io.IOException;
import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTLData {
    public int x, y, z;
    public String data;

    public PacketTLData(int x, int y, int z, String data) {
        this.x = x;
        this.y = y;
        this.z = z;
        try {
            this.data = TSMJsonManager.compress(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PacketTLData decode(PacketBuffer buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        String data = buffer.readString(32767);
        return new PacketTLData(x, y, z, data);
    }

    public static void encode(PacketTLData packet, PacketBuffer buffer) {
        buffer.writeInt(packet.x);
        buffer.writeInt(packet.y);
        buffer.writeInt(packet.z);
        buffer.writeString(packet.data);
    }

    public static void handle(PacketTLData packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntitySpotLight tile = (TileEntitySpotLight) ctx.get().getSender().world.getTileEntity(new BlockPos(packet.x, packet.y, packet.z));
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