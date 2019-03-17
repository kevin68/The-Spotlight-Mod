package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.io.IOException;
import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketData {
	public int x, y, z;
	public String data;

	public PacketData(int x, int y, int z, String data) {
		this.x = x;
		this.y = y;
		this.z = z;
		try {
			this.data = TSMJsonManager.compress(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void encode(PacketData packet, PacketBuffer buffer) {
		buffer.writeInt(packet.x);
		buffer.writeInt(packet.y);
		buffer.writeInt(packet.z);
		buffer.writeString(packet.data);
	}

	public static PacketData decode(PacketBuffer buffer) {
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		String s = buffer.readString(32767);
		return new PacketData(x, y, z, s);
	}

	public static void handle(PacketData packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			TileEntity te = Minecraft.getInstance().world.getTileEntity(new BlockPos(packet.x, packet.y, packet.z));
			if (te instanceof TileEntitySpotLight) {
				TileEntitySpotLight tile = (TileEntitySpotLight) te;
				try {
					tile.updated = TSMJsonManager.updateTileData(tile, TSMJsonManager.decompress(packet.data));
				} catch (IOException e) {
					e.printStackTrace();
				}
				tile.updating = false;
			}
		});
		ctx.get().setPacketHandled(true);
	}
}