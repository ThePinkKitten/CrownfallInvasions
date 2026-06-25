/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.block.Block
 */
package org.zeith.improvableskills.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import org.zeith.improvableskills.ImprovableSkills;

public interface TagsIS3 {
    public static void init() {
        Blocks.init();
        EntityTypes.init();
    }

    public static interface Blocks {
        public static final TagKey<Block> GROWTH_SKILL_BLOCKLIST = Blocks.tag("growth_skill_blocklist");

        private static void init() {
        }

        private static TagKey<Block> tag(String name) {
            return BlockTags.create((ResourceLocation)new ResourceLocation("improvableskills", name));
        }
    }

    public static interface EntityTypes {
        public static final TagKey<EntityType<?>> PREVENT_COWBOY_INTERACTION = EntityTypes.tag("prevent_cowboy_interaction");

        private static void init() {
        }

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.m_203882_((ResourceKey)Registries.f_256939_, (ResourceLocation)ImprovableSkills.id(name));
        }
    }
}

