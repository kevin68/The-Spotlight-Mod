package fr.mcnanotech.kevin_68.thespotlightmod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;

public class ContainerSpotLightTextures extends Container
{
    protected TileEntitySpotLight tileSpotLight;

    public ContainerSpotLightTextures(TileEntitySpotLight tileEntity, InventoryPlayer inventoryPlayer)
    {
        this.tileSpotLight = tileEntity;
        addSlotToContainer(new Slot(tileEntity, 6, 40, 80));
        addSlotToContainer(new Slot(tileEntity, 7, 120, 80));
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
    public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer player)
    {
        if(slotId < 2 && slotId >= 0)
        {
            Slot slot = (Slot)this.inventorySlots.get(slotId);
            ItemStack stack = player.inventory.getItemStack();
            if(clickedButton == 0)
            {
                if(stack != null)
                {
                    ItemStack stack2 = stack.copy();
                    stack2.stackSize = 1;
                    slot.decrStackSize(1);
                    slot.putStack(stack2);
                }
                else
                {
                    slot.decrStackSize(1);
                }
            }
            else if(clickedButton == 1)
            {
                slot.decrStackSize(1);
            }
            return stack;
        }
        return super.slotClick(slotId, clickedButton, mode, player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
    {
        return null;
    }

    public TileEntitySpotLight getSpotLight()
    {
        return this.tileSpotLight;
    }
}