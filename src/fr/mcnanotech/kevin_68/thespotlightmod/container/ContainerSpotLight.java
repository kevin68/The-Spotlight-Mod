package fr.mcnanotech.kevin_68.thespotlightmod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;

public class ContainerSpotLight extends Container
{
	protected TileEntitySpotLight tileSpotLight;
	private World worldObj;
	private boolean configSlot;
	private int widthMove;

	public ContainerSpotLight(TileEntitySpotLight tileEntity, InventoryPlayer inventoryPlayer, World world, int widthMove)
	{
		worldObj = world;
		tileSpotLight = tileEntity;
		this.widthMove = widthMove;
		bindPlayerInventory(inventoryPlayer, widthMove);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileSpotLight.isUseableByPlayer(player);
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
		Slot slot = (Slot)inventorySlots.get(slotId);
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(slotId < tileSpotLight.getSizeInventory())
			{
				if(!mergeItemStack(itemstack1, tileSpotLight.getSizeInventory(), inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if(!mergeItemStack(itemstack1, 0, tileSpotLight.getSizeInventory(), false))
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