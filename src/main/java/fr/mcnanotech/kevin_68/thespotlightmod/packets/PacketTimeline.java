package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTimeline {
	public BlockPos pos;
	public boolean enabled;

	public PacketTimeline() {
	}

	public PacketTimeline(BlockPos pos, boolean enabled) {
		this.pos = pos;
		this.enabled = enabled;
	}

	public static PacketTimeline decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
		boolean enabled = buffer.readBoolean();
		return new PacketTimeline(pos, enabled);
	}

	public static void encode(PacketTimeline packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
		buffer.writeBoolean(packet.enabled);
	}

	public static void handle(PacketTimeline packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
            TileEntitySpotLight tile = (TileEntitySpotLight)ctx.get().getSender().world.getTileEntity(packet.pos);
            tile.timelineEnabled = packet.enabled;
            tile.markForUpdate();
		});
        ctx.get().setPacketHandled(true);
    }
}
