package fr.mcnanotech.kevin_68.thespotlightmod;

import java.util.ArrayList;
import java.util.List;

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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRequestData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.BeamVec;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMKey;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMVec3;

public class TileEntitySpotLight extends TileEntity implements IInventory, IUpdatePlayerListBox
{
    private ItemStack[] slots = new ItemStack[8];

    @SideOnly(Side.CLIENT)
    private long worldTimeClient;
    @SideOnly(Side.CLIENT)
    private float activeBooleanFloat;
    public boolean isActive;

    /**
     * updated = common, data are loaded; updating = client waiting for packet
     */
    public boolean updated = false, updating = false;
    public int dimensionID;
    public boolean isBeam; // false = text mode
    public boolean redstone; // Require redstone signal

    // -------------------------------------Beam Colors
    public short beamRed, beamGreen, beamBlue, secBeamRed, secBeamGreen, secBeamBlue;
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
    public short textHeight, textScale;
    // -------------------------------------TimeLine
    public short time;
    public boolean timelineEnabled, timelineSmooth;
    private TSMKey[] tsmKeys = new TSMKey[120];
    // ------------------------------------- TimeLine calculated values
    public short[] tlBRed = new short[1200], tlBGreen = new short[1200], tlBBlue = new short[1200];// TODO
                                                                                                   // fill

    // -------------------------------------Previous values for determining
    // processing
    private short prevBeamHeight = -1, prevBeamSides = -1, prevBeamAngleX = -1, prevBeamAngleY = -1, prevBeamAngleZ = -1, prevBeamSize = -1, prevSecBeamSize = -1;
    private boolean prevBeamAutoRotateX, prevBeamAutoRotateY, prevBeamAutoRotateZ, prevWasBeam;

    // -------------------------------------Vec for renders
    public BeamVec[] bVec = null;
    public List<BeamVec[]> beams = new ArrayList<BeamVec[]>();

    public void setKey(short time, TSMKey key)
    {
        // System.out.println(time + " " + key.toString());
        this.tsmKeys[time] = key;
        if(!this.worldObj.isRemote)
        {
            processTimelineValues();
        }
        this.markForUpdate();
    }

    public TSMKey getKey(short time)
    {
        return this.tsmKeys[time];
    }

    public TSMKey[] getKeys()
    {
        return this.tsmKeys;
    }

