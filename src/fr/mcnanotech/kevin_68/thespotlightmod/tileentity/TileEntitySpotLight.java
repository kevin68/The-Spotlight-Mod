package fr.mcnanotech.kevin_68.thespotlightmod.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.items.TSMItems;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.EnumLaserInformations;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.SpotLightEntry;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMVec3;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight.BeamVec;

public class TileEntitySpotLight extends TileEntity implements IInventory, IUpdatePlayerListBox
{
	private ItemStack[] slots = new ItemStack[1];

	@SideOnly(Side.CLIENT)
	private long worldTimeClient;
	@SideOnly(Side.CLIENT)
	private float activeBooleanFloat;
	public boolean isActive;

	private byte laserRed, laserGreen, laserBlue, laserSecRed, laserSecGreen, laserSecBlue, laserAngle2, laserRotationSpeed, laserDisplayAxe, laserMainSize, laserSecSize, timelineCreateKeyTime, tilelineLastKeySelected, textRed, textGreen, textBlue, textRotationSpeed, textScale, textHeight, laserSidesNumber;
	private String laserMainTexture, laserSecTexture, text;
	private boolean laserAutoRotate, laserReverseRotation, laserSecondary, laserDouble, timelineEnabled, timelineSmooth, textEnabled, textAutoRotate, textReverseRotation;
	private int timelineTime, lastTimeUse, laserAngle1, laserHeight, textAngle1;

	public BeamVec[] bVec = null;
	public List<BeamVec[]> beams = new ArrayList<BeamVec[]>();
	private int prevHeight = -1, prevSides = -1, prevA1 = -1;
	private byte prevAxe = -1, prevA2 = -1, prevSize = -1, prevSizeSec = -1;

	private Map<EnumLaserInformations, Object> keysMap = new HashMap<EnumLaserInformations, Object>();

	private SpotLightEntry[] keyList = new SpotLightEntry[120];

	@Override
	public void update()
	{
		if(worldObj.isBlockPowered(pos))
		{
			isActive = true;

			if(bVec != null)
			{
				if(laserHeight != prevHeight || laserAngle1 != prevA1 || laserAngle2 != prevA2 || laserDisplayAxe != prevAxe || laserSidesNumber != prevSides || prevSize != laserMainSize || prevSizeSec != laserSecSize || laserAutoRotate)
				{
					prevHeight = laserHeight;
					prevA1 = laserAngle1;
					prevA2 = laserAngle2;
					prevAxe = laserDisplayAxe;
					prevSides = laserSidesNumber;
					prevSize = laserMainSize;
					prevSizeSec = laserSecSize;
					bVec = process();
				}
			}
			else
			{
				bVec = process();
			}

			if(timelineEnabled)
			{
				applyKeys();
			}
		}
		else
		{
			isActive = false;
		}
	}

	private void applyKeys()
	{
		ArrayList<Integer> keys = new ArrayList();

		for(int i = 0; i < keyList.length; i++)
		{
			SpotLightEntry entry = keyList[i];
			if(entry != null && entry.isActive())
			{
				keys.add(i * 10);
			}
		}

		if(keys.isEmpty())
		{
			set(EnumLaserInformations.TIMELINEENABLED, false);
		}

		if(timelineTime == 1199)
		{
			set(EnumLaserInformations.TIMELINETIME, 0);
		}
		else
		{
			set(EnumLaserInformations.TIMELINETIME, timelineTime + 1);
		}

		if(!worldObj.isRemote)
		{
			if(timelineSmooth)
			{
				set(EnumLaserInformations.LASERRED);
				set(EnumLaserInformations.LASERGREEN);
				set(EnumLaserInformations.LASERBLUE);
				set(EnumLaserInformations.LASERSECRED);
				set(EnumLaserInformations.LASERSECGREEN);
				set(EnumLaserInformations.LASERSECBLUE);
				set(EnumLaserInformations.LASERANGLE1);
				set(EnumLaserInformations.LASERANGLE2);
				set(EnumLaserInformations.LASERMAINSIZE);
				set(EnumLaserInformations.LASERSECSIZE);
				set(EnumLaserInformations.LASERHEIGHT);
				set(EnumLaserInformations.TEXTRED);
				set(EnumLaserInformations.TEXTGREEN);
				set(EnumLaserInformations.TEXTBLUE);
				set(EnumLaserInformations.TEXTANGLE1);
				set(EnumLaserInformations.TEXTSCALE);
				set(EnumLaserInformations.TEXTHEIGHT);
				set(EnumLaserInformations.LASERSIDESNUMBER);
				int curTime = timelineTime / 10;
				if(getKey(curTime) != null && lastTimeUse != timelineTime)
				{
					lastTimeUse = timelineTime;
					worldObj.markBlockForUpdate(pos);
					set(EnumLaserInformations.LASERAUTOROTATE, getKey(curTime).get(EnumLaserInformations.LASERAUTOROTATE));
					set(EnumLaserInformations.LASERREVERSEROTATION, getKey(curTime).get(EnumLaserInformations.LASERREVERSEROTATION));
					set(EnumLaserInformations.LASERROTATIONSPEED, getKey(curTime).get(EnumLaserInformations.LASERROTATIONSPEED));
					set(EnumLaserInformations.LASERSECONDARY, getKey(curTime).get(EnumLaserInformations.LASERSECONDARY));
					set(EnumLaserInformations.LASERDISPLAYAXE, getKey(curTime).get(EnumLaserInformations.LASERDISPLAYAXE));
					set(EnumLaserInformations.LASERDOUBLE, getKey(curTime).get(EnumLaserInformations.LASERDOUBLE));
					set(EnumLaserInformations.TEXTENABLED, getKey(curTime).get(EnumLaserInformations.TEXTENABLED));
					set(EnumLaserInformations.TEXTAUTOROTATE, getKey(curTime).get(EnumLaserInformations.TEXTAUTOROTATE));
					set(EnumLaserInformations.TEXTREVERSEROTATION, getKey(curTime).get(EnumLaserInformations.TEXTREVERSEROTATION));
					set(EnumLaserInformations.TEXTROTATIONSPEED, getKey(curTime).get(EnumLaserInformations.TEXTROTATIONSPEED));
				}
			}
			else
			{
				int curTime = timelineTime / 10;
				if(getKey(curTime) != null && lastTimeUse != timelineTime)
				{
					lastTimeUse = timelineTime;
					set(EnumLaserInformations.LASERRED, getKey(curTime).get(EnumLaserInformations.LASERRED));
					set(EnumLaserInformations.LASERGREEN, getKey(curTime).get(EnumLaserInformations.LASERGREEN));
					set(EnumLaserInformations.LASERBLUE, getKey(curTime).get(EnumLaserInformations.LASERBLUE));
					set(EnumLaserInformations.LASERSECRED, getKey(curTime).get(EnumLaserInformations.LASERSECRED));
					set(EnumLaserInformations.LASERSECGREEN, getKey(curTime).get(EnumLaserInformations.LASERSECGREEN));
					set(EnumLaserInformations.LASERSECBLUE, getKey(curTime).get(EnumLaserInformations.LASERSECBLUE));
					set(EnumLaserInformations.LASERANGLE1, getKey(curTime).get(EnumLaserInformations.LASERANGLE1));
					set(EnumLaserInformations.LASERANGLE2, getKey(curTime).get(EnumLaserInformations.LASERANGLE2));
					set(EnumLaserInformations.LASERAUTOROTATE, getKey(curTime).get(EnumLaserInformations.LASERAUTOROTATE));
					set(EnumLaserInformations.LASERREVERSEROTATION, getKey(curTime).get(EnumLaserInformations.LASERREVERSEROTATION));
					set(EnumLaserInformations.LASERROTATIONSPEED, getKey(curTime).get(EnumLaserInformations.LASERROTATIONSPEED));
					set(EnumLaserInformations.LASERSECONDARY, getKey(curTime).get(EnumLaserInformations.LASERSECONDARY));
					set(EnumLaserInformations.LASERDISPLAYAXE, getKey(curTime).get(EnumLaserInformations.LASERDISPLAYAXE));
					set(EnumLaserInformations.LASERDOUBLE, getKey(curTime).get(EnumLaserInformations.LASERDOUBLE));
					set(EnumLaserInformations.LASERMAINSIZE, getKey(curTime).get(EnumLaserInformations.LASERMAINSIZE));
					set(EnumLaserInformations.LASERSECSIZE, getKey(curTime).get(EnumLaserInformations.LASERSECSIZE));
					set(EnumLaserInformations.LASERHEIGHT, getKey(curTime).get(EnumLaserInformations.LASERHEIGHT));
					set(EnumLaserInformations.LASERSIDESNUMBER, getKey(curTime).get(EnumLaserInformations.LASERSIDESNUMBER));
					set(EnumLaserInformations.TEXTENABLED, getKey(curTime).get(EnumLaserInformations.TEXTENABLED));
					set(EnumLaserInformations.TEXTRED, getKey(curTime).get(EnumLaserInformations.TEXTRED));
					set(EnumLaserInformations.TEXTGREEN, getKey(curTime).get(EnumLaserInformations.TEXTGREEN));
					set(EnumLaserInformations.TEXTBLUE, getKey(curTime).get(EnumLaserInformations.TEXTBLUE));
					set(EnumLaserInformations.TEXTANGLE1, getKey(curTime).get(EnumLaserInformations.TEXTANGLE1));
					set(EnumLaserInformations.TEXTAUTOROTATE, getKey(curTime).get(EnumLaserInformations.TEXTAUTOROTATE));
					set(EnumLaserInformations.TEXTREVERSEROTATION, getKey(curTime).get(EnumLaserInformations.TEXTREVERSEROTATION));
					set(EnumLaserInformations.TEXTROTATIONSPEED, getKey(curTime).get(EnumLaserInformations.TEXTROTATIONSPEED));
					set(EnumLaserInformations.TEXTSCALE, getKey(curTime).get(EnumLaserInformations.TEXTSCALE));
					set(EnumLaserInformations.TEXTHEIGHT, getKey(curTime).get(EnumLaserInformations.TEXTHEIGHT));
					worldObj.markBlockForUpdate(pos);
				}
			}
		}
	}

