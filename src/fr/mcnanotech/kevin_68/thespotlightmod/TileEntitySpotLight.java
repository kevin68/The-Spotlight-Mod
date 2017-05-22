package fr.mcnanotech.kevin_68.thespotlightmod;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRequestData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRequestTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.BeamVec;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMKey;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMVec3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySpotLight extends TileEntity implements ISidedInventory, ITickable
{
    /*
     * Inventory
     */
    private NonNullList<ItemStack> slots = NonNullList.<ItemStack>withSize(8, ItemStack.EMPTY);

    /*
     * updated = common, data are loaded; updating = client waiting for packet
     */
    public boolean updated = false, updating = false, timelineUpdated = false, timelineUpdating = false;
    public boolean isActive;
    public int dimensionID;
    public boolean isBeam; // false = text mode
    public boolean helpMode; // false = disabled
    public boolean redstone; // Require redstone signal
    public boolean locked; // Locked by a user
    public String lockerUUID;// UUID of locker

    // -------------------------------------Beam Colors
    public short beamRed, beamGreen, beamBlue, secBeamRed, secBeamGreen, secBeamBlue;
    public float beamAlpha, secBeamAlpha;
    // -------------------------------------Beam Angles
    public short beamAngleX, beamAngleY, beamAngleZ, beamRotationSpeedX, beamRotationSpeedY, beamRotationSpeedZ;
    public boolean beamAutoRotateX, beamAutoRotateY, beamAutoRotateZ, beamReverseRotateX, beamReverseRotateY, beamReverseRotateZ;
    // -------------------------------------Beam Properties
    public short beamSize, secBeamSize, beamHeight, beamSides;
    public boolean secBeamEnabled, beamDouble;
    // -------------------------------------Text Colors
    public String text;
    public short textRed, textGreen, textBlue;
    // -------------------------------------Text Angles
    public short textAngleY, textRotationSpeedY;
    public boolean textAutoRotateY, textReverseRotateY;
    // -------------------------------------Text Properties
    public short textHeight, textScale, textTranslateSpeed;
    public boolean textBold, textStrike, textUnderline, textItalic, textObfuscated, textShadow, textTranslating, textReverseTranslating, text3D;
    // -------------------------------------TimeLine
    public short time;
    public boolean timelineEnabled, timelineSmooth;
    private TSMKey[] tsmKeys = new TSMKey[120];
    // ------------------------------------- TimeLine calculated values
    public short[] tlBRed = new short[1200], tlBGreen = new short[1200], tlBBlue = new short[1200], tlSecBRed = new short[1200], tlSecBGreen = new short[1200], tlSecBBlue = new short[1200], tlBAngleX = new short[1200], tlBAngleY = new short[1200], tlBAngleZ = new short[1200];// TODO
                                                                                                                                                                                                                                                                                    // fill
    public float[] tlBAlpha = new float[1200], tlSecBAlpha = new float[1200];

    // -------------------------------------Previous values for determining
    // processing
    private short prevBeamHeight = -1, prevBeamSides = -1, prevBeamAngleX = -1, prevBeamAngleY = -1, prevBeamAngleZ = -1, prevBeamSize = -1, prevSecBeamSize = -1;
    private boolean prevBeamAutoRotateX, prevBeamAutoRotateY, prevBeamAutoRotateZ, prevWasBeam;

    // -------------------------------------Vecs for renders
    public BeamVec[] bVec = null;
    public List<BeamVec[]> beams = new ArrayList<BeamVec[]>();

    public void setKey(short keyTime, TSMKey key)
    {
        this.tsmKeys[keyTime] = key;
        if(!this.world.isRemote)
        {
            processTimelineValues();
        }
        this.markForUpdate();
    }

    public TSMKey getKey(short keyTime)
    {
        return this.tsmKeys[keyTime];
    }

    public TSMKey[] getKeys()
    {
        return this.tsmKeys;
    }

    public boolean hasKey()
    {
        for(int i = 0; i < this.tsmKeys.length; i++)
        {
            if(this.tsmKeys[i] != null)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update()
    {
        try
        {
            if(!this.updated)
            {
                if(!this.world.isRemote)
                {
                    this.updated = TSMJsonManager.updateTileData(this.dimensionID, this.pos, this);
                }
                else if(!this.updating)
                {
                    this.updating = true;
                    TheSpotLightMod.network.sendToServer(new PacketRequestData(this.pos.getX(), this.pos.getY(), this.pos.getZ()));
                }
            }

            if(!this.timelineUpdated)
            {
                if(!this.world.isRemote)
                {
                    this.timelineUpdated = TSMJsonManager.updateTileTimeline(this.dimensionID, this.pos, this);
                }
                else if(!this.timelineUpdating)
                {
                    this.timelineUpdating = true;
                    TheSpotLightMod.network.sendToServer(new PacketRequestTLData(this.pos.getX(), this.pos.getY(), this.pos.getZ()));
                }
            }

            if(this.world.isBlockPowered(this.pos) || !this.redstone)
            {
                if(this.world.isRemote)
                {
                    this.isActive = true;
                    if(this.bVec != null)
                    {
                        if(this.beamAutoRotateX || this.beamAutoRotateY || this.beamAutoRotateZ || this.prevBeamAngleX != this.beamAngleX || this.prevBeamAngleY != this.beamAngleY || this.prevBeamAngleZ != this.beamAngleZ || this.prevBeamHeight != this.beamHeight || this.prevBeamSides != this.beamSides || this.prevBeamSize != this.beamSize || this.prevSecBeamSize != this.secBeamSize || this.prevBeamAutoRotateX != this.beamAutoRotateX || this.prevBeamAutoRotateY != this.beamAutoRotateY || this.prevBeamAutoRotateZ != this.beamAutoRotateZ || this.prevWasBeam != this.isBeam)
                        {
                            this.prevBeamAngleX = this.beamAngleX;
                            this.prevBeamAngleY = this.beamAngleY;
                            this.prevBeamAngleZ = this.beamAngleZ;
                            this.prevBeamHeight = this.beamHeight;
                            this.prevBeamSides = this.beamSides;
                            this.prevBeamSize = this.beamSize;
                            this.prevSecBeamSize = this.secBeamSize;
                            this.prevBeamAutoRotateX = this.beamAutoRotateX;
                            this.prevBeamAutoRotateY = this.beamAutoRotateY;
                            this.prevBeamAutoRotateZ = this.beamAutoRotateZ;
                            this.prevWasBeam = this.isBeam;
                            this.bVec = process();
                        }
                    }
                    else
                    {
                        this.bVec = process();
                    }
                }

                if(this.timelineEnabled)
                {
                    this.runTimeLine();
                }
            }
            else
            {
                this.isActive = false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void markForUpdate()
    {
        IBlockState state = this.world.getBlockState(getPos());
        this.world.notifyBlockUpdate(getPos(), state, state, 3);
    }

    private void runTimeLine()
    {
        if(this.time == 1199)
        {
            this.time = 0;
        }
        else
        {
            this.time++;
        }

        if(this.world.isRemote)
        {
            if(this.timelineSmooth)
            {
                this.beamRed = this.tlBRed[this.time];
                this.beamGreen = this.tlBGreen[this.time];
                this.beamBlue = this.tlBBlue[this.time];
                this.beamAlpha = this.tlBAlpha[this.time];
                this.secBeamRed = this.tlSecBRed[this.time];
                this.secBeamGreen = this.tlSecBGreen[this.time];
                this.secBeamBlue = this.tlSecBBlue[this.time];
                this.secBeamAlpha = this.tlSecBAlpha[this.time];
                this.beamAngleX = this.tlBAngleX[this.time];
                this.beamAngleY = this.tlBAngleY[this.time];
                this.beamAngleZ = this.tlBAngleZ[this.time];
                // TODO fill
                TSMKey k = this.tsmKeys[(this.time - this.time % 10) / 10];
                if(k != null)
                {
                    this.beamAutoRotateX = k.bARX;
                    this.beamAutoRotateY = k.bARY;
                    this.beamAutoRotateZ = k.bARZ;
                    this.beamReverseRotateX = k.bRRX;
                    this.beamReverseRotateY = k.bRRY;
                    this.beamReverseRotateZ = k.bRRZ;
                }
            }
            else
            {
                TSMKey k = this.tsmKeys[(this.time - this.time % 10) / 10];
                if(k != null)
                {
                    this.beamRed = k.bRed;
                    this.beamGreen = k.bGreen;
                    this.beamBlue = k.bBlue;
                    this.beamAlpha = k.bAlpha;
                    this.secBeamRed = k.secBRed;
                    this.secBeamGreen = k.secBGreen;
                    this.secBeamBlue = k.secBBlue;
                    this.secBeamAlpha = k.secBAlpha;
                    this.beamAngleX = k.bAngleX;
                    this.beamAngleY = k.bAngleY;
                    this.beamAngleZ = k.bAngleZ;
                    this.beamAutoRotateX = k.bARX;
                    this.beamAutoRotateY = k.bARY;
                    this.beamAutoRotateZ = k.bARZ;
                    this.beamReverseRotateX = k.bRRX;
                    this.beamReverseRotateY = k.bRRY;
                    this.beamReverseRotateZ = k.bRRZ;
                    // TODO fill
                }
            }
        }
    }

    // Server Side Only
    private void processTimelineValues()
    {
        ArrayList<Integer> keysTime = new ArrayList<Integer>();
        ArrayList<Integer> timeBetwinKeys = new ArrayList<Integer>();

        for(int i = 0; i < this.tsmKeys.length; i++)
        {
            TSMKey entry = this.tsmKeys[i];
            if(entry != null)
            {
                keysTime.add(i * 10);
            }
        }

        if(!keysTime.isEmpty() && keysTime.size() > 1)
        {
            for(int j = 0; j < keysTime.size() - 1; j++)
            {
                timeBetwinKeys.add(keysTime.get(j + 1) - keysTime.get(j));
            }
            timeBetwinKeys.add(1200 - keysTime.get(keysTime.size() - 1) + keysTime.get(0));

            for(int k = 0; k < keysTime.size() - 1; k++)
            {
                TSMKey start = this.tsmKeys[(keysTime.get(k) - keysTime.get(k) % 10) / 10];
                TSMKey end = this.tsmKeys[(keysTime.get(k + 1) - keysTime.get(k + 1) % 10) / 10];
                this.tlBRed = this.calculateValuesS(this.tlBRed, start.bRed, end.bRed, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlBGreen = this.calculateValuesS(this.tlBGreen, start.bGreen, end.bGreen, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlBBlue = this.calculateValuesS(this.tlBBlue, start.bBlue, end.bBlue, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlBAlpha = this.calculateValuesF(this.tlBAlpha, start.bAlpha, end.bAlpha, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlSecBRed = this.calculateValuesS(this.tlSecBRed, start.secBRed, end.secBRed, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlSecBGreen = this.calculateValuesS(this.tlSecBGreen, start.secBGreen, end.secBGreen, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlSecBBlue = this.calculateValuesS(this.tlSecBBlue, start.secBBlue, end.secBBlue, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlSecBAlpha = this.calculateValuesF(this.tlSecBAlpha, start.secBAlpha, end.secBAlpha, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlBAngleX = this.calculateValuesS(this.tlBAngleX, start.bAngleX, end.bAngleX, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlBAngleY = this.calculateValuesS(this.tlBAngleY, start.bAngleY, end.bAngleY, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlBAngleZ = this.calculateValuesS(this.tlBAngleZ, start.bAngleZ, end.bAngleZ, keysTime.get(k), timeBetwinKeys.get(k), false);
                // TODO fill
            }

            TSMKey start = this.tsmKeys[(keysTime.get(keysTime.size() - 1) - keysTime.get(keysTime.size() - 1) % 10) / 10];
            TSMKey end = this.tsmKeys[(keysTime.get(0) - keysTime.get(0) % 10) / 10];
            this.tlBRed = this.calculateValuesS(this.tlBRed, start.bRed, end.bRed, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlBGreen = this.calculateValuesS(this.tlBGreen, start.bGreen, end.bGreen, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlBBlue = this.calculateValuesS(this.tlBBlue, start.bBlue, end.bBlue, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlBAlpha = this.calculateValuesF(this.tlBAlpha, start.bAlpha, end.bAlpha, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlSecBRed = this.calculateValuesS(this.tlSecBRed, start.secBRed, end.secBRed, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlSecBGreen = this.calculateValuesS(this.tlSecBGreen, start.secBGreen, end.secBGreen, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlSecBBlue = this.calculateValuesS(this.tlSecBBlue, start.secBBlue, end.secBBlue, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlSecBAlpha = this.calculateValuesF(this.tlSecBAlpha, start.secBAlpha, end.secBAlpha, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlBAngleX = this.calculateValuesS(this.tlBAngleX, start.bAngleX, end.bAngleX, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlBAngleY = this.calculateValuesS(this.tlBAngleY, start.bAngleY, end.bAngleY, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlBAngleZ = this.calculateValuesS(this.tlBAngleZ, start.bAngleZ, end.bAngleZ, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            // TODO fill
        }
        else if(keysTime.size() == 1)
        {
            TSMKey k = this.tsmKeys[(keysTime.get(0) - keysTime.get(0) % 10) / 10];
            for(int i = 0; i < 1200; i++)
            {
                this.tlBRed[i] = k.bRed;
                this.tlBGreen[i] = k.bGreen;
                this.tlBBlue[i] = k.bBlue;
                this.tlBAlpha[i] = k.bAlpha;
                this.tlSecBRed[i] = k.secBRed;
                this.tlSecBGreen[i] = k.secBGreen;
                this.tlSecBBlue[i] = k.secBBlue;
                this.tlSecBAlpha[i] = k.secBAlpha;
                this.tlBAngleX[i] = k.bAngleX;
                this.tlBAngleY[i] = k.bAngleY;
                this.tlBAngleZ[i] = k.bAngleZ;
                // TODO fill
            }
        }
        String strData = TSMJsonManager.getTlDataFromTile(this).toString();
        TSMJsonManager.updateTlJsonData(this.dimensionID, this.pos, strData);
        TheSpotLightMod.network.sendToAll(new PacketTLData(this.pos.getX(), this.pos.getY(), this.pos.getZ(), strData));
    }

    private short[] calculateValuesS(short[] tab, short valStart, short valEnd, int timeStart, int timeLenght, boolean last)
    {
        float perTick = (valEnd - valStart) / (float)timeLenght;

        if(!last)
        {
            for(int l = timeStart; l < timeStart + timeLenght; l++)
            {
                tab[l] = (short)(valStart + perTick * (l - timeStart));
            }
        }
        else
        {
            for(int m = timeStart; m < 1200; m++)
            {
                tab[m] = (short)(valStart + perTick * (m - timeStart));
            }
            int firstKeyTime = timeStart + timeLenght - 1200;
            for(int n = 0; n < firstKeyTime; n++)
            {
                tab[n] = (short)(tab[1199] + perTick * n);
            }
        }
        return tab;
    }

    private float[] calculateValuesF(float[] tab, float valStart, float valEnd, int timeStart, int timeLenght, boolean last)
    {
        float perTick = (valEnd - valStart) / timeLenght;

        if(!last)
        {
            for(int l = timeStart; l < timeStart + timeLenght; l++)
            {
                tab[l] = ((int)((valStart + perTick * (l - timeStart)) * 1000.0F)) / 1000.0F;
            }
        }
        else
        {
            for(int m = timeStart; m < 1200; m++)
            {
                tab[m] = ((int)((valStart + perTick * (m - timeStart)) * 1000.0F)) / 1000.0F;
            }
            int firstKeyTime = timeStart + timeLenght - 1200;
            for(int n = 0; n < firstKeyTime; n++)
            {
                tab[n] = ((int)((tab[1199] + perTick * n) * 1000.0F)) / 1000.0F;
            }
        }
        return tab;
    }

    private BeamVec[] process()
    {
        double[] sizes = new double[] {Math.sqrt(Math.pow(this.beamSize / 200.0D, 2) / 2), Math.sqrt(Math.pow(this.secBeamSize / 200.0D, 2) / 2)};
        float timer = getWorld().getTotalWorldTime() * 0.00125F;
        float angleX = this.beamAutoRotateX ? timer * this.beamRotationSpeedX * (this.beamReverseRotateX ? -1.0F : 1.0F) : (float)Math.toRadians(this.beamAngleX);
        float angleY = this.beamAutoRotateY ? timer * this.beamRotationSpeedY * (this.beamReverseRotateY ? -1.0F : 1.0F) : (float)Math.toRadians(this.beamAngleY);
        float angleZ = this.beamAutoRotateZ ? timer * this.beamRotationSpeedZ * (this.beamReverseRotateZ ? -1.0F : 1.0F) : (float)Math.toRadians(this.beamAngleZ);

        BeamVec[] vecs = new BeamVec[4];

        for(int j = 0; j < 4; j++)
        {
            TSMVec3[] v = new TSMVec3[this.beamSides + 2];
            TSMVec3 e = null;
            double angle = Math.PI * 2 / (this.beamSides + 2);
            for(int i = 0; i < this.beamSides + 2; i++)
            {
                v[i] = new TSMVec3(Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.cos(angle * i + Math.PI / (this.beamSides + 2)), 0.0D, Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.sin(angle * i + Math.PI / (this.beamSides + 2)));
                v[i].rotateAroundX(angleX);
                v[i].rotateAroundY(angleY);
                v[i].rotateAroundZ(angleZ);
            }
            e = new TSMVec3(0, (j % 2 == 0 ? 1 : -1) * this.beamHeight, 0);
            e.rotateAroundX(angleX);
            e.rotateAroundY(angleY);
            e.rotateAroundZ(angleZ);
            vecs[j] = new BeamVec(v, e);
        }
        return vecs;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 786432.0D;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("DimID", this.dimensionID);
        nbtTagCompound.setShort("Time", this.time);
        nbtTagCompound.setBoolean("TimelineEnabled", this.timelineEnabled);
        nbtTagCompound.setBoolean("TimelineSmooth", this.timelineSmooth);
        nbtTagCompound.setBoolean("Locked", this.locked);

        if(this.lockerUUID != null && !this.lockerUUID.isEmpty())
        {
            nbtTagCompound.setString("LockerUUID", this.lockerUUID);
        }

        ItemStackHelper.saveAllItems(nbtTagCompound, this.slots);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        this.dimensionID = nbtTagCompound.getInteger("DimID");
        this.time = nbtTagCompound.getShort("Time");
        this.timelineEnabled = nbtTagCompound.getBoolean("TimelineEnabled");
        this.timelineSmooth = nbtTagCompound.getBoolean("TimelineSmooth");
        this.locked = nbtTagCompound.getBoolean("Locked");

        if(nbtTagCompound.hasKey("LockerUUID"))
        {
            this.lockerUUID = nbtTagCompound.getString("LockerUUID");
        }

        ItemStackHelper.loadAllItems(nbtTagCompound, this.slots);
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 0, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public int getSizeInventory()
    {
        return this.slots.size();
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.slots.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int amount)
    {
        ItemStack stack = ItemStackHelper.getAndSplit(this.slots, index, amount);

        if(!stack.isEmpty())
        {
            this.markDirty();
        }

        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.slots, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.slots.set(index, stack);

        if(stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
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
        return true;
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
        this.slots.clear();
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentString("Spotlight");
    }

    public void craftConfig()
    {
        if(!this.world.isRemote)
        {
            if(!this.getStackInSlot(0).isEmpty() && this.getStackInSlot(1).isEmpty())
            {
                this.decrStackSize(0, 1);
                ItemStack stack = new ItemStack(TheSpotLightMod.configSaverFull);
                TSMJsonManager.saveConfig(stack, this);
                this.setInventorySlotContents(1, stack);
            }
            if(!this.getStackInSlot(2).isEmpty() && this.getStackInSlot(3).isEmpty())
            {
                ItemStack stack = this.getStackInSlot(2).copy();
                this.decrStackSize(2, 1);
                TSMJsonManager.loadConfig(stack, this);
                this.setInventorySlotContents(3, stack);
            }
            if(!this.getStackInSlot(4).isEmpty() && this.getStackInSlot(5).isEmpty())
            {
                TSMJsonManager.deleteConfig(getStackInSlot(4));
                this.decrStackSize(4, 1);
                ItemStack stack = new ItemStack(TheSpotLightMod.configSaver);
                this.setInventorySlotContents(5, stack);
            }
        }
        this.markForUpdate();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] {0};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return false;
    }

    @Override
    public boolean isEmpty()
    {
        for(ItemStack itemstack : this.slots)
        {
            if(!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }
}