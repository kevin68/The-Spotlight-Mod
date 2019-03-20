package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.UUID;
import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketLock {
    private BlockPos pos;
    private boolean locked;
    private UUID uuid;

	public PacketLock(BlockPos pos, boolean locked, UUID uuid) {
	    this.pos = pos;
		this.locked = locked;
		this.uuid = uuid;
	}

	public static void encode(PacketLock packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
		buffer.writeBoolean(packet.locked);
		buffer.writeUniqueId(packet.uuid);
	}

	public static PacketLock decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
		boolean locked = buffer.readBoolean();
		UUID uuid = buffer.readUniqueId();
		return new PacketLock(pos, locked, uuid);
	}

	public static void handle(PacketLock packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			TileEntity te = ctx.get().getSender().world.getTileEntity(packet.pos);
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