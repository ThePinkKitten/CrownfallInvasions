/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.core.NonNullList
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.GsonHelper
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.ShapedRecipe
 *  net.minecraft.world.level.ItemLike
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.Nullable
 *  org.zeith.hammerlib.abstractions.recipes.IRecipeVisualizer
 *  org.zeith.hammerlib.abstractions.recipes.IRecipeVisualizer$VisualizedRecipeGroup
 *  org.zeith.hammerlib.abstractions.recipes.IVisualizedRecipe
 *  org.zeith.hammerlib.abstractions.recipes.layout.ISlotBuilder
 *  org.zeith.hammerlib.abstractions.recipes.layout.ISlotBuilder$SlotRole
 *  org.zeith.hammerlib.abstractions.recipes.layout.IVisualizerBuilder
 *  org.zeith.hammerlib.api.items.ConsumableItem
 *  org.zeith.hammerlib.api.recipes.BaseRecipe
 *  org.zeith.hammerlib.api.recipes.SerializableRecipeType
 *  org.zeith.hammerlib.client.render.IGuiDrawable
 *  org.zeith.hammerlib.client.utils.UV
 */
package org.zeith.improvableskills.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.abstractions.recipes.IRecipeVisualizer;
import org.zeith.hammerlib.abstractions.recipes.IVisualizedRecipe;
import org.zeith.hammerlib.abstractions.recipes.layout.ISlotBuilder;
import org.zeith.hammerlib.abstractions.recipes.layout.IVisualizerBuilder;
import org.zeith.hammerlib.api.items.ConsumableItem;
import org.zeith.hammerlib.api.recipes.BaseRecipe;
import org.zeith.hammerlib.api.recipes.SerializableRecipeType;
import org.zeith.hammerlib.client.render.IGuiDrawable;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.init.ItemsIS;
import org.zeith.improvableskills.init.RecipeTypesIS;

public class RecipeParchmentFragment
extends BaseRecipe<RecipeParchmentFragment> {
    public final List<Ingredient> ingredients;

    public RecipeParchmentFragment(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group);
        this.vanillaResult = result;
        this.ingredients = ingredients;
    }

    public ItemStack result() {
        return this.vanillaResult.m_41777_();
    }

    public List<ConsumableItem> getConsumableIngredients() {
        return this.ingredients.stream().map(i -> new ConsumableItem(1, new Ingredient[]{i})).toList();
    }

    protected SerializableRecipeType<RecipeParchmentFragment> getRecipeType() {
        return RecipeTypesIS.PARCHMENT_FRAGMENT_TYPE;
    }

    public static class Type
    extends SerializableRecipeType<RecipeParchmentFragment> {
        public RecipeParchmentFragment fromJson(ResourceLocation id, JsonObject json) {
            NonNullList<Ingredient> items = Type.itemsFromJson(GsonHelper.m_13933_((JsonObject)json, (String)"ingredients"));
            if (items.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            String s = GsonHelper.m_13851_((JsonObject)json, (String)"group", (String)"");
            ItemStack result = ShapedRecipe.m_151274_((JsonObject)GsonHelper.m_13930_((JsonObject)json, (String)"result"));
            return new RecipeParchmentFragment(id, s, result, items);
        }

        @Nullable
        public RecipeParchmentFragment fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String s = buf.m_130277_();
            int i = buf.m_130242_();
            NonNullList items = NonNullList.m_122780_((int)i, (Object)Ingredient.f_43901_);
            for (int j = 0; j < items.size(); ++j) {
                items.set(j, (Object)Ingredient.m_43940_((FriendlyByteBuf)buf));
            }
            ItemStack result = buf.m_130267_();
            return new RecipeParchmentFragment(id, s, result, (NonNullList<Ingredient>)items);
        }

        public void toNetwork(FriendlyByteBuf buf, RecipeParchmentFragment r) {
            buf.m_130070_(r.group);
            buf.m_130130_(r.ingredients.size());
            for (Ingredient ingredient : r.ingredients) {
                ingredient.m_43923_(buf);
            }
            buf.m_130055_(r.vanillaResult);
        }

        public void initVisuals(Consumer<IRecipeVisualizer<RecipeParchmentFragment, ?>> viualizerConsumer) {
            viualizerConsumer.accept(IRecipeVisualizer.simple(VisualizedTestMachine.class, (IRecipeVisualizer.VisualizedRecipeGroup)IRecipeVisualizer.groupBuilder().title((Component)Component.m_237115_((String)"jei.improvableskills:parchf")).size(132, 34).icon(IGuiDrawable.ofItem((ItemStack)new ItemStack((ItemLike)ItemsIS.PARCHMENT_FRAGMENT))).catalyst(new ItemStack[]{new ItemStack((ItemLike)ItemsIS.SKILLS_BOOK)}).build(), VisualizedTestMachine::new));
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray arr) {
            NonNullList lst = NonNullList.m_122779_();
            for (int i = 0; i < arr.size(); ++i) {
                Ingredient ingredient = Ingredient.m_43917_((JsonElement)arr.get(i));
                if (ingredient.m_43947_()) continue;
                lst.add((Object)ingredient);
            }
            return lst;
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public record VisualizedTestMachine(RecipeParchmentFragment recipe) implements IVisualizedRecipe<RecipeParchmentFragment>
    {
        public static final UV BACKGROUND = new UV(ImprovableSkills.id("textures/gui/jei.png"), 0.0f, 0.0f, 132.0f, 34.0f);

        public RecipeParchmentFragment getRecipe() {
            return this.recipe;
        }

        public void drawBackground(GuiGraphics gfx, double mouseX, double mouseY) {
            BACKGROUND.render(gfx, 0.0f, 0.0f);
        }

        public void setupLayout(IVisualizerBuilder builder) {
            ((ISlotBuilder)builder.addSlot(ISlotBuilder.SlotRole.INPUT, 8, 9).addItemStack(new ItemStack((ItemLike)ItemsIS.PARCHMENT_FRAGMENT))).build();
            ((ISlotBuilder)builder.addSlot(ISlotBuilder.SlotRole.OUTPUT, 107, 9).addItemStack(this.recipe.vanillaResult.m_41777_())).build();
            int j = 0;
            for (Ingredient ci : this.recipe.ingredients) {
                builder.addSlot(ISlotBuilder.SlotRole.INPUT, j * 18 + 26, 9).addIngredients(ci);
                ++j;
            }
        }
    }
}

