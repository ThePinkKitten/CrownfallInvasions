/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.NonNullList
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Recipe
 *  org.zeith.hammerlib.core.RecipeHelper
 *  org.zeith.hammerlib.core.adapter.recipe.RecipeBuilder
 *  org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent
 */
package org.zeith.improvableskills.api.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.core.adapter.recipe.RecipeBuilder;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;
import org.zeith.improvableskills.api.recipe.RecipeParchmentFragment;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.custom.items.ItemAbilityScroll;

public class ParchmentFragmentBuilder
extends RecipeBuilder<ParchmentFragmentBuilder> {
    protected final NonNullList<Ingredient> ingredients = NonNullList.m_122779_();
    protected boolean identifierSet;

    public ParchmentFragmentBuilder(IRecipeRegistrationEvent<Recipe<?>> event) {
        super(event);
    }

    public ParchmentFragmentBuilder abilityScroll(PlayerAbilityBase abil) {
        if (this.identifier == null) {
            ResourceLocation id = abil.getRegistryName();
            if (id == null) {
                return this;
            }
            this.identifierSet = true;
            this.id(new ResourceLocation(id.m_135827_(), "ability_scrolls/" + id.m_135815_()));
        }
        return (ParchmentFragmentBuilder)this.result(ItemAbilityScroll.of(abil));
    }

    public ParchmentFragmentBuilder add(Object ingredient) {
        this.ingredients.add((Object)RecipeHelper.fromComponent((Object)ingredient));
        return this;
    }

    public ParchmentFragmentBuilder addAll(Object ... ingredients) {
        for (Object ingredient : ingredients) {
            this.ingredients.add((Object)RecipeHelper.fromComponent((Object)ingredient));
        }
        return this;
    }

    public ParchmentFragmentBuilder addAll(Iterable<Object> ingredients) {
        for (Object ingredient : ingredients) {
            this.ingredients.add((Object)RecipeHelper.fromComponent((Object)ingredient));
        }
        return this;
    }

    protected void validate() {
        super.validate();
        if (this.ingredients.isEmpty()) {
            throw new IllegalStateException(((Object)((Object)this)).getClass().getSimpleName() + " does not have any defined ingredients!");
        }
    }

    protected Recipe<?> createRecipe() {
        return new RecipeParchmentFragment(this.getIdentifier(), this.group, this.result, this.ingredients);
    }
}

