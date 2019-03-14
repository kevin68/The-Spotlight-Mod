package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRegenerateFile
{
    public int x, y, z;
    public DimensionType dimType;

    public PacketRegenerateFile(int x, int y, int z, DimensionType dimType)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimType = dimType;
    }

    public static PacketRegenerateFile decode(PacketBuffer buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        DimensionType dimType = DimensionType.getById(buffer.readInt());
        return new PacketRegenerateFile(x, y, z, dimType);
    }

    public static void encode(PacketRegenerateFile packet, PacketBuffer buffer) {
    	buffer.writeInt(packet.x);
        buffer.writeInt(packet.y);
        buffer.writeInt(packet.z);
        buffer.writeInt(packet.dimType.getId());
    }
    
    public static void handle(PacketRegenerateFile packet, Supplier<NetworkEvent.Context> ctx) {
  		ctx.get().enqueueWork(() -> {
            TSMJsonManager.deleteFile(packet.dimType, new BlockPos(packet.x, packet.y, packet.z));
            TSMJsonManager.generateNewFiles(packet.dimType, new BlockPos(packet.x, packet.y, packet.z));
  		});
  		ctx.get().setPacketHandled(true);
  	}
}
