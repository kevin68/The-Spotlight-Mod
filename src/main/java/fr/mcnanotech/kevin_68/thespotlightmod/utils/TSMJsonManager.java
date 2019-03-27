package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import fr.mcnanotech.kevin_68.thespotlightmod.TSMNetwork;
import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumSaveCategory;
import fr.mcnanotech.kevin_68.thespotlightmod.enums.EnumTSMProperty;
import fr.mcnanotech.kevin_68.thespotlightmod.objs.TSMKey;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRegenerateFile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class TSMJsonManager
{
    public static void generateNewFiles(World world, BlockPos pos)
    {
        File folder = getSaveDir(world);
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
        File fileTL = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + "_TL" + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        if(file.exists())
        {
            deleteFile(world, pos);
        }
        if(fileTL.exists())
        {
            deleteFile(world, pos);
        }
        JsonObject json = new JsonObject();
        json.addProperty("DimID", world.getDimension().getType().getId());
        json.addProperty("X", pos.getX());
        json.addProperty("Y", pos.getY());
        json.addProperty("Z", pos.getZ());
        json.addProperty("ModeBeam", true);
        json.addProperty("Redstone", true);

        for(EnumTSMProperty prop : EnumTSMProperty.values())
        {
            List<String> catPath = new ArrayList<String>();
            EnumSaveCategory cat = prop.saveCat;
            while(cat != null)
            {
                catPath.add(cat.saveName);
                cat = cat.parent;
            }
            catPath = Lists.reverse(catPath);
            JsonObject obj = json;
            for(String s : catPath)
            {
                if(!obj.has(s))
                {
                    obj.add(s, new JsonObject());
                }
                obj = obj.get(s).getAsJsonObject();
            }

            switch(prop.type)
            {
                case BOOLEAN:
                    obj.addProperty(prop.saveName, (Boolean)prop.def);
                    break;
                case FLOAT:
                case SHORT:
                    obj.addProperty(prop.saveName, (Number)prop.def);
                    break;
                case STRING:
                    obj.addProperty(prop.saveName, (String)prop.def);
                    break;
                default:
                    System.out.println("Invalid or Missing type should not happen");
                    break;
            }
        }

        JsonObject timeline = new JsonObject();
        JsonObject calculated = new JsonObject();
        for(EnumTSMProperty prop : EnumTSMProperty.values())
        {
            if(prop.timelinable && prop.type.smoothable)
            {
                switch(prop.type)
                {
                    case SHORT:
                        Short[] emptyTabS = new Short[1200];
                        for(int i = 0; i < 1200; i++)
                        {
                            emptyTabS[i] = 0;
                        }
                        getObjFromTabS(calculated, prop.saveNameTL, emptyTabS);
                        break;
                    case FLOAT:
                        Float[] emptyTabF = new Float[1200];
                        for(int i = 0; i < 1200; i++)
                        {
                            emptyTabF[i] = 0.0F;
                        }
                        getObjFromTabF(calculated, prop.saveNameTL, emptyTabF);
                        break;
                    default:
                        System.out.println("Again, missing or invalid type");
                        break;
                }
            }
        }

        timeline.add("Calculated", calculated);
        timeline.add("Keys", getJsonFromTSMKeys(new TSMKey[120]));

        write(file, json);
        write(fileTL, timeline);
    }

    public static void deleteFile(World world, BlockPos pos)
    {
        try
        {
            File folder = getSaveDir(world);
            File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
            File fileTL = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + "_TL" + ".json");
            file.delete();
            fileTL.delete();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
     * Server Side
     */
    public static boolean updateTileData(BlockPos pos, TileEntitySpotLight tile)
    {
        File file = new File(getSaveDir(tile.getWorld()), pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
        JsonObject json = read(file, tile.getWorld());
        return updateTileData(tile, json);
    }

    /*
     * Client side
     */
    public static boolean updateTileData(TileEntitySpotLight tile, String data)
    {
        if(data.equals("null"))
        {
            return false;
        }
        JsonParser parser = new JsonParser();
        JsonObject json = (JsonObject)parser.parse(data);
        return updateTileData(tile, json);
    }

    private static boolean updateTileData(TileEntitySpotLight tile, JsonObject json)
    {
        if(json != null)
        {
            if(json.has("ModeBeam"))
            {
                tile.isBeam = json.get("ModeBeam").getAsBoolean();
            }
            if(json.has("Redstone"))
            {
                tile.redstone = json.get("Redstone").getAsBoolean();
            }
            for(EnumTSMProperty prop : EnumTSMProperty.values())
            {
                List<EnumSaveCategory> parents = new ArrayList<EnumSaveCategory>();
                EnumSaveCategory current = prop.saveCat;
                while(current != null)
                {
                    parents.add(current);
                    current = current.parent;
                }
                parents = Lists.reverse(parents);
                JsonObject curJobj = json;
                for(EnumSaveCategory cat : parents)
                {
                    if(curJobj.has(cat.saveName))
                    {
                        curJobj = curJobj.get(cat.saveName).getAsJsonObject();
                    }
                    else
                    {
                        tile.setProperty(prop, prop.def);
                    }
                }
                if(curJobj.has(prop.saveName))
                {
                    try
                    {
                        switch(prop.type)
                        {
                            case BOOLEAN:
                                tile.setProperty(prop, curJobj.get(prop.saveName).getAsBoolean());
                                break;
                            case FLOAT:
                                tile.setProperty(prop, curJobj.get(prop.saveName).getAsFloat());
                                break;
                            case SHORT:
                                tile.setProperty(prop, curJobj.get(prop.saveName).getAsShort());
                                break;
                            case STRING:
                                tile.setProperty(prop, curJobj.get(prop.saveName).getAsString());
                                break;
                            default:
                                System.out.println("And again another missing type");
                                break;
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println("Invalid value in json for: " + prop.name() + " using default");
                        tile.setProperty(prop, prop.def);
                    }
                }
                else
                {
                    tile.setProperty(prop, prop.def);
                }
            }

            tile.markForUpdate();
            return true;
        }
        else
        {
            if(!tile.getWorld().isRemote)
            {
                deleteFile(tile.getWorld(), tile.getPos());
                generateNewFiles(tile.getWorld(), tile.getPos());
            }
            else
            {
                TSMNetwork.CHANNEL.sendToServer(new PacketRegenerateFile(tile.getPos()));
            }
        }
        return false;
    }

    public static void updateJsonData(World world, BlockPos pos, String data)
    {
    	File folder = getSaveDir(world);
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
        if(!folder.exists())
        {
        	folder.mkdirs();
        }
        write(file, data);
    }

    public static String getDataFromJson(World world, BlockPos pos)
    {
        File folder = getSaveDir(world);
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
            generateNewFiles(world, pos);
        }
        JsonObject json = read(file, world);
        if(json != null)
        {
            return read(file, world).toString();
        }
        return null;
    }

    public static JsonObject getDataFromTile(TileEntitySpotLight tile)
    {
        JsonObject json = new JsonObject();
        json.addProperty("X", tile.getPos().getX());
        json.addProperty("Y", tile.getPos().getY());
        json.addProperty("Z", tile.getPos().getZ());
        json.addProperty("ModeBeam", tile.isBeam);
        json.addProperty("Redstone", tile.redstone);

        for(EnumTSMProperty prop : EnumTSMProperty.values())
        {
            List<String> catPath = new ArrayList<String>();
            EnumSaveCategory cat = prop.saveCat;
            while(cat != null)
            {
                catPath.add(cat.saveName);
                cat = cat.parent;
            }
            catPath = Lists.reverse(catPath);
            JsonObject obj = json;
            for(String s : catPath)
            {
                if(!obj.has(s))
                {
                    obj.add(s, new JsonObject());
                }
                obj = obj.get(s).getAsJsonObject();
            }

            switch(prop.type)
            {
                case BOOLEAN:
                    obj.addProperty(prop.saveName, tile.getBoolean(prop));
                    break;
                case FLOAT:
                    obj.addProperty(prop.saveName, tile.getFloat(prop));
                    break;
                case SHORT:
                    obj.addProperty(prop.saveName, tile.getShort(prop));
                    break;
                case STRING:
                    obj.addProperty(prop.saveName, tile.getString(prop));
                    break;
                default:
                    System.out.println("Invalid or Missing type should not happen");
                    break;
            }
        }
        return json;
    }

    public static void saveConfig(ItemStack stack, TileEntitySpotLight tile)
    {
        String configName = String.valueOf(System.currentTimeMillis());
        NBTTagCompound tag = new NBTTagCompound();
        tag.putString("ConfigName", configName);
        stack.setTag(tag);
        File folder = getConfItemSaveDir();
        File file = new File(folder, configName + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        JsonObject obj = getDataFromTile(tile);
        obj.add("Timeline", getTlDataFromTile(tile));
        write(file, obj);
    }

    public static void loadConfig(ItemStack stack, TileEntitySpotLight tile)
    {
        String configName = stack.getTag().getString("ConfigName");
        File file = new File(getConfItemSaveDir(), configName + ".json");
        JsonObject json = read(file, tile.getWorld());
        JsonObject obj = json.get("Timeline").getAsJsonObject();
        updateTileData(tile, json);
        updateTileTimeline(tile, obj);
        try {
            TSMNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketData(tile.getPos(), TSMJsonManager.compress(getDataFromTile(tile).toString())));
        } catch (IOException e) {
            TheSpotLightMod.LOGGER.error("Failed to compress spotlight data, packet not sended");
        }
    }

    public static void deleteConfig(ItemStack stack)
    {
        String configName = stack.getOrCreateTag().getString("ConfigName");
        File file = new File(getConfItemSaveDir(), configName + ".json");
        if (file.exists()) {        	
        	file.delete();
        }
    }

    /*
     * Server side
     */
    public static boolean updateTileTimeline(World world, BlockPos pos, TileEntitySpotLight tile)
    {
        File fileTL = new File(getSaveDir(world), pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + "_TL" + ".json");
        JsonObject json = read(fileTL, world);
        return updateTileTimeline(tile, json);
    }

    /*
     * Client side
     */
    public static boolean updateTileTimeline(TileEntitySpotLight tile, String data)
    {
        if(data.equals("null"))
        {
            return false;
        }
        JsonParser parser = new JsonParser();
        JsonObject json = (JsonObject)parser.parse(data);
        return updateTileTimeline(tile, json);
    }

    private static boolean updateTileTimeline(TileEntitySpotLight tile, JsonObject json)
    {
        try
        {
            JsonArray keys = json.get("Keys").getAsJsonArray();
            getKeysFromObj(tile, keys);
            JsonObject calculated = (JsonObject)json.get("Calculated");

            for(EnumTSMProperty prop : EnumTSMProperty.values())
            {
                if(prop.timelinable && prop.type.smoothable)
                {
                    switch(prop.type)
                    {
                        case SHORT:
                            tile.setTimelineProperty(prop, getTabFromObjS(calculated, prop.saveNameTL));
                            break;
                        case FLOAT:
                            tile.setTimelineProperty(prop, getTabFromObjF(calculated, prop.saveNameTL));
                            break;
                        default:
                            System.out.println("Again, missing or invalid type");
                            break;
                    }
                }
            }

            return true;
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            try
            {
                if(FMLEnvironment.dist.isDedicatedServer())
                {
                    deleteFile(tile.getWorld(), new BlockPos(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt()));
                    generateNewFiles(tile.getWorld(), new BlockPos(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt()));
                }
                else
                {
                    TSMNetwork.CHANNEL.sendToServer(new PacketRegenerateFile(new BlockPos(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt())));
                }
            }
            catch(NullPointerException fatal)
            {
                TheSpotLightMod.LOGGER.error("Missing an entry, please delete the file. If this happend when you where connected to a server please contact the server's operator");
            }
        }
        return false;
    }

    public static void updateTlJsonData(World world, BlockPos pos, String data)
    {
        File folder = getSaveDir(world);
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + "_TL" + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        write(file, data);
    }

    public static String getTlDataFromJson(World world, BlockPos pos)
    {
        File folder = getSaveDir(world);
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + "_TL" + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
            generateNewFiles(world, pos);
        }
        JsonObject json = read(file, world);
        if(json != null)
        {
            return read(file, world).toString();
        }
        return null;
    }

    public static JsonObject getTlDataFromTile(TileEntitySpotLight tile)
    {
        JsonObject json = new JsonObject();
        JsonObject calculated = new JsonObject();

        for(EnumTSMProperty prop : EnumTSMProperty.values())
        {
            if(prop.timelinable && prop.type.smoothable)
            {
                switch(prop.type)
                {
                    case SHORT:
                        getObjFromTabS(calculated, prop.saveNameTL, tile.getTimelineShort(prop));
                        break;
                    case FLOAT:
                        getObjFromTabF(calculated, prop.saveNameTL, tile.getTimelineFloat(prop));
                        break;
                    default:
                        System.out.println("Again, missing or invalid type");
                        break;
                }
            }
        }

        json.add("Calculated", calculated);
        json.add("Keys", getJsonFromTSMKeys(tile.getKeys()));
        return json;
    }

    private static JsonArray getJsonFromTSMKeys(TSMKey[] keys)
    {
        JsonArray obj = new JsonArray();
        for(TSMKey key : keys)
        {
            if(key != null)
            {
                JsonObject o = new JsonObject();
                o.addProperty("Time", key.time);
                for(EnumTSMProperty prop : EnumTSMProperty.values())
                {
                    if(prop.timelinable)
                    {
                        switch(prop.type)
                        {
                            case BOOLEAN:
                                o.addProperty(prop.saveNameTL, key.getB(prop));
                                break;
                            case FLOAT:
                                o.addProperty(prop.saveNameTL, key.getF(prop));
                                break;
                            case SHORT:
                                o.addProperty(prop.saveNameTL, key.getS(prop));
                                break;
                            case STRING:
                                o.addProperty(prop.saveNameTL, key.getStr(prop));
                                break;
                            default:
                                System.out.println("Yet another missing type");
                                break;

                        }
                    }
                }

                obj.add(o);
            }
        }
        return obj;
    }

    private static void getKeysFromObj(TileEntitySpotLight tile, JsonArray keys)
    {
        for(int i = 0; i < keys.size(); i++)
        {
            JsonObject obj = keys.get(i).getAsJsonObject();
            Map<EnumTSMProperty, Object> properties = new HashMap<EnumTSMProperty, Object>();
            for(EnumTSMProperty prop : EnumTSMProperty.values())
            {
                if(prop.timelinable)
                {
                    if(obj.has(prop.saveNameTL))
                    {
                        try
                        {
                            switch(prop.type)
                            {
                                case BOOLEAN:
                                    properties.put(prop, obj.get(prop.saveNameTL).getAsBoolean());
                                    break;
                                case FLOAT:
                                    properties.put(prop, obj.get(prop.saveNameTL).getAsFloat());
                                    break;
                                case SHORT:
                                    properties.put(prop, obj.get(prop.saveNameTL).getAsShort());
                                    break;
                                case STRING:
                                    properties.put(prop, obj.get(prop.saveNameTL).getAsString());
                                    break;
                                default:
                                    System.out.println("Yet another missing type again");
                                    break;
                            }
                        }
                        catch(Exception e)
                        {
                            System.out.println("Invalid value in timeline json for: " + prop.name() + " using default");
                            properties.put(prop, prop.def);
                        }
                    }
                    else
                    {
                        properties.put(prop, prop.def);
                    }
                }
            }

            TSMKey key = new TSMKey(obj.get("Time").getAsShort(), properties);
            tile.setKey(obj.get("Time").getAsShort(), key);
        }
    }

    private static void getObjFromTabS(JsonObject obj, String name, Short[] tab)
    {
        String str = "";
        boolean sames = true;
        short prev = tab[0];
        for(int i = 1; i < tab.length; i++)
        {
            sames &= tab[i] == prev;
            prev = tab[i];
        }
        if(sames)
        {
            str = "" + tab[0];
            obj.addProperty(name, str);
        }
        else
        {
            for(int i = 0; i < tab.length; i++)
            {
                str += ":" + tab[i];
            }
            str = str.substring(1);
            obj.addProperty(name, str);
        }
    }

    private static void getObjFromTabF(JsonObject obj, String name, Float[] tab)
    {
        String str = "";
        boolean sames = true;
        float prev = tab[0];
        for(int i = 1; i < tab.length; i++)
        {
            sames &= tab[i] == prev;
            prev = tab[i];
        }
        if(sames)
        {
            str = "" + tab[0];
            obj.addProperty(name, str);
        }
        else
        {
            for(int i = 0; i < tab.length; i++)
            {
                str += ":" + (tab[i] < 1.0F ? String.valueOf(tab[i]).replace("0.", "") : 1.0F);
            }
            str = str.substring(1);
            obj.addProperty(name, str);
        }
    }

    private static Short[] getTabFromObjS(JsonObject calculated, String name)
    {
        String str = calculated.get(name).getAsString();
        Short[] tab = new Short[1200];
        if(str.contains(":"))
        {
            String[] strs = str.split(":");
            for(int i = 0; i < strs.length; i++)
            {
                if(StringUtils.isNumeric(strs[i]))
                {
                    tab[i] = Short.valueOf(strs[i]);
                }
                else
                {
                    tab[i] = 0;
                }
            }
            return tab;
        }

        for(int i = 0; i < tab.length; i++)
        {
            tab[i] = StringUtils.isNumeric(str) ? Short.valueOf(str) : 0;
        }
        return tab;
    }

    private static Float[] getTabFromObjF(JsonObject calculated, String name)
    {
        String decimalPattern = "([0-9]*)\\.([0-9]*)";
        String str = calculated.get(name).getAsString();
        Float[] tab = new Float[1200];
        if(str.contains(":"))
        {
            String[] strs = str.split(":");
            for(int i = 0; i < strs.length; i++)
            {
                if(Pattern.matches(decimalPattern, strs[i]))
                {
                    tab[i] = Float.valueOf(strs[i]);
                }
                else if(StringUtils.isNumeric(strs[i]))
                {
                    tab[i] = Float.valueOf("0." + strs[i]);
                }
                else
                {
                    tab[i] = 0.0F;
                }
            }
            return tab;
        }

        for(int i = 0; i < tab.length; i++)
        {
            tab[i] = Pattern.matches(decimalPattern, str) ? Float.valueOf(str) : 0;
        }
        return tab;
    }

    private static void write(File dir, JsonObject obj)
    {
        write(dir, obj.toString());
    }

    private static void write(File dir, String obj)
    {
        try
        {
            JsonParser parser = new JsonParser();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String jsonString;
            JsonElement el = parser.parse(obj);
            jsonString = gson.toJson(el);

            FileWriter file = new FileWriter(dir);
            file.write(jsonString);
            file.flush();
            file.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static JsonObject read(File dir, World world)
    {
        JsonParser parser = new JsonParser();
        try
        {
            JsonElement json = parser.parse(new FileReader(dir));
            if(json.isJsonNull())
            {
                return null;
            }
            return json.getAsJsonObject();
        }
        catch(FileNotFoundException e)
        {
            generateNewFiles(world, new BlockPos(Integer.valueOf(dir.getName().replace(".json", "").split("_")[0]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[1]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[2])));
        }
        catch(JsonParseException e)
        {
            TheSpotLightMod.LOGGER.error("File is not a JSON, generating a new one");
            deleteFile(world, new BlockPos(Integer.valueOf(dir.getName().replace(".json", "").split("_")[0]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[1]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[2])));
            generateNewFiles(world, new BlockPos(Integer.valueOf(dir.getName().replace(".json", "").split("_")[0]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[1]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[2])));
        }
        return null;
    }

    public static String compress(String str) throws IOException
    {
        if(str == null || str.length() == 0)
        {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = out.toString("ISO-8859-1");
        return outStr;
    }

    public static String decompress(String str) throws IOException
    {
        if(str == null || str.length() == 0)
        {
            return str;
        }
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
        String outStr = "";
        String line;
        while((line = bf.readLine()) != null)
        {
            outStr += line;
        }
        return outStr;
    }

    public static HashMap<Integer, List<JsonObject>> getAll()
    {
        return null;
    }
    
    private static File getSaveDir(World world) {
        File spotlightDataDir = new File(new File(world.getSaveHandler().getWorldDirectory(), "SpotLights"), String.valueOf(world.getDimension().getType().getId()));
        spotlightDataDir.mkdirs();
        return spotlightDataDir;
    }
    
    private static File getConfItemSaveDir() {
        World w = DimensionManager.getWorld(ServerLifecycleHooks.getCurrentServer(), DimensionType.OVERWORLD, false, true);
    	return new File(getSaveDir(w), "configs");
    }
}