package fr.mcnanotech.kevin_68.thespotlightmod.network;

import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTest implements IMessage
{
	public int x, y, z;
	public byte index, value;

	public PacketTest()
	{}

	public PacketTest(int x, int y, int z, byte index, byte value)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.index = index;
		this.value = value;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		index = buffer.readByte();
		value = buffer.readByte();
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeByte(index);
		buffer.writeByte(value);
	}

	public static class Handler implements IMessageHandler<PacketTest, IMessage>
	{
		@Override
		public IMessage onMessage(PacketTest message, MessageContext ctx)
		{
			ctx.getServerHandler().playerEntity.addChatMessage(new ChatComponentText("YOLO"));

			World world = ctx.getServerHandler().playerEntity.worldObj;
			TileEntity tile = world.getTileEntity(new BlockPos(message.x, message.y, message.z));

			if(tile instanceof TileEntitySpotLight)
			{
				TileEntitySpotLight te = (TileEntitySpotLight)tile;
				te.setByte(message.index, message.value);
			}

			return null;
		}
	}
}