    @Override
    public void update()
    {
        try
        {
            if(!this.updated)
            {
                if(!this.worldObj.isRemote)
                {
                    this.updated = TSMJsonManager.updateTileData(this.dimensionID, this.pos, this);
                }
                else if(!this.updating)
                {
                    this.updating = true;
                    TheSpotLightMod.network.sendToServer(new PacketRequestData(this.pos.getX(), this.pos.getY(), this.pos.getZ()));
                }
            }

            if(this.worldObj.isBlockPowered(this.pos) || !this.redstone)
            {
                if(this.worldObj.isRemote)
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
        this.worldObj.markBlockForUpdate(getPos());
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

        if(this.worldObj.isRemote)
        {
            if(this.timelineSmooth)
            {
                this.beamRed = this.tlBRed[this.time];
                this.beamGreen = this.tlBGreen[this.time];
                this.beamBlue = this.tlBBlue[this.time];
                // TODO fill
            }
            else
            {
                TSMKey k = this.tsmKeys[(this.time - this.time % 10) / 10];
                if(k != null)
                {
                    this.beamRed = k.bRed;
                    this.beamGreen = k.bGreen;
                    this.beamBlue = k.bBlue;
                    // TODO fill
                }
            }
        }
    }

    // Server Side Only
    private void processTimelineValues()
    {
        System.out.println("processing");
        ArrayList<Integer> keysTime = new ArrayList();
        ArrayList<Integer> timeBetwinKeys = new ArrayList();

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
                this.tlBRed = this.calculateValues(this.tlBRed, start.bRed, end.bRed, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlBGreen = this.calculateValues(this.tlBGreen, start.bGreen, end.bGreen, keysTime.get(k), timeBetwinKeys.get(k), false);
                this.tlBBlue = this.calculateValues(this.tlBBlue, start.bBlue, end.bBlue, keysTime.get(k), timeBetwinKeys.get(k), false);
                // TODO fill
            }

            TSMKey start = this.tsmKeys[(keysTime.get(keysTime.size() - 1) - keysTime.get(keysTime.size() - 1) % 10) / 10];
            TSMKey end = this.tsmKeys[(keysTime.get(0) - keysTime.get(0) % 10) / 10];
            this.tlBRed = this.calculateValues(this.tlBRed, start.bRed, end.bRed, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlBGreen = this.calculateValues(this.tlBGreen, start.bGreen, end.bGreen, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
            this.tlBBlue = this.calculateValues(this.tlBBlue, start.bBlue, end.bBlue, keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
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
                // TODO fill
            }
        }
        String strData = TSMJsonManager.getDataFromTile(this).toString();
        TSMJsonManager.updateJsonData(this.dimensionID, this.pos, strData);
        TheSpotLightMod.network.sendToAll(new PacketData(this.pos.getX(), this.pos.getY(), this.pos.getZ(), strData));
    }

    private short[] calculateValues(short[] tab, short valStart, short valEnd, int timeStart, int timeLenght, boolean last)
    {
        System.out.println(last + " " + timeStart);
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
        return 65536.0D;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("DimID", this.dimensionID);
        nbtTagCompound.setShort("Time", this.time);
        nbtTagCompound.setBoolean("TimelineEnabled", this.timelineEnabled);
        nbtTagCompound.setBoolean("TimelineSmooth", this.timelineSmooth);

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
        this.dimensionID = nbtTagCompound.getInteger("DimID");
        this.time = nbtTagCompound.getShort("Time");
        this.timelineEnabled = nbtTagCompound.getBoolean("TimelineEnabled");
        this.timelineSmooth = nbtTagCompound.getBoolean("TimelineSmooth");

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

    // @Deprecated
    // public void set(EnumLaserInformations e, Object value)
    // {
    // if(e.getType() == (byte)0)// byte
    // {
    // byte val = (Byte)value;
    // switch(e)
    // {
    // case TIMELINECREATEKEYTIME:
    // this.timelineCreateKeyTime = val;
    // break;
    // case TIMELINELASTKEYSELECTED:
    // this.tilelineLastKeySelected = val;
    // break;
    // case TEXTRED:
    // this.textRed = val;
    // break;
    // case TEXTGREEN:
    // this.textGreen = val;
    // break;
    // case TEXTBLUE:
    // this.textBlue = val;
    // break;
    // case TEXTROTATIONSPEED:
    // this.textRotationSpeed = val;
    // break;
    // case TEXTSCALE:
    // this.textScale = val;
    // break;
    // case TEXTHEIGHT:
    // this.textHeight = val;
    // break;
    // default:
    // TheSpotLightMod.log.error("Error, wrong enum " + e.name());
    // break;
    // }
    // }
    // else if(e.getType() == (byte)1)// integer
    // {
    // int val = (Integer)value;
    // switch(e)
    // {
    // case TIMELINETIME:
    // this.timelineTime = val;
    // break;
    // case TEXTANGLE1:
    // this.textAngle1 = val;
    // break;
    // default:
    // TheSpotLightMod.log.error("Error, wrong enum " + e.name());
    // break;
    // }
    // }
    // else if(e.getType() == (byte)2)// boolean
    // {
    // boolean val = (Boolean)value;
    // switch(e)
    // {
    // case TIMELINEENABLED:
    // this.timelineEnabled = val;
    // break;
    // case TIMELINESMOOTH:
    // this.timelineSmooth = val;
    // break;
    // case TEXTENABLED:
    // this.textEnabled = val;
    // break;
    // case TEXTAUTOROTATE:
    // this.textAutoRotate = val;
    // break;
    // case TEXTREVERSEROTATION:
    // this.textReverseRotation = val;
    // break;
    // default:
    // TheSpotLightMod.log.error("Error, wrong enum " + e.name());
    // break;
    // }
    // }
    // else if(e.getType() == (byte)3)// string
    // {
    // String val = (String)value;
    // switch(e)
    // {
    // case LASERMAINTEXTURE:
    // this.laserMainTexture = val;
    // break;
    // case LASERSECTEXTURE:
    // this.laserSecTexture = val;
    // break;
    // case TEXT:
    // this.text = val;
    // break;
    // default:
    // TheSpotLightMod.log.error("Error, wrong enum " + e.name());
    // break;
    // }
    // }
    // else if(e.getType() == (byte)4)// Commands
    // {
    // switch(e)
    // {
    // case COMMANDSETDEFAULT:
    // setDefaultValue();
    // break;
    // case COMMANDAPPLYCONFIG:
    // applyConfig((Integer)value);
    // break;
    // case COMMANDREMOVECONFIG:
    // removeConfig((Integer)value);
    // break;
    // case COMMANDCREATECONFIG:
    // createConfig((String)value);
    // break;
    // default:
    // TheSpotLightMod.log.error("Error, wrong enum " + e.name());
    // break;
    // }
    // }
    // else
    // {
    // TheSpotLightMod.log.error("Wrong type : " + e.getType());
    // }
    // this.worldObj.markBlockForUpdate(this.pos);
    // }

    /**
     * Only used for keys
     * 
     * @param e
     */
    // @Deprecated
    // public void set(EnumLaserInformations e)
    // {
    // // switch(e.getType())
    // {
    // case 0:
    // set(e, ((byte[])this.keysMap.get(e))[this.timelineTime]);
    // break;
    // case 1:
    // set(e, ((int[])this.keysMap.get(e))[this.timelineTime]);
    // break;
    // }
    // }
    //
    // @Deprecated
    // public Object get(EnumLaserInformations e)
    // {
    // switch(e)
    // {
    // case LASERMAINTEXTURE:
    // return this.laserMainTexture;
    // case LASERSECTEXTURE:
    // return this.laserSecTexture;
    // case TIMELINELASTKEYSELECTED:
    // return this.tilelineLastKeySelected;
    // case TIMELINEENABLED:
    // return this.timelineEnabled;
    // case TIMELINETIME:
    // return this.timelineTime;
    // case TIMELINESMOOTH:
    // return this.timelineSmooth;
    // case TIMELINECREATEKEYTIME:
    // return this.timelineCreateKeyTime;
    // case TEXT:
    // return this.text;
    // case TEXTENABLED:
    // return this.textEnabled;
    // case TEXTRED:
    // return this.textRed;
    // case TEXTGREEN:
    // return this.textGreen;
    // case TEXTBLUE:
    // return this.textBlue;
    // case TEXTROTATIONSPEED:
    // return this.textRotationSpeed;
    // case TEXTAUTOROTATE:
    // return this.textAutoRotate;
    // case TEXTREVERSEROTATION:
    // return this.textReverseRotation;
    // case TEXTANGLE1:
    // return this.textAngle1;
    // case TEXTSCALE:
    // return this.textScale;
    // case TEXTHEIGHT:
    // return this.textHeight;
    // default:
    // TheSpotLightMod.log.error("Error, wrong enum " + e.name());
    // break;
    // }
    // return null;
    // }

    // @Deprecated
    // public void setKey(int index, SpotLightEntry value)
    // {
    // // if(index >= 0 && index < this.keyList.length)
    // {
    // this.keyList[index] = value;
    // }
    // else
    // {
    // TheSpotLightMod.log.error("fatal error, index invalid !");
    // }
    // keysProcess();
    // // this.worldObj.markBlockForUpdate(this.pos);
    // }
    //
    // @Deprecated
    // public SpotLightEntry getKey(int index)
    // {
    // // if(index >= 0 && index < this.keyList.length)
    // // {
    // // return this.keyList[index];
    // // }
    // return null;
    // }

    // @Deprecated
    // public void setDefaultValue()
    // {
    // this.laserMainTexture = "beacon_beam";
    // this.laserSecTexture = "beacon_beam";
    // this.tilelineLastKeySelected = -1;
    // this.timelineEnabled = false;
    // this.timelineTime = 0;
    // this.timelineSmooth = false;
    // this.timelineCreateKeyTime = 0;
    // this.text = "";
    // this.textEnabled = false;
    // this.textRed = (byte)255;
    // this.textGreen = (byte)255;
    // this.textBlue = (byte)255;
    // this.textAngle1 = 0;
    // this.textAutoRotate = false;
    // this.textReverseRotation = false;
    // this.textRotationSpeed = 0;
    // this.textScale = (byte)10;
    // this.textHeight = (byte)128;
    // this.keysMap.put(EnumLaserInformations.LASERRED, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.LASERGREEN, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.LASERBLUE, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.LASERSECRED, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.LASERSECGREEN, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.LASERSECBLUE, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.LASERANGLE1, new int[1200]);
    // this.keysMap.put(EnumLaserInformations.LASERANGLE2, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.LASERMAINSIZE, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.LASERSECSIZE, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.LASERHEIGHT, new int[1200]);
    // this.keysMap.put(EnumLaserInformations.TEXTRED, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.TEXTGREEN, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.TEXTBLUE, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.TEXTANGLE1, new int[1200]);
    // this.keysMap.put(EnumLaserInformations.TEXTSCALE, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.TEXTHEIGHT, new byte[1200]);
    // this.keysMap.put(EnumLaserInformations.LASERSIDESNUMBER, new byte[1200]);
    // this.keyList = new SpotLightEntry[120];
    // this.worldObj.markBlockForUpdate(this.pos);
    // }

    @Deprecated
    public boolean applyConfig(int id)
    {
        // ItemStack stack = this.slots[0];
        // if(stack.hasTagCompound())
        // {
        // if(stack.getTagCompound().hasKey("TSMConfigs"))
        // {
        // NBTTagList list = stack.getTagCompound().getTagList("TSMConfigs",
        // NBT.TAG_COMPOUND);
        // NBTTagCompound tag = list.getCompoundTagAt(id - 1);
        // NBTTagCompound conf =
        // UtilSpotLight.getConfig(tag.getInteger("ConfigId"));
        // this.laserMainTexture = conf.getString("TextureName");
        // this.laserSecTexture = conf.getString("SecTextureName");
        // this.tilelineLastKeySelected = conf.getByte("LastKeySelected");
        // this.timelineEnabled = conf.getBoolean("TimeLineEnabled");
        // this.timelineTime = conf.getInteger("Time");
        // this.timelineSmooth = conf.getBoolean("SmoothMode");
        // this.timelineCreateKeyTime = conf.getByte("CreateKeyTime");
        // this.text = conf.getString("DisplayText");
        // this.textEnabled = conf.getBoolean("TextEnabled");
        // this.textRed = conf.getByte("TxtRed");
        // this.textGreen = conf.getByte("TxtGreen");
        // this.textBlue = conf.getByte("TxtBlue");
        // this.textAngle1 = conf.getInteger("TxtAngle1");
        // this.textAutoRotate = conf.getBoolean("TxtAutoRotate");
        // this.textReverseRotation = conf.getBoolean("TxtReverseRotation");
        // this.textRotationSpeed = conf.getByte("TxtRotationSpeed");
        // this.textScale = conf.getByte("TxtScale");
        // this.textHeight = conf.getByte("TxtHeight");
        //
        // this.keysMap.put(EnumLaserInformations.LASERRED,
        // conf.getByteArray("RedKey"));
        // this.keysMap.put(EnumLaserInformations.LASERGREEN,
        // conf.getByteArray("GreenKey"));
        // this.keysMap.put(EnumLaserInformations.LASERBLUE,
        // conf.getByteArray("BlueKey"));
        // this.keysMap.put(EnumLaserInformations.LASERSECRED,
        // conf.getByteArray("SecRedKey"));
        // this.keysMap.put(EnumLaserInformations.LASERSECGREEN,
        // conf.getByteArray("SecGreenKey"));
        // this.keysMap.put(EnumLaserInformations.LASERSECBLUE,
        // conf.getByteArray("SecBlueKey"));
        // this.keysMap.put(EnumLaserInformations.LASERANGLE1,
        // conf.getIntArray("Angle1Key"));
        // this.keysMap.put(EnumLaserInformations.LASERANGLE2,
        // conf.getByteArray("Angle2Key"));
        // this.keysMap.put(EnumLaserInformations.LASERMAINSIZE,
        // conf.getByteArray("MainLazerSize"));
        // this.keysMap.put(EnumLaserInformations.LASERSECSIZE,
        // conf.getByteArray("SecLazerSize"));
        // this.keysMap.put(EnumLaserInformations.LASERHEIGHT,
        // conf.getIntArray("LazerHeightKey"));
        // this.keysMap.put(EnumLaserInformations.TEXTRED,
        // conf.getByteArray("TxtRedKey"));
        // this.keysMap.put(EnumLaserInformations.TEXTGREEN,
        // conf.getByteArray("TxtGreenKey"));
        // this.keysMap.put(EnumLaserInformations.TEXTBLUE,
        // conf.getByteArray("TxtBlueKey"));
        // this.keysMap.put(EnumLaserInformations.TEXTANGLE1,
        // conf.getIntArray("TxtAngle1Key"));
        // this.keysMap.put(EnumLaserInformations.TEXTSCALE,
        // conf.getByteArray("TxtScaleKey"));
        // this.keysMap.put(EnumLaserInformations.TEXTHEIGHT,
        // conf.getByteArray("TxtHeightKey"));
        // this.keysMap.put(EnumLaserInformations.LASERSIDESNUMBER,
        // conf.getByteArray("SidesKey"));
        //
        // NBTTagList nbttaglist = conf.getTagList("SpotLightKeys",
        // Constants.NBT.TAG_COMPOUND);
        // for(int i = 0; i < nbttaglist.tagCount(); ++i)
        // {
        // NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
        // int j = nbttagcompound1.getByte("Key") & 255;
        //
        // if(j >= 0 && j < this.keyList.length)
        // {
        // this.keyList[j] =
        // SpotLightEntry.loadSpotLightEntryFromNBT(nbttagcompound1);
        // }
        // }
        // return true;
        // }
        // this.worldObj.markBlockForUpdate(this.pos);
        // }

        return false;
    }

    @Deprecated
    public void createConfig(String name)
    {
        // if(this.slots[0] != null)
        // {
        // NBTTagCompound itemTag = this.slots[0].hasTagCompound() ?
        // this.slots[0].getTagCompound() : new NBTTagCompound();
        // NBTTagList list = itemTag.hasKey("TSMConfigs") ?
        // itemTag.getTagList("TSMConfigs", NBT.TAG_COMPOUND) : new
        // NBTTagList();
        // NBTTagCompound nbt = new NBTTagCompound();
        // nbt.setString("ConfigName", name);
        // nbt.setInteger("ConfigId", UtilSpotLight.addConfig(getTags(name)));
        // list.appendTag(nbt);
        // itemTag.setTag("TSMConfigs", list);
        // this.slots[0].setTagCompound(itemTag);
        // }
    }

    @Deprecated
    public NBTTagCompound getTags(String name)
    {
        // NBTTagCompound nbtTagCompound = new NBTTagCompound();
        // if(name != null && !name.isEmpty())
        // {
        // nbtTagCompound.setString("ConfigName", name);
        // }
        // if(this.laserMainTexture != null && !this.laserMainTexture.isEmpty())
        // {
        // nbtTagCompound.setString("TextureName", this.laserMainTexture);
        // }
        // if(this.laserSecTexture != null && !this.laserSecTexture.isEmpty())
        // {
        // nbtTagCompound.setString("SecTextureName", this.laserSecTexture);
        // }
        // nbtTagCompound.setByte("LastKeySelected",
        // this.tilelineLastKeySelected);
        // nbtTagCompound.setBoolean("TimeLineEnabled", this.timelineEnabled);
        // nbtTagCompound.setInteger("Time", this.timelineTime);
        // nbtTagCompound.setBoolean("SmoothMode", this.timelineSmooth);
        // nbtTagCompound.setByte("CreateKeyTime", this.timelineCreateKeyTime);
        // if(this.text != null && !this.text.isEmpty())
        // {
        // nbtTagCompound.setString("DisplayText", this.text);
        // }
        // nbtTagCompound.setBoolean("TextEnabled", this.textEnabled);
        // nbtTagCompound.setByte("TxtRed", this.textRed);
        // nbtTagCompound.setByte("TxtGreen", this.textGreen);
        // nbtTagCompound.setByte("TxtBlue", this.textBlue);
        // nbtTagCompound.setInteger("TxtAngle1", this.textAngle1);
        // nbtTagCompound.setBoolean("TxtAutoRotate", this.textAutoRotate);
        // nbtTagCompound.setBoolean("TxtReverseRotation",
        // this.textReverseRotation);
        // nbtTagCompound.setByte("TxtRotationSpeed", this.textRotationSpeed);
        // nbtTagCompound.setByte("TxtScale", this.textScale);
        // nbtTagCompound.setByte("TxtHeight", this.textHeight);
        //
        // nbtTagCompound.setByteArray("RedKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.LASERRED));
        // nbtTagCompound.setByteArray("GreenKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.LASERGREEN));
        // nbtTagCompound.setByteArray("BlueKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.LASERBLUE));
        // nbtTagCompound.setByteArray("SecRedKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.LASERSECRED));
        // nbtTagCompound.setByteArray("SecGreenKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.LASERSECGREEN));
        // nbtTagCompound.setByteArray("SecBlueKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.LASERSECBLUE));
        // nbtTagCompound.setIntArray("Angle1Key",
        // (int[])this.keysMap.get(EnumLaserInformations.LASERANGLE1));
        // nbtTagCompound.setByteArray("Angle2Key",
        // (byte[])this.keysMap.get(EnumLaserInformations.LASERANGLE2));
        // nbtTagCompound.setByteArray("MainLazerSize",
        // (byte[])this.keysMap.get(EnumLaserInformations.LASERMAINSIZE));
        // nbtTagCompound.setByteArray("SecLazerSize",
        // (byte[])this.keysMap.get(EnumLaserInformations.LASERSECSIZE));
        // nbtTagCompound.setIntArray("LazerHeightKey",
        // (int[])this.keysMap.get(EnumLaserInformations.LASERHEIGHT));
        // nbtTagCompound.setByteArray("SidesKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.LASERSIDESNUMBER));
        // nbtTagCompound.setByteArray("TxtRedKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.TEXTRED));
        // nbtTagCompound.setByteArray("TxtGreenKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.TEXTGREEN));
        // nbtTagCompound.setByteArray("TxtBlueKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.TEXTBLUE));
        // nbtTagCompound.setIntArray("TxtAngle1Key",
        // (int[])this.keysMap.get(EnumLaserInformations.TEXTANGLE1));
        // nbtTagCompound.setByteArray("TxtScaleKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.TEXTSCALE));
        // nbtTagCompound.setByteArray("TxtHeightKey",
        // (byte[])this.keysMap.get(EnumLaserInformations.TEXTHEIGHT));
        //
        // NBTTagList nbttaglist = new NBTTagList();
        // for(int i = 0; i < this.keyList.length; ++i)
        // {
        // if(this.keyList[i] != null)
        // {
        // NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        // nbttagcompound1.setByte("Key", (byte)i);
        // this.keyList[i].writeToNBT(nbttagcompound1);
        // nbttaglist.appendTag(nbttagcompound1);
        // }
        // }
        // nbtTagCompound.setTag("SpotLightKeys", nbttaglist);
        // return nbtTagCompound;
        return null;
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
            itemstack = this.slots[slot].splitStack(amount);
            if(this.slots[slot].stackSize == 0)
            {
                this.slots[slot] = null;
            }
            return itemstack;
        }
        return null;
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
        return null;
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
        // switch(slot)
        // {
        // case 0:
        // case 2:
        // case 4:
        // {
        // return stack != null && stack.getItem() != null && stack.getItem() ==
        // TheSpotLightMod.configSaver;
        // }
        // case 1:
        // case 3:
        // case 5:
        // {
        // return false;
        // }
        // }
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
        this.slots[0] = null;
    }

    @Override
    public IChatComponent getDisplayName()
    {
        return new ChatComponentText("Test");
    }

    public void craftConfig()
    {
        if(!this.worldObj.isRemote)
        {
            if(this.getStackInSlot(0) != null && this.getStackInSlot(1) == null)
            {
                this.decrStackSize(0, 1);
                ItemStack stack = new ItemStack(TheSpotLightMod.configSaver_full);
                TSMJsonManager.saveConfig(stack, this);
                this.setInventorySlotContents(1, stack);
            }
            if(this.getStackInSlot(2) != null && this.getStackInSlot(3) == null)
            {
                ItemStack stack = this.getStackInSlot(2).copy();
                this.decrStackSize(2, 1);
                TSMJsonManager.loadConfig(stack, this);
                this.setInventorySlotContents(3, stack);
            }
            if(this.getStackInSlot(4) != null && this.getStackInSlot(5) == null)
            {
                TSMJsonManager.deleteConfig(getStackInSlot(4));
                this.decrStackSize(4, 1);
                ItemStack stack = new ItemStack(TheSpotLightMod.configSaver);
                this.setInventorySlotContents(5, stack);
            }
        }
        this.markForUpdate();
    }
}