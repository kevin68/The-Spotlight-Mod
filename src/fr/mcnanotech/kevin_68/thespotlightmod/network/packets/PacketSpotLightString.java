package fr.mcnanotech.kevin_68.thespotlightmod.network.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.minecraftforgefrance.ffmtlibs.network.FFMTPacket;

public class PacketSpotLightString extends FFMTPacket
{
    public int x, y, z;
    public int index;
    public String value;

    public PacketSpotLightString()
    {

    }

    public PacketSpotLightString(int x, int y, int z, int index, String value)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.index = index;
        this.value = value;
    }

    @Override
    public int getDiscriminator()
    {
        return 4;
    }

    @Override
    public void writeData(ByteBuf buffer) throws IOException
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(index);
        ByteBufUtils.writeUTF8String(buffer, value);
    }

    @Override
    public void readData(ByteBuf buffer)
    {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        index = buffer.readInt();
        value = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {

    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        if(tile instanceof TileEntitySpotLight)
        {
            TileEntitySpotLight te = (TileEntitySpotLight)tile;
            te.set(index, value);
        }
    }
}