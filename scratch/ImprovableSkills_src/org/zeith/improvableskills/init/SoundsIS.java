/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  org.zeith.hammerlib.annotations.SimplyRegister
 */
package org.zeith.improvableskills.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.zeith.hammerlib.annotations.SimplyRegister;

public interface SoundsIS {
    public static final SoundEvent PAGE_TURNS = SoundEvent.m_262824_((ResourceLocation)new ResourceLocation("improvableskills", "page_turns"));
    public static final SoundEvent TREASURE_FOUND = SoundEvent.m_262824_((ResourceLocation)new ResourceLocation("improvableskills", "treasure_found"));
    public static final SoundEvent CONNECT = SoundEvent.m_262824_((ResourceLocation)new ResourceLocation("improvableskills", "connect"));

    @SimplyRegister
    public static void register(BiConsumer<ResourceLocation, SoundEvent> r) {
        for (Field f : SoundsIS.class.getDeclaredFields()) {
            if (!SoundEvent.class.isAssignableFrom(f.getType()) || !Modifier.isStatic(f.getModifiers())) continue;
            f.setAccessible(true);
            try {
                SoundEvent se = (SoundEvent)f.get(null);
                r.accept(se.m_11660_(), se);
            }
            catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }
}

