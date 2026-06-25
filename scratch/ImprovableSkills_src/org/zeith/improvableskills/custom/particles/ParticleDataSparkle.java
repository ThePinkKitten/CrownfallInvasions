/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.core.particles.DustParticleOptionsBase
 *  net.minecraft.core.particles.ParticleOptions$Deserializer
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.util.ExtraCodecs
 *  org.joml.Vector3f
 */
package org.zeith.improvableskills.custom.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;
import org.zeith.improvableskills.init.ParticleTypesIS;

public class ParticleDataSparkle
extends DustParticleOptionsBase {
    public static final Codec<ParticleDataSparkle> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ExtraCodecs.f_252432_.fieldOf("color").forGetter(DustParticleOptionsBase::m_252837_), (App)Codec.FLOAT.fieldOf("scale").forGetter(DustParticleOptionsBase::m_175813_), (App)Codec.INT.fieldOf("age").forGetter(ParticleDataSparkle::getAge)).apply((Applicative)instance, ParticleDataSparkle::new));
    public static final ParticleOptions.Deserializer<ParticleDataSparkle> DESERIALIZER = new ParticleOptions.Deserializer<ParticleDataSparkle>(){

        public ParticleDataSparkle fromCommand(ParticleType<ParticleDataSparkle> type, StringReader reader) throws CommandSyntaxException {
            Vector3f color = DustParticleOptionsBase.m_252853_((StringReader)reader);
            reader.expect(' ');
            float scale = reader.readFloat();
            reader.expect(' ');
            int age = reader.readInt();
            return new ParticleDataSparkle(color, scale, age);
        }

        public ParticleDataSparkle fromNetwork(ParticleType<ParticleDataSparkle> type, FriendlyByteBuf buffer) {
            Vector3f color = DustParticleOptionsBase.m_253064_((FriendlyByteBuf)buffer);
            float scale = buffer.readFloat();
            int age = buffer.readInt();
            return new ParticleDataSparkle(color, scale, age);
        }
    };
    private final int age;

    public ParticleDataSparkle(Vector3f color, float scale, int age) {
        super(color, scale);
        this.age = age;
    }

    public ParticleType<ParticleDataSparkle> m_6012_() {
        return ParticleTypesIS.SPARKLE;
    }

    public int getAge() {
        return this.age;
    }
}

