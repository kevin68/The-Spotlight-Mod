package fr.mcnanotech.kevin_68.thespotlightmod.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.EnumLaserInformations;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMVec3;

public class TileEntityReflector extends TileEntity implements IReflector
{
	public int angle1;
	public byte angle2;

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound)
	{
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("Angle1", angle1);
		nbtTagCompound.setByte("Angle2", angle2);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound);
		angle1 = nbtTagCompound.getInteger("Angle1");
		angle2 = nbtTagCompound.getByte("Angle2");
	}

	public void set(EnumLaserInformations e, Object value)
	{
		switch(e)
		{
		case LASERANGLE1:
			angle1 = (Integer)value;
			break;
		case LASERANGLE2:
			angle2 = (Byte)value;
			break;
		default:
			break;
		}
		worldObj.markBlockForUpdate(pos);
	}

	public Object get(EnumLaserInformations e)
	{
		switch(e)
		{
		case LASERANGLE1:
			return angle1;
		case LASERANGLE2:
			return angle2;
		default:
			return null;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 65536.0D;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(pos, 3, nbt);
	}

	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return worldObj.getTileEntity(pos) != this ? false : player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public TSMVec3 normal()
	{
		TSMVec3 v = new TSMVec3(0.0, 1.0, 0.0);
		v.rotateAroundZ((float)Math.toRadians(angle1));
		v.rotateAroundY((float)Math.toRadians(angle2 & 0xFF));
		return v;
	}

	@Override
	public TSMVec3 normalRev()
	{
		TSMVec3 v = new TSMVec3(0.0, -1.0, 0.0);
		v.rotateAroundZ((float)Math.toRadians(angle1));
		v.rotateAroundY((float)Math.toRadians(angle2 & 0xFF));
		return v;
	}

	@Override
	public BlockPos getBlockPos()
	{
		return getPos();
	}
}