	private void keysProcess()
	{
		ArrayList<Integer> keys = new ArrayList();
		ArrayList<Integer> timeBetwinKeys = new ArrayList();

		for(int i = 0; i < keyList.length; i++)
		{
			SpotLightEntry entry = keyList[i];
			if(entry != null && entry.isActive())
			{
				keys.add(i * 10);
			}
		}

		if(!keys.isEmpty() && keys.size() > 1)
		{
			for(int j = 0; j < keys.size() - 1; j++)
			{
				timeBetwinKeys.add(keys.get(j + 1) - keys.get(j));
			}
			timeBetwinKeys.add(1200 - keys.get(keys.size() - 1) + keys.get(0));

			for(int k = 0; k < keys.size() - 1; k++)
			{
				for(EnumLaserInformations e : EnumLaserInformations.values())
				{
					if(e.shouldProcessKeys())
					{
						switch(e.getType())
						{
						case 0:
							int startB = (Byte)keyList[keys.get(k) / 10].get(e) & 0xFF;
							int endB = (Byte)keyList[keys.get(k + 1) / 10].get(e) & 0xFF;
							int deltaB = endB - startB;
							float tickB = (float)deltaB / (float)timeBetwinKeys.get(k);
							byte[] b = new byte[1200];

							for(int l = keys.get(k); l < keys.get(k + 1); l++)
							{
								b[l] = (byte)(startB + tickB * (l - keys.get(k)));
							}

							keysMap.put(e, b);
							break;
						case 1:
							int startI = (Integer)keyList[keys.get(k) / 10].get(e) & 0xFF;
							int endI = (Integer)keyList[keys.get(k + 1) / 10].get(e) & 0xFF;
							int deltaI = endI - startI;
							float tickI = (float)deltaI / (float)timeBetwinKeys.get(k);
							int[] in = new int[1200];

							for(int l = keys.get(k); l < keys.get(k + 1); l++)
							{
								in[l] = (byte)(startI + tickI * (l - keys.get(k)));
							}

							keysMap.put(e, in);
							break;
						}
					}
				}
			}

			for(EnumLaserInformations e : EnumLaserInformations.values())
			{
				if(e.shouldProcessKeys())
				{
					switch(e.getType())
					{
					case 0:
						int startB = (Byte)keyList[keys.get(keys.size() - 1) / 10].get(e) & 0xFF;
						int endB = (Byte)keyList[keys.get(0) / 10].get(e) & 0xFF;
						int deltaB = endB - startB;
						float tickB = (float)deltaB / (float)timeBetwinKeys.get(keys.size() - 1);
						byte[] b = new byte[1200];

						for(int m = keys.get(keys.size() - 1); m < 1200; m++)
						{
							b[m] = (byte)(startB + tickB * (m - keys.get(keys.size() - 1)));
						}
						for(int n = 0; n < keys.get(0); n++)
						{
							b[n] = (byte)(((byte[])keysMap.get(e))[1199] + tickB * n);
						}
						keysMap.put(e, b);

						break;
					case 1:
						int startI = (Integer)keyList[keys.get(keys.size() - 1) / 10].get(e);
						int endI = (Integer)keyList[keys.get(0) / 10].get(e);
						int deltaI = endI - startI;
						float tickI = (float)deltaI / (float)timeBetwinKeys.get(keys.size() - 1);
						int[] in = new int[1200];

						for(int m = keys.get(keys.size() - 1); m < 1200; m++)
						{
							in[m] = (int)(startI + tickI * (m - keys.get(keys.size() - 1)));
						}
						for(int n = 0; n < keys.get(0); n++)
						{
							in[n] = (int)(((int[])keysMap.get(e))[1199] + tickI * n);
						}
						keysMap.put(e, in);
						break;
					}
				}
			}
		}
		else if(keys.size() == 1)
		{

			for(EnumLaserInformations e : EnumLaserInformations.values())
			{
				if(e.shouldProcessKeys())
				{
					switch(e.getType())
					{
					case 0:
						byte[] b = new byte[1200];

						for(int i = 0; i < 1200; i++)
						{
							b[i] = (Byte)keyList[keys.get(0) / 10].get(e);
						}
						break;
					case 1:
						int[] in = new int[1200];

						for(int i = 0; i < 1200; i++)
						{
							in[i] = (Integer)keyList[keys.get(0) / 10].get(e);
						}
						break;
					}

				}
			}
		}
	}

