/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.OwnableEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.entity.projectile.ThrownPotion
 *  org.zeith.hammerlib.util.java.Cast
 */
package org.zeith.improvableskills.api;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import org.zeith.hammerlib.util.java.Cast;

public class DamageSourceProcessor {
    @Nullable
    public static Player getAttackerAsPlayer(DamageSource src) {
        return (Player)Cast.cast((Object)(src != null ? src.m_7639_() : null), Player.class);
    }

    @Nonnull
    public static DamageType getDamageType(DamageSource src) {
        if (src == null) {
            return DamageType.UNKNOWN;
        }
        for (DamageType t : DamageType.TYPES) {
            if (!t.isThisType(src)) continue;
            return t;
        }
        return DamageType.UNKNOWN;
    }

    public static boolean isMinionEntity(Entity ent) {
        return DamageSourceProcessor.getMinionOwner(ent) != null;
    }

    public static boolean isAlchemicalEntity(Entity ent) {
        return DamageSourceProcessor.getAlchemicalOwner(ent) != null;
    }

    public static boolean isRangedDamage(DamageSource src) {
        return DamageSourceProcessor.getRangedOwner(src) != null;
    }

    public static Player getMinionOwner(Entity ent) {
        LivingEntity livingEntity;
        if (ent instanceof OwnableEntity && (livingEntity = ((OwnableEntity)ent).m_269323_()) instanceof Player) {
            Player pl = (Player)livingEntity;
            return pl;
        }
        return null;
    }

    public static Player getAlchemicalOwner(Entity ent) {
        Entity entity;
        if (ent instanceof ThrownPotion && (entity = ((ThrownPotion)ent).m_19749_()) instanceof Player) {
            Player pl = (Player)entity;
            return pl;
        }
        return null;
    }

    public static Player getRangedOwner(DamageSource ds) {
        Entity entity;
        if (DamageSourceProcessor.getAlchemicalOwner(ds.m_7640_()) != null) {
            return null;
        }
        if (ds.m_7640_() instanceof Projectile && (entity = ds.m_7639_()) instanceof Player) {
            Player pl = (Player)entity;
            return pl;
        }
        return null;
    }

    public static Player getMeleeAttacker(DamageSource ds) {
        if (DamageSourceProcessor.getDamageType(ds) == DamageType.MELEE) {
            return (Player)Cast.cast((Object)ds.m_7639_(), Player.class);
        }
        return null;
    }

    public static class DamageType {
        private static final List<DamageType> TYPES = new ArrayList<DamageType>();
        private static DamageType[] arTypes;
        public static final DamageType MELEE;
        public static final DamageType RANGED;
        public static final DamageType MINION;
        public static final DamageType ALCHEMICAL;
        public static final DamageType UNKNOWN;
        private final Predicate<DamageSource> test;

        public DamageType(Predicate<DamageSource> test) {
            TYPES.add(this);
            arTypes = TYPES.toArray(new DamageType[0]);
            this.test = test;
        }

        public boolean isThisType(DamageSource src) {
            return this.test.test(src);
        }

        public static DamageType[] getTypes() {
            return arTypes;
        }

        static {
            MELEE = new DamageType(d -> d.m_7639_() == d.m_7640_() && d.m_7639_() instanceof Player);
            RANGED = new DamageType(DamageSourceProcessor::isRangedDamage);
            MINION = new DamageType(d -> DamageSourceProcessor.isMinionEntity(d.m_7639_()));
            ALCHEMICAL = new DamageType(d -> DamageSourceProcessor.isAlchemicalEntity(d.m_7640_()));
            UNKNOWN = new DamageType(d -> false);
        }
    }
}

