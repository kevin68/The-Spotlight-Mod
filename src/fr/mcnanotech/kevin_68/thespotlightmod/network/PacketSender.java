package fr.mcnanotech.kevin_68.thespotlightmod.network;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketSpotLightBoolean;
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketSpotLightByte;
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketSpotLightKey;
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketSpotLightString;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.SpotLightEntry;

public class PacketSender
{
	public static void sendSpotLightPacket(TileEntitySpotLight tile, int index, int value)
	{
		try
		{
			TheSpotLightMod.packetHandler.sendToServer(new PacketSpotLight(tile.xCoord, tile.yCoord, tile.zCoord, index, value));
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
			TheSpotLightMod.packetHandler.sendToServer(new PacketSpotLightByte(tile.xCoord, tile.yCoord, tile.zCoord, index, value));
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
			TheSpotLightMod.packetHandler.sendToServer(new PacketSpotLightBoolean(tile.xCoord, tile.yCoord, tile.zCoord, index, value));
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
			TheSpotLightMod.packetHandler.sendToServer(new PacketSpotLightString(tile.xCoord, tile.yCoord, tile.zCoord, index, value));
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
			TheSpotLightMod.packetHandler.sendToServer(new PacketSpotLightKey(tile.xCoord, tile.yCoord, tile.zCoord, index, entry));
		}
		catch(Exception e)
		{
			TheSpotLightMod.log.error("Failed to send spotight packet");
			e.printStackTrace();
		}
	}
}
