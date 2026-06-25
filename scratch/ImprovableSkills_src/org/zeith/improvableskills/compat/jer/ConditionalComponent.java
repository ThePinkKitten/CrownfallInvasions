/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jeresources.api.conditionals.Conditional
 *  net.minecraft.network.chat.Component
 */
package org.zeith.improvableskills.compat.jer;

import jeresources.api.conditionals.Conditional;
import net.minecraft.network.chat.Component;

public class ConditionalComponent
extends Conditional {
    public final Component par;

    public ConditionalComponent(Component par) {
        this.par = par;
    }

    public Component toStringTextComponent() {
        return this.par;
    }
}

