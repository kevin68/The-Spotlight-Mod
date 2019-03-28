package fr.mcnanotech.kevin_68.thespotlightmod.container;

import java.util.ArrayList;
import java.util.List;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMObjects;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerSpotLight extends Container {
    protected TileEntitySpotLight tileSpotLight;
    
    private List<Slot> configSlots = new ArrayList<Slot>();
    private List<Slot> textureSlots = new ArrayList<Slot>();
    private List<Slot> playerSlots = new ArrayList<Slot>();

    public ContainerSpotLight(TileEntitySpotLight tileEntity, InventoryPlayer inventoryPlayer, int invX, int invY) {
        this.tileSpotLight = tileEntity;
        bindPlayerInventory(inventoryPlayer, invX, invY);
        configSlots.add(addSlot(new SlotInputItem(tileEntity, 0, 40, 30, TSMObjects.CONFIG_SAVER)));
        configSlots.add(addSlot(new SlotOuput(tileEntity, 1, 40, 80)));
        configSlots.add(addSlot(new SlotInputItem(tileEntity, 2, 80, 30, TSMObjects.CONFIG_SAVER_FULL)));
        configSlots.add(addSlot(new SlotOuput(tileEntity, 3, 80, 80)));
        configSlots.add(addSlot(new SlotInputItem(tileEntity, 4, 120, 30, TSMObjects.CONFIG_SAVER_FULL)));
        configSlots.add( addSlot(new SlotOuput(tileEntity, 5, 120, 80)));
        textureSlots.add(addSlot(new TSMSlot(tileEntity, 6, 40, 80)));
        textureSlots.add(addSlot(new TSMSlot(tileEntity, 7, 120, 80)));
    }
    
    public ContainerSpotLight(TileEntitySpotLight tileEntity, InventoryPlayer inventoryPlayer) {
        this(tileEntity, inventoryPlayer, 8, 142);
    }


    public void showConfigSlot(boolean show) {
        configSlots.forEach(s -> ((TSMSlot)s).enable(show));
    }
    
    public void showTextureSlot(boolean show) {
        textureSlots.forEach(s -> ((TSMSlot)s).enable(show));
    }
    
    public void showPlayerSlot(boolean show) {
        playerSlots.forEach(s -> ((TSMSlot)s).enable(show));
    }
    
    public void changePlayerInventoryPos(int x, int y) {
        for (int i = 0; i < playerSlots.size(); i++) {
            Slot s = playerSlots.get(i);
            s.xPos = x + i * 18;
            s.yPos = y;
        }
    }
    
    public void resetSlot() {
        showConfigSlot(false);
        showTextureSlot(false);
        showPlayerSlot(true);
        changePlayerInventoryPos(8, 142);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tileSpotLight.isUsableByPlayer(player);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer, int x, int y) {
        for (int i = 0; i < 9; i++) {
            playerSlots.add(addSlot(new TSMSlot(inventoryPlayer, i, x + i * 18, y, true)));
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int clickedButton, ClickType clickType, EntityPlayer player) {
        if (slotId >= 15 && slotId < 17) {
            Slot slot = this.inventorySlots.get(slotId);
            ItemStack stack = player.inventory.getItemStack();
            if (clickedButton == 0) {
                if (!stack.isEmpty()) {
                    ItemStack stack2 = stack.copy();
                    stack2.setCount(1);
                    slot.decrStackSize(1);
                    slot.putStack(stack2);
                } else {
                    slot.decrStackSize(1);
                }
            } else if (clickedButton == 1) {
                slot.decrStackSize(1);
            }
            return stack;
        }
        return super.slotClick(slotId, clickedButton, clickType, player);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        this.tileSpotLight.craftConfig();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        if (slotId >= 15 && slotId < 17) {
            return ItemStack.EMPTY;
        }
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (slotId < this.tileSpotLight.getSizeInventory()) {
                if (!mergeItemStack(itemstack1, this.tileSpotLight.getSizeInventory() - 2, this.inventorySlots.size() - 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(itemstack1, 0, this.tileSpotLight.getSizeInventory() - 2, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    public TileEntitySpotLight getSpotLight() {
        return this.tileSpotLight;
    }

    private static class TSMSlot extends Slot {

        private boolean enabled;

        public TSMSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean enabled) {
            super(inventoryIn, index, xPosition, yPosition);
            this.enabled = enabled;
        }
        
        public TSMSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            this(inventoryIn, index, xPosition, yPosition, false);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public boolean isEnabled() {
            return this.enabled;
        }
        
        public void enable(boolean enable) {
            this.enabled = enable;
        }
    }

    private static class SlotOuput extends TSMSlot {

        public SlotOuput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }

    private static class SlotInputItem extends TSMSlot {
        private final Item acceptedItem;

        public SlotInputItem(IInventory inventoryIn, int index, int xPosition, int yPosition, Item item) {
            super(inventoryIn, index, xPosition, yPosition);
            this.acceptedItem = item;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() == acceptedItem;
        }
    }
}