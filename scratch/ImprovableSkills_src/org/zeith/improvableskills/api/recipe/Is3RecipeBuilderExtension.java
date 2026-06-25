/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.crafting.Recipe
 *  org.zeith.hammerlib.api.recipes.RecipeBuilderExtension
 *  org.zeith.hammerlib.api.recipes.RecipeBuilderExtension$RegisterExt
 *  org.zeith.hammerlib.event.recipe.RegisterRecipesEvent
 *  org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent
 */
package org.zeith.improvableskills.api.recipe;

import net.minecraft.world.item.crafting.Recipe;
import org.zeith.hammerlib.api.recipes.RecipeBuilderExtension;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;
import org.zeith.improvableskills.api.recipe.ParchmentFragmentBuilder;

@RecipeBuilderExtension.RegisterExt
public class Is3RecipeBuilderExtension
extends RecipeBuilderExtension {
    public Is3RecipeBuilderExtension(RegisterRecipesEvent event) {
        super(event);
    }

    public ParchmentFragmentBuilder parchment() {
        return new ParchmentFragmentBuilder((IRecipeRegistrationEvent<Recipe<?>>)this.event);
    }
}

