package fr.mcnanotech.kevin_68.thespotlightmod.network.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.minecraftforgefrance.ffmtlibs.network.FFMTPacket;

public class PacketSpotLight extends FFMTPacket
{
    public int x, y, z;
    public int index, value;

    public PacketSpotLight()
    {

    }

    public PacketSpotLight(int x, int y, int z, int index, int value)
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
        return 0;
    }

    @Override
    public void writeData(ByteBuf buffer) throws IOException
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(index);
        buffer.writeInt(value);
    }

    @Override
    public void readData(ByteBuf buffer)
    {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        index = buffer.readInt();
        value = buffer.readInt();
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