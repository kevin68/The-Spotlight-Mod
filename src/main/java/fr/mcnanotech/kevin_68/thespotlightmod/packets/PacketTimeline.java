package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTimeline {
	public int x, y, z;
	public boolean enabled;

	public PacketTimeline() {
	}

	public PacketTimeline(int x, int y, int z, boolean enabled) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.enabled = enabled;
	}

	public static PacketTimeline decode(PacketBuffer buffer) {
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		boolean enabled = buffer.readBoolean();
		return new PacketTimeline(x, y, z, enabled);
	}

	public static void encode(PacketTimeline packet, PacketBuffer buffer) {
		buffer.writeInt(packet.x);
		buffer.writeInt(packet.y);
		buffer.writeInt(packet.z);
		buffer.writeBoolean(packet.enabled);
	}

	public static void handle(PacketTimeline packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
            TileEntitySpotLight tile = (TileEntitySpotLight)ctx.get().getSender().world.getTileEntity(new BlockPos(packet.x, packet.y, packet.z));
            tile.timelineEnabled = packet.enabled;
            tile.markForUpdate();
		});
        ctx.get().setPacketHandled(true);
    }
}
