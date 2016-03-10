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
        if(event.world.getTileEntity(event.pos) instanceof TileEntitySpotLight)
        {
            TileEntitySpotLight te = (TileEntitySpotLight)event.world.getTileEntity(event.pos);
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