	private void proc()
	{
		double a1 = Math.toRadians(laserAngle1);
		double a2 = laserAutoRotate ? getWorld().getTotalWorldTime() * 0.025D * (1.0D - ((byte)1 & 1) * 2.5D) * ((laserRotationSpeed & 0xFF) / 4.0D) * (laserReverseRotation ? -1.0D : 1.0D) : Math.toRadians(laserAngle2 & 0xFF);
		double laserlen = laserHeight;

		BeamVec[] vecs = new BeamVec[4];

		for(int j = 0; j < 4; j++)
		{
			TSMVec3 height = null;
			if(laserDisplayAxe == 0)
			{
				height = new TSMVec3(0, (j % 2 == 0 ? 1 : -1) * laserHeight, 0);
				height.rotateAroundZ((float)a1);
				height.rotateAroundY(-(float)a2);
			}
			else if(laserDisplayAxe == 1)
			{
				height = new TSMVec3((j % 2 == 0 ? 1 : -1) * laserHeight, 0, 0);
				height.rotateAroundZ(-(float)a1);
				height.rotateAroundX(-(float)a2);
			}
			else
			{
				height = new TSMVec3(0, 0, (j % 2 == 0 ? 1 : -1) * laserHeight);
				height.rotateAroundX((float)a1);
				height.rotateAroundZ((float)a2);
			}

			if(height.getReflectorCrossedByVector(worldObj, pos.getX(), pos.getY(), pos.getZ()) == null)
			{
				vecs[j] = new BeamVec(null, height);
			}
			IReflector prev = null;

			while(height.getReflectorCrossedByVector(worldObj, pos.getX(), pos.getY(), pos.getZ()) != null)
			{
				IReflector ref = height.getReflectorCrossedByVector(worldObj, pos.getX(), pos.getY(), pos.getZ());
				BlockPos p = prev == null ? pos : prev.getBlockPos();
				TSMVec3 l = new TSMVec3(ref.getBlockPos().getX() - p.getX(), ref.getBlockPos().getY() - p.getY(), ref.getBlockPos().getZ() - p.getZ());
				TSMVec3 normal = ref.normal().add(l).norm() <= ref.normalRev().add(l).norm() ? ref.normal() : ref.normalRev();
				laserlen -= l.norm();
				TSMVec3 e = l.multiply(l.norm() / laserHeight * laserlen);
				e.rotateAround(normal, (float)(Math.PI / 2));
				height = e;
				prev = ref;

			}
		}
	}

	private BeamVec[] process()
	{
		double[] sizes = new double[] {Math.sqrt(Math.pow((laserMainSize & 0xFF) / 200.0D, 2) / 2), Math.sqrt(Math.pow((laserSecSize & 0xFF) / 200.0D, 2) / 2)};
		double a1 = Math.toRadians(laserAngle1);
		double a2 = laserAutoRotate ? getWorld().getTotalWorldTime() * 0.025D * (1.0D - ((byte)1 & 1) * 2.5D) * ((laserRotationSpeed & 0xFF) / 4.0D) * (laserReverseRotation ? -1.0D : 1.0D) : Math.toRadians(laserAngle2 & 0xFF);

		BeamVec[] vecs = new BeamVec[4];

		for(int j = 0; j < 4; j++)
		{
			TSMVec3[] v = new TSMVec3[laserSidesNumber + 2];
			TSMVec3 e = null;
			double angle = Math.PI * 2 / (laserSidesNumber + 2);
			if(laserDisplayAxe == 0)
			{
				for(int i = 0; i < laserSidesNumber + 2; i++)
				{
					v[i] = new TSMVec3(Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.cos(angle * i + Math.PI / (laserSidesNumber + 2)), 0.0D, Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.sin(angle * i + Math.PI / (laserSidesNumber + 2)));
					v[i].rotateAroundZ((float)a1);
					v[i].rotateAroundY(-(float)a2);
				}
				e = new TSMVec3(0, (j % 2 == 0 ? 1 : -1) * laserHeight, 0);
				e.rotateAroundZ((float)a1);
				e.rotateAroundY(-(float)a2);
			}
			else if(laserDisplayAxe == 1)
			{
				for(int i = 0; i < laserSidesNumber + 2; i++)
				{
					v[i] = new TSMVec3(0.0D, Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.cos(angle * i + Math.PI / (laserSidesNumber + 2)), Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.sin(angle * i + Math.PI / (laserSidesNumber + 2)));
					v[i].rotateAroundZ(-(float)a1);
					v[i].rotateAroundX(-(float)a2);
				}
				e = new TSMVec3((j % 2 == 0 ? 1 : -1) * laserHeight, 0, 0);
				e.rotateAroundZ(-(float)a1);
				e.rotateAroundX(-(float)a2);
			}
			else
			{
				for(int i = 0; i < laserSidesNumber + 2; i++)
				{
					v[i] = new TSMVec3(Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.cos(angle * i + Math.PI / (laserSidesNumber + 2)), Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.sin(angle * i + Math.PI / (laserSidesNumber + 2)), 0.0D);
					v[i].rotateAroundX((float)a1);
					v[i].rotateAroundZ((float)a2);
				}
				e = new TSMVec3(0, 0, (j % 2 == 0 ? 1 : -1) * laserHeight);
				e.rotateAroundX((float)a1);
				e.rotateAroundZ((float)a2);
			}
			vecs[j] = new BeamVec(v, e);
		}
		return vecs;
	}

