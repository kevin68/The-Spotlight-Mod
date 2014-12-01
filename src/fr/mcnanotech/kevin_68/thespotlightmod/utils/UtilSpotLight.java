package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
					cutted.substring(0, cutted.length() - words[i - 2].length());
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
			return name;
		}

		public int getTxtColor()
		{
			return txtColor;
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
			return path;
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
			return id;
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
			return vecs;
		}

		public TSMVec3 getLenVec()
		{
			return lenght;
		}
	}
}