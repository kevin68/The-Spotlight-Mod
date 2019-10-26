package fr.mcnanotech.kevin_68.thespotlightmod.client.text3d;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public abstract class Model3DTextBase extends Model
{
    public Map<Character, Float> charSizes = new HashMap<Character, Float>();
    
    public abstract void renderChar(char ch, float scale, float x);

    public void renderAll(RendererModel[] renders, float scale, float x)
    {
        if(renders != null && renders.length > 0)
        {
            for(RendererModel render : renders)
            {
                render.rotateAngleZ = (float)Math.PI;
                render.offsetX += x;
                render.render(scale);
                render.offsetX -= x;
            }
        }
    }
}
