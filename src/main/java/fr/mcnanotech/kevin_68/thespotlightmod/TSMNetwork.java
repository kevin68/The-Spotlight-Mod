package fr.mcnanotech.kevin_68.thespotlightmod;

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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class TSMNetwork {
	public static final String PROTOCOL_VERSION = String.valueOf(1);

	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(TheSpotLightMod.MODID, "tsm")).networkProtocolVersion(() -> PROTOCOL_VERSION)
			.clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).simpleChannel();
	
	public static void registerPackets() {

        CHANNEL.messageBuilder(PacketOpenGui.class, 0).encoder(PacketOpenGui::encode).decoder(PacketOpenGui::decode).consumer(PacketOpenGui::handle).add();
        CHANNEL.messageBuilder(PacketTimeline.class, 1).encoder(PacketTimeline::encode).decoder(PacketTimeline::decode).consumer(PacketTimeline::handle).add();
        CHANNEL.messageBuilder(PacketTimelineReset.class, 2).encoder(PacketTimelineReset::encode).decoder(PacketTimelineReset::decode).consumer(PacketTimelineReset::handle).add();
        CHANNEL.messageBuilder(PacketRequestData.class, 3).encoder(PacketRequestData::encode).decoder(PacketRequestData::decode).consumer(PacketRequestData::handle).add();
        CHANNEL.messageBuilder(PacketData.class, 4).encoder(PacketData::encode).decoder(PacketData::decode).consumer(PacketData::handle).add();
        CHANNEL.messageBuilder(PacketUpdateData.class, 5).encoder(PacketUpdateData::encode).decoder(PacketUpdateData::decode).consumer(PacketUpdateData::handle).add();
        CHANNEL.messageBuilder(PacketRegenerateFile.class, 6).encoder(PacketRegenerateFile::encode).decoder(PacketRegenerateFile::decode).consumer(PacketRegenerateFile::handle).add();

        network.registerMessage(PacketTimelineDeleteKey.Handler.class, PacketTimelineDeleteKey.class, 7, Side.SERVER);
        network.registerMessage(PacketTimelineSmooth.Handler.class, PacketTimelineSmooth.class, 8, Side.SERVER);
        network.registerMessage(PacketTLData.Handler.class, PacketTLData.class, 9, Side.CLIENT);
        network.registerMessage(PacketRequestTLData.Handler.class, PacketRequestTLData.class, 10, Side.SERVER);
        network.registerMessage(PacketUpdateTLData.Handler.class, PacketUpdateTLData.class, 11, Side.SERVER);
        network.registerMessage(PacketLock.Handler.class, PacketLock.class, 12, Side.SERVER);

	}
}
