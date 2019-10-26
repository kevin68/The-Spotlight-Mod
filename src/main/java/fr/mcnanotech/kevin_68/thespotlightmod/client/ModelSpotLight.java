package fr.mcnanotech.kevin_68.thespotlightmod.client;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class ModelSpotLight extends Model
{
    RendererModel shape1;
    RendererModel shape2;
    RendererModel shape3;
    RendererModel shape4;
    RendererModel shape5;
    RendererModel shape6;
    RendererModel shape7;
    RendererModel shape8;
    RendererModel shape19;
    RendererModel shape9;
    RendererModel shape10;
    RendererModel shape11;
    RendererModel shape12;
    RendererModel shape13;
    RendererModel shape14;
    RendererModel shape15;
    RendererModel shape16;
    RendererModel shape17;
    RendererModel shape18;

    public ModelSpotLight()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.shape1 = new RendererModel(this, 0, 0);
        this.shape1.addBox(-5F, -4F, -5F, 10, 8, 10);
        this.shape1.setRotationPoint(0F, 0F, 0F);
        this.shape1.setTextureSize(64, 32);
        this.shape1.mirror = true;

        this.shape2 = new RendererModel(this, 46, 0);
        this.shape2.addBox(-4F, -4F, -6F, 8, 8, 1);
        this.shape2.setRotationPoint(0F, 0F, 0F);
        this.shape2.setTextureSize(64, 32);
        this.shape2.mirror = true;

        this.shape3 = new RendererModel(this, 46, 0);
        this.shape3.addBox(-4F, -4F, 5F, 8, 8, 1);
        this.shape3.setRotationPoint(0F, 0F, 0F);
        this.shape3.setTextureSize(64, 32);
        this.shape3.mirror = true;

        this.shape4 = new RendererModel(this, 46, 9);
        this.shape4.addBox(5F, -4F, -4F, 1, 8, 8);
        this.shape4.setRotationPoint(0F, 0F, 0F);
        this.shape4.setTextureSize(64, 32);
        this.shape4.mirror = true;

        this.shape5 = new RendererModel(this, 46, 9);
        this.shape5.addBox(-6F, -4F, -4F, 1, 8, 8);
        this.shape5.setRotationPoint(0F, 0F, 0F);
        this.shape5.setTextureSize(64, 32);
        this.shape5.mirror = true;

        this.shape6 = new RendererModel(this, 0, 0);
        this.shape6.addBox(-2F, -4F, -7F, 4, 8, 1);
        this.shape6.setRotationPoint(0F, 0F, 0F);
        this.shape6.setTextureSize(64, 32);
        this.shape6.mirror = true;

        this.shape7 = new RendererModel(this, 0, 0);
        this.shape7.addBox(-2F, -4F, 6F, 4, 8, 1);
        this.shape7.setRotationPoint(0F, 0F, 0F);
        this.shape7.setTextureSize(64, 32);
        this.shape7.mirror = true;

        this.shape8 = new RendererModel(this, 0, 20);
        this.shape8.addBox(6F, -4F, -2F, 1, 8, 4);
        this.shape8.setRotationPoint(0F, 0F, 0F);
        this.shape8.setTextureSize(64, 32);
        this.shape8.mirror = true;

        this.shape19 = new RendererModel(this, 0, 20);
        this.shape19.addBox(-7F, -4F, -2F, 1, 8, 4);
        this.shape19.setRotationPoint(0F, 0F, 0F);
        this.shape19.setTextureSize(64, 32);
        this.shape19.mirror = true;

        this.shape9 = new RendererModel(this, 10, 18);
        this.shape9.addBox(-2F, -7F, -8F, 4, 4, 1);
        this.shape9.setRotationPoint(0F, 0F, 0F);
        this.shape9.setTextureSize(64, 32);
        this.shape9.mirror = true;

        this.shape10 = new RendererModel(this, 20, 18);
        this.shape10.addBox(-2F, -7F, 7F, 4, 4, 1);
        this.shape10.setRotationPoint(0F, 0F, 0F);
        this.shape10.setTextureSize(64, 32);
        this.shape10.mirror = true;

        this.shape11 = new RendererModel(this, 10, 18);
        this.shape11.addBox(-2F, 3F, -8F, 4, 4, 1);
        this.shape11.setRotationPoint(0F, 0F, 0F);
        this.shape11.setTextureSize(64, 32);
        this.shape11.mirror = true;

        this.shape12 = new RendererModel(this, 20, 18);
        this.shape12.addBox(-2F, 3F, 7F, 4, 4, 1);
        this.shape12.setRotationPoint(0F, 0F, 0F);
        this.shape12.setTextureSize(64, 32);
        this.shape12.mirror = true;

        this.shape13 = new RendererModel(this, 10, 23);
        this.shape13.addBox(7F, -7F, -2F, 1, 4, 4);
        this.shape13.setRotationPoint(0F, 0F, 0F);
        this.shape13.setTextureSize(64, 32);
        this.shape13.mirror = true;

        this.shape14 = new RendererModel(this, 10, 23);
        this.shape14.addBox(7F, 3F, -2F, 1, 4, 4);
        this.shape14.setRotationPoint(0F, 0F, 0F);
        this.shape14.setTextureSize(64, 32);
        this.shape14.mirror = true;

        this.shape15 = new RendererModel(this, 20, 23);
        this.shape15.addBox(-8F, -7F, -2F, 1, 4, 4);
        this.shape15.setRotationPoint(0F, 0F, 0F);
        this.shape15.setTextureSize(64, 32);
        this.shape15.mirror = true;

        this.shape16 = new RendererModel(this, 20, 23);
        this.shape16.addBox(-8F, 3F, -2F, 1, 4, 4);
        this.shape16.setRotationPoint(0F, 0F, 0F);
        this.shape16.setTextureSize(64, 32);
        this.shape16.mirror = true;

        this.shape17 = new RendererModel(this, 30, 0);
        this.shape17.addBox(-2F, -5F, -2F, 4, 1, 4);
        this.shape17.setRotationPoint(0F, 0F, 0F);
        this.shape17.setTextureSize(64, 32);
        this.shape17.mirror = true;

        this.shape18 = new RendererModel(this, 30, 0);
        this.shape18.addBox(-2F, 4F, -2F, 4, 1, 4);
        this.shape18.setRotationPoint(0F, 0F, 0F);
        this.shape18.setTextureSize(64, 32);
        this.shape18.mirror = true;
    }

    public void render(float scale) {
        this.shape1.render(scale);
        this.shape2.render(scale);
        this.shape3.render(scale);
        this.shape4.render(scale);
        this.shape5.render(scale);
        this.shape6.render(scale);
        this.shape7.render(scale);
        this.shape8.render(scale);
        this.shape19.render(scale);
        this.shape9.render(scale);
        this.shape10.render(scale);
        this.shape11.render(scale);
        this.shape12.render(scale);
        this.shape13.render(scale);
        this.shape14.render(scale);
        this.shape15.render(scale);
        this.shape16.render(scale);
        this.shape17.render(scale);
        this.shape18.render(scale);
    }

    public void setRotation(float x, float y, float z)
    {
        setRotation(this.shape1, x, y, z);
        setRotation(this.shape2, x, y, z);
        setRotation(this.shape3, x, y, z);
        setRotation(this.shape4, x, y, z);
        setRotation(this.shape5, x, y, z);
        setRotation(this.shape6, x, y, z);
        setRotation(this.shape7, x, y, z);
        setRotation(this.shape8, x, y, z);
        setRotation(this.shape9, x, y, z);
        setRotation(this.shape10, x, y, z);
        setRotation(this.shape11, x, y, z);
        setRotation(this.shape12, x, y, z);
        setRotation(this.shape13, x, y, z);
        setRotation(this.shape14, x, y, z);
        setRotation(this.shape15, x, y, z);
        setRotation(this.shape16, x, y, z);
        setRotation(this.shape17, x, y, z);
        setRotation(this.shape18, x, y, z);
        setRotation(this.shape19, x, y, z);
    }

    private void setRotation(RendererModel model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
