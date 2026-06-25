/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleOptions$Deserializer
 *  net.minecraft.core.particles.ParticleType
 */
package org.zeith.improvableskills.custom.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class ParticleTypeWithCodec<T extends ParticleOptions>
extends ParticleType<T> {
    private final Codec<T> codec;

    public ParticleTypeWithCodec(boolean overrideLimiter, Codec<T> codec, ParticleOptions.Deserializer<T> deserializer) {
        super(overrideLimiter, deserializer);
        this.codec = codec;
    }

    public Codec<T> m_7652_() {
        return this.codec;
    }
}

