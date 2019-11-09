package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.io.IOException;
import java.util.function.Supplier;

import org.apache.logging.log4j.Level;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class PacketUpdateTLData {
    private BlockPos pos;
    private String newData;

    public PacketUpdateTLData(BlockPos pos, String newData, boolean alreadyCompressed) {
        this.pos = pos;
        if (alreadyCompressed) {
            this.newData = newData;
        }
        else {
            try {
                this.newData = TSMJsonManager.compress(newData);
            } catch (IOException e) {
                TheSpotLightMod.LOGGER.catching(Level.WARN, e);
            }
        }
    }
    
    public PacketUpdateTLData(BlockPos pos, String newData) {
        this(pos, newData, false);
    }
    
    public static PacketUpdateTLData decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        String newData = buffer.readString(32767);
        return new PacketUpdateTLData(pos, newData, true);
    }

    public static void encode(PacketUpdateTLData packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeString(packet.newData);
    }

    public static void handle(PacketUpdateTLData packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                TileEntitySpotLight te = (TileEntitySpotLight) ctx.get().getSender().world.getTileEntity(packet.pos);
                te.timelineUpdated = false;
                TSMJsonManager.updateTlJsonData(ctx.get().getSender().getServerWorld(), packet.pos, TSMJsonManager.decompress(packet.newData));
                TSMNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketTLData(packet.pos, TSMJsonManager.decompress(packet.newData)));
            } catch (IOException e) {
                TheSpotLightMod.LOGGER.catching(Level.WARN, e);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}