package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTimelineDeleteKey {
    public int x, y, z;
    public short time;

    public PacketTimelineDeleteKey(int x, int y, int z, short time) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.time = time;
    }

    public static PacketTimelineDeleteKey decode(PacketBuffer buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        short time = buffer.readShort();
        return new PacketTimelineDeleteKey(x, y, z, time);
    }

    public static void encode(PacketTimelineDeleteKey packet, PacketBuffer buffer) {
        buffer.writeInt(packet.x);
        buffer.writeInt(packet.y);
        buffer.writeInt(packet.z);
        buffer.writeShort(packet.time);
    }

    public static void handle(PacketTimelineDeleteKey packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntitySpotLight tile = (TileEntitySpotLight) ctx.get().getSender().world.getTileEntity(new BlockPos(packet.x, packet.y, packet.z));
            tile.setKey(packet.time, null);
            tile.markForUpdate();
        });
        ctx.get().setPacketHandled(true);
    }
}