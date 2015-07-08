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
		if(this.worldObj.isBlockPowered(this.pos))
		{
			this.isActive = true;

			if(this.bVec != null)
			{
				if(this.laserHeight != this.prevHeight || this.laserAngle1 != this.prevA1 || this.laserAngle2 != this.prevA2 || this.laserDisplayAxe != this.prevAxe || this.laserSidesNumber != this.prevSides || this.prevSize != this.laserMainSize || this.prevSizeSec != this.laserSecSize || this.laserAutoRotate)
				{
					this.prevHeight = this.laserHeight;
					this.prevA1 = this.laserAngle1;
					this.prevA2 = this.laserAngle2;
					this.prevAxe = this.laserDisplayAxe;
					this.prevSides = this.laserSidesNumber;
					this.prevSize = this.laserMainSize;
					this.prevSizeSec = this.laserSecSize;
					this.bVec = process();
				}
			}
			else
			{
				this.bVec = process();
			}

			if(this.timelineEnabled)
			{
				applyKeys();
			}
		}
		else
		{
			this.isActive = false;
		}
	}

	private void applyKeys()
	{
		ArrayList<Integer> keys = new ArrayList();

		for(int i = 0; i < this.keyList.length; i++)
		{
			SpotLightEntry entry = this.keyList[i];
			if(entry != null && entry.isActive())
			{
				keys.add(i * 10);
			}
		}

		if(keys.isEmpty())
		{
			set(EnumLaserInformations.TIMELINEENABLED, false);
		}

		if(this.timelineTime == 1199)
		{
			set(EnumLaserInformations.TIMELINETIME, 0);
		}
		else
		{
			set(EnumLaserInformations.TIMELINETIME, this.timelineTime + 1);
		}

		if(!this.worldObj.isRemote)
		{
			if(this.timelineSmooth)
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
				int curTime = this.timelineTime / 10;
				if(getKey(curTime) != null && this.lastTimeUse != this.timelineTime)
				{
					this.lastTimeUse = this.timelineTime;
					this.worldObj.markBlockForUpdate(this.pos);
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
				int curTime = this.timelineTime / 10;
				if(getKey(curTime) != null && this.lastTimeUse != this.timelineTime)
				{
					this.lastTimeUse = this.timelineTime;
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
					this.worldObj.markBlockForUpdate(this.pos);
				}
			}
		}
	}

	private void keysProcess()
	{
		ArrayList<Integer> keys = new ArrayList();
		ArrayList<Integer> timeBetwinKeys = new ArrayList();

		for(int i = 0; i < this.keyList.length; i++)
		{
			SpotLightEntry entry = this.keyList[i];
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
							int startB = (Byte)this.keyList[keys.get(k) / 10].get(e) & 0xFF;
							int endB = (Byte)this.keyList[keys.get(k + 1) / 10].get(e) & 0xFF;
							int deltaB = endB - startB;
							float tickB = (float)deltaB / (float)timeBetwinKeys.get(k);
							byte[] b = new byte[1200];

							for(int l = keys.get(k); l < keys.get(k + 1); l++)
							{
								b[l] = (byte)(startB + tickB * (l - keys.get(k)));
							}

							this.keysMap.put(e, b);
							break;
						case 1:
							int startI = (Integer)this.keyList[keys.get(k) / 10].get(e) & 0xFF;
							int endI = (Integer)this.keyList[keys.get(k + 1) / 10].get(e) & 0xFF;
							int deltaI = endI - startI;
							float tickI = (float)deltaI / (float)timeBetwinKeys.get(k);
							int[] in = new int[1200];

							for(int l = keys.get(k); l < keys.get(k + 1); l++)
							{
								in[l] = (byte)(startI + tickI * (l - keys.get(k)));
							}

							this.keysMap.put(e, in);
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
						int startB = (Byte)this.keyList[keys.get(keys.size() - 1) / 10].get(e) & 0xFF;
						int endB = (Byte)this.keyList[keys.get(0) / 10].get(e) & 0xFF;
						int deltaB = endB - startB;
						float tickB = (float)deltaB / (float)timeBetwinKeys.get(keys.size() - 1);
						byte[] b = new byte[1200];

						for(int m = keys.get(keys.size() - 1); m < 1200; m++)
						{
							b[m] = (byte)(startB + tickB * (m - keys.get(keys.size() - 1)));
						}
						for(int n = 0; n < keys.get(0); n++)
						{
							b[n] = (byte)(((byte[])this.keysMap.get(e))[1199] + tickB * n);
						}
						this.keysMap.put(e, b);

						break;
					case 1:
						int startI = (Integer)this.keyList[keys.get(keys.size() - 1) / 10].get(e);
						int endI = (Integer)this.keyList[keys.get(0) / 10].get(e);
						int deltaI = endI - startI;
						float tickI = (float)deltaI / (float)timeBetwinKeys.get(keys.size() - 1);
						int[] in = new int[1200];

						for(int m = keys.get(keys.size() - 1); m < 1200; m++)
						{
							in[m] = (int)(startI + tickI * (m - keys.get(keys.size() - 1)));
						}
						for(int n = 0; n < keys.get(0); n++)
						{
							in[n] = (int)(((int[])this.keysMap.get(e))[1199] + tickI * n);
						}
						this.keysMap.put(e, in);
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
							b[i] = (Byte)this.keyList[keys.get(0) / 10].get(e);
						}
						break;
					case 1:
						int[] in = new int[1200];

						for(int i = 0; i < 1200; i++)
						{
							in[i] = (Integer)this.keyList[keys.get(0) / 10].get(e);
						}
						break;
					}

				}
			}
		}
	}
