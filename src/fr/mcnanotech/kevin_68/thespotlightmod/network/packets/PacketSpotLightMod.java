package fr.mcnanotech.kevin_68.thespotlightmod.network.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.inventory.Container;
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
        this.e = EnumLaserInformations.values()[buf.readInt()];
        switch(this.e.getType())
        {
            case 0:
                this.obj = buf.readByte();
                break;
            case 1:
                this.obj = buf.readInt();
                break;
            case 2:
                this.obj = buf.readBoolean();
                break;
            case 3:
                this.obj = ByteBufUtils.readUTF8String(buf);
                break;
            case 4:
                try
                {
                    this.obj = UtilSpotLight.readObject(buf);
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
        buf.writeInt(this.e.ordinal());
        switch(this.e.getType())
        {
            case 0:
                buf.writeByte((Byte)this.obj);
                break;
            case 1:
                buf.writeInt((Integer)this.obj);
                break;
            case 2:
                buf.writeBoolean((Boolean)this.obj);
                break;
            case 3:
                ByteBufUtils.writeUTF8String(buf, (String)this.obj);
                break;
            case 4:
                try
                {
                    UtilSpotLight.writeObject(buf, this.obj);
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