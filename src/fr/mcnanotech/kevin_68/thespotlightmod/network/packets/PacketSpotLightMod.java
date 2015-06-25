package fr.mcnanotech.kevin_68.thespotlightmod.network.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.container.ContainerSpotLightSlotConfig;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.EnumLaserInformations;
import fr.mcnanotech.kevin_68.thespotlightmod.utils.UtilSpotLight;

public class PacketSpotLightMod implements IMessage
{
    private EnumLaserInformations e;
    private Object obj;
    private boolean failed = false;

    public PacketSpotLightMod()
    {

    }

    public PacketSpotLightMod(EnumLaserInformations e, Object obj)
    {
        this.e = e;
        this.obj = obj;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        e = EnumLaserInformations.values()[buf.readInt()];
        switch(e.getType())
        {
            case 0:
                obj = buf.readByte();
                break;
            case 1:
                obj = buf.readInt();
                break;
            case 2:
                obj = buf.readBoolean();
                break;
            case 3:
                obj = ByteBufUtils.readUTF8String(buf);
                break;
            case 4:
                try
                {
                    obj = UtilSpotLight.readObject(buf);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                break;
            default:
                TheSpotLightMod.log.error("Wrong type (read)");
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(e.ordinal());
        switch(e.getType())
        {
            case 0:
                buf.writeByte((Byte)obj);
                break;
            case 1:
                buf.writeInt((Integer)obj);
                break;
            case 2:
                buf.writeBoolean((Boolean)obj);
                break;
            case 3:
                ByteBufUtils.writeUTF8String(buf, (String)obj);
                break;
            case 4:
                try
                {
                    UtilSpotLight.writeObject(buf, obj);
                }
                catch(IOException e1)
                {
                    TheSpotLightMod.log.error("Failed to write PacketSpotLightMod");
                }
                break;
            default:
                TheSpotLightMod.log.error("Wrong type (write)");
        }
    }

    public static class Handler implements IMessageHandler<PacketSpotLightMod, IMessage>
    {
        @Override
        public IMessage onMessage(PacketSpotLightMod message, MessageContext ctx)
        {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            Container c = ctx.getServerHandler().playerEntity.openContainer;
            if(!message.failed)
            {
                if(c instanceof ContainerSpotLight)
                {
                    TileEntitySpotLight te = ((ContainerSpotLight)c).getSpotLight();
                    te.set(message.e, message.obj);
                }
                else if(c instanceof ContainerSpotLightSlotConfig)
                {
                    TileEntitySpotLight te = ((ContainerSpotLightSlotConfig)c).getSpotLight();
                    te.set(message.e, message.obj);
                }
            }
            else
            {
                TheSpotLightMod.log.error("Error, packet OBJ failed");
            }
            return null;
        }
    }
}