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
import fr.mcnanotech.kevin_68.thespotlightmod.utils.SpotLightEntry;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;

public class TileEntitySpotLight extends TileEntity implements IInventory, IUpdatePlayerListBox
{
	private ItemStack[] slots = new ItemStack[1];

	@SideOnly(Side.CLIENT)
	private long worldTimeClient;
	@SideOnly(Side.CLIENT)
	private float activeBooleanFloat;
	public boolean isActive;

	public byte red, green, blue, secRed, secGreen, secBlue, angle2, rotationSpeed, displayAxe, mainLaserSize, secLaserSize, createKeyTime, lastKeySelected, txtRed, txtGreen, txtBlue, txtRotationSpeed, txtScale, txtHeight, sides;
	public String textureName, secTextureName, displayText;
	public boolean autoRotate, reverseRotation, secondaryLaser, sideLaser, timeLineEnabled, smoothMode, textEnabled, txtAutoRotate, txtReverseRotation;
	public int time, lastTimeUse, angle1, lazerHeight, txtAngle1;

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
		if(this.worldObj.isBlockPowered(pos))
		{
			this.isActive = true;

			if(isTimeLineEnabled())
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
					setBoolean((byte)22, false);
				}

				if(getTime() == 1199)
				{
					set(23, 0);
				}
				else
				{
					set(23, getTime() + 1);
				}

