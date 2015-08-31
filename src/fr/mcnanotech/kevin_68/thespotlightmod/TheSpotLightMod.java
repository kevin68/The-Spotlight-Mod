package fr.mcnanotech.kevin_68.thespotlightmod;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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

import org.apache.logging.log4j.Logger;

import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketOpenGui;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRegenerateFile;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRequestData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketUpdateData;

@Mod(modid = TheSpotLightMod.MODID, name = "TheSpotLightMod", version = "@VERSION@", dependencies = "required-after:ffmtlibs;required-after:Forge@[11.14.3.1446,)", acceptableRemoteVersions = "*")
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
    public static Item configSaver;

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

        spotlight = new BlockSpotLight().setHardness(1.0F).setResistance(10.0F).setUnlocalizedName("thespotlightmod.spotlight").setCreativeTab(TheSpotLightMod.tab).setStepSound(Block.soundTypeMetal);
        GameRegistry.registerBlock(spotlight, ItemBlock.class, "tsm_spotlight");
        configSaver = new ItemConfigSaver().setUnlocalizedName("configsaver").setCreativeTab(TheSpotLightMod.tab).setMaxStackSize(1);
        GameRegistry.registerItem(configSaver, "tsm_configsaver", TheSpotLightMod.MODID);
        GameRegistry.registerTileEntity(TileEntitySpotLight.class, "TheSpotLightMod_SpotLight");

        proxy.registerModel();
    }

    @EventHandler
    public void initTheSpotlightMod(FMLInitializationEvent event)
    {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(PacketOpenGui.Handler.class, PacketOpenGui.class, 0, Side.SERVER);
        network.registerMessage(PacketRequestData.Handler.class, PacketRequestData.class, 3, Side.SERVER);
        network.registerMessage(PacketData.Handler.class, PacketData.class, 4, Side.CLIENT);
        network.registerMessage(PacketUpdateData.Handler.class, PacketUpdateData.class, 5, Side.SERVER);
        network.registerMessage(PacketRegenerateFile.Handler.class, PacketRegenerateFile.class, 6, Side.SERVER);
        NetworkRegistry.INSTANCE.registerGuiHandler(modInstance, new GuiHandler());
        proxy.registerRender();
    }

    @EventHandler
    public void postInitTheSpotlightMod(FMLPostInitializationEvent event)
    {
        GameRegistry.addRecipe(new ItemStack(spotlight, 1, 0), new Object[] {"OGO", "RDR", "OGO", 'O', Blocks.obsidian, 'G', Blocks.glass, 'R', Items.redstone, 'D', Items.diamond});
        GameRegistry.addShapelessRecipe(new ItemStack(configSaver, 1, 0), new Object[] {Items.redstone, Blocks.obsidian});
    }
}