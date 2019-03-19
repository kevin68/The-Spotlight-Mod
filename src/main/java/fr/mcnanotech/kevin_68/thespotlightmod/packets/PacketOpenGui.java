package fr.mcnanotech.kevin_68.thespotlightmod.packets;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

// TODO still useful ?
public class PacketOpenGui {
    public BlockPos pos;
    public int id;

    public PacketOpenGui(BlockPos pos, int id) {
        this.pos = pos;
        this.id = id;
    }

    public static void encode(PacketOpenGui packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeInt(packet.id);
    }

    public static PacketOpenGui decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        int id = buffer.readInt();
        return new PacketOpenGui(pos, id);
    }

    public static void handle(PacketOpenGui packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntity tile = ctx.get().getSender().world.getTileEntity(packet.pos);
            NetworkHooks.openGui(ctx.get().getSender(), (IInteractionObject) tile, buf -> {
                buf.writeBlockPos(packet.pos);
                buf.writeInt(packet.id);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}