				if(!this.worldObj.isRemote)
				{
					if(this.isSmoothMode())
					{
						this.setByte((byte)0, (redKey[getTime()]));
						this.setByte((byte)1, (greenKey[getTime()]));
						this.setByte((byte)2, (blueKey[getTime()]));
						this.setByte((byte)3, (secRedKey[getTime()]));
						this.setByte((byte)4, (secGreenKey[getTime()]));
						this.setByte((byte)5, (secBlueKey[getTime()]));
						this.set(8, (angle1Key[getTime()]));
						this.setByte((byte)9, (angle2Key[getTime()]));
						this.setByte((byte)16, (mainSizeKey[getTime()]));
						this.setByte((byte)17, (secSizeKey[getTime()]));
						this.set(25, lazerHeightKey[getTime()]);
						this.setByte((byte)28, (txtRedKey[getTime()]));
						this.setByte((byte)29, (txtGreenKey[getTime()]));
						this.setByte((byte)30, (txtBlueKey[getTime()]));
						this.set(31, (txtAngle1Key[getTime()]));
						this.setByte((byte)40, (sidesKey[getTime()]));
						int curTime = this.getTime() / 10;
						if(this.getKey(curTime) != null && lastTimeUse != time)
						{
							lastTimeUse = time;
							this.worldObj.markBlockForUpdate(pos);
							this.setBoolean((byte)10, this.getKey(curTime).isKeyAutRot());
							this.setBoolean((byte)11, this.getKey(curTime).isKeyRevRot());
							this.setByte((byte)12, this.getKey(curTime).getKeyRotSpe());
							this.setBoolean((byte)13, this.getKey(curTime).isKeySecLas());
							this.setByte((byte)14, this.getKey(curTime).getKeyDisplayAxe());
							this.setBoolean((byte)15, this.getKey(curTime).isSideLaser());
							this.setBoolean((byte)27, this.getKey(curTime).isKeyTextEnabled());

							this.setBoolean((byte)33, this.getKey(curTime).isTxtAutoRotate());
							this.setBoolean((byte)34, this.getKey(curTime).isTxtReverseRotation());
							this.setByte((byte)35, this.getKey(curTime).getTxtRotationSpeed());
						}
					}
					else
					{
						int curTime = this.getTime() / 10;
						if(this.getKey(curTime) != null && lastTimeUse != time)
						{
							lastTimeUse = time;
							this.setByte((byte)0, this.getKey(curTime).getKeyRed());
							this.setByte((byte)1, this.getKey(curTime).getKeyGreen());
							this.setByte((byte)2, this.getKey(curTime).getKeyBlue());
							this.setByte((byte)3, this.getKey(curTime).getKeySecRed());
							this.setByte((byte)4, this.getKey(curTime).getKeySecGreen());
							this.setByte((byte)5, this.getKey(curTime).getKeySecBlue());
							this.set(8, this.getKey(curTime).getKeyAngle1());
							this.setByte((byte)9, this.getKey(curTime).getKeyAngle2());
							this.setBoolean((byte)10, this.getKey(curTime).isKeyAutRot());
							this.setBoolean((byte)11, this.getKey(curTime).isKeyRevRot());
							this.setByte((byte)12, this.getKey(curTime).getKeyRotSpe());
							this.setBoolean((byte)13, this.getKey(curTime).isKeySecLas());
							this.setByte((byte)14, this.getKey(curTime).getKeyDisplayAxe());
							this.setBoolean((byte)15, this.getKey(curTime).isSideLaser());
							this.setByte((byte)16, this.getKey(curTime).getKeyMainLaserSize());
							this.setByte((byte)17, this.getKey(curTime).getKeySecLaserSize());
							this.set(25, this.getKey(curTime).getKeyLaserHeight());
							this.setBoolean((byte)27, this.getKey(curTime).isKeyTextEnabled());
							this.setByte((byte)28, this.getKey(curTime).getKeyTxtRed());
							this.setByte((byte)29, this.getKey(curTime).getKeyTxtGreen());
							this.setByte((byte)30, this.getKey(curTime).getKeyTxtBlue());
							this.set(31, this.getKey(curTime).getTxtAngle1());
							this.setBoolean((byte)33, this.getKey(curTime).isTxtAutoRotate());
							this.setBoolean((byte)34, this.getKey(curTime).isTxtReverseRotation());
							this.setByte((byte)35, this.getKey(curTime).getTxtRotationSpeed());
							this.worldObj.markBlockForUpdate(pos);
						}
					}
				}
			}
		}
		else
		{
			this.isActive = false;
		}
	}

	public void keysProcess()
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
			timeBetwinKeys.add((1200 - keys.get(keys.size() - 1) + keys.get(0)));

			for(int k = 0; k < (keys.size() - 1); k++)
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
					redKey[l] = (byte)(startRed + (tickRed * (l - keys.get(k))));
					greenKey[l] = (byte)(startGreen + (tickGreen * (l - keys.get(k))));
					blueKey[l] = (byte)(startBlue + (tickBlue * (l - keys.get(k))));
					secRedKey[l] = (byte)(startSecRed + (tickSecRed * (l - keys.get(k))));
					secGreenKey[l] = (byte)(startSecGreen + (tickSecGreen * (l - keys.get(k))));
					secBlueKey[l] = (byte)(startSecBlue + (tickSecBlue * (l - keys.get(k))));
					angle1Key[l] = (int)(startAngle1 + (tickAngle1 * (l - keys.get(k))));
					angle2Key[l] = (byte)(startAngle2 + (tickAngle2 * (l - keys.get(k))));
					mainSizeKey[l] = (byte)(startMainSize + (tickMainSize * (l - keys.get(k))));
					secSizeKey[l] = (byte)(startSecSize + (tickSecSize * (l - keys.get(k))));
					lazerHeightKey[l] = (int)(startLaserHeight + (tickLaserHeight * (l - keys.get(k))));
					txtRedKey[l] = (byte)(startTxtRed + (tickTxtRed * (l - keys.get(k))));
					txtGreenKey[l] = (byte)(startTxtGreen + (tickTxtGreen * (l - keys.get(k))));
					txtBlueKey[l] = (byte)(startTxtBlue + (tickTxtBlue * (l - keys.get(k))));
					txtAngle1Key[l] = (int)(startTxtAngle1 + (tickTxtAngle1 * (l - keys.get(k))));
					txtScaleKey[l] = (byte)(startTxtScale + (tickTxtScale * (l - keys.get(k))));
					txtHeightKey[l] = (byte)(startTxtHeight + (tickTxtHeight * (l - keys.get(k))));
					sidesKey[l] = (byte)(startSides + (tickSides * (l - keys.get(k))));
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
				redKey[m] = (byte)(startRed + (tickRed * (m - keys.get(keys.size() - 1))));
				greenKey[m] = (byte)(startGreen + (tickGreen * (m - keys.get(keys.size() - 1))));
				blueKey[m] = (byte)(startBlue + (tickBlue * (m - keys.get(keys.size() - 1))));
				secRedKey[m] = (byte)(startSecRed + (tickSecRed * (m - keys.get(keys.size() - 1))));
				secGreenKey[m] = (byte)(startSecGreen + (tickSecGreen * (m - keys.get(keys.size() - 1))));
				secBlueKey[m] = (byte)(startSecBlue + (tickSecBlue * (m - keys.get(keys.size() - 1))));
				angle1Key[m] = (int)(startAngle1 + (tickAngle1 * (m - keys.get(keys.size() - 1))));
				angle2Key[m] = (byte)(startAngle2 + (tickAngle2 * (m - keys.get(keys.size() - 1))));
				mainSizeKey[m] = (byte)(startMainSize + (tickMainSize * (m - keys.get(keys.size() - 1))));
				secSizeKey[m] = (byte)(startSecSize + (tickSecSize * (m - keys.get(keys.size() - 1))));
				lazerHeightKey[m] = (int)(startLaserHeight + (tickLaserHeight * (m - keys.get(keys.size() - 1))));
				txtRedKey[m] = (byte)(startTxtRed + (tickTxtRed * (m - keys.get(keys.size() - 1))));
				txtGreenKey[m] = (byte)(startTxtGreen + (tickTxtGreen * (m - keys.get(keys.size() - 1))));
				txtBlueKey[m] = (byte)(startTxtBlue + (tickTxtBlue * (m - keys.get(keys.size() - 1))));
				txtAngle1Key[m] = (int)(startTxtAngle1 + (tickTxtAngle1 * (m - keys.get(keys.size() - 1))));
				txtScaleKey[m] = (byte)(startTxtScale + (tickTxtScale * (m - keys.get(keys.size() - 1))));
				txtHeightKey[m] = (byte)(startTxtHeight + (tickTxtHeight * (m - keys.get(keys.size() - 1))));
				sidesKey[m] = (byte)(startSides + (tickSides * (m - keys.get(keys.size() - 1))));
			}
			for(int n = 0; n < keys.get(0); n++)
			{
				redKey[n] = (byte)(redKey[1199] + (tickRed * n));
				greenKey[n] = (byte)(greenKey[1199] + (tickGreen * n));
				blueKey[n] = (byte)(blueKey[1199] + (tickBlue * n));
				secRedKey[n] = (byte)(secRedKey[1199] + (tickSecRed * n));
				secGreenKey[n] = (byte)(secGreenKey[1199] + (tickSecGreen * n));
				secBlueKey[n] = (byte)(secBlueKey[1199] + (tickSecBlue * n));
				angle1Key[n] = (int)(angle1Key[1199] + (tickAngle1 * n));
				angle2Key[n] = (byte)(angle2Key[1199] + (tickAngle2 * n));
				mainSizeKey[n] = (byte)(mainSizeKey[1199] + (tickMainSize * n));
				secSizeKey[n] = (byte)(secSizeKey[1199] + (tickSecSize * n));
				lazerHeightKey[n] = (int)(lazerHeightKey[1199] + (tickLaserHeight * n));
				txtRedKey[n] = (byte)(txtRedKey[1199] + (tickTxtRed * n));
				txtGreenKey[n] = (byte)(txtGreenKey[1199] + (tickTxtGreen * n));
				txtBlueKey[n] = (byte)(txtBlueKey[1199] + (tickTxtBlue * n));
				txtAngle1Key[n] = (int)(txtAngle1Key[1199] + (tickTxtAngle1 * n));
				txtScaleKey[n] = (byte)(txtScaleKey[1199] + (tickTxtScale * n));
				txtHeightKey[n] = (byte)(txtHeightKey[1199] + (tickTxtHeight * n));
				sidesKey[n] = (byte)(sidesKey[1199] + (tickSides * n));
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
		nbtTagCompound.setByte("Red", red);
		nbtTagCompound.setByte("Green", green);
		nbtTagCompound.setByte("Blue", blue);
		nbtTagCompound.setByte("SecRed", secRed);
		nbtTagCompound.setByte("SecGreen", secGreen);
		nbtTagCompound.setByte("SecBlue", secBlue);
		if(textureName != null && !textureName.isEmpty())
		{
			nbtTagCompound.setString("TextureName", textureName);
		}
		if(secTextureName != null && !secTextureName.isEmpty())
		{
			nbtTagCompound.setString("SecTextureName", secTextureName);
		}
		nbtTagCompound.setInteger("Angle1", angle1);
		nbtTagCompound.setByte("Angle2", angle2);
		nbtTagCompound.setBoolean("AutoRotate", autoRotate);
		nbtTagCompound.setBoolean("ReverseRotation", reverseRotation);
		nbtTagCompound.setByte("RotationSpeed", rotationSpeed);
		nbtTagCompound.setBoolean("SecondaryLaser", secondaryLaser);
		nbtTagCompound.setByte("LastKeySelected", lastKeySelected);
		nbtTagCompound.setBoolean("TimeLineEnabled", timeLineEnabled);
		nbtTagCompound.setInteger("Time", time);
		nbtTagCompound.setBoolean("SmoothMode", smoothMode);
		nbtTagCompound.setByte("CreateKeyTime", createKeyTime);
		nbtTagCompound.setByte("DisplayAxe", displayAxe);
		nbtTagCompound.setBoolean("SideLaser", sideLaser);
		nbtTagCompound.setByte("MainLaserSize", mainLaserSize);
		nbtTagCompound.setByte("SecLaserSize", secLaserSize);
		nbtTagCompound.setInteger("LazerHeight", lazerHeight);
		if(displayText != null && !displayText.isEmpty())
		{
			nbtTagCompound.setString("DisplayText", displayText);
		}
		nbtTagCompound.setBoolean("TextEnabled", textEnabled);
		nbtTagCompound.setByte("TxtRed", txtRed);
		nbtTagCompound.setByte("TxtGreen", txtGreen);
		nbtTagCompound.setByte("TxtBlue", txtBlue);
		nbtTagCompound.setInteger("TxtAngle1", txtAngle1);
		nbtTagCompound.setBoolean("TxtAutoRotate", txtAutoRotate);
		nbtTagCompound.setBoolean("TxtReverseRotation", txtReverseRotation);
		nbtTagCompound.setByte("TxtRotationSpeed", txtRotationSpeed);
		nbtTagCompound.setByte("TxtScale", txtScale);
		nbtTagCompound.setByte("TxtHeight", txtHeight);
		nbtTagCompound.setByte("Sides", sides);

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
		red = nbtTagCompound.getByte("Red");
		green = nbtTagCompound.getByte("Green");
		blue = nbtTagCompound.getByte("Blue");
		secRed = nbtTagCompound.getByte("SecRed");
		secGreen = nbtTagCompound.getByte("SecGreen");
		secBlue = nbtTagCompound.getByte("SecBlue");
		textureName = nbtTagCompound.getString("TextureName");
		secTextureName = nbtTagCompound.getString("SecTextureName");
		angle1 = nbtTagCompound.getInteger("Angle1");
		angle2 = nbtTagCompound.getByte("Angle2");
		autoRotate = nbtTagCompound.getBoolean("AutoRotate");
		reverseRotation = nbtTagCompound.getBoolean("ReverseRotation");
		rotationSpeed = nbtTagCompound.getByte("RotationSpeed");
		secondaryLaser = nbtTagCompound.getBoolean("SecondaryLaser");
		lastKeySelected = nbtTagCompound.getByte("LastKeySelected");
		timeLineEnabled = nbtTagCompound.getBoolean("TimeLineEnabled");
		time = nbtTagCompound.getInteger("Time");
		smoothMode = nbtTagCompound.getBoolean("SmoothMode");
		createKeyTime = nbtTagCompound.getByte("CreateKeyTime");
		displayAxe = nbtTagCompound.getByte("DisplayAxe");
		sideLaser = nbtTagCompound.getBoolean("SideLaser");
		mainLaserSize = nbtTagCompound.getByte("MainLaserSize");
		secLaserSize = nbtTagCompound.getByte("SecLaserSize");
		lazerHeight = nbtTagCompound.getInteger("LazerHeight");
		displayText = nbtTagCompound.getString("DisplayText");
		textEnabled = nbtTagCompound.getBoolean("TextEnabled");
		txtRed = nbtTagCompound.getByte("TxtRed");
		txtGreen = nbtTagCompound.getByte("TxtGreen");
		txtBlue = nbtTagCompound.getByte("TxtBlue");
		txtAngle1 = nbtTagCompound.getInteger("TxtAngle1");
		txtAutoRotate = nbtTagCompound.getBoolean("TxtAutoRotate");
		txtReverseRotation = nbtTagCompound.getBoolean("TxtReverseRotation");
		txtRotationSpeed = nbtTagCompound.getByte("TxtRotationSpeed");
		txtScale = nbtTagCompound.getByte("TxtScale");
		txtHeight = nbtTagCompound.getByte("TxtHeight");
		sides = nbtTagCompound.getByte("Sides");

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
		return this.worldObj.getTileEntity(pos) != this ? false : player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		AxisAlignedBB bb = INFINITE_EXTENT_AABB;
		AxisAlignedBB cbb = getBlockType().getCollisionBoundingBox(worldObj, getPos(), worldObj.getBlockState(getPos()));
		if(cbb != null)
		{
			bb = cbb;
		}
		return bb;
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
		this.writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(pos, 3, nbt);
	}

	public void setByte(byte index, byte value)
	{
		System.out.println("SetByte");
		switch(index)
		{
		case 0:
		{
			this.red = value;
			break;
		}
		case 1:
		{
			this.green = value;
			break;
		}
		case 2:
		{
			this.blue = value;
			break;
		}
		case 3:
		{
			this.secRed = value;
			break;
		}
		case 4:
		{
			this.secGreen = value;
			break;
		}
		case 5:
		{
			this.secBlue = value;
			break;
		}
		case 9:
		{
			this.angle2 = value;
			break;
		}

		case 12:
		{
			this.rotationSpeed = value;
			break;
		}
		case 14:
		{
			this.displayAxe = value;
			break;
		}
		case 16:
		{
			this.mainLaserSize = value;
			break;
		}
		case 17:
		{
			this.secLaserSize = value;
			break;
		}
		case 20:
		{
			this.lastKeySelected = value;
			break;
		}
		case 28:
		{
			this.txtRed = value;
			break;
		}
		case 29:
		{
			this.txtGreen = value;
			break;
		}
		case 30:
		{
			this.txtBlue = value;
			break;
		}
		case 35:
		{
			this.txtRotationSpeed = value;
			break;
		}
		case 37:
		{
			this.createKeyTime = value;
			break;
		}
		case 38:
		{
			this.txtScale = value;
			break;
		}
		case 39:
		{
			this.txtHeight = value;
			break;
		}
		case 40:
		{
			this.sides = value;
			break;
		}
		case 100:
		{
			this.setDefaultValue();
			break;
		}
		default:
		{
			TheSpotLightMod.log.error("Wrong set index : " + index);
		}
		}
		this.worldObj.markBlockForUpdate(pos);
	}

	public void setBoolean(byte index, boolean value)
	{
		switch(index)
		{
		case 10:
		{
			this.autoRotate = value;
			break;
		}
		case 11:
		{
			this.reverseRotation = value;
			break;
		}
		case 13:
		{
			this.secondaryLaser = value;
			break;
		}
		case 15:
		{
			this.sideLaser = value;
			break;
		}
		case 22:
		{
			this.timeLineEnabled = value;
			break;
		}
		case 24:
		{
			this.smoothMode = value;
			break;
		}
		case 27:
		{
			this.textEnabled = value;
			break;
		}
		case 33:
		{
			this.txtAutoRotate = value;
			break;
		}
		case 34:
		{
			this.txtReverseRotation = value;
			break;
		}
		default:
		{
			TheSpotLightMod.log.error("Wrong set index : " + index);
		}
		}
		this.worldObj.markBlockForUpdate(pos);
	}

	public void set(int index, int value)
	{
		switch(index)
		{
		case 8:
		{
			this.angle1 = value;
			break;
		}
		case 23:
		{
			this.time = value;
			break;
		}
		case 25:
		{
			this.lazerHeight = value;
			break;
		}
		case 31:
		{
			this.txtAngle1 = value;
			break;
		}
		case 51:
		{
			this.applyConfig(value);
			break;
		}
		case 52:
		{
			this.removeConfig(value - 1);
			break;
		}
		default:
		{
			TheSpotLightMod.log.error("Wrong set index : " + index);
		}
		}
		this.worldObj.markBlockForUpdate(pos);
	}

	public void set(int index, String value)
	{
		switch(index)
		{
		case 6:
		{
			textureName = value;
			break;
		}
		case 7:
		{
			secTextureName = value;
			break;
		}
		case 26:
		{
			displayText = value;
			break;
		}
		case 50:
		{
			this.addOrCreateConfig(value);
			break;
		}
		default:
		{
			TheSpotLightMod.log.error("Wrong set index : " + index);
		}
		}
		this.worldObj.markBlockForUpdate(pos);
	}

	public byte getRed()
	{
		return red;
	}

	public byte getGreen()
	{
		return green;
	}

	public byte getBlue()
	{
		return blue;
	}

	public byte getSecRed()
	{
		return secRed;
	}

	public byte getSecGreen()
	{
		return secGreen;
	}

	public byte getSecBlue()
	{
		return secBlue;
	}

	public String getTextureName()
	{
		return textureName;
	}

	public String getSecTextureName()
	{
		return secTextureName;
	}

	public int getAngle1()
	{
		return angle1;
	}

	public byte getAngle2()
	{
		return angle2;
	}

	public boolean isAutoRotate()
	{
		return autoRotate;
	}

	public boolean isReverseRotation()
	{
		return reverseRotation;
	}

	public byte getRotationSpeed()
	{
		return rotationSpeed;
	}

	public boolean isSecondaryLaser()
	{
		return secondaryLaser;
	}

	public byte getDisplayAxe()
	{
		return displayAxe;
	}

	public boolean isSideLaser()
	{
		return sideLaser;
	}

	public byte getMainLaserSize()
	{
		return mainLaserSize;
	}

	public byte getSecLaserSize()
	{
		return secLaserSize;
	}

	public byte getLastKeySelected()
	{
		return lastKeySelected;
	}

	public boolean isTimeLineEnabled()
	{
		return timeLineEnabled;
	}

	public int getTime()
	{
		return time;
	}

	public boolean isSmoothMode()
	{
		return smoothMode;
	}

	public byte getCreateKeyTime()
	{
		return createKeyTime;
	}

	public int getLaserHeight()
	{
		return lazerHeight;
	}

	public String getDisplayText()
	{
		return displayText;
	}

	public boolean isTextEnabled()
	{
		return textEnabled;
	}

	public byte getTxtRed()
	{
		return txtRed;
	}

	public byte getTxtGreen()
	{
		return txtGreen;
	}

	public byte getTxtBlue()
	{
		return txtBlue;
	}

	public byte getTxtRotationSpeed()
	{
		return txtRotationSpeed;
	}

	public boolean isTxtAutoRotate()
	{
		return txtAutoRotate;
	}

	public boolean isTxtReverseRotation()
	{
		return txtReverseRotation;
	}

	public int getTxtAngle1()
	{
		return txtAngle1;
	}

	public byte getTxtScale()
	{
		return txtScale;
	}

	public byte getTxtHeight()
	{
		return txtHeight;
	}

	public byte getSides()
	{
		return sides;
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
		this.worldObj.markBlockForUpdate(pos);
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
		red = (byte)255;
		green = (byte)255;
		blue = (byte)255;
		secRed = (byte)255;
		secGreen = (byte)255;
		secBlue = (byte)255;
		textureName = "beacon_beam";
		secTextureName = "beacon_beam";
		angle1 = 0;
		angle2 = (byte)0;
		autoRotate = false;
		reverseRotation = false;
		rotationSpeed = (byte)0;
		secondaryLaser = true;
		lastKeySelected = -1;
		timeLineEnabled = false;
		time = 0;
		smoothMode = false;
		createKeyTime = 0;
		displayAxe = (byte)0;
		sideLaser = false;
		mainLaserSize = (byte)40;
		secLaserSize = (byte)75;
		lazerHeight = 256;
		displayText = "";
		textEnabled = false;
		txtRed = (byte)255;
		txtGreen = (byte)255;
		txtBlue = (byte)255;
		txtAngle1 = 0;
		txtAutoRotate = false;
		txtReverseRotation = false;
		txtRotationSpeed = 0;
		txtScale = (byte)10;
		txtHeight = (byte)128;
		sides = (byte)2;
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
		this.worldObj.markBlockForUpdate(pos);
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
				red = conf.getByte("Red");
				green = conf.getByte("Green");
				blue = conf.getByte("Blue");
				secRed = conf.getByte("SecRed");
				secGreen = conf.getByte("SecGreen");
				secBlue = conf.getByte("SecBlue");
				textureName = conf.getString("TextureName");
				secTextureName = conf.getString("SecTextureName");
				angle1 = conf.getInteger("Angle1");
				angle2 = conf.getByte("Angle2");
				autoRotate = conf.getBoolean("AutoRotate");
				reverseRotation = conf.getBoolean("ReverseRotation");
				rotationSpeed = conf.getByte("RotationSpeed");
				secondaryLaser = conf.getBoolean("SecondaryLaser");
				lastKeySelected = conf.getByte("LastKeySelected");
				timeLineEnabled = conf.getBoolean("TimeLineEnabled");
				time = conf.getInteger("Time");
				smoothMode = conf.getBoolean("SmoothMode");
				createKeyTime = conf.getByte("CreateKeyTime");
				displayAxe = conf.getByte("DisplayAxe");
				sideLaser = conf.getBoolean("SideLaser");
				mainLaserSize = conf.getByte("MainLaserSize");
				secLaserSize = conf.getByte("SecLaserSize");
				lazerHeight = conf.getInteger("LazerHeight");
				displayText = conf.getString("DisplayText");
				textEnabled = conf.getBoolean("TextEnabled");
				txtRed = conf.getByte("TxtRed");
				txtGreen = conf.getByte("TxtGreen");
				txtBlue = conf.getByte("TxtBlue");
				txtAngle1 = conf.getInteger("TxtAngle1");
				txtAutoRotate = conf.getBoolean("TxtAutoRotate");
				txtReverseRotation = conf.getBoolean("TxtReverseRotation");
				txtRotationSpeed = conf.getByte("TxtRotationSpeed");
				txtScale = conf.getByte("TxtScale");
				txtHeight = conf.getByte("TxtHeight");
				sides = conf.getByte("Sides");

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

					if(j >= 0 && j < this.keyList.length)
					{
						this.keyList[j] = SpotLightEntry.loadSpotLightEntryFromNBT(nbttagcompound1);
					}
				}
				return true;
			}
			this.worldObj.markBlockForUpdate(pos);
		}

		return false;
	}

	private void removeConfig(int value)
	{
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

	public void addOrCreateConfig(String name)
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
		nbtTagCompound.setByte("Red", red);
		nbtTagCompound.setByte("Green", green);
		nbtTagCompound.setByte("Blue", blue);
		nbtTagCompound.setByte("SecRed", secRed);
		nbtTagCompound.setByte("SecGreen", secGreen);
		nbtTagCompound.setByte("SecBlue", secBlue);
		if(textureName != null && !textureName.isEmpty())
		{
			nbtTagCompound.setString("TextureName", textureName);
		}
		if(secTextureName != null && !secTextureName.isEmpty())
		{
			nbtTagCompound.setString("SecTextureName", secTextureName);
		}
		nbtTagCompound.setInteger("Angle1", angle1);
		nbtTagCompound.setByte("Angle2", angle2);
		nbtTagCompound.setBoolean("AutoRotate", autoRotate);
		nbtTagCompound.setBoolean("ReverseRotation", reverseRotation);
		nbtTagCompound.setByte("RotationSpeed", rotationSpeed);
		nbtTagCompound.setBoolean("SecondaryLaser", secondaryLaser);
		nbtTagCompound.setByte("LastKeySelected", lastKeySelected);
		nbtTagCompound.setBoolean("TimeLineEnabled", timeLineEnabled);
		nbtTagCompound.setInteger("Time", time);
		nbtTagCompound.setBoolean("SmoothMode", smoothMode);
		nbtTagCompound.setByte("CreateKeyTime", createKeyTime);
		nbtTagCompound.setByte("DisplayAxe", displayAxe);
		nbtTagCompound.setBoolean("SideLaser", sideLaser);
		nbtTagCompound.setByte("MainLaserSize", mainLaserSize);
		nbtTagCompound.setByte("SecLaserSize", secLaserSize);
		nbtTagCompound.setInteger("LazerHeight", lazerHeight);
		if(displayText != null && !displayText.isEmpty())
		{
			nbtTagCompound.setString("DisplayText", displayText);
		}
		nbtTagCompound.setBoolean("TextEnabled", textEnabled);
		nbtTagCompound.setByte("TxtRed", txtRed);
		nbtTagCompound.setByte("TxtGreen", txtGreen);
		nbtTagCompound.setByte("TxtBlue", txtBlue);
		nbtTagCompound.setInteger("TxtAngle1", txtAngle1);
		nbtTagCompound.setBoolean("TxtAutoRotate", txtAutoRotate);
		nbtTagCompound.setBoolean("TxtReverseRotation", txtReverseRotation);
		nbtTagCompound.setByte("TxtRotationSpeed", txtRotationSpeed);
		nbtTagCompound.setByte("TxtScale", txtScale);
		nbtTagCompound.setByte("TxtHeight", txtHeight);
		nbtTagCompound.setByte("Sides", sides);

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
		return slots.length;
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
		if(stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
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
	public void clearInventory()
	{
		this.slots[0] = null;
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText("Test");
	}
}