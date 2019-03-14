package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.UUID;
import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketLock {
	public int x, y, z;
	public boolean locked;
	public UUID uuid;

	public PacketLock(int x, int y, int z, boolean locked, UUID uuid) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.locked = locked;
		this.uuid = uuid;
	}

	public static void encode(PacketLock packet, PacketBuffer buffer) {
		buffer.writeInt(packet.x);
		buffer.writeInt(packet.y);
		buffer.writeInt(packet.z);
		buffer.writeBoolean(packet.locked);
		buffer.writeUniqueId(packet.uuid);
	}

	public static PacketLock decode(PacketBuffer buffer) {
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		boolean locked = buffer.readBoolean();
		UUID uuid = buffer.readUniqueId();
		return new PacketLock(x, y, z, locked, uuid);
	}

	public static void handle(PacketLock packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			TileEntity te = ctx.get().getSender().world.getTileEntity(new BlockPos(packet.x, packet.y, packet.z));
			if (te instanceof TileEntitySpotLight) {
				TileEntitySpotLight tile = (TileEntitySpotLight) te;
				tile.locked = packet.locked;
				tile.lockerUUID = packet.uuid;
				tile.markForUpdate();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}