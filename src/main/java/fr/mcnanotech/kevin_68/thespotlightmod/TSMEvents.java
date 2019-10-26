package fr.mcnanotech.kevin_68.thespotlightmod;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = TheSpotLightMod.MOD_ID)
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
                if(!event.getPlayer().getGameProfile().getId().equals(te.lockerUUID))
                {
                    event.getPlayer().sendMessage(new TranslationTextComponent("message.spotlight.locked.break"));
                    event.setCanceled(true);
                }
            }
        }
    }
}
