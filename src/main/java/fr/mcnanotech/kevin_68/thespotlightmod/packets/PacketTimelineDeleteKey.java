package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTimelineDeleteKey {
    public BlockPos pos;
    public short time;

    public PacketTimelineDeleteKey(BlockPos pos, short time) {
        this.pos = pos;
        this.time = time;
    }

    public static PacketTimelineDeleteKey decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        short time = buffer.readShort();
        return new PacketTimelineDeleteKey(pos, time);
    }

    public static void encode(PacketTimelineDeleteKey packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeShort(packet.time);
    }

    public static void handle(PacketTimelineDeleteKey packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntitySpotLight tile = (TileEntitySpotLight) ctx.get().getSender().world.getTileEntity(packet.pos);
            tile.setKey(packet.time, null);
            tile.markForUpdate();
        });
        ctx.get().setPacketHandled(true);
    }
}