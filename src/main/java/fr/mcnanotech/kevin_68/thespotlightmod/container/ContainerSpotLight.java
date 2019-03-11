package fr.mcnanotech.kevin_68.thespotlightmod.container;

import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSpotLight extends Container
{
    protected TileEntitySpotLight tileSpotLight;

    public ContainerSpotLight(TileEntitySpotLight tileEntity, InventoryPlayer inventoryPlayer, int invX, int invY, boolean showPlayerInventory)
    {
        this.tileSpotLight = tileEntity;
        if(showPlayerInventory)
        {
            bindPlayerInventory(inventoryPlayer, invX, invY);
        }
    }
    
    public ContainerSpotLight(TileEntitySpotLight tileEntity, InventoryPlayer inventoryPlayer, boolean showPlayerInventory)
    {
        this(tileEntity, inventoryPlayer, 8, 142, showPlayerInventory);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return this.tileSpotLight.isUsableByPlayer(player);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer, int x, int y)
    {
        for(int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, x + i * 18, y));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotId);

        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(slotId < this.tileSpotLight.getSizeInventory())
            {
                if(!mergeItemStack(itemstack1, this.tileSpotLight.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!mergeItemStack(itemstack1, 0, this.tileSpotLight.getSizeInventory(), false))
            {
                return ItemStack.EMPTY;
            }

            if(itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
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