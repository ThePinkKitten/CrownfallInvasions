/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  net.minecraft.core.NonNullList
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.crafting.RecipeManager
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 *  org.zeith.hammerlib.api.inv.SimpleInventory
 *  org.zeith.hammerlib.api.items.ConsumableItem
 *  org.zeith.hammerlib.util.CommonMessages
 */
package org.zeith.improvableskills.custom.items;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.inv.SimpleInventory;
import org.zeith.hammerlib.api.items.ConsumableItem;
import org.zeith.hammerlib.util.CommonMessages;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.recipe.RecipeParchmentFragment;
import org.zeith.improvableskills.custom.items.ItemAbilityScroll;
import org.zeith.improvableskills.init.RecipeTypesIS;

public class ItemParchmentFragment
extends Item {
    public ItemParchmentFragment(Item.Properties props) {
        super(props);
    }

    public ItemParchmentFragment() {
        this(new Item.Properties());
        ImprovableSkills.TAB.add((ItemLike)this);
        ItemAbilityScroll.setCustomColor(this, 5569788);
    }

    public void m_7373_(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(CommonMessages.CRAFTING_MATERIAL);
        super.m_7373_(stack, level, tooltip, flag);
    }

    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity e) {
        if (e == null) {
            return false;
        }
        float f1 = Mth.m_14031_((float)((float)e.f_19797_ / 10.0f + e.f_31983_)) * 0.1f + 0.1f;
        CompoundTag nbt = e.getPersistentData();
        boolean fx = false;
        boolean ffx = false;
        int add = 0;
        RecipeParchmentFragment recipe = null;
        List itemsNearby = e.m_9236_().m_45976_(ItemEntity.class, e.m_20191_().m_82377_(1.0, 0.1, 1.0));
        itemsNearby.remove(e);
        RecipeManager recipes = e.m_9236_().m_7465_();
        block0: for (RecipeParchmentFragment r : recipes.m_44013_((RecipeType)RecipeTypesIS.PARCHMENT_FRAGMENT_TYPE)) {
            IntArrayList counts = new IntArrayList();
            NonNullList copy = NonNullList.m_122779_();
            for (ItemEntity ei : itemsNearby) {
                copy.add((Object)ei.m_32055_().m_41777_());
                counts.add(ei.m_32055_().m_41613_());
            }
            SimpleInventory id = new SimpleInventory(copy.size());
            for (int i = 0; i < copy.size(); ++i) {
                id.items.set(i, (Object)((ItemStack)copy.get(i)));
            }
            for (ConsumableItem ci : r.getConsumableIngredients()) {
                if (ci.consume((Container)id)) continue;
                continue block0;
            }
            double minDist = 400.0;
            for (int i = 0; i < copy.size(); ++i) {
                boolean changed;
                boolean bl = changed = ((ItemStack)copy.get(i)).m_41613_() != counts.getInt(i);
                if (!changed) continue;
                ItemEntity item = (ItemEntity)itemsNearby.get(i);
                double d = Math.max(0.8, (1.0 - item.m_20280_((Entity)e)) * 15.0) * 15.0;
                Vec3 ep = e.m_20182_();
                Vec3 ip = item.m_20182_();
                item.m_20256_(item.m_20184_().m_82549_(ep.m_82546_(ip).m_82490_(1.0 / d)));
                item.m_20242_(true);
                minDist = Math.min(item.m_20280_((Entity)e), minDist);
            }
            minDist *= 5000.0;
            fx = true;
            recipe = r;
            nbt.m_128405_("IS3ParchCraft", nbt.m_128451_("IS3ParchCraft") + 1);
            int v = nbt.m_128451_("IS3ParchCraft");
            int mv = r.ingredients.size() * 40;
            int time = v * 5 / mv;
            float prog = (float)v / (float)(mv + 40);
            if (v % Math.max(1, 5 - time) == 0) {
                e.m_9236_().m_5594_(null, e.m_20183_(), SoundEvents.f_12497_, SoundSource.AMBIENT, 2.0f, 0.25f + 1.75f * prog);
            }
            nbt.m_128350_("IS3ParchDegree", nbt.m_128457_("IS3ParchDegree") + (prog + 0.25f) * 4.0f);
            nbt.m_128350_("IS3ParchThrowback", prog);
            add = Math.round((float)v / ((float)mv + 40.0f) * 10.0f);
            if (v <= mv) break;
            if (v > mv + 40 && minDist < 0.5) {
                if (!e.m_9236_().f_46443_) {
                    Object ei2;
                    NonNullList origin = NonNullList.m_122779_();
                    for (Object ei2 : itemsNearby) {
                        origin.add((Object)ei2.m_32055_());
                        ei2.m_20242_(false);
                    }
                    id = new SimpleInventory(origin.size());
                    for (int i = 0; i < origin.size(); ++i) {
                        id.items.set(i, (Object)((ItemStack)origin.get(i)));
                    }
                    ItemStack resStack = r.m_5874_((Container)id, e.m_9236_().m_9598_());
                    ei2 = r.getConsumableIngredients().iterator();
                    while (ei2.hasNext()) {
                        ConsumableItem ci = (ConsumableItem)ei2.next();
                        if (ci.consume((Container)id)) continue;
                        continue block0;
                    }
                    Vec3 ep = e.m_20182_();
                    ItemEntity res = new ItemEntity(e.m_9236_(), ep.f_82479_, ep.f_82480_, ep.f_82481_, resStack);
                    res.m_20256_(e.m_20184_());
                    res.f_31983_ = e.f_31983_;
                    e.m_9236_().m_5594_(null, e.m_20183_(), SoundEvents.f_11880_, SoundSource.AMBIENT, 1.0f, 1.6f + e.m_9236_().f_46441_.m_188501_() * 0.2f);
                    e.m_9236_().m_7967_((Entity)res);
                }
                e.m_32055_().m_41774_(1);
                nbt.m_128473_("IS3ParchCraft");
            }
            ffx = true;
            break;
        }
        if (recipe == null && nbt.m_128441_("IS3ParchCraft")) {
            nbt.m_128473_("IS3ParchCraft");
        }
        if (fx && recipe != null && e.f_19797_ % 2 == 0 && e.m_20096_()) {
            int num = recipe.ingredients.size() + 3 + add;
            float deg = 360.0f / (float)num;
            float coff = nbt.m_128457_("IS3ParchDegree") % 360.0f;
            float throwb = 0.75f + nbt.m_128457_("IS3ParchThrowback");
            for (int i = 0; i < num; ++i) {
                double sin = Math.sin(Math.toRadians(coff));
                double cos = Math.cos(Math.toRadians(coff));
                RandomSource itemRand = e.m_9236_().f_46441_;
                Vec3 ep = e.m_20182_();
                ImprovableSkills.PROXY.sparkle(e.m_9236_(), ep.f_82479_ + (double)((itemRand.m_188501_() - itemRand.m_188501_()) * 0.05f), ep.f_82480_ + (double)((itemRand.m_188501_() - itemRand.m_188501_()) * 0.1f) + (double)e.m_20206_() * 1.5, ep.f_82481_ + (double)((itemRand.m_188501_() - itemRand.m_188501_()) * 0.05f), sin * 0.05 * (double)throwb, (double)f1 * 0.1, cos * 0.05 * (double)throwb, 8893951, 90);
                coff += deg;
            }
        }
        return false;
    }
}

