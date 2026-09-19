package com.cosmicbarri.crystal_gacha.entity;

import com.cosmicbarri.crystal_gacha.CrystalGacha;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GachaModel extends GeoModel<GachaEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrystalGacha.MODID, "geo/crystal_gacha.geo.json");
    private static final ResourceLocation ANIM = new ResourceLocation(CrystalGacha.MODID, "animations/crystal_gacha.animation.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(CrystalGacha.MODID, "textures/entities/crystal_gacha.png");

    @Override
    public ResourceLocation getModelResource(GachaEntity entity) {
        return MODEL;
    }

    @Override
    public ResourceLocation getAnimationResource(GachaEntity entity) {
        return ANIM;
    }

    @Override
    public ResourceLocation getTextureResource(GachaEntity entity) {
        return TEXTURE;
    }
}
