package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTimelineSmooth {

    public int x, y, z;
    public boolean smooth;

    public PacketTimelineSmooth(int x, int y, int z, boolean smooth) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.smooth = smooth;
    }

    public static PacketTimelineSmooth decode(PacketBuffer buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        boolean smooth = buffer.readBoolean();
        return new PacketTimelineSmooth(x, y, z, smooth);
    }

    public static void encode(PacketTimelineSmooth packet, PacketBuffer buffer) {
        buffer.writeInt(packet.x);
        buffer.writeInt(packet.y);
        buffer.writeInt(packet.z);
        buffer.writeBoolean(packet.smooth);
    }
    
    public static void handle(PacketTimelineSmooth packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntitySpotLight tile = (TileEntitySpotLight)ctx.get().getSender().world.getTileEntity(new BlockPos(packet.x, packet.y, packet.z));
            tile.timelineSmooth = packet.smooth;
            tile.markForUpdate();
        });
        ctx.get().setPacketHandled(true);
    }
}