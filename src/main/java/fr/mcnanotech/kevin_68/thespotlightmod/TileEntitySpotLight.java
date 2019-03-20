package fr.mcnanotech.kevin_68.thespotlightmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumPropVecBehaviour;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMType;
import fr.mcnanotech.kevin_68.thespotlightmod.objs.BeamVec;
import fr.mcnanotech.kevin_68.thespotlightmod.objs.TSMKey;
import fr.mcnanotech.kevin_68.thespotlightmod.objs.TSMVec3;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRequestData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRequestTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.TSMJsonManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.PacketDistributor;

public class TileEntitySpotLight extends TileEntity implements ISidedInventory, ITickable, IInteractionObject
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
    public boolean isBeam; // false = text mode
    public boolean helpMode; // false = disabled
    public boolean redstone; // Require redstone signal
    public boolean locked; // Locked by a user
    public UUID lockerUUID;// UUID of locker
    private ITextComponent customName;

    private Map<EnumTSMProperty, Object> properties = initProperties();
    private Map<EnumTSMProperty, Object[]> timelineProperties = initTimeLineProperties();
    private Map<EnumTSMProperty, Object> previousProperties = initPrevProperties();

    // -------------------------------------TimeLine
    public short time;
    public boolean timelineEnabled, timelineSmooth;
    private TSMKey[] tsmKeys = new TSMKey[120];

    // -------------------------------------Vecs for renders
    public BeamVec[] bVec = null;
    public List<BeamVec[]> beams = new ArrayList<BeamVec[]>();
    
    public TileEntitySpotLight() {
		this(TSMObjects.TILE_TSM);
	}
    
    public TileEntitySpotLight(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

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
    public void tick()
    {
        try
        {
            if(!this.updated)
            {
                if(!this.world.isRemote)
                {
                    this.updated = TSMJsonManager.updateTileData(this.pos, this);
                }
                else if(!this.updating)
                {
                    this.updating = true;
                    TSMNetwork.CHANNEL.sendToServer(new PacketRequestData(this.pos));
                }
            }

            if(!this.timelineUpdated)
            {
                if(!this.world.isRemote)
                {
                    this.timelineUpdated = TSMJsonManager.updateTileTimeline(this.world, this.pos, this);
                }
                else if(!this.timelineUpdating)
                {
                    this.timelineUpdating = true;
                    TSMNetwork.CHANNEL.sendToServer(new PacketRequestTLData(this.pos));
                }
            }

            if(this.world.isBlockPowered(this.pos) || !this.redstone)
            {
                if(this.world.isRemote)
                {
                    this.isActive = true;
                    if(this.bVec != null)
                    {
                        //Detect when we need to update the beam's vetors
                        boolean flag = false;
                        forloop: for(Entry<EnumTSMProperty, Object> ent : properties.entrySet())
                        {
                            switch(ent.getKey().vecBehaviour)
                            {
                                case NONE:
                                    continue;
                                case WHEN_ACTIVE:
                                    if(getBoolean(ent.getKey()))
                                    {
                                        flag = true;
                                        break forloop;
                                    }
                                    break;
                                case VALUE_CHANGED:
                                    if(!previousProperties.get(ent.getKey()).equals(ent.getValue()))
                                    {
                                        flag = true;
                                        break forloop;
                                    }
                                    break;
                                default:
                                    System.out.println("???? someone added an enum, this is not good");
                                    break;
                            }
                        }

                        if(flag)
                        {
                            for(Entry<EnumTSMProperty, Object> ent : properties.entrySet())
                            {
                                if(ent.getKey().vecBehaviour != EnumPropVecBehaviour.NONE)
                                {
                                    switch(ent.getKey().type)
                                    {
                                        case FLOAT:
                                            previousProperties.put(ent.getKey(), ((Float)properties.get(ent.getKey())).floatValue());
                                            break;
                                        case SHORT:
                                            previousProperties.put(ent.getKey(), ((Number)properties.get(ent.getKey())).shortValue());
                                            break;
                                        case BOOLEAN:
                                            previousProperties.put(ent.getKey(), ((Boolean)properties.get(ent.getKey())).booleanValue());
                                            break;
                                        default:
                                            System.out.println("Invalid type");
                                            break;
                                    }
                                }
                            }
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
                TSMKey k = this.tsmKeys[(this.time - this.time % 10) / 10];

                for(Entry<EnumTSMProperty, Object> ent : properties.entrySet())
                {
                    if(ent.getKey().timelinable)
                    {
                        if(ent.getKey().type.smoothable)
                        {
                            switch(ent.getKey().type)
                            {
                                case FLOAT:
                                    properties.put(ent.getKey(), ((Float)timelineProperties.get(ent.getKey())[this.time]).floatValue());
                                    break;
                                case SHORT:
                                    properties.put(ent.getKey(), ((Number)timelineProperties.get(ent.getKey())[this.time]).shortValue());
                                    break;
                                default:
                                    System.out.println("Invalid type");
                                    break;
                            }
                        }
                        else if(k != null)
                        {
                            switch(ent.getKey().type)
                            {
                                case FLOAT:
                                    properties.put(ent.getKey(), k.getF(ent.getKey()));
                                    break;
                                case SHORT:
                                    properties.put(ent.getKey(), k.getS(ent.getKey()));
                                    break;
                                case BOOLEAN:
                                    properties.put(ent.getKey(), k.getB(ent.getKey()));
                                    break;
                                case STRING:
                                    properties.put(ent.getKey(), k.getStr(ent.getKey()));
                                    break;
                            }

                        }
                    }
                }

            }
            else
            {
                TSMKey k = this.tsmKeys[(this.time - this.time % 10) / 10];
                if(k != null)
                {
                    for(Entry<EnumTSMProperty, Object> ent : properties.entrySet())
                    {
                        if(ent.getKey().timelinable)
                        {
                            switch(ent.getKey().type)
                            {
                                case FLOAT:
                                    properties.put(ent.getKey(), k.getF(ent.getKey()));
                                    break;
                                case SHORT:
                                    properties.put(ent.getKey(), k.getS(ent.getKey()));
                                    break;
                                case BOOLEAN:
                                    properties.put(ent.getKey(), k.getB(ent.getKey()));
                                    break;
                                case STRING:
                                    properties.put(ent.getKey(), k.getStr(ent.getKey()));
                                    break;
                            }
                        }
                    }
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
                for(Entry<EnumTSMProperty, Object[]> ent : timelineProperties.entrySet())
                {
                    if(ent.getKey().timelinable)
                    {
                        if(ent.getKey().type.smoothable)
                        {
                            Object[] values = null;
                            switch(ent.getKey().type)
                            {
                                case SHORT:
                                    values = this.calculateValuesS((Short[])ent.getValue(), start.getS(ent.getKey()), end.getS(ent.getKey()), keysTime.get(k), timeBetwinKeys.get(k), false);
                                    break;
                                case FLOAT:
                                    values = this.calculateValuesF((Float[])ent.getValue(), start.getF(ent.getKey()), end.getF(ent.getKey()), keysTime.get(k), timeBetwinKeys.get(k), false);
                                    break;
                                default:
                                    System.out.println("Someone forgot a type, please contact the author and copy/paste this line: " + ent.getKey().type.name());
                                    break;
                            }
                            timelineProperties.put(ent.getKey(), values);
                        }
                    }
                }
            }

            TSMKey start = this.tsmKeys[(keysTime.get(keysTime.size() - 1) - keysTime.get(keysTime.size() - 1) % 10) / 10];
            TSMKey end = this.tsmKeys[(keysTime.get(0) - keysTime.get(0) % 10) / 10];
            for(Entry<EnumTSMProperty, Object[]> ent : timelineProperties.entrySet())
            {
                if(ent.getKey().timelinable)
                {
                    if(ent.getKey().type.smoothable)
                    {
                        Object[] values = null;
                        switch(ent.getKey().type)
                        {
                            case SHORT:
                                values = this.calculateValuesS((Short[])ent.getValue(), start.getS(ent.getKey()), end.getS(ent.getKey()), keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
                                break;
                            case FLOAT:
                                values = this.calculateValuesF((Float[])ent.getValue(), start.getF(ent.getKey()), end.getF(ent.getKey()), keysTime.get(keysTime.size() - 1), timeBetwinKeys.get(keysTime.size() - 1), true);
                                break;
                            default:
                                System.out.println("(1) Someone forgot a type, please contact the author and copy/paste this line: " + ent.getKey().type.name());
                                break;
                        }
                        timelineProperties.put(ent.getKey(), values);
                    }
                }
            }
        }
        else if(keysTime.size() == 1)
        {
            // TSMKey k = this.tsmKeys[(keysTime.get(0) - keysTime.get(0) % 10) / 10];

            for(Entry<EnumTSMProperty, Object[]> ent : timelineProperties.entrySet())
            {
                if(ent.getKey().timelinable)
                {
                    if(ent.getKey().type.smoothable)
                    {
                        switch(ent.getKey().type)
                        {
                            case SHORT:
                                Short[] valS = new Short[1200];
                                for(int i = 0; i < 1200; i++)
                                {
                                    valS[i] = getShort(ent.getKey());
                                }
                                timelineProperties.put(ent.getKey(), valS);
                                break;
                            case FLOAT:
                                Float[] valF = new Float[1200];
                                for(int i = 0; i < 1200; i++)
                                {
                                    valF[i] = getFloat(ent.getKey());
                                }
                                timelineProperties.put(ent.getKey(), valF);
                                break;
                            default:
                                System.out.println("(2) Someone forgot a type, please contact the author and copy/paste this line: " + ent.getKey().type.name());
                                break;
                        }

                    }
                }
            }
        }
        String strData = TSMJsonManager.getTlDataFromTile(this).toString();
        TSMJsonManager.updateTlJsonData(this.world, this.pos, strData);
        TSMNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketTLData(this.pos, strData));
    }

    private Short[] calculateValuesS(Short[] tab, short valStart, short valEnd, int timeStart, int timeLenght, boolean last)
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

    private Float[] calculateValuesF(Float[] tab, float valStart, float valEnd, int timeStart, int timeLenght, boolean last)
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
        double[] sizes = new double[] {Math.sqrt(Math.pow(getShort(EnumTSMProperty.BEAM_SIZE) / 200.0D, 2) / 2), Math.sqrt(Math.pow(this.getShort(EnumTSMProperty.BEAM_SEC_SIZE) / 200.0D, 2) / 2)};
        float timer = getWorld().getGameTime() * 0.00125F;
        float angleX = getBoolean(EnumTSMProperty.BEAM_R_AUTO_X) ? timer * getShort(EnumTSMProperty.BEAM_R_SPEED_X) * (getBoolean(EnumTSMProperty.BEAM_R_REVERSE_X) ? -1.0F : 1.0F) : (float)Math.toRadians(getShort(EnumTSMProperty.BEAM_ANGLE_X));
        float angleY = getBoolean(EnumTSMProperty.BEAM_R_AUTO_Y) ? timer * getShort(EnumTSMProperty.BEAM_R_SPEED_Y) * (getBoolean(EnumTSMProperty.BEAM_R_REVERSE_Y) ? -1.0F : 1.0F) : (float)Math.toRadians(getShort(EnumTSMProperty.BEAM_ANGLE_Y));
        float angleZ = getBoolean(EnumTSMProperty.BEAM_R_AUTO_Z) ? timer * getShort(EnumTSMProperty.BEAM_R_SPEED_Z) * (getBoolean(EnumTSMProperty.BEAM_R_REVERSE_Z) ? -1.0F : 1.0F) : (float)Math.toRadians(getShort(EnumTSMProperty.BEAM_ANGLE_Z));

        BeamVec[] vecs = new BeamVec[4];

        for(int j = 0; j < 4; j++)
        {
            TSMVec3[] v = new TSMVec3[getShort(EnumTSMProperty.BEAM_SIDE) + 2];
            TSMVec3 e = null;
            double angle = Math.PI * 2 / (getShort(EnumTSMProperty.BEAM_SIDE) + 2);
            for(int i = 0; i < getShort(EnumTSMProperty.BEAM_SIDE) + 2; i++)
            {
                v[i] = new TSMVec3(Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.cos(angle * i + Math.PI / (getShort(EnumTSMProperty.BEAM_SIDE) + 2)), 0.0D, Math.sqrt(2 * Math.pow(sizes[j / 2], 2)) * Math.sin(angle * i + Math.PI / (getShort(EnumTSMProperty.BEAM_SIDE) + 2)));
                v[i].rotateAroundX(angleX);
                v[i].rotateAroundY(angleY);
                v[i].rotateAroundZ(angleZ);
            }
            e = new TSMVec3(0, (j % 2 == 0 ? 1 : -1) * getShort(EnumTSMProperty.BEAM_HEIGHT), 0);
            e.rotateAroundX(angleX);
            e.rotateAroundY(angleY);
            e.rotateAroundZ(angleZ);
            vecs[j] = new BeamVec(v, e);
        }
        return vecs;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 786432.0D;
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);
        compound.putShort("Time", this.time);
        compound.putBoolean("TimelineEnabled", this.timelineEnabled);
        compound.putBoolean("TimelineSmooth", this.timelineSmooth);
        compound.putBoolean("Locked", this.locked);

        if(this.lockerUUID != null)
        {
        	compound.putUniqueId("LockerUUID", this.lockerUUID);
        }

        ItemStackHelper.saveAllItems(compound, this.slots);
        
        if (this.getCustomName() != null) {
           compound.putString("CustomName", ITextComponent.Serializer.toJson(this.getCustomName()));
        }
        
        return compound;
    }

    @Override
    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        this.time = compound.getShort("Time");
        this.timelineEnabled = compound.getBoolean("TimelineEnabled");
        this.timelineSmooth = compound.getBoolean("TimelineSmooth");
        this.locked = compound.getBoolean("Locked");

        if (compound.contains("LockerUUID", Constants.NBT.TAG_STRING))
        {
        	// convert old data
            this.lockerUUID = UUID.fromString(compound.getString("LockerUUID"));
        }
        if (compound.hasUniqueId("LockerUUID"))
        {
        	// convert old data
            this.lockerUUID = compound.getUniqueId("LockerUUID");
        }
        
        if (compound.contains("CustomName", Constants.NBT.TAG_STRING)) {
            this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
         }

        ItemStackHelper.loadAllItems(compound, this.slots);
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.read(pkt.getNbtCompound());
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 0, write(new NBTTagCompound()));
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
    public ITextComponent getName()
    {
        return new TextComponentTranslation("container.spotlight");
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
                ItemStack stack = new ItemStack(TSMObjects.CONFIG_SAVER_FULL);
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
                ItemStack stack = new ItemStack(TSMObjects.CONFIG_SAVER);
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

    /*
     * Properties initializers
     */

    private Map<EnumTSMProperty, Object> initProperties()
    {
        Map<EnumTSMProperty, Object> map = new HashMap<EnumTSMProperty, Object>();
        for(EnumTSMProperty prop : EnumTSMProperty.values())
        {
            map.put(prop, prop.def);
        }
        return map;
    }

    private Map<EnumTSMProperty, Object[]> initTimeLineProperties()
    {
        Map<EnumTSMProperty, Object[]> map = new HashMap<EnumTSMProperty, Object[]>();
        for(EnumTSMProperty prop : EnumTSMProperty.values())
        {
            if(prop.timelinable && prop.type.smoothable)
            {
                switch(prop.type)
                {
                    case SHORT:
                        Short[] emptyTabS = new Short[1200];
                        for(int i = 0; i < 1200; i++)
                        {
                            emptyTabS[i] = 0;
                        }
                        map.put(prop, emptyTabS);
                        break;
                    case FLOAT:
                        Float[] emptyTabF = new Float[1200];
                        for(int i = 0; i < 1200; i++)
                        {
                            emptyTabF[i] = 0.0F;
                        }
                        map.put(prop, emptyTabF);
                        break;
                    default:
                        System.out.println("Invalid type");
                        break;
                }
            }
        }
        return map;
    }

    private Map<EnumTSMProperty, Object> initPrevProperties()
    {
        Map<EnumTSMProperty, Object> map = new HashMap<EnumTSMProperty, Object>();
        for(EnumTSMProperty prop : EnumTSMProperty.values())
        {
            if(prop.vecBehaviour != EnumPropVecBehaviour.NONE)
            {
                map.put(prop, prop.def);
            }
        }
        return map;
    }

    /*
     * Properties setters
     */

    public void setProperty(EnumTSMProperty prop, Object obj)
    {
        this.properties.put(prop, obj);
    }

    public void setTimelineProperty(EnumTSMProperty prop, Object[] objs)
    {
        if(objs.length == 1200)
        {
            this.timelineProperties.put(prop, objs);
        }
        else
        {
            System.out.println("Invalid timeline array size: " + objs.length + "/1200");
        }
    }

    /*
     * Properties getters
     */

    public Map<EnumTSMProperty, Object> cloneProperties()
    {
        Map<EnumTSMProperty, Object> copy = new HashMap<EnumTSMProperty, Object>();
        for(Entry<EnumTSMProperty, Object> ent : this.properties.entrySet())
        {
            copy.put(ent.getKey(), ent.getValue());
        }
        return copy;
    }

    public short getShort(EnumTSMProperty prop)
    {
        if(prop.type == EnumTSMType.SHORT)
        {
            return ((Number)this.properties.get(prop)).shortValue();
        }
        System.out.println("Invalid use for property: " + prop.name());
        return 0;
    }

    public float getFloat(EnumTSMProperty prop)
    {
        if(prop.type == EnumTSMType.FLOAT)
        {
            return ((Float)this.properties.get(prop)).floatValue();
        }
        System.out.println("Invalid use for property: " + prop.name());
        return 0;
    }

    public boolean getBoolean(EnumTSMProperty prop)
    {
        if(prop.type == EnumTSMType.BOOLEAN)
        {
            return ((Boolean)this.properties.get(prop)).booleanValue();
        }
        System.out.println("Invalid use for property: " + prop.name());
        return false;
    }

    public String getString(EnumTSMProperty prop)
    {
        if(prop.type == EnumTSMType.STRING)
        {
            return ((String)this.properties.get(prop)).substring(0);// Substring to copy
        }
        System.out.println("Invalid use for property: " + prop.name());
        return null;
    }

    public Short[] getTimelineShort(EnumTSMProperty prop)
    {
        if(prop.type == EnumTSMType.SHORT && prop.timelinable && prop.type.smoothable)
        {
            return (Short[])this.timelineProperties.get(prop);
        }
        System.out.println("Invalid use for timeline property: " + prop.name());
        return null;
    }

    public Float[] getTimelineFloat(EnumTSMProperty prop)
    {
        if(prop.type == EnumTSMType.FLOAT && prop.timelinable && prop.type.smoothable)
        {
            return (Float[])this.timelineProperties.get(prop);
        }
        System.out.println("Invalid use for timeline property: " + prop.name());
        return null;
    }

	@Override
	public ITextComponent getCustomName() {
		return customName;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerSpotLight(this, playerInventory, true);
	}

	@Override
	public String getGuiID() {
		return TheSpotLightMod.MODID + ":spotlight";
	}
}