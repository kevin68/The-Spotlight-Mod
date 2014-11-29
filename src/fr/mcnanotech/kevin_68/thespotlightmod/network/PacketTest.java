//package fr.mcnanotech.kevin_68.thespotlightmod.network;
//
//import io.netty.buffer.ByteBuf;
//import net.minecraft.block.Block;
//import net.minecraft.init.Blocks;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.BlockPos;
//import net.minecraft.util.ChatComponentText;
//import net.minecraft.util.MathHelper;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.common.network.ByteBufUtils;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
//import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
//
//public class PacketTest implements IMessage
//{
//	private String text;
//
//	public PacketTest()
//	{}
//
//	public PacketTest(String text)
//	{
//		this.text = text;
//	}
//
//	@Override
//	public void fromBytes(ByteBuf buf)
//	{
//		text = ByteBufUtils.readUTF8String(buf);
//	}
//
//	@Override
//	public void toBytes(ByteBuf buf)
//	{
//		ByteBufUtils.writeUTF8String(buf, text);
//	}
//
//	public static class Handler implements IMessageHandler<PacketTest, IMessage>
//	{
//		@Override
//		public IMessage onMessage(PacketTest message, MessageContext ctx)
//		{
//			ctx.getServerHandler().playerEntity.addChatMessage(new ChatComponentText("YOLO"));
//
//			return null;
//		}
//	}
//
//	private static Block getBlock(World w, int x, int y, int z, BlockPos pos)
//	{
//		return w.getBlockState(pos.add(x - pos.getX(), y - pos.getY(), z - pos.getZ())).getBlock();
//	}
//
//	private static void setBlock(World w, int x, int y, int z, BlockPos pos, Block b)
//	{
//		w.setBlockState(pos.add(pos.getX() + x, pos.getY() + y, pos.getX() + z), b.getDefaultState());
//	}
//}