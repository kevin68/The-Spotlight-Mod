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
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import fr.mcnanotech.kevin_68.thespotlightmod.TheSpotLightMod;
import fr.mcnanotech.kevin_68.thespotlightmod.TileEntitySpotLight;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketData;
import fr.mcnanotech.kevin_68.thespotlightmod.packets.PacketRegenerateFile;

public class TSMJsonManager
{
    public static void generateNewFile(int dimID, BlockPos pos)
    {
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        JsonObject json = new JsonObject();
        json.addProperty("DimID", dimID);
        json.addProperty("X", pos.getX());
        json.addProperty("Y", pos.getY());
        json.addProperty("Z", pos.getZ());
        json.addProperty("ModeBeam", true);

        JsonObject beam = new JsonObject();
        JsonObject bColors = new JsonObject();
        bColors.addProperty("R", 255);
        bColors.addProperty("G", 255);
        bColors.addProperty("B", 255);
        bColors.addProperty("SR", 255);
        bColors.addProperty("SG", 255);
        bColors.addProperty("SB", 255);
        beam.add("Colors", bColors);
        JsonObject bAngles = new JsonObject();
        bAngles.addProperty("X", 0);
        bAngles.addProperty("Y", 0);
        bAngles.addProperty("Z", 0);
        bAngles.addProperty("ARX", false);
        bAngles.addProperty("ARY", false);
        bAngles.addProperty("ARZ", false);
        bAngles.addProperty("RRX", false);
        bAngles.addProperty("RRY", false);
        bAngles.addProperty("RRZ", false);
        bAngles.addProperty("RSX", 0);
        bAngles.addProperty("RSY", 0);
        bAngles.addProperty("RSZ", 0);
        beam.add("Angles", bAngles);
        JsonObject bProperties = new JsonObject();
        bProperties.addProperty("Size", 40);
        bProperties.addProperty("SSize", 75);
        bProperties.addProperty("SBeam", true);
        bProperties.addProperty("Dbl", false);
        bProperties.addProperty("H", 256);
        bProperties.addProperty("Sides", 2);
        beam.add("Properties", bProperties);
        json.add("Beam", beam);

        JsonObject text = new JsonObject();
        text.addProperty("Text", "");
        JsonObject tColors = new JsonObject();
        tColors.addProperty("R", 255);
        tColors.addProperty("G", 255);
        tColors.addProperty("B", 255);
        text.add("Colors", tColors);
        JsonObject tAngles = new JsonObject();
        tAngles.addProperty("Y", 0);
        tAngles.addProperty("ARY", false);
        tAngles.addProperty("RRY", false);
        tAngles.addProperty("RSY", 0);
        text.add("Angles", tAngles);
        JsonObject tProperties = new JsonObject();
        tProperties.addProperty("H", 50);
        tProperties.addProperty("S", 0);
        text.add("Properties", tProperties);

        json.add("Text", text);

        write(new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json"), json);
    }

    public static void deleteFile(int dimID, BlockPos pos)
    {
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
        file.delete();
    }

    public static boolean updateTileData(int dimID, BlockPos pos, TileEntitySpotLight tile)
    {
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        JsonObject json = read(file);
        return updateTileData(tile, json);
    }

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
        try
        {
            tile.isBeam = json.get("ModeBeam").getAsBoolean();
            JsonObject beam = (JsonObject)json.get("Beam");
            JsonObject bColors = (JsonObject)beam.get("Colors");
            tile.beamRed = bColors.get("R").getAsShort();
            tile.beamGreen = bColors.get("G").getAsShort();
            tile.beamBlue = bColors.get("B").getAsShort();
            tile.secBeamRed = bColors.get("SR").getAsShort();
            tile.secBeamGreen = bColors.get("SG").getAsShort();
            tile.secBeamBlue = bColors.get("SB").getAsShort();
            JsonObject bAngles = (JsonObject)beam.get("Angles");
            tile.beamAngleX = bAngles.get("X").getAsShort();
            tile.beamAngleY = bAngles.get("Y").getAsShort();
            tile.beamAngleZ = bAngles.get("Z").getAsShort();
            tile.beamAutoRotateX = bAngles.get("ARX").getAsBoolean();
            tile.beamAutoRotateY = bAngles.get("ARY").getAsBoolean();
            tile.beamAutoRotateZ = bAngles.get("ARZ").getAsBoolean();
            tile.beamReverseRotateX = bAngles.get("RRX").getAsBoolean();
            tile.beamReverseRotateY = bAngles.get("RRY").getAsBoolean();
            tile.beamReverseRotateZ = bAngles.get("RRZ").getAsBoolean();
            tile.beamRotationSpeedX = bAngles.get("RSX").getAsShort();
            tile.beamRotationSpeedY = bAngles.get("RSY").getAsShort();
            tile.beamRotationSpeedZ = bAngles.get("RSZ").getAsShort();
            JsonObject bProperties = (JsonObject)beam.get("Properties");
            tile.beamSize = bProperties.get("Size").getAsShort();
            tile.secBeamSize = bProperties.get("SSize").getAsShort();
            tile.secBeamEnabled = bProperties.get("SBeam").getAsBoolean();
            tile.beamDouble = bProperties.get("Dbl").getAsBoolean();
            tile.beamHeight = bProperties.get("H").getAsShort();
            tile.beamSides = bProperties.get("Sides").getAsShort();

            JsonObject text = (JsonObject)json.get("Text");
            tile.text = text.get("Text").getAsString();
            JsonObject tColors = (JsonObject)text.get("Colors");
            tile.textRed = tColors.get("R").getAsShort();
            tile.textGreen = tColors.get("G").getAsShort();
            tile.textBlue = tColors.get("B").getAsShort();
            JsonObject tAngles = (JsonObject)text.get("Angles");
            tile.textAngleY = tAngles.get("Y").getAsShort();
            tile.textAutoRotateY = tAngles.get("ARY").getAsBoolean();
            tile.textReverseRotateY = tAngles.get("RRY").getAsBoolean();
            tile.textRotationSpeedY = tAngles.get("RSY").getAsShort();
            JsonObject tProperties = (JsonObject)text.get("Properties");
            tile.textHeight = tProperties.get("H").getAsShort();
            tile.textScale = tProperties.get("S").getAsShort();

            tile.markForUpdate();
            return true;
        }
        catch(NullPointerException e)
        {
            try
            {
                TheSpotLightMod.log.error("An entry is missing, regenerating file");
                if(FMLCommonHandler.instance().getSide() == Side.SERVER)
                {
                    deleteFile(json.get("DimID").getAsInt(), new BlockPos(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt()));
                    generateNewFile(json.get("DimID").getAsInt(), new BlockPos(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt()));
                }
                else
                {
                    TheSpotLightMod.network.sendToServer(new PacketRegenerateFile(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt(), json.get("DimID").getAsInt()));
                }
            }
            catch(NullPointerException fatal)
            {
                for(int i = 0; i < 20; i++)
                {
                    TheSpotLightMod.log.fatal("Missing an entry, please delete the file. If this happend when you where connected to a server please contact the server's operator");
                }
            }
        }
        return false;
    }

