package fr.mcnanotech.kevin_68.thespotlightmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.mcnanotech.kevin_68.thespotlightmod.client.TileEntitySpotLightRender;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TheSpotLightMod.MODID)
public class TheSpotLightMod {
	public static final String MODID = "thespotlightmod";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public TheSpotLightMod() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
	}

	private void setup(final FMLCommonSetupEvent event) {
		TSMNetwork.registerPackets();
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpotLight.class, new TileEntitySpotLightRender());
	}
}
