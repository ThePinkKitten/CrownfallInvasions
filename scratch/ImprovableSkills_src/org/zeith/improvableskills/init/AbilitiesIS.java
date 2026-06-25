/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.zeith.hammerlib.annotations.OnlyIf
 *  org.zeith.hammerlib.annotations.RegistryName
 *  org.zeith.hammerlib.annotations.SimplyRegister
 */
package org.zeith.improvableskills.init;

import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.improvableskills.cfg.ConfigsIS;
import org.zeith.improvableskills.custom.abilities.AbilityAnvil;
import org.zeith.improvableskills.custom.abilities.AbilityAutoXpBank;
import org.zeith.improvableskills.custom.abilities.AbilityCowboy;
import org.zeith.improvableskills.custom.abilities.AbilityCrafter;
import org.zeith.improvableskills.custom.abilities.AbilityEnchanting;
import org.zeith.improvableskills.custom.abilities.AbilityMagnetism;

@SimplyRegister
public interface AbilitiesIS {
    @RegistryName(value="enchanting")
    @OnlyIf(owner=ConfigsIS.class, member="enableAbility")
    public static final AbilityEnchanting ENCHANTING = new AbilityEnchanting();
    @RegistryName(value="crafter")
    @OnlyIf(owner=ConfigsIS.class, member="enableAbility")
    public static final AbilityCrafter CRAFTER = new AbilityCrafter();
    @RegistryName(value="anvil")
    @OnlyIf(owner=ConfigsIS.class, member="enableAbility")
    public static final AbilityAnvil ANVIL = new AbilityAnvil();
    @RegistryName(value="magnetism")
    @OnlyIf(owner=ConfigsIS.class, member="enableAbility")
    public static final AbilityMagnetism MAGNETISM = new AbilityMagnetism();
    @RegistryName(value="auto_xp_bank")
    @OnlyIf(owner=ConfigsIS.class, member="enableAbility")
    public static final AbilityAutoXpBank AUTO_XP_BANK = new AbilityAutoXpBank();
    @RegistryName(value="cowboy")
    @OnlyIf(owner=ConfigsIS.class, member="enableAbility")
    public static final AbilityCowboy COWBOY = new AbilityCowboy();
}

