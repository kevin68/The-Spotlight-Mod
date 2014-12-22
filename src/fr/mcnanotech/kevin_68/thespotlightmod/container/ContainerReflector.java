package fr.mcnanotech.kevin_68.thespotlightmod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntityReflector;

public class ContainerReflector extends Container
{
	protected TileEntityReflector te;
	private World worldObj;

	public ContainerReflector(TileEntityReflector tileEntity, InventoryPlayer inventoryPlayer, World world)
	{
		worldObj = world;
		te = tileEntity;
		bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return te.isUseableByPlayer(player);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for(int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	public TileEntityReflector getReflector()
	{
		return te;
	}
}