package fr.mcnanotech.kevin_68.thespotlightmod;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TSMEvents
{
    @SubscribeEvent
    public void onBreakEvent(BlockEvent.BreakEvent event)
    {
        if(event.getWorld().getTileEntity(event.getPos()) instanceof TileEntitySpotLight)
        {
            TileEntitySpotLight te = (TileEntitySpotLight)event.getWorld().getTileEntity(event.getPos());
            if(te.locked)
            {
                if(!event.getPlayer().getGameProfile().getId().equals(te.lockerUUID))
                {
                    event.getPlayer().addChatMessage(new TextComponentString(I18n.format("message.spotlight.locked.break")));
                    event.setCanceled(true);
                }
            }
        }
    }
}