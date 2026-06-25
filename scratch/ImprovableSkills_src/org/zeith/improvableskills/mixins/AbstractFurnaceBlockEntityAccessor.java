/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.NonNullList
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.AbstractCookingRecipe
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package org.zeith.improvableskills.mixins;

import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={AbstractFurnaceBlockEntity.class})
public interface AbstractFurnaceBlockEntityAccessor {
    @Accessor
    public int getLitTime();

    @Accessor
    public void setLitTime(int var1);

    @Accessor
    public int getCookingProgress();

    @Accessor
    public void setCookingProgress(int var1);

    @Accessor
    public int getCookingTotalTime();

    @Invoker
    public boolean callBurn(RegistryAccess var1, @Nullable Recipe<?> var2, NonNullList<ItemStack> var3, int var4);

    @Accessor
    public RecipeType<? extends AbstractCookingRecipe> getRecipeType();

    @Accessor
    public NonNullList<ItemStack> getItems();
}

