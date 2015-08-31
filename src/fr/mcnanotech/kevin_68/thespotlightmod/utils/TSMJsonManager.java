package fr.mcnanotech.kevin_68.thespotlightmod.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
import fr.mcnanotech.kevin_68.thespotlightmod.network.packets.PacketRegenerateFile;
import fr.mcnanotech.kevin_68.thespotlightmod.tileentity.TileEntitySpotLight;

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

        JsonObject beam = new JsonObject();
        JsonObject colors = new JsonObject();
        colors.addProperty("R", 255);
        colors.addProperty("G", 255);
        colors.addProperty("B", 255);
        colors.addProperty("SR", 255);
        colors.addProperty("SG", 255);
        colors.addProperty("SB", 255);
        beam.add("Colors", colors);
        JsonObject angles = new JsonObject();
        angles.addProperty("X", 0);
        angles.addProperty("Y", 0);
        angles.addProperty("Z", 0);
        angles.addProperty("ARX", false);
        angles.addProperty("ARY", false);
        angles.addProperty("ARZ", false);
        angles.addProperty("RRX", false);
        angles.addProperty("RRY", false);
        angles.addProperty("RRZ", false);
        angles.addProperty("RSX", 0);
        angles.addProperty("RSY", 0);
        angles.addProperty("RSZ", 0);
        beam.add("Angles", angles);
        JsonObject properties = new JsonObject();
        properties.addProperty("Size", 40);
        properties.addProperty("SSize", 75);
        properties.addProperty("SBeam", true);
        properties.addProperty("Dbl", false);
        properties.addProperty("H", 256);
        properties.addProperty("Sides", 2);
        beam.add("Properties", properties);
        json.add("Beam", beam);

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
            JsonObject beam = (JsonObject)json.get("Beam");
            JsonObject colors = (JsonObject)beam.get("Colors");
            tile.beamRed = colors.get("R").getAsShort();
            tile.beamGreen = colors.get("G").getAsShort();
            tile.beamBlue = colors.get("B").getAsShort();
            tile.secBeamRed = colors.get("SR").getAsShort();
            tile.secBeamGreen = colors.get("SG").getAsShort();
            tile.secBeamBlue = colors.get("SB").getAsShort();
            JsonObject angles = (JsonObject)beam.get("Angles");
            tile.beamAngleX = angles.get("X").getAsShort();
            tile.beamAngleY = angles.get("Y").getAsShort();
            tile.beamAngleZ = angles.get("Z").getAsShort();
            tile.beamAutoRotateX = angles.get("ARX").getAsBoolean();
            tile.beamAutoRotateY = angles.get("ARY").getAsBoolean();
            tile.beamAutoRotateZ = angles.get("ARZ").getAsBoolean();
            tile.beamReverseRotateX = angles.get("RRX").getAsBoolean();
            tile.beamReverseRotateY = angles.get("RRY").getAsBoolean();
            tile.beamReverseRotateZ = angles.get("RRZ").getAsBoolean();
            tile.beamRotationSpeedX = angles.get("RSX").getAsShort();
            tile.beamRotationSpeedY = angles.get("RSY").getAsShort();
            tile.beamRotationSpeedZ = angles.get("RSZ").getAsShort();
            JsonObject properties = (JsonObject)beam.get("Properties");
            tile.beamSize = properties.get("Size").getAsShort();
            tile.secBeamSize = properties.get("SSize").getAsShort();
            tile.secBeamEnabled = properties.get("SBeam").getAsBoolean();
            tile.beamDouble = properties.get("Dbl").getAsBoolean();
            tile.beamHeight = properties.get("H").getAsShort();
            tile.beamSides = properties.get("Sides").getAsShort();
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
        write(file, data);
    }

    public static String getDataFromJson(int dimID, BlockPos pos)
    {
        File folder = new File(DimensionManager.getCurrentSaveRootDirectory(), new File("SpotLights", String.valueOf(dimID)).getPath());
        File file = new File(folder, pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + ".json");
        JsonObject json = read(file);
        if(json != null)
        {
            return read(file).toString();
        }
        return null;
    }

    public static String getDataFromTile(TileEntitySpotLight tile)
    {
        JsonObject json = new JsonObject();
        json.addProperty("DimID", tile.dimensionID);
        json.addProperty("X", tile.getPos().getX());
        json.addProperty("Y", tile.getPos().getY());
        json.addProperty("Z", tile.getPos().getZ());

        JsonObject beam = new JsonObject();
        JsonObject colors = new JsonObject();
        colors.addProperty("R", tile.beamRed);
        colors.addProperty("G", tile.beamGreen);
        colors.addProperty("B", tile.beamBlue);
        colors.addProperty("SR", tile.secBeamRed);
        colors.addProperty("SG", tile.secBeamGreen);
        colors.addProperty("SB", tile.secBeamBlue);
        beam.add("Colors", colors);
        JsonObject angles = new JsonObject();
        angles.addProperty("X", tile.beamAngleX);
        angles.addProperty("Y", tile.beamAngleY);
        angles.addProperty("Z", tile.beamAngleZ);
        angles.addProperty("ARX", tile.beamAutoRotateX);
        angles.addProperty("ARY", tile.beamAutoRotateY);
        angles.addProperty("ARZ", tile.beamAutoRotateZ);
        angles.addProperty("RRX", tile.beamReverseRotateX);
        angles.addProperty("RRY", tile.beamReverseRotateY);
        angles.addProperty("RRZ", tile.beamReverseRotateZ);
        angles.addProperty("RSX", tile.beamRotationSpeedX);
        angles.addProperty("RSY", tile.beamRotationSpeedY);
        angles.addProperty("RSZ", tile.beamRotationSpeedZ);
        beam.add("Angles", angles);
        JsonObject properties = new JsonObject();
        properties.addProperty("Size", tile.beamSize);
        properties.addProperty("SSize", tile.secBeamSize);
        properties.addProperty("SBeam", tile.secBeamEnabled);
        properties.addProperty("Dbl", tile.beamDouble);
        properties.addProperty("H", tile.beamHeight);
        properties.addProperty("Sides", tile.beamSides);
        beam.add("Properties", properties);
        json.add("Beam", beam);

        return json.toString();
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
}