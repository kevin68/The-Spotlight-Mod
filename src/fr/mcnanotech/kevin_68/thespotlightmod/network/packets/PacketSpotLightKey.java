package fr.mcnanotech.kevin_68.thespotlightmod.network.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.SpotLightEntry;
import fr.minecraftforgefrance.ffmtlibs.network.FFMTPacket;

public class PacketSpotLightKey extends FFMTPacket
{
	public int x, y, z;
	public int index;
	public NBTTagCompound tag = new NBTTagCompound();

	public PacketSpotLightKey()
	{

	}

	public PacketSpotLightKey(int x, int y, int z, int index, SpotLightEntry entry)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.index = index;
		entry.writeToNBT(tag);
	}

	@Override
	public int getDiscriminator()
	{
		return 2;
	}

	@Override
	public void writeData(ByteBuf buffer) throws IOException
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(index);
		ByteBufUtils.writeTag(buffer, tag);
	}

	@Override
	public void readData(ByteBuf buffer)
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		index = buffer.readInt();
		tag = ByteBufUtils.readTag(buffer);
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
			SpotLightEntry entry = SpotLightEntry.loadSpotLightEntryFromNBT(tag);
			te.setKey(index, entry);
		}
	}
}