	@SideOnly(Side.CLIENT)
	public float isActive()
	{
		if(!isActive)
		{
			return 0.0F;
		}
		else
		{
			int i = (int)(worldObj.getTotalWorldTime() - worldTimeClient);
			worldTimeClient = worldObj.getTotalWorldTime();

			if(i > 1)
			{
				activeBooleanFloat -= i / 40.0F;

				if(activeBooleanFloat < 0.0F)
				{
					activeBooleanFloat = 0.0F;
				}
			}

			activeBooleanFloat += 0.025F;

			if(activeBooleanFloat > 1.0F)
			{
				activeBooleanFloat = 1.0F;
			}

			return activeBooleanFloat;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 65536.0D;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound)
	{
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setByte("Red", laserRed);
		nbtTagCompound.setByte("Green", laserGreen);
		nbtTagCompound.setByte("Blue", laserBlue);
		nbtTagCompound.setByte("SecRed", laserSecRed);
		nbtTagCompound.setByte("SecGreen", laserSecGreen);
		nbtTagCompound.setByte("SecBlue", laserSecBlue);
		if(laserMainTexture != null && !laserMainTexture.isEmpty())
		{
			nbtTagCompound.setString("TextureName", laserMainTexture);
		}
		if(laserSecTexture != null && !laserSecTexture.isEmpty())
		{
			nbtTagCompound.setString("SecTextureName", laserSecTexture);
		}
		nbtTagCompound.setInteger("Angle1", laserAngle1);
		nbtTagCompound.setByte("Angle2", laserAngle2);
		nbtTagCompound.setBoolean("AutoRotate", laserAutoRotate);
		nbtTagCompound.setBoolean("ReverseRotation", laserReverseRotation);
		nbtTagCompound.setByte("RotationSpeed", laserRotationSpeed);
		nbtTagCompound.setBoolean("SecondaryLaser", laserSecondary);
		nbtTagCompound.setByte("LastKeySelected", tilelineLastKeySelected);
		nbtTagCompound.setBoolean("TimeLineEnabled", timelineEnabled);
		nbtTagCompound.setInteger("Time", timelineTime);
		nbtTagCompound.setBoolean("SmoothMode", timelineSmooth);
		nbtTagCompound.setByte("CreateKeyTime", timelineCreateKeyTime);
		nbtTagCompound.setByte("DisplayAxe", laserDisplayAxe);
		nbtTagCompound.setBoolean("SideLaser", laserDouble);
		nbtTagCompound.setByte("MainLaserSize", laserMainSize);
		nbtTagCompound.setByte("SecLaserSize", laserSecSize);
		nbtTagCompound.setInteger("LazerHeight", laserHeight);
		if(text != null && !text.isEmpty())
		{
			nbtTagCompound.setString("DisplayText", text);
		}
		nbtTagCompound.setBoolean("TextEnabled", textEnabled);
		nbtTagCompound.setByte("TxtRed", textRed);
		nbtTagCompound.setByte("TxtGreen", textGreen);
		nbtTagCompound.setByte("TxtBlue", textBlue);
		nbtTagCompound.setInteger("TxtAngle1", textAngle1);
		nbtTagCompound.setBoolean("TxtAutoRotate", textAutoRotate);
		nbtTagCompound.setBoolean("TxtReverseRotation", textReverseRotation);
		nbtTagCompound.setByte("TxtRotationSpeed", textRotationSpeed);
		nbtTagCompound.setByte("TxtScale", textScale);
		nbtTagCompound.setByte("TxtHeight", textHeight);
		nbtTagCompound.setByte("Sides", laserSidesNumber);

		nbtTagCompound.setByteArray("RedKey", (byte[])keysMap.get(EnumLaserInformations.LASERRED));
		nbtTagCompound.setByteArray("GreenKey", (byte[])keysMap.get(EnumLaserInformations.LASERGREEN));
		nbtTagCompound.setByteArray("BlueKey", (byte[])keysMap.get(EnumLaserInformations.LASERBLUE));
		nbtTagCompound.setByteArray("SecRedKey", (byte[])keysMap.get(EnumLaserInformations.LASERSECRED));
		nbtTagCompound.setByteArray("SecGreenKey", (byte[])keysMap.get(EnumLaserInformations.LASERSECGREEN));
		nbtTagCompound.setByteArray("SecBlueKey", (byte[])keysMap.get(EnumLaserInformations.LASERSECBLUE));
		nbtTagCompound.setIntArray("Angle1Key", (int[])keysMap.get(EnumLaserInformations.LASERANGLE1));
		nbtTagCompound.setByteArray("Angle2Key", (byte[])keysMap.get(EnumLaserInformations.LASERANGLE2));
		nbtTagCompound.setByteArray("MainLazerSize", (byte[])keysMap.get(EnumLaserInformations.LASERMAINSIZE));
		nbtTagCompound.setByteArray("SecLazerSize", (byte[])keysMap.get(EnumLaserInformations.LASERSECSIZE));
		nbtTagCompound.setIntArray("LazerHeightKey", (int[])keysMap.get(EnumLaserInformations.LASERHEIGHT));
		nbtTagCompound.setByteArray("SidesKey", (byte[])keysMap.get(EnumLaserInformations.LASERSIDESNUMBER));
		nbtTagCompound.setByteArray("TxtRedKey", (byte[])keysMap.get(EnumLaserInformations.TEXTRED));
		nbtTagCompound.setByteArray("TxtGreenKey", (byte[])keysMap.get(EnumLaserInformations.TEXTGREEN));
		nbtTagCompound.setByteArray("TxtBlueKey", (byte[])keysMap.get(EnumLaserInformations.TEXTBLUE));
		nbtTagCompound.setIntArray("TxtAngle1Key", (int[])keysMap.get(EnumLaserInformations.TEXTANGLE1));
		nbtTagCompound.setByteArray("TxtScaleKey", (byte[])keysMap.get(EnumLaserInformations.TEXTSCALE));
		nbtTagCompound.setByteArray("TxtHeightKey", (byte[])keysMap.get(EnumLaserInformations.TEXTHEIGHT));

		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < keyList.length; ++i)
		{
			if(keyList[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Key", (byte)i);
				keyList[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbtTagCompound.setTag("SpotLightKeys", nbttaglist);

		NBTTagList taglist = new NBTTagList();
		for(int i = 0; i < slots.length; ++i)
		{
			if(slots[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				slots[i].writeToNBT(nbttagcompound1);
				taglist.appendTag(nbttagcompound1);
			}
		}
		nbtTagCompound.setTag("Items", taglist);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound);
		laserRed = nbtTagCompound.getByte("Red");
		laserGreen = nbtTagCompound.getByte("Green");
		laserBlue = nbtTagCompound.getByte("Blue");
		laserSecRed = nbtTagCompound.getByte("SecRed");
		laserSecGreen = nbtTagCompound.getByte("SecGreen");
		laserSecBlue = nbtTagCompound.getByte("SecBlue");
		laserMainTexture = nbtTagCompound.getString("TextureName");
		laserSecTexture = nbtTagCompound.getString("SecTextureName");
		laserAngle1 = nbtTagCompound.getInteger("Angle1");
		laserAngle2 = nbtTagCompound.getByte("Angle2");
		laserAutoRotate = nbtTagCompound.getBoolean("AutoRotate");
		laserReverseRotation = nbtTagCompound.getBoolean("ReverseRotation");
		laserRotationSpeed = nbtTagCompound.getByte("RotationSpeed");
		laserSecondary = nbtTagCompound.getBoolean("SecondaryLaser");
		tilelineLastKeySelected = nbtTagCompound.getByte("LastKeySelected");
		timelineEnabled = nbtTagCompound.getBoolean("TimeLineEnabled");
		timelineTime = nbtTagCompound.getInteger("Time");
		timelineSmooth = nbtTagCompound.getBoolean("SmoothMode");
		timelineCreateKeyTime = nbtTagCompound.getByte("CreateKeyTime");
		laserDisplayAxe = nbtTagCompound.getByte("DisplayAxe");
		laserDouble = nbtTagCompound.getBoolean("SideLaser");
		laserMainSize = nbtTagCompound.getByte("MainLaserSize");
		laserSecSize = nbtTagCompound.getByte("SecLaserSize");
		laserHeight = nbtTagCompound.getInteger("LazerHeight");
		text = nbtTagCompound.getString("DisplayText");
		textEnabled = nbtTagCompound.getBoolean("TextEnabled");
		textRed = nbtTagCompound.getByte("TxtRed");
		textGreen = nbtTagCompound.getByte("TxtGreen");
		textBlue = nbtTagCompound.getByte("TxtBlue");
		textAngle1 = nbtTagCompound.getInteger("TxtAngle1");
		textAutoRotate = nbtTagCompound.getBoolean("TxtAutoRotate");
		textReverseRotation = nbtTagCompound.getBoolean("TxtReverseRotation");
		textRotationSpeed = nbtTagCompound.getByte("TxtRotationSpeed");
		textScale = nbtTagCompound.getByte("TxtScale");
		textHeight = nbtTagCompound.getByte("TxtHeight");
		laserSidesNumber = nbtTagCompound.getByte("Sides");

		keysMap.put(EnumLaserInformations.LASERRED, nbtTagCompound.getByteArray("RedKey"));
		keysMap.put(EnumLaserInformations.LASERGREEN, nbtTagCompound.getByteArray("GreenKey"));
		keysMap.put(EnumLaserInformations.LASERBLUE, nbtTagCompound.getByteArray("BlueKey"));
		keysMap.put(EnumLaserInformations.LASERSECRED, nbtTagCompound.getByteArray("SecRedKey"));
		keysMap.put(EnumLaserInformations.LASERSECGREEN, nbtTagCompound.getByteArray("SecGreenKey"));
		keysMap.put(EnumLaserInformations.LASERSECBLUE, nbtTagCompound.getByteArray("SecBlueKey"));
		keysMap.put(EnumLaserInformations.LASERANGLE1, nbtTagCompound.getIntArray("Angle1Key"));
		keysMap.put(EnumLaserInformations.LASERANGLE2, nbtTagCompound.getByteArray("Angle2Key"));
		keysMap.put(EnumLaserInformations.LASERMAINSIZE, nbtTagCompound.getByteArray("MainLazerSize"));
		keysMap.put(EnumLaserInformations.LASERSECSIZE, nbtTagCompound.getByteArray("SecLazerSize"));
		keysMap.put(EnumLaserInformations.LASERHEIGHT, nbtTagCompound.getIntArray("LazerHeightKey"));
		keysMap.put(EnumLaserInformations.LASERSIDESNUMBER, nbtTagCompound.getByteArray("SidesKey"));
		keysMap.put(EnumLaserInformations.TEXTRED, nbtTagCompound.getByteArray("TxtRedKey"));
		keysMap.put(EnumLaserInformations.TEXTGREEN, nbtTagCompound.getByteArray("TxtGreenKey"));
		keysMap.put(EnumLaserInformations.TEXTBLUE, nbtTagCompound.getByteArray("TxtBlueKey"));
		keysMap.put(EnumLaserInformations.TEXTANGLE1, nbtTagCompound.getIntArray("TxtAngle1Key"));
		keysMap.put(EnumLaserInformations.TEXTSCALE, nbtTagCompound.getByteArray("TxtScaleKey"));
		keysMap.put(EnumLaserInformations.TEXTHEIGHT, nbtTagCompound.getByteArray("TxtHeightKey"));

		NBTTagList nbttaglist = nbtTagCompound.getTagList("SpotLightKeys", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Key") & 255;

			if(j >= 0 && j < keyList.length)
			{
				keyList[j] = SpotLightEntry.loadSpotLightEntryFromNBT(nbttagcompound1);
			}
		}

		NBTTagList nbttaglistItems = nbtTagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < nbttaglistItems.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglistItems.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			if(j >= 0 && j < slots.length)
			{
				slots[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return worldObj.getTileEntity(pos) != this ? false : player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
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

	public void set(EnumLaserInformations e, Object value)
	{
		if(e.getType() == (byte)0)// byte
		{
			byte val = (Byte)value;
			switch(e)
			{
			case LASERRED:
				laserRed = val;
				break;
			case LASERGREEN:
				laserGreen = val;
				break;
			case LASERBLUE:
				laserBlue = val;
				break;
			case LASERSECRED:
				laserSecRed = val;
				break;
			case LASERSECGREEN:
				laserSecGreen = val;
				break;
			case LASERSECBLUE:
				laserSecBlue = val;
				break;
			case LASERANGLE2:
				laserAngle2 = val;
				break;
			case LASERROTATIONSPEED:
				laserRotationSpeed = val;
				break;
			case LASERDISPLAYAXE:
				laserDisplayAxe = val;
				break;
			case LASERMAINSIZE:
				laserMainSize = val;
				break;
			case LASERSECSIZE:
				laserSecSize = val;
				break;
			case TIMELINECREATEKEYTIME:
				timelineCreateKeyTime = val;
				break;
			case TIMELINELASTKEYSELECTED:
				tilelineLastKeySelected = val;
				break;
			case LASERSIDESNUMBER:
				laserSidesNumber = val;
				break;
			case TEXTRED:
				textRed = val;
				break;
			case TEXTGREEN:
				textGreen = val;
				break;
			case TEXTBLUE:
				textBlue = val;
				break;
			case TEXTROTATIONSPEED:
				textRotationSpeed = val;
				break;
			case TEXTSCALE:
				textScale = val;
				break;
			case TEXTHEIGHT:
				textHeight = val;
				break;
			default:
				TheSpotLightMod.log.error("Error, wrong enum " + e.name());
				break;
			}
		}
		else if(e.getType() == (byte)1)// integer
		{
			int val = (Integer)value;
			switch(e)
			{
			case LASERANGLE1:
				laserAngle1 = val;
				break;
			case LASERHEIGHT:
				laserHeight = val;
				break;
			case TIMELINETIME:
				timelineTime = val;
				break;
			case TEXTANGLE1:
				textAngle1 = val;
				break;
			default:
				TheSpotLightMod.log.error("Error, wrong enum " + e.name());
				break;
			}
		}
		else if(e.getType() == (byte)2)// boolean
		{
			boolean val = (Boolean)value;
			switch(e)
			{
			case LASERAUTOROTATE:
				laserAutoRotate = val;
				break;
			case LASERREVERSEROTATION:
				laserReverseRotation = val;
				break;
			case LASERSECONDARY:
				laserSecondary = val;
				break;
			case LASERDOUBLE:
				laserDouble = val;
				break;
			case TIMELINEENABLED:
				timelineEnabled = val;
				break;
			case TIMELINESMOOTH:
				timelineSmooth = val;
				break;
			case TEXTENABLED:
				textEnabled = val;
				break;
			case TEXTAUTOROTATE:
				textAutoRotate = val;
				break;
			case TEXTREVERSEROTATION:
				textReverseRotation = val;
				break;
			default:
				TheSpotLightMod.log.error("Error, wrong enum " + e.name());
				break;
			}
		}
		else if(e.getType() == (byte)3)// string
		{
			String val = (String)value;
			switch(e)
			{
			case LASERMAINTEXTURE:
				laserMainTexture = val;
				break;
			case LASERSECTEXTURE:
				laserSecTexture = val;
				break;
			case TEXT:
				text = val;
				break;
			default:
				TheSpotLightMod.log.error("Error, wrong enum " + e.name());
				break;
			}
		}
		else if(e.getType() == (byte)4)// Commands
		{
			switch(e)
			{
			case COMMANDSETDEFAULT:
				setDefaultValue();
				break;
			case COMMANDAPPLYCONFIG:
				applyConfig((Integer)value);
				break;
			case COMMANDREMOVECONFIG:
				removeConfig((Integer)value);
				break;
			case COMMANDCREATECONFIG:
				createConfig((String)value);
				break;
			default:
				TheSpotLightMod.log.error("Error, wrong enum " + e.name());
				break;
			}
		}
		else
		{
			TheSpotLightMod.log.error("Wrong type : " + e.getType());
		}
		worldObj.markBlockForUpdate(pos);
	}

	/**
	 * Only used for keys
	 * 
	 * @param e
	 */
	public void set(EnumLaserInformations e)
	{
		switch(e.getType())
		{
		case 0:
			set(e, ((byte[])keysMap.get(e))[timelineTime]);
			break;
		case 1:
			set(e, ((int[])keysMap.get(e))[timelineTime]);
			break;
		}
	}

	public Object get(EnumLaserInformations e)
	{
		switch(e)
		{
		case LASERRED:
			return laserRed;
		case LASERGREEN:
			return laserGreen;
		case LASERBLUE:
			return laserBlue;
		case LASERSECRED:
			return laserSecRed;
		case LASERSECGREEN:
			return laserSecBlue;
		case LASERSECBLUE:
			return laserSecBlue;
		case LASERMAINTEXTURE:
			return laserMainTexture;
		case LASERSECTEXTURE:
			return laserSecTexture;
		case LASERANGLE1:
			return laserAngle1;
		case LASERANGLE2:
			return laserAngle2;
		case LASERAUTOROTATE:
			return laserAutoRotate;
		case LASERREVERSEROTATION:
			return laserReverseRotation;
		case LASERROTATIONSPEED:
			return laserRotationSpeed;
		case LASERSECONDARY:
			return laserSecondary;
		case LASERDISPLAYAXE:
			return laserDisplayAxe;
		case LASERDOUBLE:
			return laserDouble;
		case LASERMAINSIZE:
			return laserMainSize;
		case LASERSECSIZE:
			return laserSecSize;
		case LASERHEIGHT:
			return laserHeight;
		case LASERSIDESNUMBER:
			return laserSidesNumber;
		case TIMELINELASTKEYSELECTED:
			return tilelineLastKeySelected;
		case TIMELINEENABLED:
			return timelineEnabled;
		case TIMELINETIME:
			return timelineTime;
		case TIMELINESMOOTH:
			return timelineSmooth;
		case TIMELINECREATEKEYTIME:
			return timelineCreateKeyTime;
		case TEXT:
			return text;
		case TEXTENABLED:
			return textEnabled;
		case TEXTRED:
			return textRed;
		case TEXTGREEN:
			return textGreen;
		case TEXTBLUE:
			return textBlue;
		case TEXTROTATIONSPEED:
			return textRotationSpeed;
		case TEXTAUTOROTATE:
			return textAutoRotate;
		case TEXTREVERSEROTATION:
			return textReverseRotation;
		case TEXTANGLE1:
			return textAngle1;
		case TEXTSCALE:
			return textScale;
		case TEXTHEIGHT:
			return textHeight;
		default:
			TheSpotLightMod.log.error("Error, wrong enum " + e.name());
			break;
		}
		return null;
	}

	public void setKey(int index, SpotLightEntry value)
	{
		if(index >= 0 && index < keyList.length)
		{
			keyList[index] = value;
		}
		else
		{
			TheSpotLightMod.log.error("fatal error, index invalid !");
		}
		keysProcess();
		worldObj.markBlockForUpdate(pos);
	}

	public SpotLightEntry getKey(int index)
	{
		if(index >= 0 && index < keyList.length)
		{
			return keyList[index];
		}
		return null;
	}

	public void setDefaultValue()
	{
		laserRed = (byte)255;
		laserGreen = (byte)255;
		laserBlue = (byte)255;
		laserSecRed = (byte)255;
		laserSecGreen = (byte)255;
		laserSecBlue = (byte)255;
		laserMainTexture = "beacon_beam";
		laserSecTexture = "beacon_beam";
		laserAngle1 = 0;
		laserAngle2 = (byte)0;
		laserAutoRotate = false;
		laserReverseRotation = false;
		laserRotationSpeed = (byte)0;
		laserSecondary = true;
		tilelineLastKeySelected = -1;
		timelineEnabled = false;
		timelineTime = 0;
		timelineSmooth = false;
		timelineCreateKeyTime = 0;
		laserDisplayAxe = (byte)0;
		laserDouble = false;
		laserMainSize = (byte)40;
		laserSecSize = (byte)75;
		laserHeight = 256;
		text = "";
		textEnabled = false;
		textRed = (byte)255;
		textGreen = (byte)255;
		textBlue = (byte)255;
		textAngle1 = 0;
		textAutoRotate = false;
		textReverseRotation = false;
		textRotationSpeed = 0;
		textScale = (byte)10;
		textHeight = (byte)128;
		laserSidesNumber = (byte)2;
		keysMap.put(EnumLaserInformations.LASERRED, new byte[1200]);
		keysMap.put(EnumLaserInformations.LASERGREEN, new byte[1200]);
		keysMap.put(EnumLaserInformations.LASERBLUE, new byte[1200]);
		keysMap.put(EnumLaserInformations.LASERSECRED, new byte[1200]);
		keysMap.put(EnumLaserInformations.LASERSECGREEN, new byte[1200]);
		keysMap.put(EnumLaserInformations.LASERSECBLUE, new byte[1200]);
		keysMap.put(EnumLaserInformations.LASERANGLE1, new int[1200]);
		keysMap.put(EnumLaserInformations.LASERANGLE2, new byte[1200]);
		keysMap.put(EnumLaserInformations.LASERMAINSIZE, new byte[1200]);
		keysMap.put(EnumLaserInformations.LASERSECSIZE, new byte[1200]);
		keysMap.put(EnumLaserInformations.LASERHEIGHT, new int[1200]);
		keysMap.put(EnumLaserInformations.TEXTRED, new byte[1200]);
		keysMap.put(EnumLaserInformations.TEXTGREEN, new byte[1200]);
		keysMap.put(EnumLaserInformations.TEXTBLUE, new byte[1200]);
		keysMap.put(EnumLaserInformations.TEXTANGLE1, new int[1200]);
		keysMap.put(EnumLaserInformations.TEXTSCALE, new byte[1200]);
		keysMap.put(EnumLaserInformations.TEXTHEIGHT, new byte[1200]);
		keysMap.put(EnumLaserInformations.LASERSIDESNUMBER, new byte[1200]);
		keyList = new SpotLightEntry[120];
		worldObj.markBlockForUpdate(pos);
	}

	public boolean applyConfig(int id)
	{
		ItemStack stack = slots[0];
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasKey("TSMConfigs"))
			{
				NBTTagList list = stack.getTagCompound().getTagList("TSMConfigs", NBT.TAG_COMPOUND);
				NBTTagCompound tag = list.getCompoundTagAt(id - 1);
				NBTTagCompound conf = UtilSpotLight.getConfig(tag.getInteger("ConfigId"));
				laserRed = conf.getByte("Red");
				laserGreen = conf.getByte("Green");
				laserBlue = conf.getByte("Blue");
				laserSecRed = conf.getByte("SecRed");
				laserSecGreen = conf.getByte("SecGreen");
				laserSecBlue = conf.getByte("SecBlue");
				laserMainTexture = conf.getString("TextureName");
				laserSecTexture = conf.getString("SecTextureName");
				laserAngle1 = conf.getInteger("Angle1");
				laserAngle2 = conf.getByte("Angle2");
				laserAutoRotate = conf.getBoolean("AutoRotate");
				laserReverseRotation = conf.getBoolean("ReverseRotation");
				laserRotationSpeed = conf.getByte("RotationSpeed");
				laserSecondary = conf.getBoolean("SecondaryLaser");
				tilelineLastKeySelected = conf.getByte("LastKeySelected");
				timelineEnabled = conf.getBoolean("TimeLineEnabled");
				timelineTime = conf.getInteger("Time");
				timelineSmooth = conf.getBoolean("SmoothMode");
				timelineCreateKeyTime = conf.getByte("CreateKeyTime");
				laserDisplayAxe = conf.getByte("DisplayAxe");
				laserDouble = conf.getBoolean("SideLaser");
				laserMainSize = conf.getByte("MainLaserSize");
				laserSecSize = conf.getByte("SecLaserSize");
				laserHeight = conf.getInteger("LazerHeight");
				text = conf.getString("DisplayText");
				textEnabled = conf.getBoolean("TextEnabled");
				textRed = conf.getByte("TxtRed");
				textGreen = conf.getByte("TxtGreen");
				textBlue = conf.getByte("TxtBlue");
				textAngle1 = conf.getInteger("TxtAngle1");
				textAutoRotate = conf.getBoolean("TxtAutoRotate");
				textReverseRotation = conf.getBoolean("TxtReverseRotation");
				textRotationSpeed = conf.getByte("TxtRotationSpeed");
				textScale = conf.getByte("TxtScale");
				textHeight = conf.getByte("TxtHeight");
				laserSidesNumber = conf.getByte("Sides");

				keysMap.put(EnumLaserInformations.LASERRED, conf.getByteArray("RedKey"));
				keysMap.put(EnumLaserInformations.LASERGREEN, conf.getByteArray("GreenKey"));
				keysMap.put(EnumLaserInformations.LASERBLUE, conf.getByteArray("BlueKey"));
				keysMap.put(EnumLaserInformations.LASERSECRED, conf.getByteArray("SecRedKey"));
				keysMap.put(EnumLaserInformations.LASERSECGREEN, conf.getByteArray("SecGreenKey"));
				keysMap.put(EnumLaserInformations.LASERSECBLUE, conf.getByteArray("SecBlueKey"));
				keysMap.put(EnumLaserInformations.LASERANGLE1, conf.getIntArray("Angle1Key"));
				keysMap.put(EnumLaserInformations.LASERANGLE2, conf.getByteArray("Angle2Key"));
				keysMap.put(EnumLaserInformations.LASERMAINSIZE, conf.getByteArray("MainLazerSize"));
				keysMap.put(EnumLaserInformations.LASERSECSIZE, conf.getByteArray("SecLazerSize"));
				keysMap.put(EnumLaserInformations.LASERHEIGHT, conf.getIntArray("LazerHeightKey"));
				keysMap.put(EnumLaserInformations.TEXTRED, conf.getByteArray("TxtRedKey"));
				keysMap.put(EnumLaserInformations.TEXTGREEN, conf.getByteArray("TxtGreenKey"));
				keysMap.put(EnumLaserInformations.TEXTBLUE, conf.getByteArray("TxtBlueKey"));
				keysMap.put(EnumLaserInformations.TEXTANGLE1, conf.getIntArray("TxtAngle1Key"));
				keysMap.put(EnumLaserInformations.TEXTSCALE, conf.getByteArray("TxtScaleKey"));
				keysMap.put(EnumLaserInformations.TEXTHEIGHT, conf.getByteArray("TxtHeightKey"));
				keysMap.put(EnumLaserInformations.LASERSIDESNUMBER, conf.getByteArray("SidesKey"));

				NBTTagList nbttaglist = conf.getTagList("SpotLightKeys", Constants.NBT.TAG_COMPOUND);
				for(int i = 0; i < nbttaglist.tagCount(); ++i)
				{
					NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
					int j = nbttagcompound1.getByte("Key") & 255;

					if(j >= 0 && j < keyList.length)
					{
						keyList[j] = SpotLightEntry.loadSpotLightEntryFromNBT(nbttagcompound1);
					}
				}
				return true;
			}
			worldObj.markBlockForUpdate(pos);
		}

		return false;
	}

	private void removeConfig(int id)
	{
		int value = id - 1;
		UtilSpotLight.removeConfig(value);
		if(slots[0] != null)
		{
			NBTTagCompound itemTag = slots[0].hasTagCompound() ? slots[0].getTagCompound() : new NBTTagCompound();
			NBTTagList list = itemTag.hasKey("TSMConfigs") ? itemTag.getTagList("TSMConfigs", NBT.TAG_COMPOUND) : new NBTTagList();
			for(int i = 0; i < list.tagCount(); i++)
			{
				if(list.getCompoundTagAt(i).getInteger("ConfigId") == value)
				{
					list.removeTag(i);
				}
			}
			itemTag.setTag("TSMConfigs", list);
			slots[0].setTagCompound(itemTag);
		}
	}

	public void createConfig(String name)
	{
		if(slots[0] != null)
		{
			NBTTagCompound itemTag = slots[0].hasTagCompound() ? slots[0].getTagCompound() : new NBTTagCompound();
			NBTTagList list = itemTag.hasKey("TSMConfigs") ? itemTag.getTagList("TSMConfigs", NBT.TAG_COMPOUND) : new NBTTagList();
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("ConfigName", name);
			nbt.setInteger("ConfigId", UtilSpotLight.addConfig(getTags(name)));
			list.appendTag(nbt);
			itemTag.setTag("TSMConfigs", list);
			slots[0].setTagCompound(itemTag);
		}
	}

	public NBTTagCompound getTags(String name)
	{
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		if(name != null && !name.isEmpty())
		{
			nbtTagCompound.setString("ConfigName", name);
		}
		nbtTagCompound.setByte("Red", laserRed);
		nbtTagCompound.setByte("Green", laserGreen);
		nbtTagCompound.setByte("Blue", laserBlue);
		nbtTagCompound.setByte("SecRed", laserSecRed);
		nbtTagCompound.setByte("SecGreen", laserSecGreen);
		nbtTagCompound.setByte("SecBlue", laserSecBlue);
		if(laserMainTexture != null && !laserMainTexture.isEmpty())
		{
			nbtTagCompound.setString("TextureName", laserMainTexture);
		}
		if(laserSecTexture != null && !laserSecTexture.isEmpty())
		{
			nbtTagCompound.setString("SecTextureName", laserSecTexture);
		}
		nbtTagCompound.setInteger("Angle1", laserAngle1);
		nbtTagCompound.setByte("Angle2", laserAngle2);
		nbtTagCompound.setBoolean("AutoRotate", laserAutoRotate);
		nbtTagCompound.setBoolean("ReverseRotation", laserReverseRotation);
		nbtTagCompound.setByte("RotationSpeed", laserRotationSpeed);
		nbtTagCompound.setBoolean("SecondaryLaser", laserSecondary);
		nbtTagCompound.setByte("LastKeySelected", tilelineLastKeySelected);
		nbtTagCompound.setBoolean("TimeLineEnabled", timelineEnabled);
		nbtTagCompound.setInteger("Time", timelineTime);
		nbtTagCompound.setBoolean("SmoothMode", timelineSmooth);
		nbtTagCompound.setByte("CreateKeyTime", timelineCreateKeyTime);
		nbtTagCompound.setByte("DisplayAxe", laserDisplayAxe);
		nbtTagCompound.setBoolean("SideLaser", laserDouble);
		nbtTagCompound.setByte("MainLaserSize", laserMainSize);
		nbtTagCompound.setByte("SecLaserSize", laserSecSize);
		nbtTagCompound.setInteger("LazerHeight", laserHeight);
		if(text != null && !text.isEmpty())
		{
			nbtTagCompound.setString("DisplayText", text);
		}
		nbtTagCompound.setBoolean("TextEnabled", textEnabled);
		nbtTagCompound.setByte("TxtRed", textRed);
		nbtTagCompound.setByte("TxtGreen", textGreen);
		nbtTagCompound.setByte("TxtBlue", textBlue);
		nbtTagCompound.setInteger("TxtAngle1", textAngle1);
		nbtTagCompound.setBoolean("TxtAutoRotate", textAutoRotate);
		nbtTagCompound.setBoolean("TxtReverseRotation", textReverseRotation);
		nbtTagCompound.setByte("TxtRotationSpeed", textRotationSpeed);
		nbtTagCompound.setByte("TxtScale", textScale);
		nbtTagCompound.setByte("TxtHeight", textHeight);
		nbtTagCompound.setByte("Sides", laserSidesNumber);

		nbtTagCompound.setByteArray("RedKey", (byte[])keysMap.get(EnumLaserInformations.LASERRED));
		nbtTagCompound.setByteArray("GreenKey", (byte[])keysMap.get(EnumLaserInformations.LASERGREEN));
		nbtTagCompound.setByteArray("BlueKey", (byte[])keysMap.get(EnumLaserInformations.LASERBLUE));
		nbtTagCompound.setByteArray("SecRedKey", (byte[])keysMap.get(EnumLaserInformations.LASERSECRED));
		nbtTagCompound.setByteArray("SecGreenKey", (byte[])keysMap.get(EnumLaserInformations.LASERSECGREEN));
		nbtTagCompound.setByteArray("SecBlueKey", (byte[])keysMap.get(EnumLaserInformations.LASERSECBLUE));
		nbtTagCompound.setIntArray("Angle1Key", (int[])keysMap.get(EnumLaserInformations.LASERANGLE1));
		nbtTagCompound.setByteArray("Angle2Key", (byte[])keysMap.get(EnumLaserInformations.LASERANGLE2));
		nbtTagCompound.setByteArray("MainLazerSize", (byte[])keysMap.get(EnumLaserInformations.LASERMAINSIZE));
		nbtTagCompound.setByteArray("SecLazerSize", (byte[])keysMap.get(EnumLaserInformations.LASERSECSIZE));
		nbtTagCompound.setIntArray("LazerHeightKey", (int[])keysMap.get(EnumLaserInformations.LASERHEIGHT));
		nbtTagCompound.setByteArray("SidesKey", (byte[])keysMap.get(EnumLaserInformations.LASERSIDESNUMBER));
		nbtTagCompound.setByteArray("TxtRedKey", (byte[])keysMap.get(EnumLaserInformations.TEXTRED));
		nbtTagCompound.setByteArray("TxtGreenKey", (byte[])keysMap.get(EnumLaserInformations.TEXTGREEN));
		nbtTagCompound.setByteArray("TxtBlueKey", (byte[])keysMap.get(EnumLaserInformations.TEXTBLUE));
		nbtTagCompound.setIntArray("TxtAngle1Key", (int[])keysMap.get(EnumLaserInformations.TEXTANGLE1));
		nbtTagCompound.setByteArray("TxtScaleKey", (byte[])keysMap.get(EnumLaserInformations.TEXTSCALE));
		nbtTagCompound.setByteArray("TxtHeightKey", (byte[])keysMap.get(EnumLaserInformations.TEXTHEIGHT));

		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < keyList.length; ++i)
		{
			if(keyList[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Key", (byte)i);
				keyList[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbtTagCompound.setTag("SpotLightKeys", nbttaglist);
		return nbtTagCompound;
	}

	@Override
	public int getSizeInventory()
	{
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return slots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if(slots[slot] != null)
		{
			ItemStack itemstack;
			if(slots[slot].stackSize <= amount)
			{
				itemstack = slots[slot];
				slots[slot] = null;
				return itemstack;
			}
			else
			{
				itemstack = slots[slot].splitStack(amount);
				if(slots[slot].stackSize == 0)
				{
					slots[slot] = null;
				}
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if(slots[slot] != null)
		{
			ItemStack itemstack = slots[slot];
			slots[slot] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		slots[slot] = stack;
		if(stack != null && stack.stackSize > getInventoryStackLimit())
		{
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getCommandSenderName()
	{
		return "container.spotlight";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public void openInventory(EntityPlayer playerIn)
	{}

	@Override
	public void closeInventory(EntityPlayer playerIn)
	{}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return stack != null && stack.getItem() != null && stack.getItem() == TSMItems.configSaver;
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{

	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		slots[0] = null;
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText("Test");
	}
}