package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.io.IOException;
import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class PacketUpdateData {
    private BlockPos pos;
    private String newData;

    public PacketUpdateData(BlockPos pos, String newData) {
        this.pos = pos;
        try {
            this.newData = TSMJsonManager.compress(newData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PacketUpdateData decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        String newData = buffer.readString(32767);
        return new PacketUpdateData(pos, newData);
    }

    public static void encode(PacketUpdateData packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeString(packet.newData);
    }

    public static void handle(PacketUpdateData packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                TileEntitySpotLight te = (TileEntitySpotLight) ctx.get().getSender().world.getTileEntity(packet.pos);
                te.updated = false;
                TSMJsonManager.updateJsonData(ctx.get().getSender().world, packet.pos, TSMJsonManager.decompress(packet.newData));
                TSMNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketData(packet.pos, TSMJsonManager.decompress(packet.newData)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}