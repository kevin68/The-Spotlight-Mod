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
	public BlockPos pos;
	public String data;

	public PacketData(BlockPos pos, String data) {
		this.pos = pos;
		try {
			this.data = TSMJsonManager.compress(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void encode(PacketData packet, PacketBuffer buffer) {
		buffer.writeBlockPos(packet.pos);
		buffer.writeString(packet.data);
	}

	public static PacketData decode(PacketBuffer buffer) {
		BlockPos pos = buffer.readBlockPos();
		String s = buffer.readString(32767);
		return new PacketData(pos, s);
	}

	public static void handle(PacketData packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			TileEntity te = Minecraft.getInstance().world.getTileEntity(packet.pos);
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