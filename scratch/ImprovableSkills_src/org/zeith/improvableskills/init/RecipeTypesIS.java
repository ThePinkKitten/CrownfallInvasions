/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.zeith.hammerlib.annotations.RegistryName
 *  org.zeith.hammerlib.annotations.SimplyRegister
 */
package org.zeith.improvableskills.init;

import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.improvableskills.api.recipe.RecipeParchmentFragment;

@SimplyRegister
public interface RecipeTypesIS {
    @RegistryName(value="parchment_fragment")
    public static final RecipeParchmentFragment.Type PARCHMENT_FRAGMENT_TYPE = new RecipeParchmentFragment.Type();
}

