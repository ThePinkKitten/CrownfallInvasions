/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Util
 *  org.zeith.hammerlib.util.java.Threading
 */
package org.zeith.improvableskills.utils;

import net.minecraft.Util;
import org.zeith.hammerlib.util.java.Threading;

public class Sys {
    public static void openURL(String url) {
        if (url == null || url.isBlank()) {
            return;
        }
        Threading.createAndStart((String)"OpenURL", () -> {
            try {
                Util.m_137581_().m_137646_(url);
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }
}

