/*
 * Decompiled with CFR 0.152.
 */
package org.zeith.improvableskills.api.client;

import org.zeith.improvableskills.api.client.ISlotRenderer;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;

public interface IClientAbilityExtensions {
    public static final IClientAbilityExtensions DEFAULT = new IClientAbilityExtensions(){};

    public static IClientAbilityExtensions of(PlayerAbilityBase a) {
        IClientAbilityExtensions e;
        Object object = a.getRenderPropertiesInternal();
        return object instanceof IClientAbilityExtensions ? (e = (IClientAbilityExtensions)object) : DEFAULT;
    }

    default public ISlotRenderer slotRenderer() {
        return ISlotRenderer.NONE;
    }
}

