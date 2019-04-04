package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.io.IOException;
import java.util.function.Supplier;

import org.apache.logging.log4j.Level;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRequestData {
    private BlockPos pos;

	public PacketRequestData() {
	}

	public PacketRequestData(BlockPos pos) {
		this.pos = pos;
	}

	public static PacketRequestData decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
		return new PacketRequestData(pos);
	}

	public static void encode(PacketRequestData packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
	}

	public static void handle(PacketRequestData packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			String data = TSMJsonManager.getDataFromJson(ctx.get().getSender().world, packet.pos);
			try {
                TSMNetwork.CHANNEL.reply(new PacketData(packet.pos, TSMJsonManager.compress(data == null ? "null" : data)), ctx.get());
            } catch (IOException e) {
                TheSpotLightMod.LOGGER.error("Failed to compress spotlight data, response packet not sended");
                TheSpotLightMod.LOGGER.catching(Level.WARN, e);
            }
		});
		ctx.get().setPacketHandled(true);
	}
}