package fr.mcnanotech.kevin_68.thespotlightmod.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSpotLight extends ModelBase
{
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape19;
    ModelRenderer Shape9;
    ModelRenderer Shape10;
    ModelRenderer Shape11;
    ModelRenderer Shape12;
    ModelRenderer Shape13;
    ModelRenderer Shape14;
    ModelRenderer Shape15;
    ModelRenderer Shape16;
    ModelRenderer Shape17;
    ModelRenderer Shape18;

    public ModelSpotLight()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.Shape1 = new ModelRenderer(this, 0, 0);
        this.Shape1.addBox(-5F, -4F, -5F, 10, 8, 10);
        this.Shape1.setRotationPoint(0F, 0F, 0F);
        this.Shape1.setTextureSize(64, 32);
        this.Shape1.mirror = true;
        setRotation(this.Shape1, 0F, 0F, 0F);
        this.Shape2 = new ModelRenderer(this, 46, 0);
        this.Shape2.addBox(-4F, -4F, -6F, 8, 8, 1);
        this.Shape2.setRotationPoint(0F, 0F, 0F);
        this.Shape2.setTextureSize(64, 32);
        this.Shape2.mirror = true;
        setRotation(this.Shape2, 0F, 0F, 0F);
        this.Shape3 = new ModelRenderer(this, 46, 0);
        this.Shape3.addBox(-4F, -4F, 5F, 8, 8, 1);
        this.Shape3.setRotationPoint(0F, 0F, 0F);
        this.Shape3.setTextureSize(64, 32);
        this.Shape3.mirror = true;
        setRotation(this.Shape3, 0F, 0F, 0F);
        this.Shape4 = new ModelRenderer(this, 46, 9);
        this.Shape4.addBox(5F, -4F, -4F, 1, 8, 8);
        this.Shape4.setRotationPoint(0F, 0F, 0F);
        this.Shape4.setTextureSize(64, 32);
        this.Shape4.mirror = true;
        setRotation(this.Shape4, 0F, 0F, 0F);
        this.Shape5 = new ModelRenderer(this, 46, 9);
        this.Shape5.addBox(-6F, -4F, -4F, 1, 8, 8);
        this.Shape5.setRotationPoint(0F, 0F, 0F);
        this.Shape5.setTextureSize(64, 32);
        this.Shape5.mirror = true;
        setRotation(this.Shape5, 0F, 0F, 0F);
        this.Shape6 = new ModelRenderer(this, 0, 0);
        this.Shape6.addBox(-2F, -4F, -7F, 4, 8, 1);
        this.Shape6.setRotationPoint(0F, 0F, 0F);
        this.Shape6.setTextureSize(64, 32);
        this.Shape6.mirror = true;
        setRotation(this.Shape6, 0F, 0F, 0F);
        this.Shape7 = new ModelRenderer(this, 0, 0);
        this.Shape7.addBox(-2F, -4F, 6F, 4, 8, 1);
        this.Shape7.setRotationPoint(0F, 0F, 0F);
        this.Shape7.setTextureSize(64, 32);
        this.Shape7.mirror = true;
        setRotation(this.Shape7, 0F, 0F, 0F);
        this.Shape8 = new ModelRenderer(this, 0, 20);
        this.Shape8.addBox(6F, -4F, -2F, 1, 8, 4);
        this.Shape8.setRotationPoint(0F, 0F, 0F);
        this.Shape8.setTextureSize(64, 32);
        this.Shape8.mirror = true;
        setRotation(this.Shape8, 0F, 0F, 0F);
        this.Shape19 = new ModelRenderer(this, 0, 20);
        this.Shape19.addBox(-7F, -4F, -2F, 1, 8, 4);
        this.Shape19.setRotationPoint(0F, 0F, 0F);
        this.Shape19.setTextureSize(64, 32);
        this.Shape19.mirror = true;
        setRotation(this.Shape19, 0F, 0F, 0F);
        this.Shape9 = new ModelRenderer(this, 10, 18);
        this.Shape9.addBox(-2F, -7F, -8F, 4, 4, 1);
        this.Shape9.setRotationPoint(0F, 0F, 0F);
        this.Shape9.setTextureSize(64, 32);
        this.Shape9.mirror = true;
        setRotation(this.Shape9, 0F, 0F, 0F);
        this.Shape10 = new ModelRenderer(this, 20, 18);
        this.Shape10.addBox(-2F, -7F, 7F, 4, 4, 1);
        this.Shape10.setRotationPoint(0F, 0F, 0F);
        this.Shape10.setTextureSize(64, 32);
        this.Shape10.mirror = true;
        setRotation(this.Shape10, 0F, 0F, 0F);
        this.Shape11 = new ModelRenderer(this, 10, 18);
        this.Shape11.addBox(-2F, 3F, -8F, 4, 4, 1);
        this.Shape11.setRotationPoint(0F, 0F, 0F);
        this.Shape11.setTextureSize(64, 32);
        this.Shape11.mirror = true;
        setRotation(this.Shape11, 0F, 0F, 0F);
        this.Shape12 = new ModelRenderer(this, 20, 18);
        this.Shape12.addBox(-2F, 3F, 7F, 4, 4, 1);
        this.Shape12.setRotationPoint(0F, 0F, 0F);
        this.Shape12.setTextureSize(64, 32);
        this.Shape12.mirror = true;
        setRotation(this.Shape12, 0F, 0F, 0F);
        this.Shape13 = new ModelRenderer(this, 10, 23);
        this.Shape13.addBox(7F, -7F, -2F, 1, 4, 4);
        this.Shape13.setRotationPoint(0F, 0F, 0F);
        this.Shape13.setTextureSize(64, 32);
        this.Shape13.mirror = true;
        setRotation(this.Shape13, 0F, 0F, 0F);
        this.Shape14 = new ModelRenderer(this, 10, 23);
        this.Shape14.addBox(7F, 3F, -2F, 1, 4, 4);
        this.Shape14.setRotationPoint(0F, 0F, 0F);
        this.Shape14.setTextureSize(64, 32);
        this.Shape14.mirror = true;
        setRotation(this.Shape14, 0F, 0F, 0F);
        this.Shape15 = new ModelRenderer(this, 20, 23);
        this.Shape15.addBox(-8F, -7F, -2F, 1, 4, 4);
        this.Shape15.setRotationPoint(0F, 0F, 0F);
        this.Shape15.setTextureSize(64, 32);
        this.Shape15.mirror = true;
        setRotation(this.Shape15, 0F, 0F, 0F);
        this.Shape16 = new ModelRenderer(this, 20, 23);
        this.Shape16.addBox(-8F, 3F, -2F, 1, 4, 4);
        this.Shape16.setRotationPoint(0F, 0F, 0F);
        this.Shape16.setTextureSize(64, 32);
        this.Shape16.mirror = true;
        setRotation(this.Shape16, 0F, 0F, 0F);
        this.Shape17 = new ModelRenderer(this, 30, 0);
        this.Shape17.addBox(-2F, -5F, -2F, 4, 1, 4);
        this.Shape17.setRotationPoint(0F, 0F, 0F);
        this.Shape17.setTextureSize(64, 32);
        this.Shape17.mirror = true;
        setRotation(this.Shape17, 0F, 0F, 0F);
        this.Shape18 = new ModelRenderer(this, 30, 0);
        this.Shape18.addBox(-2F, 4F, -2F, 4, 1, 4);
        this.Shape18.setRotationPoint(0F, 0F, 0F);
        this.Shape18.setTextureSize(64, 32);
        this.Shape18.mirror = true;
        setRotation(this.Shape18, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
//        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Shape1.render(f5);
        this.Shape2.render(f5);
        this.Shape3.render(f5);
        this.Shape4.render(f5);
        this.Shape5.render(f5);
        this.Shape6.render(f5);
        this.Shape7.render(f5);
        this.Shape8.render(f5);
        this.Shape19.render(f5);
        this.Shape9.render(f5);
        this.Shape10.render(f5);
        this.Shape11.render(f5);
        this.Shape12.render(f5);
        this.Shape13.render(f5);
        this.Shape14.render(f5);
        this.Shape15.render(f5);
        this.Shape16.render(f5);
        this.Shape17.render(f5);
        this.Shape18.render(f5);
    }

    public void setRotation(float x, float y, float z)
    {
        setRotation(this.Shape1, x, y, z);
        setRotation(this.Shape2, x, y, z);
        setRotation(this.Shape3, x, y, z);
        setRotation(this.Shape4, x, y, z);
        setRotation(this.Shape5, x, y, z);
        setRotation(this.Shape6, x, y, z);
        setRotation(this.Shape7, x, y, z);
        setRotation(this.Shape8, x, y, z);
        setRotation(this.Shape9, x, y, z);
        setRotation(this.Shape10, x, y, z);
        setRotation(this.Shape11, x, y, z);
        setRotation(this.Shape12, x, y, z);
        setRotation(this.Shape13, x, y, z);
        setRotation(this.Shape14, x, y, z);
        setRotation(this.Shape15, x, y, z);
        setRotation(this.Shape16, x, y, z);
        setRotation(this.Shape17, x, y, z);
        setRotation(this.Shape18, x, y, z);
        setRotation(this.Shape19, x, y, z);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}