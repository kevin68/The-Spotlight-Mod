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
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.shape1 = new ModelRenderer(this, 8, 10);
		this.shape1.addBox(-7F, -4F, -7F, 14, 8, 14);
		this.shape1.setRotationPoint(0F, 16F, 0F);
		this.shape1.setTextureSize(64, 32);
		this.shape1.mirror = true;
		setRotation(this.shape1, 0F, 0F, 0F);
		this.shape2 = new ModelRenderer(this, 0, 0);
		this.shape2.addBox(-7F, -6F, -7F, 14, 2, 1);
		this.shape2.setRotationPoint(0F, 16F, 0F);
		this.shape2.setTextureSize(64, 32);
		this.shape2.mirror = true;
		setRotation(this.shape2, 0F, 0F, 0F);
		this.shape3 = new ModelRenderer(this, 0, 3);
		this.shape3.addBox(-7F, -6F, 6F, 14, 2, 1);
		this.shape3.setRotationPoint(0F, 16F, 0F);
		this.shape3.setTextureSize(64, 32);
		this.shape3.mirror = true;
		setRotation(this.shape3, 0F, 0F, 0F);
		this.shape4 = new ModelRenderer(this, 0, 0);
		this.shape4.addBox(-7F, 4F, -7F, 14, 2, 1);
		this.shape4.setRotationPoint(0F, 16F, 0F);
		this.shape4.setTextureSize(64, 32);
		this.shape4.mirror = true;
		setRotation(this.shape4, 0F, 0F, 0F);
		this.shape5 = new ModelRenderer(this, 0, 3);
		this.shape5.addBox(-7F, 4F, 6F, 14, 2, 1);
		this.shape5.setRotationPoint(0F, 16F, 0F);
		this.shape5.setTextureSize(64, 32);
		this.shape5.mirror = true;
		setRotation(this.shape5, 0F, 0F, 0F);
		this.shape6 = new ModelRenderer(this, 30, 0);
		this.shape6.addBox(-6F, -6F, -7F, 12, 2, 1);
		this.shape6.setRotationPoint(0F, 16F, 0F);
		this.shape6.setTextureSize(64, 32);
		this.shape6.mirror = true;
		setRotation(this.shape6, 0F, 1.579523F, 0F);
		this.shape = new ModelRenderer(this, 30, 0);
		this.shape.addBox(-6F, 4F, -7F, 12, 2, 1);
		this.shape.setRotationPoint(0F, 16F, 0F);
		this.shape.setTextureSize(64, 32);
		this.shape.mirror = true;
		setRotation(this.shape, 0F, 1.579523F, 0F);
		this.shape8 = new ModelRenderer(this, 30, 3);
		this.shape8.addBox(-6F, -6F, 6F, 12, 2, 1);
		this.shape8.setRotationPoint(0F, 16F, 0F);
		this.shape8.setTextureSize(64, 32);
		this.shape8.mirror = true;
		setRotation(this.shape8, 0F, 1.579523F, 0F);
		this.shape9 = new ModelRenderer(this, 30, 3);
		this.shape9.addBox(-6F, 4F, 6F, 12, 2, 1);
		this.shape9.setRotationPoint(0F, 16F, 0F);
		this.shape9.setTextureSize(64, 32);
		this.shape9.mirror = true;
		setRotation(this.shape9, 0F, 1.579523F, 0F);
		this.shape10 = new ModelRenderer(this, -6, 6 + 6 * tex);
		this.shape10.addBox(-3F, -5F, -3F, 6, 0, 6);
		this.shape10.setRotationPoint(0F, 16F, 0F);
		this.shape10.setTextureSize(64, 32);
		this.shape10.mirror = true;
		setRotation(this.shape10, 0F, 0F, 0F);
		this.shape11 = new ModelRenderer(this, -6, 6 + 6 * tex);
		this.shape11.addBox(-3F, 5F, -3F, 6, 0, 6);
		this.shape11.setRotationPoint(0F, 16F, 0F);
		this.shape11.setTextureSize(64, 32);
		this.shape11.mirror = true;
		setRotation(this.shape11, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.shape1.render(f5);
		this.shape2.render(f5);
		this.shape3.render(f5);
		this.shape4.render(f5);
		this.shape5.render(f5);
		this.shape6.render(f5);
		this.shape.render(f5);
		this.shape8.render(f5);
		this.shape9.render(f5);
		this.shape10.render(f5);
		this.shape11.render(f5);
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