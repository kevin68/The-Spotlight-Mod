package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.io.IOException;
import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class PacketUpdateTLData {
    public BlockPos pos;
    public DimensionType dim;
    public String newData;

    public PacketUpdateTLData(BlockPos pos, DimensionType dim, String newData) {
        this.pos = pos;
        this.dim = dim;
        try {
            this.newData = TSMJsonManager.compress(newData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static PacketUpdateTLData decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        DimensionType dim = DimensionType.getById(buffer.readInt());
        String newData = buffer.readString(32767);
        return new PacketUpdateTLData(pos, dim, newData);
    }

    public static void encode(PacketUpdateTLData packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeInt(packet.dim.getId());
        buffer.writeString(packet.newData);
    }

    public static void handle(PacketUpdateTLData packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                TileEntitySpotLight te = (TileEntitySpotLight) ctx.get().getSender().world.getTileEntity(packet.pos);
                te.timelineUpdated = false;
                TSMJsonManager.updateTlJsonData(packet.dim, packet.pos, TSMJsonManager.decompress(packet.newData));
                TSMNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketTLData(packet.pos, TSMJsonManager.decompress(packet.newData)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}