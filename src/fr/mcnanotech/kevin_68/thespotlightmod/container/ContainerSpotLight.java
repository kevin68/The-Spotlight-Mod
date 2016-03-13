package fr.mcnanotech.kevin_68.thespotlightmod.container;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerSpotLight extends Container
{
    protected TileEntitySpotLight tileSpotLight;

    public ContainerSpotLight(TileEntitySpotLight tileEntity, InventoryPlayer inventoryPlayer, World world, int widthMove, boolean showPlayerInventory)
    {
        this.tileSpotLight = tileEntity;
        if(showPlayerInventory)
        {
            bindPlayerInventory(inventoryPlayer, widthMove);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return this.tileSpotLight.isUseableByPlayer(player);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer, int widthMove)
    {
        for(int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, widthMove + i * 18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
    {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(slotId);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(slotId < this.tileSpotLight.getSizeInventory())
            {
                if(!mergeItemStack(itemstack1, this.tileSpotLight.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if(!mergeItemStack(itemstack1, 0, this.tileSpotLight.getSizeInventory(), false))
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