package fr.mcnanotech.kevin_68.thespotlightmod.tileentity;

import java.util.ArrayList;

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
	private int prevHeight = -1, prevSides = -1, prevA1 = -1;
	private byte prevAxe = -1, prevA2 = -1, prevSize = -1, prevSizeSec = -1;

	public byte[] redKey = new byte[1200];
	public byte[] greenKey = new byte[1200];
	public byte[] blueKey = new byte[1200];
	public byte[] secRedKey = new byte[1200];
	public byte[] secGreenKey = new byte[1200];
	public byte[] secBlueKey = new byte[1200];
	public int[] angle1Key = new int[1200];
	public byte[] angle2Key = new byte[1200];
	public byte[] mainSizeKey = new byte[1200];
	public byte[] secSizeKey = new byte[1200];
	public int[] lazerHeightKey = new int[1200];
	public byte[] txtRedKey = new byte[1200];
	public byte[] txtGreenKey = new byte[1200];
	public byte[] txtBlueKey = new byte[1200];
	public int[] txtAngle1Key = new int[1200];
	public byte[] txtScaleKey = new byte[1200];
	public byte[] txtHeightKey = new byte[1200];
	public byte[] sidesKey = new byte[1200];

	private SpotLightEntry[] keyList = new SpotLightEntry[120];

	@Override
	public void update()
	{
		if(worldObj.isBlockPowered(pos))
		{
			isActive = true;

			// checkBlocks();// TODO next

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

	private void checkBlocks()
	{
		if(bVec != null)
		{
			TSMVec3 v = bVec[0].getLenVec();
			v.getLaserBlocksCrossedByVector(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ());
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
				set(EnumLaserInformations.LASERRED, redKey[timelineTime]);
				set(EnumLaserInformations.LASERGREEN, greenKey[timelineTime]);
				set(EnumLaserInformations.LASERBLUE, blueKey[timelineTime]);
				set(EnumLaserInformations.LASERSECRED, secRedKey[timelineTime]);
				set(EnumLaserInformations.LASERSECGREEN, secGreenKey[timelineTime]);
				set(EnumLaserInformations.LASERSECBLUE, secBlueKey[timelineTime]);
				set(EnumLaserInformations.LASERANGLE1, angle1Key[timelineTime]);
				set(EnumLaserInformations.LASERANGLE2, angle2Key[timelineTime]);
				set(EnumLaserInformations.LASERMAINSIZE, mainSizeKey[timelineTime]);
				set(EnumLaserInformations.LASERSECSIZE, secSizeKey[timelineTime]);
				set(EnumLaserInformations.LASERHEIGHT, lazerHeightKey[timelineTime]);
				set(EnumLaserInformations.TEXTRED, txtRedKey[timelineTime]);
				set(EnumLaserInformations.TEXTGREEN, txtGreenKey[timelineTime]);
				set(EnumLaserInformations.TEXTBLUE, txtBlueKey[timelineTime]);
				set(EnumLaserInformations.TEXTANGLE1, txtAngle1Key[timelineTime]);
				set(EnumLaserInformations.LASERSIDESNUMBER, sidesKey[timelineTime]);
				int curTime = timelineTime / 10;
				if(getKey(curTime) != null && lastTimeUse != timelineTime)
				{
					lastTimeUse = timelineTime;
					worldObj.markBlockForUpdate(pos);
					set(EnumLaserInformations.LASERAUTOROTATE, getKey(curTime).isKeyAutRot());
					set(EnumLaserInformations.LASERREVERSEROTATION, getKey(curTime).isKeyRevRot());
					set(EnumLaserInformations.LASERROTATIONSPEED, getKey(curTime).getKeyRotSpe());
					set(EnumLaserInformations.LASERSECONDARY, getKey(curTime).isKeySecLas());
					set(EnumLaserInformations.LASERDISPLAYAXE, getKey(curTime).getKeyDisplayAxe());
					set(EnumLaserInformations.LASERDOUBLE, getKey(curTime).isSideLaser());
					set(EnumLaserInformations.TEXTENABLED, getKey(curTime).isKeyTextEnabled());
					set(EnumLaserInformations.TEXTAUTOROTATE, getKey(curTime).isTxtAutoRotate());
					set(EnumLaserInformations.TEXTREVERSEROTATION, getKey(curTime).isTxtReverseRotation());
					set(EnumLaserInformations.TEXTROTATIONSPEED, getKey(curTime).getTxtRotationSpeed());
				}
			}
			else
			{
				int curTime = timelineTime / 10;
				if(getKey(curTime) != null && lastTimeUse != timelineTime)
				{
					lastTimeUse = timelineTime;
					set(EnumLaserInformations.LASERRED, getKey(curTime).getKeyRed());
					set(EnumLaserInformations.LASERGREEN, getKey(curTime).getKeyGreen());
					set(EnumLaserInformations.LASERBLUE, getKey(curTime).getKeyBlue());
					set(EnumLaserInformations.LASERSECRED, getKey(curTime).getKeySecRed());
					set(EnumLaserInformations.LASERSECGREEN, getKey(curTime).getKeySecGreen());
					set(EnumLaserInformations.LASERSECBLUE, getKey(curTime).getKeySecBlue());
					set(EnumLaserInformations.LASERANGLE1, getKey(curTime).getKeyAngle1());
					set(EnumLaserInformations.LASERANGLE2, getKey(curTime).getKeyAngle2());
					set(EnumLaserInformations.LASERAUTOROTATE, getKey(curTime).isKeyAutRot());
					set(EnumLaserInformations.LASERREVERSEROTATION, getKey(curTime).isKeyRevRot());
					set(EnumLaserInformations.LASERROTATIONSPEED, getKey(curTime).getKeyRotSpe());
					set(EnumLaserInformations.LASERSECONDARY, getKey(curTime).isKeySecLas());
					set(EnumLaserInformations.LASERDISPLAYAXE, getKey(curTime).getKeyDisplayAxe());
					set(EnumLaserInformations.LASERDOUBLE, getKey(curTime).isSideLaser());
					set(EnumLaserInformations.LASERMAINSIZE, getKey(curTime).getKeyMainLaserSize());
					set(EnumLaserInformations.LASERSECSIZE, getKey(curTime).getKeySecLaserSize());
					set(EnumLaserInformations.LASERHEIGHT, getKey(curTime).getKeyLaserHeight());
					set(EnumLaserInformations.TEXTENABLED, getKey(curTime).isKeyTextEnabled());
					set(EnumLaserInformations.TEXTRED, getKey(curTime).getKeyTxtRed());
					set(EnumLaserInformations.TEXTGREEN, getKey(curTime).getKeyTxtGreen());
					set(EnumLaserInformations.TEXTBLUE, getKey(curTime).getKeyTxtBlue());
					set(EnumLaserInformations.TEXTANGLE1, getKey(curTime).getTxtAngle1());
					set(EnumLaserInformations.TEXTAUTOROTATE, getKey(curTime).isTxtAutoRotate());
					set(EnumLaserInformations.TEXTREVERSEROTATION, getKey(curTime).isTxtReverseRotation());
					set(EnumLaserInformations.TEXTROTATIONSPEED, getKey(curTime).getTxtRotationSpeed());
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
				int startRed = keyList[keys.get(k) / 10].getKeyRed() & 0xFF;
				int endRed = keyList[keys.get(k + 1) / 10].getKeyRed() & 0xFF;
				int deltaRed = endRed - startRed;
				float tickRed = (float)deltaRed / (float)timeBetwinKeys.get(k);

				int startGreen = keyList[keys.get(k) / 10].getKeyGreen() & 0xFF;
				int endGreen = keyList[keys.get(k + 1) / 10].getKeyGreen() & 0xFF;
				int deltaGreen = endGreen - startGreen;
				float tickGreen = (float)deltaGreen / (float)timeBetwinKeys.get(k);

				int startBlue = keyList[keys.get(k) / 10].getKeyBlue() & 0xFF;
				int endBlue = keyList[keys.get(k + 1) / 10].getKeyBlue() & 0xFF;
				int deltaBlue = endBlue - startBlue;
				float tickBlue = (float)deltaBlue / (float)timeBetwinKeys.get(k);

				int startSecRed = keyList[keys.get(k) / 10].getKeySecRed() & 0xFF;
				int endSecRed = keyList[keys.get(k + 1) / 10].getKeySecRed() & 0xFF;
				int deltaSecRed = endSecRed - startSecRed;
				float tickSecRed = (float)deltaSecRed / (float)timeBetwinKeys.get(k);

				int startSecGreen = keyList[keys.get(k) / 10].getKeySecGreen() & 0xFF;
				int endSecGreen = keyList[keys.get(k + 1) / 10].getKeySecGreen() & 0xFF;
				int deltaSecGreen = endSecGreen - startSecGreen;
				float tickSecGreen = (float)deltaSecGreen / (float)timeBetwinKeys.get(k);

				int startSecBlue = keyList[keys.get(k) / 10].getKeySecBlue() & 0xFF;
				int endSecBlue = keyList[keys.get(k + 1) / 10].getKeySecBlue() & 0xFF;
				int deltaSecBlue = endSecBlue - startSecBlue;
				float tickSecBlue = (float)deltaSecBlue / (float)timeBetwinKeys.get(k);

				int startAngle1 = keyList[keys.get(k) / 10].getKeyAngle1();
				int endAngle1 = keyList[keys.get(k + 1) / 10].getKeyAngle1();
				int deltaAngle1 = endAngle1 - startAngle1;
				float tickAngle1 = (float)deltaAngle1 / (float)timeBetwinKeys.get(k);

				int startAngle2 = keyList[keys.get(k) / 10].getKeyAngle2() & 0xFF;
				int endAngle2 = keyList[keys.get(k + 1) / 10].getKeyAngle2() & 0xFF;
				int deltaAngle2 = endAngle2 - startAngle2;
				float tickAngle2 = (float)deltaAngle2 / (float)timeBetwinKeys.get(k);

				int startMainSize = keyList[keys.get(k) / 10].getKeyMainLaserSize() & 0xFF;
				int endMainSize = keyList[keys.get(k + 1) / 10].getKeyMainLaserSize() & 0xFF;
				int deltaMainSize = endMainSize - startMainSize;
				float tickMainSize = (float)deltaMainSize / (float)timeBetwinKeys.get(k);

				int startSecSize = keyList[keys.get(k) / 10].getKeySecLaserSize() & 0xFF;
				int endSecSize = keyList[keys.get(k + 1) / 10].getKeySecLaserSize() & 0xFF;
				int deltaSecSize = endSecSize - startSecSize;
				float tickSecSize = (float)deltaSecSize / (float)timeBetwinKeys.get(k);

				int startLaserHeight = keyList[keys.get(k) / 10].getKeyLaserHeight();
				int endLaserHeight = keyList[keys.get(k + 1) / 10].getKeyLaserHeight();
				int deltaLaserHeight = endLaserHeight - startLaserHeight;
				float tickLaserHeight = (float)deltaLaserHeight / (float)timeBetwinKeys.get(k);

				int startTxtRed = keyList[keys.get(k) / 10].getKeyTxtRed() & 0xFF;
				int endTxtRed = keyList[keys.get(k + 1) / 10].getKeyTxtRed() & 0xFF;
				int deltaTxtRed = endTxtRed - startTxtRed;
				float tickTxtRed = (float)deltaTxtRed / (float)timeBetwinKeys.get(k);

				int startTxtGreen = keyList[keys.get(k) / 10].getKeyTxtGreen() & 0xFF;
				int endTxtGreen = keyList[keys.get(k + 1) / 10].getKeyTxtGreen() & 0xFF;
				int deltaTxtGreen = endTxtGreen - startTxtGreen;
				float tickTxtGreen = (float)deltaTxtGreen / (float)timeBetwinKeys.get(k);

				int startTxtBlue = keyList[keys.get(k) / 10].getKeyTxtBlue() & 0xFF;
				int endTxtBlue = keyList[keys.get(k + 1) / 10].getKeyTxtBlue() & 0xFF;
				int deltaTxtBlue = endTxtBlue - startTxtBlue;
				float tickTxtBlue = (float)deltaTxtBlue / (float)timeBetwinKeys.get(k);

				int startTxtAngle1 = keyList[keys.get(k) / 10].getTxtAngle1();
				int endTxtAngle1 = keyList[keys.get(k + 1) / 10].getTxtAngle1();
				int deltaTxtAngle1 = endTxtAngle1 - startTxtAngle1;
				float tickTxtAngle1 = (float)deltaTxtAngle1 / (float)timeBetwinKeys.get(k);

				int startTxtScale = keyList[keys.get(k) / 10].getTxtScale() & 0xFF;
				int endTxtScale = keyList[keys.get(k + 1) / 10].getTxtScale() & 0xFF;
				int deltaTxtScale = endTxtScale - startTxtScale;
				float tickTxtScale = (float)deltaTxtScale / (float)timeBetwinKeys.get(k);

				int startTxtHeight = keyList[keys.get(k) / 10].getTxtHeight() & 0xFF;
				int endTxtHeight = keyList[keys.get(k + 1) / 10].getTxtHeight() & 0xFF;
				int deltaTxtHeight = endTxtHeight - startTxtHeight;
				float tickTxtHeight = (float)deltaTxtHeight / (float)timeBetwinKeys.get(k);

				int startSides = keyList[keys.get(k) / 10].getSides() & 0xFF;
				int endSides = keyList[keys.get(k + 1) / 10].getSides() & 0xFF;
				int deltaSides = endSides - startSides;
				float tickSides = (float)deltaSides / (float)timeBetwinKeys.get(k);

				for(int l = keys.get(k); l < keys.get(k + 1); l++)
				{
					redKey[l] = (byte)(startRed + tickRed * (l - keys.get(k)));
					greenKey[l] = (byte)(startGreen + tickGreen * (l - keys.get(k)));
					blueKey[l] = (byte)(startBlue + tickBlue * (l - keys.get(k)));
					secRedKey[l] = (byte)(startSecRed + tickSecRed * (l - keys.get(k)));
					secGreenKey[l] = (byte)(startSecGreen + tickSecGreen * (l - keys.get(k)));
					secBlueKey[l] = (byte)(startSecBlue + tickSecBlue * (l - keys.get(k)));
					angle1Key[l] = (int)(startAngle1 + tickAngle1 * (l - keys.get(k)));
					angle2Key[l] = (byte)(startAngle2 + tickAngle2 * (l - keys.get(k)));
					mainSizeKey[l] = (byte)(startMainSize + tickMainSize * (l - keys.get(k)));
					secSizeKey[l] = (byte)(startSecSize + tickSecSize * (l - keys.get(k)));
					lazerHeightKey[l] = (int)(startLaserHeight + tickLaserHeight * (l - keys.get(k)));
					txtRedKey[l] = (byte)(startTxtRed + tickTxtRed * (l - keys.get(k)));
					txtGreenKey[l] = (byte)(startTxtGreen + tickTxtGreen * (l - keys.get(k)));
					txtBlueKey[l] = (byte)(startTxtBlue + tickTxtBlue * (l - keys.get(k)));
					txtAngle1Key[l] = (int)(startTxtAngle1 + tickTxtAngle1 * (l - keys.get(k)));
					txtScaleKey[l] = (byte)(startTxtScale + tickTxtScale * (l - keys.get(k)));
					txtHeightKey[l] = (byte)(startTxtHeight + tickTxtHeight * (l - keys.get(k)));
					sidesKey[l] = (byte)(startSides + tickSides * (l - keys.get(k)));
				}
			}

			int startRed = keyList[keys.get(keys.size() - 1) / 10].getKeyRed() & 0xFF;
			int endRed = keyList[keys.get(0) / 10].getKeyRed() & 0xFF;
			int deltaRed = endRed - startRed;
			float tickRed = (float)deltaRed / (float)timeBetwinKeys.get(keys.size() - 1);

			int startGreen = keyList[keys.get(keys.size() - 1) / 10].getKeyGreen() & 0xFF;
			int endGreen = keyList[keys.get(0) / 10].getKeyGreen() & 0xFF;
			int deltaGreen = endGreen - startGreen;
			float tickGreen = (float)deltaGreen / (float)timeBetwinKeys.get(keys.size() - 1);

			int startBlue = keyList[keys.get(keys.size() - 1) / 10].getKeyBlue() & 0xFF;
			int endBlue = keyList[keys.get(0) / 10].getKeyBlue() & 0xFF;
			int deltaBlue = endBlue - startBlue;
			float tickBlue = (float)deltaBlue / (float)timeBetwinKeys.get(keys.size() - 1);

			int startSecRed = keyList[keys.get(keys.size() - 1) / 10].getKeySecRed() & 0xFF;
			int endSecRed = keyList[keys.get(0) / 10].getKeySecRed() & 0xFF;
			int deltaSecRed = endSecRed - startSecRed;
			float tickSecRed = (float)deltaSecRed / (float)timeBetwinKeys.get(keys.size() - 1);

			int startSecGreen = keyList[keys.get(keys.size() - 1) / 10].getKeySecGreen() & 0xFF;
			int endSecGreen = keyList[keys.get(0) / 10].getKeySecGreen() & 0xFF;
			int deltaSecGreen = endSecGreen - startSecGreen;
			float tickSecGreen = (float)deltaSecGreen / (float)timeBetwinKeys.get(keys.size() - 1);

			int startSecBlue = keyList[keys.get(keys.size() - 1) / 10].getKeySecBlue() & 0xFF;
			int endSecBlue = keyList[keys.get(0) / 10].getKeySecBlue() & 0xFF;
			int deltaSecBlue = endSecBlue - startSecBlue;
			float tickSecBlue = (float)deltaSecBlue / (float)timeBetwinKeys.get(keys.size() - 1);

			int startAngle1 = keyList[keys.get(keys.size() - 1) / 10].getKeyAngle1();
			int endAngle1 = keyList[keys.get(0) / 10].getKeyAngle1();
			int deltaAngle1 = endAngle1 - startAngle1;
			float tickAngle1 = (float)deltaAngle1 / (float)timeBetwinKeys.get(keys.size() - 1);

			int startAngle2 = keyList[keys.get(keys.size() - 1) / 10].getKeyAngle2() & 0xFF;
			int endAngle2 = keyList[keys.get(0) / 10].getKeyAngle2() & 0xFF;
			int deltaAngle2 = endAngle2 - startAngle2;
			float tickAngle2 = (float)deltaAngle2 / (float)timeBetwinKeys.get(keys.size() - 1);

			int startMainSize = keyList[keys.get(keys.size() - 1) / 10].getKeyMainLaserSize() & 0xFF;
			int endMainSize = keyList[keys.get(0) / 10].getKeyMainLaserSize() & 0xFF;
			int deltaMainSize = endMainSize - startMainSize;
			float tickMainSize = (float)deltaMainSize / (float)timeBetwinKeys.get(keys.size() - 1);

			int startSecSize = keyList[keys.get(keys.size() - 1) / 10].getKeySecLaserSize() & 0xFF;
			int endSecSize = keyList[keys.get(0) / 10].getKeySecLaserSize() & 0xFF;
			int deltaSecSize = endSecSize - startSecSize;
			float tickSecSize = (float)deltaSecSize / (float)timeBetwinKeys.get(keys.size() - 1);

			int startLaserHeight = keyList[keys.get(keys.size() - 1) / 10].getKeyLaserHeight();
			int endLaserHeight = keyList[keys.get(0) / 10].getKeyLaserHeight();
			int deltaLaserHeight = endLaserHeight - startLaserHeight;
			float tickLaserHeight = (float)deltaLaserHeight / (float)timeBetwinKeys.get(keys.size() - 1);

			int startTxtRed = keyList[keys.get(keys.size() - 1) / 10].getKeyTxtRed() & 0xFF;
			int endTxtRed = keyList[keys.get(0) / 10].getKeyTxtRed() & 0xFF;
			int deltaTxtRed = endTxtRed - startTxtRed;
			float tickTxtRed = (float)deltaTxtRed / (float)timeBetwinKeys.get(keys.size() - 1);

			int startTxtGreen = keyList[keys.get(keys.size() - 1) / 10].getKeyTxtGreen() & 0xFF;
			int endTxtGreen = keyList[keys.get(0) / 10].getKeyTxtGreen() & 0xFF;
			int deltaTxtGreen = endTxtGreen - startTxtGreen;
			float tickTxtGreen = (float)deltaTxtGreen / (float)timeBetwinKeys.get(keys.size() - 1);

			int startTxtBlue = keyList[keys.get(keys.size() - 1) / 10].getKeyTxtBlue() & 0xFF;
			int endTxtBlue = keyList[keys.get(0) / 10].getKeyTxtBlue() & 0xFF;
			int deltaTxtBlue = endTxtBlue - startTxtBlue;
			float tickTxtBlue = (float)deltaTxtBlue / (float)timeBetwinKeys.get(keys.size() - 1);

			int startTxtAngle1 = keyList[keys.get(keys.size() - 1) / 10].getTxtAngle1();
			int endTxtAngle1 = keyList[keys.get(0) / 10].getTxtAngle1();
			int deltaTxtAngle1 = endTxtAngle1 - startTxtAngle1;
			float tickTxtAngle1 = (float)deltaTxtAngle1 / (float)timeBetwinKeys.get(keys.size() - 1);

			int startTxtScale = keyList[keys.get(keys.size() - 1) / 10].getTxtScale() & 0xFF;
			int endTxtScale = keyList[keys.get(0) / 10].getTxtScale() & 0xFF;
			int deltaTxtScale = endTxtScale - startTxtBlue;
			float tickTxtScale = (float)deltaTxtScale / (float)timeBetwinKeys.get(keys.size() - 1);

			int startTxtHeight = keyList[keys.get(keys.size() - 1) / 10].getTxtHeight() & 0xFF;
			int endTxtHeight = keyList[keys.get(0) / 10].getTxtHeight() & 0xFF;
			int deltaTxtHeight = endTxtHeight - startTxtHeight;
			float tickTxtHeight = (float)deltaTxtHeight / (float)timeBetwinKeys.get(keys.size() - 1);

			int startSides = keyList[keys.get(keys.size() - 1) / 10].getSides() & 0xFF;
			int endSides = keyList[keys.get(0) / 10].getSides() & 0xFF;
			int deltaSides = endSides - startSides;
			float tickSides = (float)deltaSides / (float)timeBetwinKeys.get(keys.size() - 1);

			for(int m = keys.get(keys.size() - 1); m < 1200; m++)
			{
				redKey[m] = (byte)(startRed + tickRed * (m - keys.get(keys.size() - 1)));
				greenKey[m] = (byte)(startGreen + tickGreen * (m - keys.get(keys.size() - 1)));
				blueKey[m] = (byte)(startBlue + tickBlue * (m - keys.get(keys.size() - 1)));
				secRedKey[m] = (byte)(startSecRed + tickSecRed * (m - keys.get(keys.size() - 1)));
				secGreenKey[m] = (byte)(startSecGreen + tickSecGreen * (m - keys.get(keys.size() - 1)));
				secBlueKey[m] = (byte)(startSecBlue + tickSecBlue * (m - keys.get(keys.size() - 1)));
				angle1Key[m] = (int)(startAngle1 + tickAngle1 * (m - keys.get(keys.size() - 1)));
				angle2Key[m] = (byte)(startAngle2 + tickAngle2 * (m - keys.get(keys.size() - 1)));
				mainSizeKey[m] = (byte)(startMainSize + tickMainSize * (m - keys.get(keys.size() - 1)));
				secSizeKey[m] = (byte)(startSecSize + tickSecSize * (m - keys.get(keys.size() - 1)));
				lazerHeightKey[m] = (int)(startLaserHeight + tickLaserHeight * (m - keys.get(keys.size() - 1)));
				txtRedKey[m] = (byte)(startTxtRed + tickTxtRed * (m - keys.get(keys.size() - 1)));
				txtGreenKey[m] = (byte)(startTxtGreen + tickTxtGreen * (m - keys.get(keys.size() - 1)));
				txtBlueKey[m] = (byte)(startTxtBlue + tickTxtBlue * (m - keys.get(keys.size() - 1)));
				txtAngle1Key[m] = (int)(startTxtAngle1 + tickTxtAngle1 * (m - keys.get(keys.size() - 1)));
				txtScaleKey[m] = (byte)(startTxtScale + tickTxtScale * (m - keys.get(keys.size() - 1)));
				txtHeightKey[m] = (byte)(startTxtHeight + tickTxtHeight * (m - keys.get(keys.size() - 1)));
				sidesKey[m] = (byte)(startSides + tickSides * (m - keys.get(keys.size() - 1)));
			}
			for(int n = 0; n < keys.get(0); n++)
			{
				redKey[n] = (byte)(redKey[1199] + tickRed * n);
				greenKey[n] = (byte)(greenKey[1199] + tickGreen * n);
				blueKey[n] = (byte)(blueKey[1199] + tickBlue * n);
				secRedKey[n] = (byte)(secRedKey[1199] + tickSecRed * n);
				secGreenKey[n] = (byte)(secGreenKey[1199] + tickSecGreen * n);
				secBlueKey[n] = (byte)(secBlueKey[1199] + tickSecBlue * n);
				angle1Key[n] = (int)(angle1Key[1199] + tickAngle1 * n);
				angle2Key[n] = (byte)(angle2Key[1199] + tickAngle2 * n);
				mainSizeKey[n] = (byte)(mainSizeKey[1199] + tickMainSize * n);
				secSizeKey[n] = (byte)(secSizeKey[1199] + tickSecSize * n);
				lazerHeightKey[n] = (int)(lazerHeightKey[1199] + tickLaserHeight * n);
				txtRedKey[n] = (byte)(txtRedKey[1199] + tickTxtRed * n);
				txtGreenKey[n] = (byte)(txtGreenKey[1199] + tickTxtGreen * n);
				txtBlueKey[n] = (byte)(txtBlueKey[1199] + tickTxtBlue * n);
				txtAngle1Key[n] = (int)(txtAngle1Key[1199] + tickTxtAngle1 * n);
				txtScaleKey[n] = (byte)(txtScaleKey[1199] + tickTxtScale * n);
				txtHeightKey[n] = (byte)(txtHeightKey[1199] + tickTxtHeight * n);
				sidesKey[n] = (byte)(sidesKey[1199] + tickSides * n);
			}
		}
		else if(keys.size() == 1)
		{
			for(int i = 0; i < 1200; i++)
			{
				redKey[i] = keyList[keys.get(0) / 10].getKeyRed();
				greenKey[i] = keyList[keys.get(0) / 10].getKeyGreen();
				blueKey[i] = keyList[keys.get(0) / 10].getKeyBlue();
				secRedKey[i] = keyList[keys.get(0) / 10].getKeySecRed();
				secGreenKey[i] = keyList[keys.get(0) / 10].getKeySecGreen();
				secBlueKey[i] = keyList[keys.get(0) / 10].getKeySecBlue();
				angle1Key[i] = keyList[keys.get(0) / 10].getKeyAngle1();
				angle2Key[i] = keyList[keys.get(0) / 10].getKeyAngle2();
				mainSizeKey[i] = keyList[keys.get(0) / 10].getKeyMainLaserSize();
				secSizeKey[i] = keyList[keys.get(0) / 10].getKeySecLaserSize();
				lazerHeightKey[i] = keyList[keys.get(0) / 10].getKeyLaserHeight();
				txtRedKey[i] = keyList[keys.get(0) / 10].getKeyTxtRed();
				txtGreenKey[i] = keyList[keys.get(0) / 10].getKeyTxtGreen();
				txtBlueKey[i] = keyList[keys.get(0) / 10].getKeyTxtBlue();
				txtAngle1Key[i] = keyList[keys.get(0) / 10].getTxtAngle1();
				txtScaleKey[i] = keyList[keys.get(0) / 10].getTxtScale();
				txtHeightKey[i] = keyList[keys.get(0) / 10].getTxtHeight();
				sidesKey[i] = keyList[keys.get(0) / 10].getSides();
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

		nbtTagCompound.setByteArray("RedKey", redKey);
		nbtTagCompound.setByteArray("GreenKey", greenKey);
		nbtTagCompound.setByteArray("BlueKey", blueKey);
		nbtTagCompound.setByteArray("SecRedKey", secRedKey);
		nbtTagCompound.setByteArray("SecGreenKey", secGreenKey);
		nbtTagCompound.setByteArray("SecBlueKey", secBlueKey);
		nbtTagCompound.setIntArray("Angle1Key", angle1Key);
		nbtTagCompound.setByteArray("Angle2Key", angle2Key);
		nbtTagCompound.setByteArray("MainLazerSize", mainSizeKey);
		nbtTagCompound.setByteArray("SecLazerSize", secSizeKey);
		nbtTagCompound.setIntArray("LazerHeightKey", lazerHeightKey);
		nbtTagCompound.setByteArray("TxtRedKey", txtRedKey);
		nbtTagCompound.setByteArray("TxtGreenKey", txtGreenKey);
		nbtTagCompound.setByteArray("TxtBlueKey", txtBlueKey);
		nbtTagCompound.setIntArray("TxtAngle1Key", txtAngle1Key);
		nbtTagCompound.setByteArray("TxtScaleKey", txtScaleKey);
		nbtTagCompound.setByteArray("TxtHeightKey", txtHeightKey);
		nbtTagCompound.setByteArray("SidesKey", sidesKey);

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

		redKey = nbtTagCompound.getByteArray("RedKey");
		greenKey = nbtTagCompound.getByteArray("GreenKey");
		blueKey = nbtTagCompound.getByteArray("BlueKey");
		secRedKey = nbtTagCompound.getByteArray("SecRedKey");
		secGreenKey = nbtTagCompound.getByteArray("SecGreenKey");
		secBlueKey = nbtTagCompound.getByteArray("SecBlueKey");
		angle1Key = nbtTagCompound.getIntArray("Angle1Key");
		angle2Key = nbtTagCompound.getByteArray("Angle2Key");
		mainSizeKey = nbtTagCompound.getByteArray("MainLazerSize");
		secSizeKey = nbtTagCompound.getByteArray("SecLazerSize");
		lazerHeightKey = nbtTagCompound.getIntArray("LazerHeightKey");
		txtRedKey = nbtTagCompound.getByteArray("TxtRedKey");
		txtGreenKey = nbtTagCompound.getByteArray("TxtGreenKey");
		txtBlueKey = nbtTagCompound.getByteArray("TxtBlueKey");
		txtAngle1Key = nbtTagCompound.getIntArray("TxtAngle1Key");
		txtScaleKey = nbtTagCompound.getByteArray("TxtScaleKey");
		txtHeightKey = nbtTagCompound.getByteArray("TxtHeightKey");
		sidesKey = nbtTagCompound.getByteArray("SidesKey");

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
		redKey = new byte[1200];
		greenKey = new byte[1200];
		blueKey = new byte[1200];
		secRedKey = new byte[1200];
		secGreenKey = new byte[1200];
		secBlueKey = new byte[1200];
		angle1Key = new int[1200];
		angle2Key = new byte[1200];
		mainSizeKey = new byte[1200];
		secSizeKey = new byte[1200];
		lazerHeightKey = new int[1200];
		txtRedKey = new byte[1200];
		txtGreenKey = new byte[1200];
		txtBlueKey = new byte[1200];
		txtAngle1Key = new int[1200];
		txtScaleKey = new byte[1200];
		txtHeightKey = new byte[1200];
		sidesKey = new byte[1200];
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

				redKey = conf.getByteArray("RedKey");
				greenKey = conf.getByteArray("GreenKey");
				blueKey = conf.getByteArray("BlueKey");
				secRedKey = conf.getByteArray("SecRedKey");
				secGreenKey = conf.getByteArray("SecGreenKey");
				secBlueKey = conf.getByteArray("SecBlueKey");
				angle1Key = conf.getIntArray("Angle1Key");
				angle2Key = conf.getByteArray("Angle2Key");
				mainSizeKey = conf.getByteArray("MainLazerSize");
				secSizeKey = conf.getByteArray("SecLazerSize");
				lazerHeightKey = conf.getIntArray("LazerHeightKey");
				txtRedKey = conf.getByteArray("TxtRedKey");
				txtGreenKey = conf.getByteArray("TxtGreenKey");
				txtBlueKey = conf.getByteArray("TxtBlueKey");
				txtAngle1Key = conf.getIntArray("TxtAngle1Key");
				txtScaleKey = conf.getByteArray("TxtScaleKey");
				txtHeightKey = conf.getByteArray("TxtHeightKey");
				sidesKey = conf.getByteArray("SidesKey");

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

		nbtTagCompound.setByteArray("RedKey", redKey);
		nbtTagCompound.setByteArray("GreenKey", greenKey);
		nbtTagCompound.setByteArray("BlueKey", blueKey);
		nbtTagCompound.setByteArray("SecRedKey", secRedKey);
		nbtTagCompound.setByteArray("SecGreenKey", secGreenKey);
		nbtTagCompound.setByteArray("SecBlueKey", secBlueKey);
		nbtTagCompound.setIntArray("Angle1Key", angle1Key);
		nbtTagCompound.setByteArray("Angle2Key", angle2Key);
		nbtTagCompound.setByteArray("MainLazerSize", mainSizeKey);
		nbtTagCompound.setByteArray("SecLazerSize", secSizeKey);
		nbtTagCompound.setIntArray("LazerHeightKey", lazerHeightKey);
		nbtTagCompound.setByteArray("TxtRedKey", txtRedKey);
		nbtTagCompound.setByteArray("TxtGreenKey", txtGreenKey);
		nbtTagCompound.setByteArray("TxtBlueKey", txtBlueKey);
		nbtTagCompound.setIntArray("TxtAngle1Key", txtAngle1Key);
		nbtTagCompound.setByteArray("TxtScaleKey", txtScaleKey);
		nbtTagCompound.setByteArray("TxtHeightKey", txtHeightKey);
		nbtTagCompound.setByteArray("SidesKey", sidesKey);

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
	public String getName()
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
