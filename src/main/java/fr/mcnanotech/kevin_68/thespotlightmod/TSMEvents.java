package fr.mcnanotech.kevin_68.thespotlightmod;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = TheSpotLightMod.MODID)
public class TSMEvents
{
    @SubscribeEvent
    public static void onBreakEvent(BlockEvent.BreakEvent event)
    {
        if(event.getWorld().getTileEntity(event.getPos()) instanceof TileEntitySpotLight)
        {
            TileEntitySpotLight te = (TileEntitySpotLight)event.getWorld().getTileEntity(event.getPos());
            if(te.locked)
            {
                if(!event.getPlayer().getGameProfile().getId().toString().equals(te.lockerUUID))
                {
                    event.getPlayer().sendMessage(new TextComponentTranslation("message.spotlight.locked.break"));
                    event.setCanceled(true);
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(TheSpotLightMod.SPOTLIGHT);
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event)
    {
    	event.getRegistry().register(new ItemBlock(TheSpotLightMod.SPOTLIGHT).setRegistryName("tsm_spotlight"));
        event.getRegistry().register(TheSpotLightMod.CONFIG_SAVER);
        event.getRegistry().register(TheSpotLightMod.CONFIG_SAVER_FULL);
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModel(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TheSpotLightMod.SPOTLIGHT), 0, new ModelResourceLocation(TheSpotLightMod.MODID + ":tsm_spotlight", "inventory"));
        ModelLoader.setCustomModelResourceLocation(TheSpotLightMod.CONFIG_SAVER, 0, new ModelResourceLocation(TheSpotLightMod.MODID + ":tsm_configsaver", "inventory"));
        ModelLoader.setCustomModelResourceLocation(TheSpotLightMod.CONFIG_SAVER_FULL, 0, new ModelResourceLocation(TheSpotLightMod.MODID + ":tsm_configsaver_full", "inventory"));
    }
}