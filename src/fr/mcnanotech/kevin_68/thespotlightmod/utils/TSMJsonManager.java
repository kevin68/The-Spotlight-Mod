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
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
    public static void generateNewFiles(int dimID, BlockPos pos)
    {
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
        File fileTL = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + "_TL" + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        if(file.exists())
        {
            deleteFile(dimID, pos);
        }
        if(fileTL.exists())
        {
            deleteFile(dimID, pos);
        }
        JsonObject json = new JsonObject();
        json.addProperty("DimID", dimID);
        json.addProperty("X", pos.getX());
        json.addProperty("Y", pos.getY());
        json.addProperty("Z", pos.getZ());
        json.addProperty("ModeBeam", true);
        json.addProperty("Redstone", true);

        JsonObject beam = new JsonObject();
        JsonObject bColors = new JsonObject();
        bColors.addProperty("R", 255);
        bColors.addProperty("G", 255);
        bColors.addProperty("B", 255);
        bColors.addProperty("A", 1.0F);
        bColors.addProperty("SR", 255);
        bColors.addProperty("SG", 255);
        bColors.addProperty("SB", 255);
        bColors.addProperty("SA", 0.125F);
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
        tProperties.addProperty("B", false);
        tProperties.addProperty("ST", false);
        tProperties.addProperty("U", false);
        tProperties.addProperty("I", false);
        tProperties.addProperty("O", false);
        tProperties.addProperty("Sh", false);
        tProperties.addProperty("T", false);
        tProperties.addProperty("TS", 50);
        tProperties.addProperty("RT", false);
        text.add("Properties", tProperties);
        json.add("Text", text);

        JsonObject timeline = new JsonObject();
        JsonObject calculated = new JsonObject();
        getObjFromTabS(calculated, "BR", new short[1200]);
        getObjFromTabS(calculated, "BG", new short[1200]);
        getObjFromTabS(calculated, "BB", new short[1200]);
        getObjFromTabF(calculated, "BA", new float[1200]);
        getObjFromTabS(calculated, "SBR", new short[1200]);
        getObjFromTabS(calculated, "SBG", new short[1200]);
        getObjFromTabS(calculated, "SBB", new short[1200]);
        getObjFromTabF(calculated, "SBA", new float[1200]);
        getObjFromTabS(calculated, "AX", new short[1200]);
        getObjFromTabS(calculated, "AY", new short[1200]);
        getObjFromTabS(calculated, "AZ", new short[1200]);
        // TODO fill
        timeline.add("Calculated", calculated);
        timeline.add("Keys", getJsonFromTSMKeys(new TSMKey[120]));

        write(file, json);
        write(fileTL, timeline);
    }

    public static void deleteFile(int dimID, BlockPos pos)
    {
        try
        {
            File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
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
    public static boolean updateTileData(int dimID, BlockPos pos, TileEntitySpotLight tile)
    {
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
        JsonObject json = read(file);
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
        try
        {
            tile.isBeam = json.get("ModeBeam").getAsBoolean();
            tile.redstone = json.get("Redstone").getAsBoolean();
            JsonObject beam = (JsonObject)json.get("Beam");
            JsonObject bColors = (JsonObject)beam.get("Colors");
            tile.beamRed = bColors.get("R").getAsShort();
            tile.beamGreen = bColors.get("G").getAsShort();
            tile.beamBlue = bColors.get("B").getAsShort();
            tile.beamAlpha = bColors.get("A").getAsFloat();
            tile.secBeamRed = bColors.get("SR").getAsShort();
            tile.secBeamGreen = bColors.get("SG").getAsShort();
            tile.secBeamBlue = bColors.get("SB").getAsShort();
            tile.secBeamAlpha = bColors.get("SA").getAsFloat();
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
            tile.textBold = tProperties.get("B").getAsBoolean();
            tile.textStrike = tProperties.get("ST").getAsBoolean();
            tile.textUnderline = tProperties.get("U").getAsBoolean();
            tile.textItalic = tProperties.get("I").getAsBoolean();
            tile.textObfuscated = tProperties.get("O").getAsBoolean();
            tile.textShadow = tProperties.get("Sh").getAsBoolean();
            tile.textTranslating = tProperties.get("T").getAsBoolean();
            tile.textTranslateSpeed = tProperties.get("TS").getAsShort();
            tile.textReverseTranslating = tProperties.get("RT").getAsBoolean();
            tile.markForUpdate();
            return true;
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            try
            {
                if(FMLCommonHandler.instance().getSide() == Side.SERVER)
                {
                    deleteFile(json.get("DimID").getAsInt(), new BlockPos(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt()));
                    generateNewFiles(json.get("DimID").getAsInt(), new BlockPos(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt()));
                }
                else
                {
                    TheSpotLightMod.network.sendToServer(new PacketRegenerateFile(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt(), json.get("DimID").getAsInt()));
                }
            }
            catch(NullPointerException fatal)
            {
                TheSpotLightMod.log.error("Missing an entry, please delete the file. If this happend when you where connected to a server please contact the server's operator");
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
            generateNewFiles(dimID, pos);
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
        json.addProperty("Redstone", tile.redstone);

        JsonObject beam = new JsonObject();
        JsonObject bColors = new JsonObject();
        bColors.addProperty("R", tile.beamRed);
        bColors.addProperty("G", tile.beamGreen);
        bColors.addProperty("B", tile.beamBlue);
        bColors.addProperty("A", tile.beamAlpha);
        bColors.addProperty("SR", tile.secBeamRed);
        bColors.addProperty("SG", tile.secBeamGreen);
        bColors.addProperty("SB", tile.secBeamBlue);
        bColors.addProperty("SA", tile.secBeamAlpha);
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
        tProperties.addProperty("B", tile.textBold);
        tProperties.addProperty("ST", tile.textStrike);
        tProperties.addProperty("U", tile.textUnderline);
        tProperties.addProperty("I", tile.textItalic);
        tProperties.addProperty("O", tile.textObfuscated);
        tProperties.addProperty("Sh", tile.textShadow);
        tProperties.addProperty("T", tile.textTranslating);
        tProperties.addProperty("TS", tile.textTranslateSpeed);
        tProperties.addProperty("RT", tile.textReverseTranslating);
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
        JsonObject obj = getDataFromTile(tile);
        obj.add("Timeline", getTlDataFromTile(tile));
        write(file, obj);
    }

    public static void loadConfig(ItemStack stack, TileEntitySpotLight tile)
    {
        String configName = stack.getTagCompound().getString("ConfigName");
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", "configs").getPath());
        File file = new File(folder, configName + ".json");
        JsonObject json = read(file);
        json.addProperty("DimID", tile.dimensionID);
        JsonObject obj = json.get("Timeline").getAsJsonObject();
        updateTileData(tile, json);
        updateTileTimeline(tile, obj);
        TheSpotLightMod.network.sendToAll(new PacketData(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), getDataFromTile(tile).toString()));
    }

    public static void deleteConfig(ItemStack stack)
    {
        String configName = stack.getTagCompound().getString("ConfigName");
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", "configs").getPath());
        File file = new File(folder, configName + ".json");
        file.delete();
    }

    /*
     * Server side
     */
    public static boolean updateTileTimeline(int dimID, BlockPos pos, TileEntitySpotLight tile)
    {
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
        File fileTL = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + "_TL" + ".json");
        JsonObject json = read(fileTL);
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
            tile.tlBRed = getTabFromObjS(calculated, "BR");
            tile.tlBGreen = getTabFromObjS(calculated, "BG");
            tile.tlBBlue = getTabFromObjS(calculated, "BB");
            tile.tlBAlpha = getTabFromObjF(calculated, "BA");
            tile.tlSecBRed = getTabFromObjS(calculated, "SBR");
            tile.tlSecBGreen = getTabFromObjS(calculated, "SBG");
            tile.tlSecBBlue = getTabFromObjS(calculated, "SBB");
            tile.tlSecBAlpha = getTabFromObjF(calculated, "SBA");
            tile.tlBAngleX = getTabFromObjS(calculated, "AX");
            tile.tlBAngleY = getTabFromObjS(calculated, "AY");
            tile.tlBAngleZ = getTabFromObjS(calculated, "AZ");
            // TODO fill
            return true;
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            try
            {
                if(FMLCommonHandler.instance().getSide() == Side.SERVER)
                {
                    deleteFile(json.get("DimID").getAsInt(), new BlockPos(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt()));
                    generateNewFiles(json.get("DimID").getAsInt(), new BlockPos(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt()));
                }
                else
                {
                    TheSpotLightMod.network.sendToServer(new PacketRegenerateFile(json.get("X").getAsInt(), json.get("Y").getAsInt(), json.get("Z").getAsInt(), json.get("DimID").getAsInt()));
                }
            }
            catch(NullPointerException fatal)
            {
                TheSpotLightMod.log.error("Missing an entry, please delete the file. If this happend when you where connected to a server please contact the server's operator");
            }
        }
        return false;
    }

    public static void updateTlJsonData(int dimID, BlockPos pos, String data)
    {
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + "_TL" + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        write(file, data);
    }

    public static String getTlDataFromJson(int dimID, BlockPos pos)
    {
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + "_TL" + ".json");
        if(!folder.exists())
        {
            folder.mkdirs();
            generateNewFiles(dimID, pos);
        }
        JsonObject json = read(file);
        if(json != null)
        {
            return read(file).toString();
        }
        return null;
    }

    public static JsonObject getTlDataFromTile(TileEntitySpotLight tile)
    {
        JsonObject json = new JsonObject();
        JsonObject calculated = new JsonObject();
        getObjFromTabS(calculated, "BR", tile.tlBRed);
        getObjFromTabS(calculated, "BG", tile.tlBGreen);
        getObjFromTabS(calculated, "BB", tile.tlBBlue);
        getObjFromTabF(calculated, "BA", tile.tlBAlpha);
        getObjFromTabS(calculated, "SBR", tile.tlSecBRed);
        getObjFromTabS(calculated, "SBG", tile.tlSecBGreen);
        getObjFromTabS(calculated, "SBB", tile.tlSecBBlue);
        getObjFromTabF(calculated, "SBA", tile.tlSecBAlpha);
        getObjFromTabS(calculated, "AX", tile.tlBAngleX);
        getObjFromTabS(calculated, "AY", tile.tlBAngleY);
        getObjFromTabS(calculated, "AZ", tile.tlBAngleZ);
        // TODO fill
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
                o.addProperty("BR", key.bRed);
                o.addProperty("BG", key.bGreen);
                o.addProperty("BB", key.bBlue);
                o.addProperty("BA", key.bAlpha);
                o.addProperty("SBR", key.secBRed);
                o.addProperty("SBG", key.secBGreen);
                o.addProperty("SBB", key.secBBlue);
                o.addProperty("SBA", key.secBAlpha);
                o.addProperty("AX", key.bAngleX);
                o.addProperty("AY", key.bAngleY);
                o.addProperty("AZ", key.bAngleZ);
                o.addProperty("ARX", key.bARX);
                o.addProperty("ARY", key.bARY);
                o.addProperty("ARZ", key.bARZ);
                o.addProperty("RRX", key.bRRX);
                o.addProperty("RRY", key.bRRY);
                o.addProperty("RRZ", key.bRRZ);
                // TODO fill
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
            TSMKey k = new TSMKey(obj.get("Time").getAsShort(), obj.get("BR").getAsShort(), obj.get("BG").getAsShort(), obj.get("BB").getAsShort(), obj.get("BA").getAsShort(), obj.get("SBR").getAsShort(), obj.get("SBG").getAsShort(), obj.get("SBB").getAsShort(), obj.get("SBA").getAsFloat(), obj.get("AX").getAsShort(), obj.get("AY").getAsShort(), obj.get("AZ").getAsShort(), obj.get("ARX").getAsBoolean(), obj.get("ARY").getAsBoolean(), obj.get("ARZ").getAsBoolean(), obj.get("RRX").getAsBoolean(), obj.get("RRY").getAsBoolean(), obj.get("RRZ").getAsBoolean());
            tile.setKey(obj.get("Time").getAsShort(), k);
            // TODO fill
        }
    }

    private static void getObjFromTabS(JsonObject obj, String name, short[] tab)
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

    private static void getObjFromTabF(JsonObject obj, String name, float[] tab)
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
                str += ":" + (tab[i] < 1.0F ? String.valueOf(tab[i]).replace("0.", "") : 1.0F);//TODO test
            }
            str = str.substring(1);
            obj.addProperty(name, str);
        }
    }

    private static short[] getTabFromObjS(JsonObject calculated, String name)
    {
        String str = calculated.get(name).getAsString();
        short[] tab = new short[1200];
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

    private static float[] getTabFromObjF(JsonObject calculated, String name)
    {
        String decimalPattern = "([0-9]*)\\.([0-9]*)";
        String str = calculated.get(name).getAsString();
        float[] tab = new float[1200];
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
                    tab[i] = 0;
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

    private static JsonObject read(File dir)
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
            generateNewFiles(Integer.valueOf(new File(dir.getParent()).getName()), new BlockPos(Integer.valueOf(dir.getName().replace(".json", "").split("_")[0]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[1]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[2])));
        }
        catch(JsonParseException e)
        {
            TheSpotLightMod.log.error("File is not a JSON, generating a new one");
            deleteFile(Integer.valueOf(new File(dir.getParent()).getName()), new BlockPos(Integer.valueOf(dir.getName().replace(".json", "").split("_")[0]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[1]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[2])));
            generateNewFiles(Integer.valueOf(new File(dir.getParent()).getName()), new BlockPos(Integer.valueOf(dir.getName().replace(".json", "").split("_")[0]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[1]), Integer.valueOf(dir.getName().replace(".json", "").split("_")[2])));
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
    
    public static HashMap<Integer, List<JsonObject>> getAll()
    {
    	return null;
    }
}