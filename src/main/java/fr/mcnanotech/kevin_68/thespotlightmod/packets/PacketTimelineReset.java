package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTimelineReset {
	public int x, y, z;

	public PacketTimelineReset(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static PacketTimelineReset decode(PacketBuffer buffer) {
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		return new PacketTimelineReset(x, y, z);
	}

	public static void encode(PacketTimelineReset packet, PacketBuffer buffer) {
		buffer.writeInt(packet.x);
		buffer.writeInt(packet.y);
		buffer.writeInt(packet.z);
	}

	public static void handle(PacketTimelineReset packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
            TileEntitySpotLight tile = (TileEntitySpotLight)ctx.get().getSender().world.getTileEntity(new BlockPos(packet.x, packet.y, packet.z));
            tile.time = 0;
            tile.markForUpdate();
		});
        ctx.get().setPacketHandled(true);
    }
}
