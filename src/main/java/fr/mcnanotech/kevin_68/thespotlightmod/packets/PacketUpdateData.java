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

public class PacketUpdateData
{
    public int x, y, z;
    public DimensionType dimType;
    public String newData;

    public PacketUpdateData(int x, int y, int z, DimensionType dimType, String newData)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimType = dimType;
        try
        {
            this.newData = TSMJsonManager.compress(newData);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static PacketUpdateData decode(PacketBuffer buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        DimensionType dimType = DimensionType.getById(buffer.readInt());
        String newData = buffer.readString(50000);
        return new PacketUpdateData(x, y, z, dimType, newData);
    }

    public static void encode(PacketUpdateData packet, PacketBuffer buffer) {
    	buffer.writeInt(packet.x);
        buffer.writeInt(packet.y);
        buffer.writeInt(packet.z);
        buffer.writeInt(packet.dimType.getId());
        buffer.writeString(packet.newData);
    }
    
    public static void handle(PacketUpdateData packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			try
            {
                TileEntitySpotLight te = (TileEntitySpotLight)ctx.get().getSender().world.getTileEntity(new BlockPos(packet.x, packet.y, packet.z));
                te.updated = false;
                TSMJsonManager.updateJsonData(packet.dimType, new BlockPos(packet.x, packet.y, packet.z), TSMJsonManager.decompress(packet.newData));
                TSMNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketData(packet.x, packet.y, packet.z, TSMJsonManager.decompress(packet.newData)));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
		});
		ctx.get().setPacketHandled(true);
	}
}