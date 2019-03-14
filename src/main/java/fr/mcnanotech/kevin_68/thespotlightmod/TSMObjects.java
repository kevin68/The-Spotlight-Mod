package fr.mcnanotech.kevin_68.thespotlightmod;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(modid = TheSpotLightMod.MODID)
public class TSMObjects {

	@ObjectHolder(TheSpotLightMod.MODID + ":tsm_spotlight")
	public static final Block SPOTLIGHT = null;

	@ObjectHolder(TheSpotLightMod.MODID + ":tsm_configsaver")
	public static final Item CONFIG_SAVER = null;
	@ObjectHolder(TheSpotLightMod.MODID + ":tsm_configsaver_full")
	public static final Item CONFIG_SAVER_FULL = null;

	@ObjectHolder(TheSpotLightMod.MODID + ":spotlight")
	public static final TileEntityType<?> TILE_TSM = null;
	
    public static final ItemGroup TSM_GROUP = new ItemGroup("thespotlightmod.tab")
    {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon()
        {
            return new ItemStack(TSMObjects.SPOTLIGHT);
        }
    };

	@SubscribeEvent
	public static void registerBlock(final RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new BlockSpotLight().setRegistryName("tsm_spotlight"));
	}

	@SubscribeEvent
	public static void registerItem(final RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemBlock(SPOTLIGHT, new Item.Properties().group(TSM_GROUP)).setRegistryName("tsm_spotlight"));
		event.getRegistry().register(new Item(new Item.Properties().group(TSM_GROUP)).setRegistryName("tsm_configsaver"));
		event.getRegistry().register(new Item(new Item.Properties().group(TSM_GROUP)).setRegistryName("tsm_configsaver_full"));
	}

	@SubscribeEvent
	public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
		event.getRegistry().register(buildTileType(TheSpotLightMod.MODID + ":spotlight", TileEntityType.Builder.create(TileEntitySpotLight::new)).setRegistryName("spotlight"));
	}

	public static <T extends TileEntity> TileEntityType<T> buildTileType(String id, TileEntityType.Builder<T> builder) {
		Type<?> type = null;

		try {
			type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(1631)).getChoiceType(TypeReferences.BLOCK_ENTITY, id);
		} catch (IllegalArgumentException illegalstateexception) {
			if (SharedConstants.developmentMode) {
				throw illegalstateexception;
			}
		}

		TileEntityType<T> tileentitytype = builder.build(type);
		return tileentitytype;
	}

}
