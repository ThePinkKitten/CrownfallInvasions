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
import org.zeith.improvableskills.client.gui.PageletDiscord;
import org.zeith.improvableskills.custom.pagelets.PageletAbilities;
import org.zeith.improvableskills.custom.pagelets.PageletNews;
import org.zeith.improvableskills.custom.pagelets.PageletSkills;
import org.zeith.improvableskills.custom.pagelets.PageletUpdate;
import org.zeith.improvableskills.custom.pagelets.PageletXPStorage;

@SimplyRegister
public interface PageletsIS {
    @RegistryName(value="skills")
    public static final PageletSkills SKILLS = new PageletSkills();
    @RegistryName(value="abilities")
    public static final PageletAbilities ABILITIES = new PageletAbilities();
    @RegistryName(value="xp_bank")
    @OnlyIf(owner=ConfigsIS.class, member="xpBank")
    public static final PageletXPStorage XP_STORAGE = new PageletXPStorage();
    @RegistryName(value="update")
    public static final PageletUpdate UPDATE = new PageletUpdate();
    @RegistryName(value="news")
    public static final PageletNews NEWS = new PageletNews();
    @RegistryName(value="discord")
    public static final PageletDiscord DISCORD = new PageletDiscord();
}

