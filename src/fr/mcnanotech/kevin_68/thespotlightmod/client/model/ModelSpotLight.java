package fr.mcnanotech.kevin_68.thespotlightmod.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSpotLight extends ModelBase
{
	ModelRenderer shape1;
	ModelRenderer shape2;
	ModelRenderer shape3;
	ModelRenderer shape4;
	ModelRenderer shape5;
	ModelRenderer shape6;
	ModelRenderer shape;
	ModelRenderer shape8;
	ModelRenderer shape9;
	ModelRenderer shape10;
	ModelRenderer shape11;

	public ModelSpotLight(int tex)
	{
		textureWidth = 64;
		textureHeight = 32;

		shape1 = new ModelRenderer(this, 8, 10);
		shape1.addBox(-7F, -4F, -7F, 14, 8, 14);
		shape1.setRotationPoint(0F, 16F, 0F);
		shape1.setTextureSize(64, 32);
		shape1.mirror = true;
		setRotation(shape1, 0F, 0F, 0F);
		shape2 = new ModelRenderer(this, 0, 0);
		shape2.addBox(-7F, -6F, -7F, 14, 2, 1);
		shape2.setRotationPoint(0F, 16F, 0F);
		shape2.setTextureSize(64, 32);
		shape2.mirror = true;
		setRotation(shape2, 0F, 0F, 0F);
		shape3 = new ModelRenderer(this, 0, 3);
		shape3.addBox(-7F, -6F, 6F, 14, 2, 1);
		shape3.setRotationPoint(0F, 16F, 0F);
		shape3.setTextureSize(64, 32);
		shape3.mirror = true;
		setRotation(shape3, 0F, 0F, 0F);
		shape4 = new ModelRenderer(this, 0, 0);
		shape4.addBox(-7F, 4F, -7F, 14, 2, 1);
		shape4.setRotationPoint(0F, 16F, 0F);
		shape4.setTextureSize(64, 32);
		shape4.mirror = true;
		setRotation(shape4, 0F, 0F, 0F);
		shape5 = new ModelRenderer(this, 0, 3);
		shape5.addBox(-7F, 4F, 6F, 14, 2, 1);
		shape5.setRotationPoint(0F, 16F, 0F);
		shape5.setTextureSize(64, 32);
		shape5.mirror = true;
		setRotation(shape5, 0F, 0F, 0F);
		shape6 = new ModelRenderer(this, 30, 0);
		shape6.addBox(-6F, -6F, -7F, 12, 2, 1);
		shape6.setRotationPoint(0F, 16F, 0F);
		shape6.setTextureSize(64, 32);
		shape6.mirror = true;
		setRotation(shape6, 0F, 1.579523F, 0F);
		shape = new ModelRenderer(this, 30, 0);
		shape.addBox(-6F, 4F, -7F, 12, 2, 1);
		shape.setRotationPoint(0F, 16F, 0F);
		shape.setTextureSize(64, 32);
		shape.mirror = true;
		setRotation(shape, 0F, 1.579523F, 0F);
		shape8 = new ModelRenderer(this, 30, 3);
		shape8.addBox(-6F, -6F, 6F, 12, 2, 1);
		shape8.setRotationPoint(0F, 16F, 0F);
		shape8.setTextureSize(64, 32);
		shape8.mirror = true;
		setRotation(shape8, 0F, 1.579523F, 0F);
		shape9 = new ModelRenderer(this, 30, 3);
		shape9.addBox(-6F, 4F, 6F, 12, 2, 1);
		shape9.setRotationPoint(0F, 16F, 0F);
		shape9.setTextureSize(64, 32);
		shape9.mirror = true;
		setRotation(shape9, 0F, 1.579523F, 0F);
		shape10 = new ModelRenderer(this, -6, 6 + 6 * tex);
		shape10.addBox(-3F, -5F, -3F, 6, 0, 6);
		shape10.setRotationPoint(0F, 16F, 0F);
		shape10.setTextureSize(64, 32);
		shape10.mirror = true;
		setRotation(shape10, 0F, 0F, 0F);
		shape11 = new ModelRenderer(this, -6, 6 + 6 * tex);
		shape11.addBox(-3F, 5F, -3F, 6, 0, 6);
		shape11.setRotationPoint(0F, 16F, 0F);
		shape11.setTextureSize(64, 32);
		shape11.mirror = true;
		setRotation(shape11, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		shape1.render(f5);
		shape2.render(f5);
		shape3.render(f5);
		shape4.render(f5);
		shape5.render(f5);
		shape6.render(f5);
		shape.render(f5);
		shape8.render(f5);
		shape9.render(f5);
		shape10.render(f5);
		shape11.render(f5);
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