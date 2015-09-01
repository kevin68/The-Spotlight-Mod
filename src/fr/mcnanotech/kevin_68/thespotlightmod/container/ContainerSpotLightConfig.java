package fr.mcnanotech.kevin_68.thespotlightmod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;

public class ContainerSpotLightConfig extends Container
{
    protected TileEntitySpotLight tileSpotLight;

    public ContainerSpotLightConfig(TileEntitySpotLight tileEntity, InventoryPlayer inventoryPlayer)
    {
        this.tileSpotLight = tileEntity;
        addSlotToContainer(new Slot(tileEntity, 0, 40, 30)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack != null && stack.getItem() != null && stack.getItem() == TheSpotLightMod.configSaver;
            }
        });
        addSlotToContainer(new Slot(tileEntity, 1, 40, 80){
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }
        });
        addSlotToContainer(new Slot(tileEntity, 2, 80, 30){
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack != null && stack.getItem() != null && stack.getItem() == TheSpotLightMod.configSaver_full;
            }
        });
        addSlotToContainer(new Slot(tileEntity, 3, 80, 80){
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }
        });
        addSlotToContainer(new Slot(tileEntity, 4, 120, 30){
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack != null && stack.getItem() != null && stack.getItem() == TheSpotLightMod.configSaver_full;
            }
        });
        addSlotToContainer(new Slot(tileEntity, 5, 120, 80){
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }
        });
        bindPlayerInventory(inventoryPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return this.tileSpotLight.isUseableByPlayer(player);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
    {
        for(int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        this.tileSpotLight.craftConfig();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotId);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(slotId < this.tileSpotLight.getSizeInventory())
            {
                if(!this.mergeItemStack(itemstack1, this.tileSpotLight.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if(!this.mergeItemStack(itemstack1, 0, this.tileSpotLight.getSizeInventory(), false))
            {
                return null;
            }
            if(itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    public TileEntitySpotLight getSpotLight()
    {
        return this.tileSpotLight;
    }
}