    public static void updateJsonData(int dimID, BlockPos pos, String data)
    {
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        write(file, data);
    }

    public static String getDataFromJson(int dimID, BlockPos pos)
    {
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        JsonObject json = read(file);
        if(json != null)
        {
            return read(file).toString();
        }
        return null;
    }

    public static JsonObject getDataFromTile(TileEntitySpotLight tile)
    {
        JsonObject json = new JsonObject();
        json.addProperty("DimID", tile.dimensionID);
        json.addProperty("X", tile.getPos().getX());
        json.addProperty("Y", tile.getPos().getY());
        json.addProperty("Z", tile.getPos().getZ());
        json.addProperty("ModeBeam", tile.isBeam);

        JsonObject beam = new JsonObject();
        JsonObject bColors = new JsonObject();
        bColors.addProperty("R", tile.beamRed);
        bColors.addProperty("G", tile.beamGreen);
        bColors.addProperty("B", tile.beamBlue);
        bColors.addProperty("SR", tile.secBeamRed);
        bColors.addProperty("SG", tile.secBeamGreen);
        bColors.addProperty("SB", tile.secBeamBlue);
        beam.add("Colors", bColors);
        JsonObject bAngles = new JsonObject();
        bAngles.addProperty("X", tile.beamAngleX);
        bAngles.addProperty("Y", tile.beamAngleY);
        bAngles.addProperty("Z", tile.beamAngleZ);
        bAngles.addProperty("ARX", tile.beamAutoRotateX);
        bAngles.addProperty("ARY", tile.beamAutoRotateY);
        bAngles.addProperty("ARZ", tile.beamAutoRotateZ);
        bAngles.addProperty("RRX", tile.beamReverseRotateX);
        bAngles.addProperty("RRY", tile.beamReverseRotateY);
        bAngles.addProperty("RRZ", tile.beamReverseRotateZ);
        bAngles.addProperty("RSX", tile.beamRotationSpeedX);
        bAngles.addProperty("RSY", tile.beamRotationSpeedY);
        bAngles.addProperty("RSZ", tile.beamRotationSpeedZ);
        beam.add("Angles", bAngles);
        JsonObject bProperties = new JsonObject();
        bProperties.addProperty("Size", tile.beamSize);
        bProperties.addProperty("SSize", tile.secBeamSize);
        bProperties.addProperty("SBeam", tile.secBeamEnabled);
        bProperties.addProperty("Dbl", tile.beamDouble);
        bProperties.addProperty("H", tile.beamHeight);
        bProperties.addProperty("Sides", tile.beamSides);
        beam.add("Properties", bProperties);
        json.add("Beam", beam);

        JsonObject text = new JsonObject();
        text.addProperty("Text", tile.text);
        JsonObject tColors = new JsonObject();
        tColors.addProperty("R", tile.textRed);
        tColors.addProperty("G", tile.textGreen);
        tColors.addProperty("B", tile.textBlue);
        text.add("Colors", tColors);
        JsonObject tAngles = new JsonObject();
        tAngles.addProperty("Y", tile.textAngleY);
        tAngles.addProperty("ARY", tile.textAutoRotateY);
        tAngles.addProperty("RRY", tile.textReverseRotateY);
        tAngles.addProperty("RSY", tile.textRotationSpeedY);
        text.add("Angles", tAngles);
        JsonObject tProperties = new JsonObject();
        tProperties.addProperty("H", tile.textHeight);
        tProperties.addProperty("S", tile.textScale);
        text.add("Properties", tProperties);
        json.add("Text", text);
        
        return json;
    }

