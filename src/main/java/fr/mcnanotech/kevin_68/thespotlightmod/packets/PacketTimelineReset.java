package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTimelineReset {
    private BlockPos pos;

	public PacketTimelineReset(BlockPos pos) {
	    this.pos = pos;
	}

	public static PacketTimelineReset decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
		return new PacketTimelineReset(pos);
	}

	public static void encode(PacketTimelineReset packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
	}

	public static void handle(PacketTimelineReset packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
            TileEntitySpotLight tile = (TileEntitySpotLight)ctx.get().getSender().world.getTileEntity(packet.pos);
            tile.time = 0;
            tile.markForUpdate();
		});
        ctx.get().setPacketHandled(true);
    }
}
