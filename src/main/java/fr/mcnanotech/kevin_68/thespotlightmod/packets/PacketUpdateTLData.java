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
    public int x, y, z;
    public DimensionType dim;
    public String newData;

    public PacketUpdateTLData() {
    }

    public PacketUpdateTLData(int x, int y, int z, DimensionType dim, String newData) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
        try {
            this.newData = TSMJsonManager.compress(newData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static PacketUpdateTLData decode(PacketBuffer buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        DimensionType dim = DimensionType.getById(buffer.readInt());
        String newData = buffer.readString(32767);
        return new PacketUpdateTLData(x, y, z, dim, newData);
    }

    public static void encode(PacketUpdateTLData packet, PacketBuffer buffer) {
        buffer.writeInt(packet.x);
        buffer.writeInt(packet.y);
        buffer.writeInt(packet.z);
        buffer.writeInt(packet.dim.getId());
        buffer.writeString(packet.newData);
    }

    public static void handle(PacketUpdateTLData packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                BlockPos pos = new BlockPos(packet.x, packet.y, packet.z);
                TileEntitySpotLight te = (TileEntitySpotLight) ctx.get().getSender().world.getTileEntity(pos);
                te.timelineUpdated = false;
                TSMJsonManager.updateTlJsonData(packet.dim, pos, TSMJsonManager.decompress(packet.newData));
                TSMNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketTLData(packet.x, packet.y, packet.z, TSMJsonManager.decompress(packet.newData)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}