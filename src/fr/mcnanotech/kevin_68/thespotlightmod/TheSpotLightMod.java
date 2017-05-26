package fr.mcnanotech.kevin_68.thespotlightmod;

import org.apache.logging.log4j.Logger;

import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketLock;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketOpenGui;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRegenerateFile;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRequestData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRequestTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTLData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimeline;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineDeleteKey;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineReset;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketTimelineSmooth;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateTLData;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = TheSpotLightMod.MODID, name = "The SpotLight Mod", version = "@VERSION@", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.10.2]", updateJSON = "http://dl.mcnanotech.fr/kevin_68/TSM/version.json")
public class TheSpotLightMod
{
    public static final String MODID = "thespotlightmod";

    @Instance(MODID)
    public static TheSpotLightMod modInstance;

    @SidedProxy(clientSide = "fr.mcnanotech.kevin_68.thespotlightmod.ClientProxy", serverSide = "fr.mcnanotech.kevin_68.thespotlightmod.CommonProxy")
    public static CommonProxy proxy;

    public static SimpleNetworkWrapper network;
    public static Logger log;

    public static Block spotlight;
    public static Item configSaver, configSaverFull;

    public static CreativeTabs tab = new CreativeTabs("thespotlightmod.tab")
    {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(spotlight);
        }
    };

    @EventHandler
    public void preInitTheSpotlightMod(FMLPreInitializationEvent event)
    {
        log = event.getModLog();

        spotlight = new BlockSpotLight().setCreativeTab(TheSpotLightMod.tab);
        spotlight.setHardness(1.0F).setResistance(10.0F);
        spotlight.setUnlocalizedName("thespotlightmod.spotlight").setRegistryName("tsm_spotlight");
        GameRegistry.<Block>register(spotlight);
        GameRegistry.<Item>register(new ItemBlock(spotlight), spotlight.getRegistryName());

        configSaver = new Item().setCreativeTab(TheSpotLightMod.tab).setMaxStackSize(64);
        configSaver.setUnlocalizedName("configsaver").setRegistryName("tsm_configsaver");
        GameRegistry.<Item>register(configSaver);

        configSaverFull = new Item().setCreativeTab(TheSpotLightMod.tab).setMaxStackSize(1);
        configSaverFull.setUnlocalizedName("configsaver_full").setRegistryName("tsm_configsaver_full");
        GameRegistry.<Item>register(configSaverFull);

        GameRegistry.registerTileEntity(TileEntitySpotLight.class, "TheSpotLightMod_SpotLight");

        proxy.registerModel();
    }

    @EventHandler
    public void initTheSpotlightMod(FMLInitializationEvent event)
    {
        TSMEvents ev = new TSMEvents();
        MinecraftForge.EVENT_BUS.register(ev);
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(PacketOpenGui.Handler.class, PacketOpenGui.class, 0, Side.SERVER);
        network.registerMessage(PacketTimeline.Handler.class, PacketTimeline.class, 1, Side.SERVER);
        network.registerMessage(PacketTimelineReset.Handler.class, PacketTimelineReset.class, 2, Side.SERVER);
        network.registerMessage(PacketRequestData.Handler.class, PacketRequestData.class, 3, Side.SERVER);
        network.registerMessage(PacketData.Handler.class, PacketData.class, 4, Side.CLIENT);
        network.registerMessage(PacketUpdateData.Handler.class, PacketUpdateData.class, 5, Side.SERVER);
        network.registerMessage(PacketRegenerateFile.Handler.class, PacketRegenerateFile.class, 6, Side.SERVER);
        network.registerMessage(PacketTimelineDeleteKey.Handler.class, PacketTimelineDeleteKey.class, 7, Side.SERVER);
        network.registerMessage(PacketTimelineSmooth.Handler.class, PacketTimelineSmooth.class, 8, Side.SERVER);
        network.registerMessage(PacketTLData.Handler.class, PacketTLData.class, 9, Side.CLIENT);
        network.registerMessage(PacketRequestTLData.Handler.class, PacketRequestTLData.class, 10, Side.SERVER);
        network.registerMessage(PacketUpdateTLData.Handler.class, PacketUpdateTLData.class, 11, Side.SERVER);
        network.registerMessage(PacketLock.Handler.class, PacketLock.class, 12, Side.SERVER);
        NetworkRegistry.INSTANCE.registerGuiHandler(modInstance, new GuiHandler());
        proxy.registerRender();
    }

    @EventHandler
    public void postInitTheSpotlightMod(FMLPostInitializationEvent event)
    {
        GameRegistry.addRecipe(new ItemStack(spotlight, 1, 0), new Object[] {"OGO", "RDR", "OGO", 'O', Blocks.OBSIDIAN, 'G', Blocks.GLASS, 'R', Items.REDSTONE, 'D', Items.DIAMOND});
        GameRegistry.addShapelessRecipe(new ItemStack(configSaver, 1, 0), new Object[] {Items.REDSTONE, Blocks.OBSIDIAN});
    }
}