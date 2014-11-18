package fr.mcnanotech.kevin_68.thespotlightmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.mcnanotech.kevin_68.thespotlightmod.blocks.TSMBlocks;
import fr.mcnanotech.kevin_68.thespotlightmod.items.TSMItems;
import fr.mcnanotech.kevin_68.thespotlightmod.network.GuiHandler;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TSMTileEntity;
import fr.minecraftforgefrance.ffmtlibs.network.PacketManager;

@Mod(modid = TheSpotLightMod.MODID, name = "TheSpotLightMod", version = "@VERSION@", dependencies = "required-after:ffmtlibs")
public class TheSpotLightMod
{
    public static final String MODID = "thespotlightmod";

    @Instance(MODID)
    public static TheSpotLightMod modInstance;

    @SidedProxy(clientSide = "fr.mcnanotech.kevin_68.thespotlightmod.ClientProxy", serverSide = "fr.mcnanotech.kevin_68.thespotlightmod.CommonProxy")
    public static CommonProxy proxy;

    public static final PacketManager packetHandler = new PacketManager("fr.mcnanotech.kevin_68.thespotlightmod.network.packets", MODID, "TheSpotLightMod");

    public static Logger log;

    public static CreativeTabs tab = new CreativeTabs("thespotlightmod.tab")
    {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(TSMBlocks.spotlight);
        }
    };

    @EventHandler
    public void preInitTheSpotlightMod(FMLPreInitializationEvent event)
    {
        log = event.getModLog();
        TSMBlocks.initBlock();
        TSMItems.initItems();
        TSMTileEntity.registerTiles();

        NetworkRegistry.INSTANCE.registerGuiHandler(this.modInstance, new GuiHandler());
    }

    @EventHandler
    public void initTheSpotlightMod(FMLInitializationEvent event)
    {

    }

    @EventHandler
    public void postInitTheSpotlightMod(FMLPostInitializationEvent event)
    {
        proxy.register();
        GameRegistry.addRecipe(new ItemStack(TSMBlocks.spotlight, 1, 0), new Object[] {"OGO", "RDR", "OGO", 'O', Blocks.obsidian, 'G', Blocks.glass, 'R', Items.redstone, 'D', Items.diamond});
        GameRegistry.addShapelessRecipe(new ItemStack(TSMItems.configSaver, 1, 0), new Object[] {Items.redstone, Blocks.obsidian});
    }
}