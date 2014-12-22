package fr.mcnanotech.kevin_68.thespotlightmod.network.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerReflector;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLightSlotConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntityReflector;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.EnumLaserInformations;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.minecraftforgefrance.ffmtlibs.network.FFMTPacket;

public class PacketSpotLightMod extends FFMTPacket
{
	private EnumLaserInformations e;
	private Object obj;
	private boolean failed = false;

	public PacketSpotLightMod()
	{

	}

	public PacketSpotLightMod(EnumLaserInformations e, Object obj)
	{
		this.e = e;
		this.obj = obj;
	}

	@Override
	public int getDiscriminator()
	{
		return 0;
	}

	@Override
	public void writeData(ByteBuf buffer) throws IOException
	{
		buffer.writeInt(e.ordinal());
		switch(e.getType())
		{
		case 0:
			buffer.writeByte((Byte)obj);
			break;
		case 1:
			buffer.writeInt((Integer)obj);
			break;
		case 2:
			buffer.writeBoolean((Boolean)obj);
			break;
		case 3:
			ByteBufUtils.writeUTF8String(buffer, (String)obj);
			break;
		case 4:
			UtilSpotLight.writeObject(buffer, obj);
			break;
		default:
			TheSpotLightMod.log.error("Wrong type (write)");
		}
	}

	@Override
	public void readData(ByteBuf buffer)
	{
		e = EnumLaserInformations.values()[buffer.readInt()];
		switch(e.getType())
		{
		case 0:
			obj = buffer.readByte();
			break;
		case 1:
			obj = buffer.readInt();
			break;
		case 2:
			obj = buffer.readBoolean();
			break;
		case 3:
			obj = ByteBufUtils.readUTF8String(buffer);
			break;
		case 4:
			try
			{
				obj = UtilSpotLight.readObject(buffer);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			break;
		default:
			TheSpotLightMod.log.error("Wrong type (read)");
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{

	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		World world = player.worldObj;
		Container c = player.openContainer;
		if(!failed)
		{
			if(c instanceof ContainerSpotLight)
			{
				TileEntitySpotLight te = ((ContainerSpotLight)c).getSpotLight();
				te.set(e, obj);
			}
			else if(c instanceof ContainerSpotLightSlotConfig)
			{
				TileEntitySpotLight te = ((ContainerSpotLightSlotConfig)c).getSpotLight();
				te.set(e, obj);
			}
			else if(c instanceof ContainerReflector)
			{
				TileEntityReflector te = ((ContainerReflector)c).getReflector();
				te.set(e, obj);
			}
		}
		else
		{
			TheSpotLightMod.log.error("Error, packet OBJ failed");
		}
	}
}