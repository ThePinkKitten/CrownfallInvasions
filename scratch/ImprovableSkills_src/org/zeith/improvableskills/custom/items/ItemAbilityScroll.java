/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2IntArrayMap
 *  it.unimi.dsi.fastutil.objects.Object2IntMap
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.tooltip.TooltipComponent
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.registries.ForgeRegistries
 *  org.zeith.hammerlib.api.items.ITabItem
 *  org.zeith.hammerlib.core.RecipeHelper
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 */
package org.zeith.improvableskills.custom.items;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.api.items.ITabItem;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.recipe.RecipeParchmentFragment;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.api.tooltip.AbilityTooltip;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.ItemsIS;
import org.zeith.improvableskills.init.RecipeTypesIS;
import org.zeith.improvableskills.net.PacketScrollUnlockedAbility;

public class ItemAbilityScroll
extends Item
implements ITabItem {
    private static final Map<String, PlayerAbilityBase> ABILITY_MAP = new HashMap<String, PlayerAbilityBase>();
    private static final Object2IntMap<Item> CUSTOM_COLORS = new Object2IntArrayMap();

    public static void setCustomColor(Item item, int rgb) {
        CUSTOM_COLORS.put((Object)item, rgb);
    }

    public static int getCustomColor(Item item) {
        return CUSTOM_COLORS.getOrDefault((Object)item, 43008);
    }

    public ItemAbilityScroll() {
        super(new Item.Properties().m_41487_(1));
        ImprovableSkills.TAB.add((ItemLike)this);
    }

    @Nullable
    public String getCreatorModId(ItemStack stack) {
        PlayerAbilityBase v = ItemAbilityScroll.getAbilityFromScroll(stack);
        if (v != null) {
            return ImprovableSkills.ABILITIES().getKey((Object)v).m_135827_();
        }
        return "improvableskills";
    }

    @Nullable
    public static PlayerAbilityBase getAbilityFromScroll(ItemStack stack) {
        if (!stack.m_41619_() && stack.m_41720_() instanceof ItemAbilityScroll && stack.m_41782_() && stack.m_41783_().m_128425_("Ability", 8)) {
            String skill = stack.m_41783_().m_128461_("Ability");
            if (ABILITY_MAP.containsKey(skill)) {
                return ABILITY_MAP.get(skill);
            }
            PlayerAbilityBase b = (PlayerAbilityBase)ImprovableSkills.ABILITIES().getValue(new ResourceLocation(stack.m_41783_().m_128461_("Ability")));
            ABILITY_MAP.put(skill, b);
            return b;
        }
        return null;
    }

    public static ItemStack of(PlayerAbilityBase base) {
        ItemStack stack = new ItemStack((ItemLike)ItemsIS.ABILITY_SCROLL);
        CompoundTag tag = new CompoundTag();
        tag.m_128359_("Ability", base.getRegistryName().toString());
        stack.m_41751_(tag);
        return stack;
    }

    public static void getItems(Collection<ItemStack> items) {
        ImprovableSkills.ABILITIES().getValues().stream().sorted(Comparator.comparing(PlayerAbilityBase::getUnlocalizedName)).forEach(skill -> items.add(ItemAbilityScroll.of(skill)));
    }

    public CreativeModeTab getItemCategory() {
        return ImprovableSkills.TAB.tab();
    }

    public void fillItemCategory(CreativeModeTab tab, Set<ItemStack> items) {
        if (this.allowedIn(tab)) {
            ItemAbilityScroll.getItems(items);
        }
    }

    public void m_7373_(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        PlayerAbilityBase base = ItemAbilityScroll.getAbilityFromScroll(stack);
        if (base == null) {
            return;
        }
        tooltip.add((Component)base.getLocalizedName(SyncSkills.getData()).m_130940_(ChatFormatting.GRAY));
        if (flagIn.m_7050_()) {
            tooltip.add((Component)Component.m_237113_((String)(" - " + base.getRegistryName())).m_130940_(ChatFormatting.DARK_GRAY));
        }
        if (ImprovableSkills.PROXY.hasShiftDown()) {
            boolean hasAdded = false;
            List recipes = worldIn.m_7465_().m_44013_((RecipeType)RecipeTypesIS.PARCHMENT_FRAGMENT_TYPE);
            for (RecipeParchmentFragment recipe : recipes) {
                PlayerAbilityBase match;
                ItemStack result = recipe.result();
                if (result.m_41720_() != this || (match = ItemAbilityScroll.getAbilityFromScroll(result)) != base) continue;
                MutableComponent comp = Component.m_237113_((String)"");
                int i = 0;
                Iterator it = Stream.concat(Stream.of(Ingredient.m_43929_((ItemLike[])new ItemLike[]{ItemsIS.PARCHMENT_FRAGMENT})), recipe.ingredients.stream()).map(m -> {
                    ItemStack st = RecipeHelper.cycleIngredientStack((Ingredient)m, (long)1000L);
                    return ((MutableComponent)st.m_41611_()).m_130948_(Style.f_131099_.m_178520_(ItemAbilityScroll.getCustomColor(st.m_41720_())));
                }).iterator();
                while (it.hasNext()) {
                    if (i > 0) {
                        comp.m_130946_(", ");
                    }
                    comp.m_7220_((Component)it.next());
                    ++i;
                }
                tooltip.add((Component)Component.m_237110_((String)"recipe.improvableskills:ability", (Object[])new Object[]{comp}).m_130940_(ChatFormatting.GRAY));
                hasAdded = true;
            }
            if (!hasAdded) {
                int j;
                int i;
                String ln = I18n.m_118938_((String)("recipe." + base.getRegistryName().m_135827_() + ":ability." + base.getRegistryName().m_135815_()), (Object[])new Object[0]).replace('&', '\u00a7');
                while ((i = ln.indexOf(60)) != -1 && (j = ln.indexOf(62, i + 1)) != -1) {
                    String to = ln.substring(i + 1, j);
                    Item it = (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation(to));
                    String t = it != null ? it.m_7968_().m_41611_().getString() : Component.m_237115_((String)"text.improvableskills:unresolved_item").m_130940_(ChatFormatting.DARK_RED).getString();
                    ln = ln.replaceAll("<" + to + ">", t);
                }
                tooltip.add((Component)Component.m_237113_((String)ln).m_130940_(ChatFormatting.GRAY));
            }
        } else {
            tooltip.add((Component)Component.m_237113_((String)I18n.m_118938_((String)"text.improvableskills:shiftfrecipe", (Object[])new Object[0]).replace('&', '\u00a7')).m_130940_(ChatFormatting.GRAY));
        }
    }

    public InteractionResultHolder<ItemStack> m_7203_(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack held = playerIn.m_21120_(handIn);
        if (worldIn.f_46443_) {
            return new InteractionResultHolder(InteractionResult.SUCCESS, (Object)held);
        }
        return PlayerDataManager.handleDataSafely(playerIn, data -> {
            PlayerAbilityBase base = ItemAbilityScroll.getAbilityFromScroll(held);
            if (base != null && !data.hasAbility(base) && data.unlockAbility(base, true)) {
                int slot;
                ItemStack used = held.m_41777_();
                held.m_41774_(1);
                playerIn.m_6674_(handIn);
                worldIn.m_5594_(null, playerIn.m_20183_(), SoundEvents.f_11887_, SoundSource.PLAYERS, 0.5f, 1.0f);
                int n = slot = handIn == InteractionHand.OFF_HAND ? -2 : playerIn.m_150109_().f_35977_;
                if (playerIn instanceof ServerPlayer) {
                    Network.sendTo((IPacket)new PacketScrollUnlockedAbility(slot, used, base.getRegistryName()), (Player)playerIn);
                }
                return new InteractionResultHolder(InteractionResult.SUCCESS, (Object)held);
            }
            return new InteractionResultHolder(InteractionResult.PASS, (Object)held);
        }, new InteractionResultHolder(InteractionResult.PASS, (Object)held));
    }

    public Optional<TooltipComponent> m_142422_(ItemStack stack) {
        return Optional.ofNullable(ItemAbilityScroll.getAbilityFromScroll(stack)).map(AbilityTooltip::new);
    }
}

