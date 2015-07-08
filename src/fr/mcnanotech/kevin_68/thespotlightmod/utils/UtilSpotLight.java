package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import fr.mcnanotech.kevin_68.thespotlightmod.network.PacketSender;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.Constants.NBT;

public class UtilSpotLight
{
	private static File mcDir;
	private static File mainDir;
	private static File texturesFile;
	private static File mapDir;
	private static File folderDir;
	private static File configsFile;

	private static NBTTagCompound getTexturesData()
	{
		mcDir = Minecraft.getMinecraft().mcDataDir;
		mainDir = new File(new File(mcDir, "thespolightmod"), "spotlight");
		if(!mainDir.exists())
		{
			mainDir.mkdirs();
		}
		texturesFile = new File(mainDir, "textures.dat");
		if(!texturesFile.exists())
		{
			try
			{
				texturesFile.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			FileInputStream fileinputstream = new FileInputStream(texturesFile);
			NBTTagCompound compound = CompressedStreamTools.readCompressed(fileinputstream);
			fileinputstream.close();
			return compound;
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
			NBTTagCompound compound = new NBTTagCompound();
			saveTexturesData(compound);
			setTextures("beacon_beam", "textures/entity/beacon_beam.png");
			setTextures("dirt", "textures/blocks/dirt.png");
			return compound;
		}
	}

	private static void saveTexturesData(NBTTagCompound compound)
	{
		try
		{
			FileOutputStream fileoutputstream = new FileOutputStream(texturesFile);
			CompressedStreamTools.writeCompressed(compound, fileoutputstream);
			fileoutputstream.close();
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}

	public static void setTextures(String name, String path)
	{
		NBTTagCompound compoundBase = getTexturesData();
		NBTTagList list = compoundBase.hasKey("textures") ? compoundBase.getTagList("textures", NBT.TAG_COMPOUND) : new NBTTagList();
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("name", name);
		tag.setString("path", path);
		list.appendTag(tag);
		compoundBase.setTag("textures", list);
		saveTexturesData(compoundBase);
	}

	public static void deleteTexure(String name)
	{
		NBTTagCompound compoundBase = getTexturesData();
		NBTTagList list = compoundBase.getTagList("textures", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++)
		{
			if(list.getCompoundTagAt(i).getString("name").equals(name))
			{
				list.removeTag(i);
			}
		}
		saveTexturesData(compoundBase);
	}

	public static ArrayList<BaseListEntry> listTextures()
	{
		ArrayList<BaseListEntry> list = new ArrayList();

		NBTTagCompound compoundBase = getTexturesData();
		NBTTagList tagList = compoundBase.getTagList("textures", Constants.NBT.TAG_COMPOUND);
		list.add(new TextureEntry("Beacon Beam", "textures/entity/beacon_beam.png"));
		for(int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			list.add(new TextureEntry(tag.getString("name"), tag.getString("path")));
		}

		return list;
	}

	public static TextureEntry getEntryByName(String name)
	{
		for(int i = 0; i < listTextures().size(); i++)
		{
			if(listTextures().get(i).name.equals(name))
			{
				return (TextureEntry)listTextures().get(i);
			}
		}

		return new TextureEntry("beacon_beam", "textures/entity/beacon_beam.png");
	}

	private static NBTTagCompound getConfigurationData()
	{
		mapDir = DimensionManager.getCurrentSaveRootDirectory();
		folderDir = new File(new File(mapDir, "thespolightmod"), "spotlight");
		if(!folderDir.exists())
		{
			folderDir.mkdirs();
		}
		configsFile = new File(folderDir, "configurations.dat");
		if(!configsFile.exists())
		{
			try
			{
				configsFile.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			FileInputStream fileinputstream = new FileInputStream(configsFile);
			NBTTagCompound compound = CompressedStreamTools.readCompressed(fileinputstream);
			fileinputstream.close();
			return compound;
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
			NBTTagCompound compound = new NBTTagCompound();
			saveConfigurationData(compound);
			return compound;
		}
	}

	private static void saveConfigurationData(NBTTagCompound compound)
	{
		try
		{
			FileOutputStream fileoutputstream = new FileOutputStream(configsFile);
			CompressedStreamTools.writeCompressed(compound, fileoutputstream);
			fileoutputstream.close();
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}

	public static int addConfig(NBTTagCompound conf)
	{
		NBTTagCompound compoundBase = getConfigurationData();
		NBTTagList list = compoundBase.hasKey("configs") ? compoundBase.getTagList("configs", Constants.NBT.TAG_COMPOUND) : new NBTTagList();
		List<Integer> used = new ArrayList<Integer>();
		int id = 0;
		for(int i = 0; i < list.tagCount(); i++)
		{
			used.add(list.getCompoundTagAt(i).getInteger("ConfigID"));
		}
		while(used.contains(id))
		{
			id++;
		}
		conf.setInteger("ConfigID", id);
		list.appendTag(conf);
		compoundBase.setTag("configs", list);
		saveConfigurationData(compoundBase);
		return id;
	}

	public static NBTTagCompound getConfig(int id)
	{
		NBTTagCompound compoundBase = getConfigurationData();
		NBTTagList list = compoundBase.hasKey("configs") ? compoundBase.getTagList("configs", Constants.NBT.TAG_COMPOUND) : new NBTTagList();
		for(int i = 0; i < list.tagCount(); i++)
		{
			if(list.getCompoundTagAt(i).getInteger("ConfigID") == id)
			{
				return list.getCompoundTagAt(i);
			}
		}
		return null;
	}

	public static void removeConfig(int id)
	{
		NBTTagCompound compoundBase = getConfigurationData();
		NBTTagList list = compoundBase.hasKey("configs") ? compoundBase.getTagList("configs", Constants.NBT.TAG_COMPOUND) : new NBTTagList();
		for(int i = 0; i < list.tagCount(); i++)
		{
			if(list.getCompoundTagAt(i).getInteger("ConfigID") == id)
			{
				list.removeTag(i);
			}
		}
		saveConfigurationData(compoundBase);
	}

	public static ArrayList formatedText(FontRenderer font, String str, int mouseX, int width, boolean reversed)
	{
		int maxSize = reversed ? mouseX - 70 : width - mouseX - 70;
		ArrayList list = new ArrayList();
		if(font.getStringWidth(str) >= maxSize)
		{
			String[] words = str.split(" ");
			String cutted = "";
			int i = 0;
			while(font.getStringWidth(cutted) <= maxSize)
			{
				cutted += words[i] + " ";
				i++;
				if(i >= words.length)
				{
					break;
				}
			}
			if(font.getStringWidth(cutted) >= maxSize)
			{
				cutted.substring(0, cutted.length() - words[i - 1].length());
			}
			if(font.getStringWidth(cutted) >= maxSize)
			{
				if(i > 1)
				{
					cutted.substring(0, cutted.length() - words[i - 2].length());
				}
			}
			list.add(cutted);
			String forNext = "";
			for(int j = i; j < words.length; j++)
			{
				forNext += words[j] + " ";
			}
			list.addAll(formatedText(font, forNext, mouseX, width, reversed));
		}
		else
		{
			list.add(str);
		}
		return list;
	}

	public static void writeObject(ByteBuf to, Object obj) throws IOException
	{
		byte[] serialized = serialize(obj);
		to.writeInt(serialized.length);
		for(int i = 0; i < serialized.length; i++)
		{
			to.writeByte(serialized[i]);
		}
	}

	public static Object readObject(ByteBuf from) throws ClassNotFoundException, IOException
	{
		int len = from.readInt();
		byte[] serialized = new byte[len];
		for(int i = 0; i < len; i++)
		{
			serialized[i] = from.readByte();
		}
		return deserialize(serialized);
	}

	private static byte[] serialize(Object obj) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}

	private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException
	{
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}

	public static void createKey(TileEntitySpotLight t)
	{
		SpotLightEntry entry = new SpotLightEntry(true, (Byte)t.get(EnumLaserInformations.LASERRED), (Byte)t.get(EnumLaserInformations.LASERGREEN), (Byte)t.get(EnumLaserInformations.LASERBLUE), (Byte)t.get(EnumLaserInformations.LASERSECRED), (Byte)t.get(EnumLaserInformations.LASERSECGREEN), (Byte)t.get(EnumLaserInformations.LASERSECBLUE), (Integer)t.get(EnumLaserInformations.LASERANGLE1), (Byte)t.get(EnumLaserInformations.LASERANGLE2), (Boolean)t.get(EnumLaserInformations.LASERAUTOROTATE), (Boolean)t.get(EnumLaserInformations.LASERREVERSEROTATION), (Byte)t.get(EnumLaserInformations.LASERROTATIONSPEED), (Boolean)t.get(EnumLaserInformations.LASERSECONDARY), (Byte)t.get(EnumLaserInformations.LASERDISPLAYAXE), (Boolean)t.get(EnumLaserInformations.LASERDOUBLE), (Byte)t.get(EnumLaserInformations.LASERMAINSIZE), (Byte)t.get(EnumLaserInformations.LASERSECSIZE), (Integer)t.get(EnumLaserInformations.LASERHEIGHT), (Boolean)t.get(EnumLaserInformations.TEXTENABLED), (Byte)t.get(EnumLaserInformations.TEXTRED), (Byte)t.get(EnumLaserInformations.TEXTGREEN), (Byte)t.get(EnumLaserInformations.TEXTBLUE), (Integer)t.get(EnumLaserInformations.TEXTANGLE1), (Boolean)t.get(EnumLaserInformations.TEXTAUTOROTATE), (Boolean)t.get(EnumLaserInformations.TEXTREVERSEROTATION), (Byte)t.get(EnumLaserInformations.TEXTROTATIONSPEED), (Byte)t.get(EnumLaserInformations.TEXTSCALE), (Byte)t.get(EnumLaserInformations.TEXTHEIGHT), (Byte)t.get(EnumLaserInformations.LASERSIDESNUMBER));
		PacketSender.sendSpotLightPacket(t, (Byte)t.get(EnumLaserInformations.TIMELINECREATEKEYTIME) & 0xFF, entry);
	}

	public static class BaseListEntry
	{
		private String name;
		private int txtColor;

		public BaseListEntry(String name, int txtColor)
		{
			this.name = name;
			this.txtColor = txtColor;
		}

		public String getName()
		{
			return this.name;
		}

		public int getTxtColor()
		{
			return this.txtColor;
		}
	}

	public static class TextureEntry extends BaseListEntry
	{
		private String path;

		public TextureEntry(String name, String path)
		{
			super(name, 0xffffff);
			this.path = path;
		}

		public String getPath()
		{
			return this.path;
		}
	}

	public static class ConfigEntry extends BaseListEntry
	{
		private int id;

		public ConfigEntry(String name, int id)
		{
			super(name, 0xffffff);
			this.id = id;
		}

		public int getId()
		{
			return this.id;
		}
	}

	public static class BeamVec
	{
		private TSMVec3[] vecs;
		private TSMVec3 lenght;

		public BeamVec(TSMVec3[] vecs, TSMVec3 lenght)
		{
			this.vecs = vecs;
			this.lenght = lenght;
		}

		public TSMVec3[] getVecs()
		{
			return this.vecs;
		}

		public TSMVec3 getLenVec()
		{
			return this.lenght;
		}
	}
}