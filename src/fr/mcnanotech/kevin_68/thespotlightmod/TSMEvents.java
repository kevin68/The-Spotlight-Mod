package fr.mcnanotech.kevin_68.thespotlightmod;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
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
                System.out.println(event.getPlayer().getGameProfile().getId());
                System.out.println(te.lockerUUID);
                if(!event.getPlayer().getGameProfile().getId().equals(te.lockerUUID))
                {
                    event.getPlayer().addChatMessage(new ChatComponentText(I18n.format("message.spotlight.locked.break")));
                    event.setCanceled(true);
                }
            }
        }
    }
}