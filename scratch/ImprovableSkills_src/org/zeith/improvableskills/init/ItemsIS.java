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
import org.zeith.improvableskills.custom.items.ItemAbilityScroll;
import org.zeith.improvableskills.custom.items.ItemCreativeSkillScroll;
import org.zeith.improvableskills.custom.items.ItemParchmentFragment;
import org.zeith.improvableskills.custom.items.ItemSkillScroll;
import org.zeith.improvableskills.custom.items.ItemSkillsBook;

@SimplyRegister
public interface ItemsIS {
    @RegistryName(value="parchment_fragment")
    public static final ItemParchmentFragment PARCHMENT_FRAGMENT = new ItemParchmentFragment();
    @RegistryName(value="skills_book")
    public static final ItemSkillsBook SKILLS_BOOK = new ItemSkillsBook();
    @RegistryName(value="scroll_ability")
    public static final ItemAbilityScroll ABILITY_SCROLL = new ItemAbilityScroll();
    @RegistryName(value="scroll_normal")
    public static final ItemSkillScroll SKILL_SCROLL = new ItemSkillScroll();
    @RegistryName(value="scroll_creative")
    public static final ItemCreativeSkillScroll CREATIVE_SKILL_SCROLL = new ItemCreativeSkillScroll();
}

