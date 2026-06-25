/*
 * Decompiled with CFR 0.152.
 */
package org.zeith.improvableskills.api.client;

import org.zeith.improvableskills.api.client.ISlotRenderer;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;

public interface IClientSkillExtensions {
    public static final IClientSkillExtensions DEFAULT = new IClientSkillExtensions(){};

    public static IClientSkillExtensions of(PlayerSkillBase s) {
        IClientSkillExtensions e;
        Object object = s.getRenderPropertiesInternal();
        return object instanceof IClientSkillExtensions ? (e = (IClientSkillExtensions)object) : DEFAULT;
    }

    default public ISlotRenderer slotRenderer() {
        return ISlotRenderer.NONE;
    }
}

