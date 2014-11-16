package fr.mcnanotech.kevin_68.thespotlightmod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;

public class ContainerSpotLightSlotConfig extends Container
{
    protected TileEntitySpotLight tileSpotLight;
    private World worldObj;

    public ContainerSpotLightSlotConfig(TileEntitySpotLight tileEntity, InventoryPlayer inventoryPlayer, World world)
    {
        this.worldObj = world;
        this.tileSpotLight = tileEntity;
        addSlotToContainer(new Slot(tileEntity, 0, 8, 114));
        bindPlayerInventory(inventoryPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return tileSpotLight.isUseableByPlayer(player);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
    {
        for(int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
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
        return tileSpotLight;
    }
}