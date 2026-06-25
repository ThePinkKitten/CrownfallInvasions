/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.DustParticleBase
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.core.particles.DustParticleOptionsBase
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package org.zeith.improvableskills.client.rendering.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DustParticleBase;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.improvableskills.custom.particles.ParticleDataSparkle;

public class SparkleParticle
extends DustParticleBase<ParticleDataSparkle> {
    protected SparkleParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, ParticleDataSparkle data, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, (DustParticleOptionsBase)data, sprites);
        this.f_107225_ = data.getAge();
        this.f_172258_ = 0.98f;
    }

    public ParticleRenderType m_7556_() {
        return ParticleRenderType.f_107431_;
    }

    protected float m_172104_(float color, float factor) {
        return color;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<ParticleDataSparkle> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(ParticleDataSparkle data, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            return new SparkleParticle(level, x, y, z, xd, yd, zd, data, this.sprites);
        }
    }
}

