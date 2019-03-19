package fr.mcnanotech.kevin_68.thespotlightmod;

import fr.mcnanotech.kevin_68.thespotlightmod.client.gui.GuiSpotLight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
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
            TileEntity tileentity = world.getTileEntity(pos);
            return new GuiSpotLight(Minecraft.getInstance().player.inventory, (TileEntitySpotLight)tileentity, world);
        }

        return null;
    }
}