    public static void saveConfig(ItemStack stack, TileEntitySpotLight tile)
    {
        String configName = String.valueOf(System.currentTimeMillis());
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("ConfigName", configName);
        stack.setTagCompound(tag);
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", "configs").getPath());
        File file = new File(folder, configName + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        write(file, getDataFromTile(tile));
    }

    public static void loadConfig(ItemStack stack, TileEntitySpotLight tile)
    {
        String configName = stack.getTagCompound().getString("ConfigName");
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", "configs").getPath());
        File file = new File(folder, configName + ".json");
        updateTileData(tile, read(file));
        TheSpotLightMod.network.sendToAll(new PacketData(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), getDataFromTile(tile).toString()));
    }

    public static void deleteConfig(ItemStack stack)
    {
        String configName = stack.getTagCompound().getString("ConfigName");
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", "configs").getPath());
        File file = new File(folder, configName + ".json");
        file.delete();
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

    @SuppressWarnings("resource")
    private static JsonObject read(File dir)
    {
        JsonParser parser = new JsonParser();
        try
        {
            JsonObject json = (JsonObject)parser.parse(new FileReader(dir));
            return json;
        }
        catch(FileNotFoundException e)
        {
            TheSpotLightMod.log.error("File missing, generating a new one");
            generateNewFile(Integer.valueOf(new File(dir.getParent()).getName()), new BlockPos(Integer.valueOf(dir.getName().replace(".json", "").split("_")[0]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[1]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[2])));
        }
        catch(JsonParseException e)
        {
            TheSpotLightMod.log.error("File is not a JSON, generating a new one");
            deleteFile(Integer.valueOf(new File(dir.getParent()).getName()), new BlockPos(Integer.valueOf(dir.getName().replace(".json", "").split("_")[0]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[1]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[2])));
            generateNewFile(Integer.valueOf(new File(dir.getParent()).getName()), new BlockPos(Integer.valueOf(dir.getName().replace(".json", "").split("_")[0]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[1]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[2])));
        }
        return null;
    }

    public static String compress(String str) throws IOException
    {
        if(str == null || str.length() == 0)
        {
            return str;
        }
        // System.out.println("String length : " + str.length());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = out.toString("ISO-8859-1");
        // System.out.println(outStr);
        // System.out.println("Output String lenght : " + outStr.length());
        return outStr;
    }

    public static String decompress(String str) throws IOException
    {
        if(str == null || str.length() == 0)
        {
            return str;
        }
        // System.out.println("Input String length : " + str.length());
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
        String outStr = "";
        String line;
        while((line = bf.readLine()) != null)
        {
            outStr += line;
        }
        // System.out.println("Output String lenght : " + outStr.length());
        return outStr;
    }
}