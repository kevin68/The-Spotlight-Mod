package fr.mcnanotech.kevin_68.thespotlightmod.network;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayerMP;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketSpotLightBoolean;
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketSpotLightByte;
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketSpotLightKey;
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketSpotLightOpenConfigList;
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketSpotLightString;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.SpotLightEntry;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.BaseListEntry;

public class PacketSender
{
	public static void sendSpotLightPacket(TileEntitySpotLight tile, int index, int value)
	{
		try
		{
			TheSpotLightMod.packetHandler.sendToServer(new PacketSpotLight(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), index, value));
		}
		catch(Exception e)
		{
			TheSpotLightMod.log.error("Failed to send spotight packet");
			e.printStackTrace();
		}
	}

	public static void sendSpotLightPacketByte(TileEntitySpotLight tile, byte index, byte value)
	{
		try
		{
			// TheSpotLightMod.networkTest.sendToServer(new
			// PacketTest(tile.getPos().getX(), tile.getPos().getY(),
			// tile.getPos().getZ(), index, value));
			TheSpotLightMod.packetHandler.sendToServer(new PacketSpotLightByte(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), index, value));
		}
		catch(Exception e)
		{
			TheSpotLightMod.log.error("Failed to send spotight packet");
			e.printStackTrace();
		}
	}

	public static void sendSpotLightPacketBoolean(TileEntitySpotLight tile, byte index, boolean value)
	{
		try
		{
			TheSpotLightMod.packetHandler.sendToServer(new PacketSpotLightBoolean(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), index, value));
		}
		catch(Exception e)
		{
			TheSpotLightMod.log.error("Failed to send spotight packet");
			e.printStackTrace();
		}
	}

	public static void sendSpotLightPacket(TileEntitySpotLight tile, int index, String value)
	{
		try
		{
			TheSpotLightMod.packetHandler.sendToServer(new PacketSpotLightString(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), index, value));
		}
		catch(Exception e)
		{
			TheSpotLightMod.log.error("Failed to send spotight packet");
			e.printStackTrace();
		}
	}

	public static void sendSpotLightPacket(TileEntitySpotLight tile, int index, SpotLightEntry entry)
	{
		try
		{
			TheSpotLightMod.packetHandler.sendToServer(new PacketSpotLightKey(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), index, entry));
		}
		catch(Exception e)
		{
			TheSpotLightMod.log.error("Failed to send spotight packet");
			e.printStackTrace();
		}
	}

	public static void sendSpotLightPacketConfig(TileEntitySpotLight tile, boolean toServer, ArrayList<BaseListEntry> list, EntityPlayerMP client, int type)
	{
		try
		{
			if(toServer)
			{
				TheSpotLightMod.packetHandler.sendToServer(new PacketSpotLightOpenConfigList(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), null, type));
			}
			else
			{
				TheSpotLightMod.packetHandler.sendTo(new PacketSpotLightOpenConfigList(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), list, type), client);
			}
		}
		catch(Exception e)
		{
			TheSpotLightMod.log.error("Failed to send spotight packet");
			e.printStackTrace();
		}
	}
}