// TODO verify : not used ?!
//	private void proc()
//	{
//		double a1 = Math.toRadians(this.laserAngle1);
//		double a2 = this.laserAutoRotate ? getWorld().getTotalWorldTime() * 0.025D * (1.0D - ((byte)1 & 1) * 2.5D) * ((this.laserRotationSpeed & 0xFF) / 4.0D) * (this.laserReverseRotation ? -1.0D : 1.0D) : Math.toRadians(this.laserAngle2 & 0xFF);
//		double laserlen = this.laserHeight;
//
//		BeamVec[] vecs = new BeamVec[4];
//
//		for(int j = 0; j < 4; j++)
//		{
//			TSMVec3 height = null;
//			if(this.laserDisplayAxe == 0)
//			{
//				height = new TSMVec3(0, (j % 2 == 0 ? 1 : -1) * this.laserHeight, 0);
//				height.rotateAroundZ((float)a1);
//				height.rotateAroundY(-(float)a2);
//			}
//			else if(this.laserDisplayAxe == 1)
//			{
//				height = new TSMVec3((j % 2 == 0 ? 1 : -1) * this.laserHeight, 0, 0);
//				height.rotateAroundZ(-(float)a1);
//				height.rotateAroundX(-(float)a2);
//			}
//			else
//			{
//				height = new TSMVec3(0, 0, (j % 2 == 0 ? 1 : -1) * this.laserHeight);
//				height.rotateAroundX((float)a1);
//				height.rotateAroundZ((float)a2);
//			}
//		}
//	}

	private BeamVec[] process()
	{
		double[] sizes = new double[] {Math.sqrt(Math.pow((this.laserMainSize & 0xFF) / 200.0D, 2) / 2), Math.sqrt(Math.pow((this.laserSecSize & 0xFF) / 200.0D, 2) / 2)};
		double a1 = Math.toRadians(this.laserAngle1);
		double a2 = this.laserAutoRotate ? getWorld().getTotalWorldTime() * 0.025D * (1.0D - ((byte)1 & 1) * 2.5D) * ((this.laserRotationSpeed & 0xFF) / 4.0D) * (this.laserReverseRotation ? -1.0D : 1.0D) : Math.toRadians(this.laserAngle2 & 0xFF);

		BeamVec[] vecs = new BeamVec[4];

		for(int j = 0; j < 4; j++)
		{
			TSMVec3[] v = new TSMVec3[this.laserSidesNumber + 2];
			TSMVec3 e = null;
			double angle = Math.PI * 2 / (this.laserSidesNumber + 2);
			if(this.laserDisplayAxe == 0)
			{
				for(int i = 0; i < this.laserSidesNumber + 2; i++)
				{
					v[i] = new TSMVec3(Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.cos(angle * i + Math.PI / (this.laserSidesNumber + 2)), 0.0D, Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.sin(angle * i + Math.PI / (this.laserSidesNumber + 2)));
					v[i].rotateAroundZ((float)a1);
					v[i].rotateAroundY(-(float)a2);
				}
				e = new TSMVec3(0, (j % 2 == 0 ? 1 : -1) * this.laserHeight, 0);
				e.rotateAroundZ((float)a1);
				e.rotateAroundY(-(float)a2);
			}
			else if(this.laserDisplayAxe == 1)
			{
				for(int i = 0; i < this.laserSidesNumber + 2; i++)
				{
					v[i] = new TSMVec3(0.0D, Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.cos(angle * i + Math.PI / (this.laserSidesNumber + 2)), Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.sin(angle * i + Math.PI / (this.laserSidesNumber + 2)));
					v[i].rotateAroundZ(-(float)a1);
					v[i].rotateAroundX(-(float)a2);
				}
				e = new TSMVec3((j % 2 == 0 ? 1 : -1) * this.laserHeight, 0, 0);
				e.rotateAroundZ(-(float)a1);
				e.rotateAroundX(-(float)a2);
			}
			else
			{
				for(int i = 0; i < this.laserSidesNumber + 2; i++)
				{
					v[i] = new TSMVec3(Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.cos(angle * i + Math.PI / (this.laserSidesNumber + 2)), Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.sin(angle * i + Math.PI / (this.laserSidesNumber + 2)), 0.0D);
					v[i].rotateAroundX((float)a1);
					v[i].rotateAroundZ((float)a2);
				}
				e = new TSMVec3(0, 0, (j % 2 == 0 ? 1 : -1) * this.laserHeight);
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
		if(!this.isActive)
		{
			return 0.0F;
		}
		else
		{
			int i = (int)(this.worldObj.getTotalWorldTime() - this.worldTimeClient);
			this.worldTimeClient = this.worldObj.getTotalWorldTime();

			if(i > 1)
			{
				this.activeBooleanFloat -= i / 40.0F;

				if(this.activeBooleanFloat < 0.0F)
				{
					this.activeBooleanFloat = 0.0F;
				}
			}

			this.activeBooleanFloat += 0.025F;

			if(this.activeBooleanFloat > 1.0F)
			{
				this.activeBooleanFloat = 1.0F;
			}

			return this.activeBooleanFloat;
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
		nbtTagCompound.setByte("Red", this.laserRed);
		nbtTagCompound.setByte("Green", this.laserGreen);
		nbtTagCompound.setByte("Blue", this.laserBlue);
		nbtTagCompound.setByte("SecRed", this.laserSecRed);
		nbtTagCompound.setByte("SecGreen", this.laserSecGreen);
		nbtTagCompound.setByte("SecBlue", this.laserSecBlue);
		if(this.laserMainTexture != null && !this.laserMainTexture.isEmpty())
		{
			nbtTagCompound.setString("TextureName", this.laserMainTexture);
		}
		if(this.laserSecTexture != null && !this.laserSecTexture.isEmpty())
		{
			nbtTagCompound.setString("SecTextureName", this.laserSecTexture);
		}
		nbtTagCompound.setInteger("Angle1", this.laserAngle1);
		nbtTagCompound.setByte("Angle2", this.laserAngle2);
		nbtTagCompound.setBoolean("AutoRotate", this.laserAutoRotate);
		nbtTagCompound.setBoolean("ReverseRotation", this.laserReverseRotation);
		nbtTagCompound.setByte("RotationSpeed", this.laserRotationSpeed);
		nbtTagCompound.setBoolean("SecondaryLaser", this.laserSecondary);
		nbtTagCompound.setByte("LastKeySelected", this.tilelineLastKeySelected);
		nbtTagCompound.setBoolean("TimeLineEnabled", this.timelineEnabled);
		nbtTagCompound.setInteger("Time", this.timelineTime);
		nbtTagCompound.setBoolean("SmoothMode", this.timelineSmooth);
		nbtTagCompound.setByte("CreateKeyTime", this.timelineCreateKeyTime);
		nbtTagCompound.setByte("DisplayAxe", this.laserDisplayAxe);
		nbtTagCompound.setBoolean("SideLaser", this.laserDouble);
		nbtTagCompound.setByte("MainLaserSize", this.laserMainSize);
		nbtTagCompound.setByte("SecLaserSize", this.laserSecSize);
		nbtTagCompound.setInteger("LazerHeight", this.laserHeight);
		if(this.text != null && !this.text.isEmpty())
		{
			nbtTagCompound.setString("DisplayText", this.text);
		}
		nbtTagCompound.setBoolean("TextEnabled", this.textEnabled);
		nbtTagCompound.setByte("TxtRed", this.textRed);
		nbtTagCompound.setByte("TxtGreen", this.textGreen);
		nbtTagCompound.setByte("TxtBlue", this.textBlue);
		nbtTagCompound.setInteger("TxtAngle1", this.textAngle1);
		nbtTagCompound.setBoolean("TxtAutoRotate", this.textAutoRotate);
		nbtTagCompound.setBoolean("TxtReverseRotation", this.textReverseRotation);
		nbtTagCompound.setByte("TxtRotationSpeed", this.textRotationSpeed);
		nbtTagCompound.setByte("TxtScale", this.textScale);
		nbtTagCompound.setByte("TxtHeight", this.textHeight);
		nbtTagCompound.setByte("Sides", this.laserSidesNumber);

		nbtTagCompound.setByteArray("RedKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERRED));
		nbtTagCompound.setByteArray("GreenKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERGREEN));
		nbtTagCompound.setByteArray("BlueKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERBLUE));
		nbtTagCompound.setByteArray("SecRedKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERSECRED));
		nbtTagCompound.setByteArray("SecGreenKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERSECGREEN));
		nbtTagCompound.setByteArray("SecBlueKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERSECBLUE));
		nbtTagCompound.setIntArray("Angle1Key", (int[])this.keysMap.get(EnumLaserInformations.LASERANGLE1));
		nbtTagCompound.setByteArray("Angle2Key", (byte[])this.keysMap.get(EnumLaserInformations.LASERANGLE2));
		nbtTagCompound.setByteArray("MainLazerSize", (byte[])this.keysMap.get(EnumLaserInformations.LASERMAINSIZE));
		nbtTagCompound.setByteArray("SecLazerSize", (byte[])this.keysMap.get(EnumLaserInformations.LASERSECSIZE));
		nbtTagCompound.setIntArray("LazerHeightKey", (int[])this.keysMap.get(EnumLaserInformations.LASERHEIGHT));
		nbtTagCompound.setByteArray("SidesKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERSIDESNUMBER));
		nbtTagCompound.setByteArray("TxtRedKey", (byte[])this.keysMap.get(EnumLaserInformations.TEXTRED));
		nbtTagCompound.setByteArray("TxtGreenKey", (byte[])this.keysMap.get(EnumLaserInformations.TEXTGREEN));
		nbtTagCompound.setByteArray("TxtBlueKey", (byte[])this.keysMap.get(EnumLaserInformations.TEXTBLUE));
		nbtTagCompound.setIntArray("TxtAngle1Key", (int[])this.keysMap.get(EnumLaserInformations.TEXTANGLE1));
		nbtTagCompound.setByteArray("TxtScaleKey", (byte[])this.keysMap.get(EnumLaserInformations.TEXTSCALE));
		nbtTagCompound.setByteArray("TxtHeightKey", (byte[])this.keysMap.get(EnumLaserInformations.TEXTHEIGHT));

		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < this.keyList.length; ++i)
		{
			if(this.keyList[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Key", (byte)i);
				this.keyList[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbtTagCompound.setTag("SpotLightKeys", nbttaglist);

		NBTTagList taglist = new NBTTagList();
		for(int i = 0; i < this.slots.length; ++i)
		{
			if(this.slots[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.slots[i].writeToNBT(nbttagcompound1);
				taglist.appendTag(nbttagcompound1);
			}
		}
		nbtTagCompound.setTag("Items", taglist);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound);
		this.laserRed = nbtTagCompound.getByte("Red");
		this.laserGreen = nbtTagCompound.getByte("Green");
		this.laserBlue = nbtTagCompound.getByte("Blue");
		this.laserSecRed = nbtTagCompound.getByte("SecRed");
		this.laserSecGreen = nbtTagCompound.getByte("SecGreen");
		this.laserSecBlue = nbtTagCompound.getByte("SecBlue");
		this.laserMainTexture = nbtTagCompound.getString("TextureName");
		this.laserSecTexture = nbtTagCompound.getString("SecTextureName");
		this.laserAngle1 = nbtTagCompound.getInteger("Angle1");
		this.laserAngle2 = nbtTagCompound.getByte("Angle2");
		this.laserAutoRotate = nbtTagCompound.getBoolean("AutoRotate");
		this.laserReverseRotation = nbtTagCompound.getBoolean("ReverseRotation");
		this.laserRotationSpeed = nbtTagCompound.getByte("RotationSpeed");
		this.laserSecondary = nbtTagCompound.getBoolean("SecondaryLaser");
		this.tilelineLastKeySelected = nbtTagCompound.getByte("LastKeySelected");
		this.timelineEnabled = nbtTagCompound.getBoolean("TimeLineEnabled");
		this.timelineTime = nbtTagCompound.getInteger("Time");
		this.timelineSmooth = nbtTagCompound.getBoolean("SmoothMode");
		this.timelineCreateKeyTime = nbtTagCompound.getByte("CreateKeyTime");
		this.laserDisplayAxe = nbtTagCompound.getByte("DisplayAxe");
		this.laserDouble = nbtTagCompound.getBoolean("SideLaser");
		this.laserMainSize = nbtTagCompound.getByte("MainLaserSize");
		this.laserSecSize = nbtTagCompound.getByte("SecLaserSize");
		this.laserHeight = nbtTagCompound.getInteger("LazerHeight");
		this.text = nbtTagCompound.getString("DisplayText");
		this.textEnabled = nbtTagCompound.getBoolean("TextEnabled");
		this.textRed = nbtTagCompound.getByte("TxtRed");
		this.textGreen = nbtTagCompound.getByte("TxtGreen");
		this.textBlue = nbtTagCompound.getByte("TxtBlue");
		this.textAngle1 = nbtTagCompound.getInteger("TxtAngle1");
		this.textAutoRotate = nbtTagCompound.getBoolean("TxtAutoRotate");
		this.textReverseRotation = nbtTagCompound.getBoolean("TxtReverseRotation");
		this.textRotationSpeed = nbtTagCompound.getByte("TxtRotationSpeed");
		this.textScale = nbtTagCompound.getByte("TxtScale");
		this.textHeight = nbtTagCompound.getByte("TxtHeight");
		this.laserSidesNumber = nbtTagCompound.getByte("Sides");

		this.keysMap.put(EnumLaserInformations.LASERRED, nbtTagCompound.getByteArray("RedKey"));
		this.keysMap.put(EnumLaserInformations.LASERGREEN, nbtTagCompound.getByteArray("GreenKey"));
		this.keysMap.put(EnumLaserInformations.LASERBLUE, nbtTagCompound.getByteArray("BlueKey"));
		this.keysMap.put(EnumLaserInformations.LASERSECRED, nbtTagCompound.getByteArray("SecRedKey"));
		this.keysMap.put(EnumLaserInformations.LASERSECGREEN, nbtTagCompound.getByteArray("SecGreenKey"));
		this.keysMap.put(EnumLaserInformations.LASERSECBLUE, nbtTagCompound.getByteArray("SecBlueKey"));
		this.keysMap.put(EnumLaserInformations.LASERANGLE1, nbtTagCompound.getIntArray("Angle1Key"));
		this.keysMap.put(EnumLaserInformations.LASERANGLE2, nbtTagCompound.getByteArray("Angle2Key"));
		this.keysMap.put(EnumLaserInformations.LASERMAINSIZE, nbtTagCompound.getByteArray("MainLazerSize"));
		this.keysMap.put(EnumLaserInformations.LASERSECSIZE, nbtTagCompound.getByteArray("SecLazerSize"));
		this.keysMap.put(EnumLaserInformations.LASERHEIGHT, nbtTagCompound.getIntArray("LazerHeightKey"));
		this.keysMap.put(EnumLaserInformations.LASERSIDESNUMBER, nbtTagCompound.getByteArray("SidesKey"));
		this.keysMap.put(EnumLaserInformations.TEXTRED, nbtTagCompound.getByteArray("TxtRedKey"));
		this.keysMap.put(EnumLaserInformations.TEXTGREEN, nbtTagCompound.getByteArray("TxtGreenKey"));
		this.keysMap.put(EnumLaserInformations.TEXTBLUE, nbtTagCompound.getByteArray("TxtBlueKey"));
		this.keysMap.put(EnumLaserInformations.TEXTANGLE1, nbtTagCompound.getIntArray("TxtAngle1Key"));
		this.keysMap.put(EnumLaserInformations.TEXTSCALE, nbtTagCompound.getByteArray("TxtScaleKey"));
		this.keysMap.put(EnumLaserInformations.TEXTHEIGHT, nbtTagCompound.getByteArray("TxtHeightKey"));

		NBTTagList nbttaglist = nbtTagCompound.getTagList("SpotLightKeys", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Key") & 255;

			if(j >= 0 && j < this.keyList.length)
			{
				this.keyList[j] = SpotLightEntry.loadSpotLightEntryFromNBT(nbttagcompound1);
			}
		}

		NBTTagList nbttaglistItems = nbtTagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < nbttaglistItems.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglistItems.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			if(j >= 0 && j < this.slots.length)
			{
				this.slots[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
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
		return new S35PacketUpdateTileEntity(this.pos, 3, nbt);
	}

	public void set(EnumLaserInformations e, Object value)
	{
		if(e.getType() == (byte)0)// byte
		{
			byte val = (Byte)value;
			switch(e)
			{
			case LASERRED:
				this.laserRed = val;
				break;
			case LASERGREEN:
				this.laserGreen = val;
				break;
			case LASERBLUE:
				this.laserBlue = val;
				break;
			case LASERSECRED:
				this.laserSecRed = val;
				break;
			case LASERSECGREEN:
				this.laserSecGreen = val;
				break;
			case LASERSECBLUE:
				this.laserSecBlue = val;
				break;
			case LASERANGLE2:
				this.laserAngle2 = val;
				break;
			case LASERROTATIONSPEED:
				this.laserRotationSpeed = val;
				break;
			case LASERDISPLAYAXE:
				this.laserDisplayAxe = val;
				break;
			case LASERMAINSIZE:
				this.laserMainSize = val;
				break;
			case LASERSECSIZE:
				this.laserSecSize = val;
				break;
			case TIMELINECREATEKEYTIME:
				this.timelineCreateKeyTime = val;
				break;
			case TIMELINELASTKEYSELECTED:
				this.tilelineLastKeySelected = val;
				break;
			case LASERSIDESNUMBER:
				this.laserSidesNumber = val;
				break;
			case TEXTRED:
				this.textRed = val;
				break;
			case TEXTGREEN:
				this.textGreen = val;
				break;
			case TEXTBLUE:
				this.textBlue = val;
				break;
			case TEXTROTATIONSPEED:
				this.textRotationSpeed = val;
				break;
			case TEXTSCALE:
				this.textScale = val;
				break;
			case TEXTHEIGHT:
				this.textHeight = val;
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
				this.laserAngle1 = val;
				break;
			case LASERHEIGHT:
				this.laserHeight = val;
				break;
			case TIMELINETIME:
				this.timelineTime = val;
				break;
			case TEXTANGLE1:
				this.textAngle1 = val;
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
				this.laserAutoRotate = val;
				break;
			case LASERREVERSEROTATION:
				this.laserReverseRotation = val;
				break;
			case LASERSECONDARY:
				this.laserSecondary = val;
				break;
			case LASERDOUBLE:
				this.laserDouble = val;
				break;
			case TIMELINEENABLED:
				this.timelineEnabled = val;
				break;
			case TIMELINESMOOTH:
				this.timelineSmooth = val;
				break;
			case TEXTENABLED:
				this.textEnabled = val;
				break;
			case TEXTAUTOROTATE:
				this.textAutoRotate = val;
				break;
			case TEXTREVERSEROTATION:
				this.textReverseRotation = val;
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
				this.laserMainTexture = val;
				break;
			case LASERSECTEXTURE:
				this.laserSecTexture = val;
				break;
			case TEXT:
				this.text = val;
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
		this.worldObj.markBlockForUpdate(this.pos);
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
			set(e, ((byte[])this.keysMap.get(e))[this.timelineTime]);
			break;
		case 1:
			set(e, ((int[])this.keysMap.get(e))[this.timelineTime]);
			break;
		}
	}

	public Object get(EnumLaserInformations e)
	{
		switch(e)
		{
		case LASERRED:
			return this.laserRed;
		case LASERGREEN:
			return this.laserGreen;
		case LASERBLUE:
			return this.laserBlue;
		case LASERSECRED:
			return this.laserSecRed;
		case LASERSECGREEN:
			return this.laserSecBlue;
		case LASERSECBLUE:
			return this.laserSecBlue;
		case LASERMAINTEXTURE:
			return this.laserMainTexture;
		case LASERSECTEXTURE:
			return this.laserSecTexture;
		case LASERANGLE1:
			return this.laserAngle1;
		case LASERANGLE2:
			return this.laserAngle2;
		case LASERAUTOROTATE:
			return this.laserAutoRotate;
		case LASERREVERSEROTATION:
			return this.laserReverseRotation;
		case LASERROTATIONSPEED:
			return this.laserRotationSpeed;
		case LASERSECONDARY:
			return this.laserSecondary;
		case LASERDISPLAYAXE:
			return this.laserDisplayAxe;
		case LASERDOUBLE:
			return this.laserDouble;
		case LASERMAINSIZE:
			return this.laserMainSize;
		case LASERSECSIZE:
			return this.laserSecSize;
		case LASERHEIGHT:
			return this.laserHeight;
		case LASERSIDESNUMBER:
			return this.laserSidesNumber;
		case TIMELINELASTKEYSELECTED:
			return this.tilelineLastKeySelected;
		case TIMELINEENABLED:
			return this.timelineEnabled;
		case TIMELINETIME:
			return this.timelineTime;
		case TIMELINESMOOTH:
			return this.timelineSmooth;
		case TIMELINECREATEKEYTIME:
			return this.timelineCreateKeyTime;
		case TEXT:
			return this.text;
		case TEXTENABLED:
			return this.textEnabled;
		case TEXTRED:
			return this.textRed;
		case TEXTGREEN:
			return this.textGreen;
		case TEXTBLUE:
			return this.textBlue;
		case TEXTROTATIONSPEED:
			return this.textRotationSpeed;
		case TEXTAUTOROTATE:
			return this.textAutoRotate;
		case TEXTREVERSEROTATION:
			return this.textReverseRotation;
		case TEXTANGLE1:
			return this.textAngle1;
		case TEXTSCALE:
			return this.textScale;
		case TEXTHEIGHT:
			return this.textHeight;
		default:
			TheSpotLightMod.log.error("Error, wrong enum " + e.name());
			break;
		}
		return null;
	}

	public void setKey(int index, SpotLightEntry value)
	{
		if(index >= 0 && index < this.keyList.length)
		{
			this.keyList[index] = value;
		}
		else
		{
			TheSpotLightMod.log.error("fatal error, index invalid !");
		}
		keysProcess();
		this.worldObj.markBlockForUpdate(this.pos);
	}

	public SpotLightEntry getKey(int index)
	{
		if(index >= 0 && index < this.keyList.length)
		{
			return this.keyList[index];
		}
		return null;
	}

	public void setDefaultValue()
	{
		this.laserRed = (byte)255;
		this.laserGreen = (byte)255;
		this.laserBlue = (byte)255;
		this.laserSecRed = (byte)255;
		this.laserSecGreen = (byte)255;
		this.laserSecBlue = (byte)255;
		this.laserMainTexture = "beacon_beam";
		this.laserSecTexture = "beacon_beam";
		this.laserAngle1 = 0;
		this.laserAngle2 = (byte)0;
		this.laserAutoRotate = false;
		this.laserReverseRotation = false;
		this.laserRotationSpeed = (byte)0;
		this.laserSecondary = true;
		this.tilelineLastKeySelected = -1;
		this.timelineEnabled = false;
		this.timelineTime = 0;
		this.timelineSmooth = false;
		this.timelineCreateKeyTime = 0;
		this.laserDisplayAxe = (byte)0;
		this.laserDouble = false;
		this.laserMainSize = (byte)40;
		this.laserSecSize = (byte)75;
		this.laserHeight = 256;
		this.text = "";
		this.textEnabled = false;
		this.textRed = (byte)255;
		this.textGreen = (byte)255;
		this.textBlue = (byte)255;
		this.textAngle1 = 0;
		this.textAutoRotate = false;
		this.textReverseRotation = false;
		this.textRotationSpeed = 0;
		this.textScale = (byte)10;
		this.textHeight = (byte)128;
		this.laserSidesNumber = (byte)2;
		this.keysMap.put(EnumLaserInformations.LASERRED, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.LASERGREEN, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.LASERBLUE, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.LASERSECRED, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.LASERSECGREEN, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.LASERSECBLUE, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.LASERANGLE1, new int[1200]);
		this.keysMap.put(EnumLaserInformations.LASERANGLE2, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.LASERMAINSIZE, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.LASERSECSIZE, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.LASERHEIGHT, new int[1200]);
		this.keysMap.put(EnumLaserInformations.TEXTRED, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.TEXTGREEN, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.TEXTBLUE, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.TEXTANGLE1, new int[1200]);
		this.keysMap.put(EnumLaserInformations.TEXTSCALE, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.TEXTHEIGHT, new byte[1200]);
		this.keysMap.put(EnumLaserInformations.LASERSIDESNUMBER, new byte[1200]);
		this.keyList = new SpotLightEntry[120];
		this.worldObj.markBlockForUpdate(this.pos);
	}

	public boolean applyConfig(int id)
	{
		ItemStack stack = this.slots[0];
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasKey("TSMConfigs"))
			{
				NBTTagList list = stack.getTagCompound().getTagList("TSMConfigs", NBT.TAG_COMPOUND);
				NBTTagCompound tag = list.getCompoundTagAt(id - 1);
				NBTTagCompound conf = UtilSpotLight.getConfig(tag.getInteger("ConfigId"));
				this.laserRed = conf.getByte("Red");
				this.laserGreen = conf.getByte("Green");
				this.laserBlue = conf.getByte("Blue");
				this.laserSecRed = conf.getByte("SecRed");
				this.laserSecGreen = conf.getByte("SecGreen");
				this.laserSecBlue = conf.getByte("SecBlue");
				this.laserMainTexture = conf.getString("TextureName");
				this.laserSecTexture = conf.getString("SecTextureName");
				this.laserAngle1 = conf.getInteger("Angle1");
				this.laserAngle2 = conf.getByte("Angle2");
				this.laserAutoRotate = conf.getBoolean("AutoRotate");
				this.laserReverseRotation = conf.getBoolean("ReverseRotation");
				this.laserRotationSpeed = conf.getByte("RotationSpeed");
				this.laserSecondary = conf.getBoolean("SecondaryLaser");
				this.tilelineLastKeySelected = conf.getByte("LastKeySelected");
				this.timelineEnabled = conf.getBoolean("TimeLineEnabled");
				this.timelineTime = conf.getInteger("Time");
				this.timelineSmooth = conf.getBoolean("SmoothMode");
				this.timelineCreateKeyTime = conf.getByte("CreateKeyTime");
				this.laserDisplayAxe = conf.getByte("DisplayAxe");
				this.laserDouble = conf.getBoolean("SideLaser");
				this.laserMainSize = conf.getByte("MainLaserSize");
				this.laserSecSize = conf.getByte("SecLaserSize");
				this.laserHeight = conf.getInteger("LazerHeight");
				this.text = conf.getString("DisplayText");
				this.textEnabled = conf.getBoolean("TextEnabled");
				this.textRed = conf.getByte("TxtRed");
				this.textGreen = conf.getByte("TxtGreen");
				this.textBlue = conf.getByte("TxtBlue");
				this.textAngle1 = conf.getInteger("TxtAngle1");
				this.textAutoRotate = conf.getBoolean("TxtAutoRotate");
				this.textReverseRotation = conf.getBoolean("TxtReverseRotation");
				this.textRotationSpeed = conf.getByte("TxtRotationSpeed");
				this.textScale = conf.getByte("TxtScale");
				this.textHeight = conf.getByte("TxtHeight");
				this.laserSidesNumber = conf.getByte("Sides");

				this.keysMap.put(EnumLaserInformations.LASERRED, conf.getByteArray("RedKey"));
				this.keysMap.put(EnumLaserInformations.LASERGREEN, conf.getByteArray("GreenKey"));
				this.keysMap.put(EnumLaserInformations.LASERBLUE, conf.getByteArray("BlueKey"));
				this.keysMap.put(EnumLaserInformations.LASERSECRED, conf.getByteArray("SecRedKey"));
				this.keysMap.put(EnumLaserInformations.LASERSECGREEN, conf.getByteArray("SecGreenKey"));
				this.keysMap.put(EnumLaserInformations.LASERSECBLUE, conf.getByteArray("SecBlueKey"));
				this.keysMap.put(EnumLaserInformations.LASERANGLE1, conf.getIntArray("Angle1Key"));
				this.keysMap.put(EnumLaserInformations.LASERANGLE2, conf.getByteArray("Angle2Key"));
				this.keysMap.put(EnumLaserInformations.LASERMAINSIZE, conf.getByteArray("MainLazerSize"));
				this.keysMap.put(EnumLaserInformations.LASERSECSIZE, conf.getByteArray("SecLazerSize"));
				this.keysMap.put(EnumLaserInformations.LASERHEIGHT, conf.getIntArray("LazerHeightKey"));
				this.keysMap.put(EnumLaserInformations.TEXTRED, conf.getByteArray("TxtRedKey"));
				this.keysMap.put(EnumLaserInformations.TEXTGREEN, conf.getByteArray("TxtGreenKey"));
				this.keysMap.put(EnumLaserInformations.TEXTBLUE, conf.getByteArray("TxtBlueKey"));
				this.keysMap.put(EnumLaserInformations.TEXTANGLE1, conf.getIntArray("TxtAngle1Key"));
				this.keysMap.put(EnumLaserInformations.TEXTSCALE, conf.getByteArray("TxtScaleKey"));
				this.keysMap.put(EnumLaserInformations.TEXTHEIGHT, conf.getByteArray("TxtHeightKey"));
				this.keysMap.put(EnumLaserInformations.LASERSIDESNUMBER, conf.getByteArray("SidesKey"));

				NBTTagList nbttaglist = conf.getTagList("SpotLightKeys", Constants.NBT.TAG_COMPOUND);
				for(int i = 0; i < nbttaglist.tagCount(); ++i)
				{
					NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
					int j = nbttagcompound1.getByte("Key") & 255;

					if(j >= 0 && j < this.keyList.length)
					{
						this.keyList[j] = SpotLightEntry.loadSpotLightEntryFromNBT(nbttagcompound1);
					}
				}
				return true;
			}
			this.worldObj.markBlockForUpdate(this.pos);
		}

		return false;
	}

	private void removeConfig(int id)
	{
		int value = id - 1;
		UtilSpotLight.removeConfig(value);
		if(this.slots[0] != null)
		{
			NBTTagCompound itemTag = this.slots[0].hasTagCompound() ? this.slots[0].getTagCompound() : new NBTTagCompound();
			NBTTagList list = itemTag.hasKey("TSMConfigs") ? itemTag.getTagList("TSMConfigs", NBT.TAG_COMPOUND) : new NBTTagList();
			for(int i = 0; i < list.tagCount(); i++)
			{
				if(list.getCompoundTagAt(i).getInteger("ConfigId") == value)
				{
					list.removeTag(i);
				}
			}
			itemTag.setTag("TSMConfigs", list);
			this.slots[0].setTagCompound(itemTag);
		}
	}

	public void createConfig(String name)
	{
		if(this.slots[0] != null)
		{
			NBTTagCompound itemTag = this.slots[0].hasTagCompound() ? this.slots[0].getTagCompound() : new NBTTagCompound();
			NBTTagList list = itemTag.hasKey("TSMConfigs") ? itemTag.getTagList("TSMConfigs", NBT.TAG_COMPOUND) : new NBTTagList();
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("ConfigName", name);
			nbt.setInteger("ConfigId", UtilSpotLight.addConfig(getTags(name)));
			list.appendTag(nbt);
			itemTag.setTag("TSMConfigs", list);
			this.slots[0].setTagCompound(itemTag);
		}
	}

	public NBTTagCompound getTags(String name)
	{
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		if(name != null && !name.isEmpty())
		{
			nbtTagCompound.setString("ConfigName", name);
		}
		nbtTagCompound.setByte("Red", this.laserRed);
		nbtTagCompound.setByte("Green", this.laserGreen);
		nbtTagCompound.setByte("Blue", this.laserBlue);
		nbtTagCompound.setByte("SecRed", this.laserSecRed);
		nbtTagCompound.setByte("SecGreen", this.laserSecGreen);
		nbtTagCompound.setByte("SecBlue", this.laserSecBlue);
		if(this.laserMainTexture != null && !this.laserMainTexture.isEmpty())
		{
			nbtTagCompound.setString("TextureName", this.laserMainTexture);
		}
		if(this.laserSecTexture != null && !this.laserSecTexture.isEmpty())
		{
			nbtTagCompound.setString("SecTextureName", this.laserSecTexture);
		}
		nbtTagCompound.setInteger("Angle1", this.laserAngle1);
		nbtTagCompound.setByte("Angle2", this.laserAngle2);
		nbtTagCompound.setBoolean("AutoRotate", this.laserAutoRotate);
		nbtTagCompound.setBoolean("ReverseRotation", this.laserReverseRotation);
		nbtTagCompound.setByte("RotationSpeed", this.laserRotationSpeed);
		nbtTagCompound.setBoolean("SecondaryLaser", this.laserSecondary);
		nbtTagCompound.setByte("LastKeySelected", this.tilelineLastKeySelected);
		nbtTagCompound.setBoolean("TimeLineEnabled", this.timelineEnabled);
		nbtTagCompound.setInteger("Time", this.timelineTime);
		nbtTagCompound.setBoolean("SmoothMode", this.timelineSmooth);
		nbtTagCompound.setByte("CreateKeyTime", this.timelineCreateKeyTime);
		nbtTagCompound.setByte("DisplayAxe", this.laserDisplayAxe);
		nbtTagCompound.setBoolean("SideLaser", this.laserDouble);
		nbtTagCompound.setByte("MainLaserSize", this.laserMainSize);
		nbtTagCompound.setByte("SecLaserSize", this.laserSecSize);
		nbtTagCompound.setInteger("LazerHeight", this.laserHeight);
		if(this.text != null && !this.text.isEmpty())
		{
			nbtTagCompound.setString("DisplayText", this.text);
		}
		nbtTagCompound.setBoolean("TextEnabled", this.textEnabled);
		nbtTagCompound.setByte("TxtRed", this.textRed);
		nbtTagCompound.setByte("TxtGreen", this.textGreen);
		nbtTagCompound.setByte("TxtBlue", this.textBlue);
		nbtTagCompound.setInteger("TxtAngle1", this.textAngle1);
		nbtTagCompound.setBoolean("TxtAutoRotate", this.textAutoRotate);
		nbtTagCompound.setBoolean("TxtReverseRotation", this.textReverseRotation);
		nbtTagCompound.setByte("TxtRotationSpeed", this.textRotationSpeed);
		nbtTagCompound.setByte("TxtScale", this.textScale);
		nbtTagCompound.setByte("TxtHeight", this.textHeight);
		nbtTagCompound.setByte("Sides", this.laserSidesNumber);

		nbtTagCompound.setByteArray("RedKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERRED));
		nbtTagCompound.setByteArray("GreenKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERGREEN));
		nbtTagCompound.setByteArray("BlueKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERBLUE));
		nbtTagCompound.setByteArray("SecRedKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERSECRED));
		nbtTagCompound.setByteArray("SecGreenKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERSECGREEN));
		nbtTagCompound.setByteArray("SecBlueKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERSECBLUE));
		nbtTagCompound.setIntArray("Angle1Key", (int[])this.keysMap.get(EnumLaserInformations.LASERANGLE1));
		nbtTagCompound.setByteArray("Angle2Key", (byte[])this.keysMap.get(EnumLaserInformations.LASERANGLE2));
		nbtTagCompound.setByteArray("MainLazerSize", (byte[])this.keysMap.get(EnumLaserInformations.LASERMAINSIZE));
		nbtTagCompound.setByteArray("SecLazerSize", (byte[])this.keysMap.get(EnumLaserInformations.LASERSECSIZE));
		nbtTagCompound.setIntArray("LazerHeightKey", (int[])this.keysMap.get(EnumLaserInformations.LASERHEIGHT));
		nbtTagCompound.setByteArray("SidesKey", (byte[])this.keysMap.get(EnumLaserInformations.LASERSIDESNUMBER));
		nbtTagCompound.setByteArray("TxtRedKey", (byte[])this.keysMap.get(EnumLaserInformations.TEXTRED));
		nbtTagCompound.setByteArray("TxtGreenKey", (byte[])this.keysMap.get(EnumLaserInformations.TEXTGREEN));
		nbtTagCompound.setByteArray("TxtBlueKey", (byte[])this.keysMap.get(EnumLaserInformations.TEXTBLUE));
		nbtTagCompound.setIntArray("TxtAngle1Key", (int[])this.keysMap.get(EnumLaserInformations.TEXTANGLE1));
		nbtTagCompound.setByteArray("TxtScaleKey", (byte[])this.keysMap.get(EnumLaserInformations.TEXTSCALE));
		nbtTagCompound.setByteArray("TxtHeightKey", (byte[])this.keysMap.get(EnumLaserInformations.TEXTHEIGHT));

		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < this.keyList.length; ++i)
		{
			if(this.keyList[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Key", (byte)i);
				this.keyList[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbtTagCompound.setTag("SpotLightKeys", nbttaglist);
		return nbtTagCompound;
	}

	@Override
	public int getSizeInventory()
	{
		return this.slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.slots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if(this.slots[slot] != null)
		{
			ItemStack itemstack;
			if(this.slots[slot].stackSize <= amount)
			{
				itemstack = this.slots[slot];
				this.slots[slot] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.slots[slot].splitStack(amount);
				if(this.slots[slot].stackSize == 0)
				{
					this.slots[slot] = null;
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
		if(this.slots[slot] != null)
		{
			ItemStack itemstack = this.slots[slot];
			this.slots[slot] = null;
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
		this.slots[slot] = stack;
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
		this.slots[0] = null;
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText("Test");
	}
}