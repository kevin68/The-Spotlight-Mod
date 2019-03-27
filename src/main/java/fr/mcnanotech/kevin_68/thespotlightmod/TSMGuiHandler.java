package fr.mcnanotech.kevin_68.thespotlightmod;

import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class TSMGuiHandler {
    public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer) {
        BlockPos pos = openContainer.getAdditionalData().readBlockPos();

        if (openContainer.getId().equals(new ResourceLocation(TheSpotLightMod.MODID, "spotlight"))) {
            World world = Minecraft.getInstance().world;
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntitySpotLight) {
                TileEntitySpotLight tileSpotlight = (TileEntitySpotLight)tile;
                InventoryPlayer playerInv = Minecraft.getInstance().player.inventory;
                return new GuiSpotLight(playerInv, tileSpotlight, world);
            }
        }

        return null;
    }
}
