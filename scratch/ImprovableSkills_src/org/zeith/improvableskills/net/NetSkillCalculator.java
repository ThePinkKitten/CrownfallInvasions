/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtIo
 *  net.minecraft.nbt.Tag
 *  org.zeith.hammerlib.net.lft.ITransportAcceptor
 *  org.zeith.hammerlib.net.lft.TransportSessionBuilder
 */
package org.zeith.improvableskills.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import org.zeith.hammerlib.net.lft.ITransportAcceptor;
import org.zeith.hammerlib.net.lft.TransportSessionBuilder;
import org.zeith.improvableskills.ImprovableSkills;

public class NetSkillCalculator
implements ITransportAcceptor {
    public void read(InputStream readable, int length) {
        try {
            CompoundTag nbt = NbtIo.m_128939_((InputStream)readable);
            ImprovableSkills.SKILLS().forEach(skill -> skill.xpCalculator.readClientNBT(nbt.m_128469_("SkillCost" + skill.getRegistryName().toString())));
            ImprovableSkills.LOG.info("Received server settings.");
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
    }

    public static TransportSessionBuilder pack() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CompoundTag nbt = new CompoundTag();
        ImprovableSkills.SKILLS().forEach(skill -> {
            CompoundTag tag = new CompoundTag();
            skill.xpCalculator.writeServerNBT(tag);
            nbt.m_128365_("SkillCost" + skill.getRegistryName().toString(), (Tag)tag);
        });
        try {
            NbtIo.m_128947_((CompoundTag)nbt, (OutputStream)baos);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new TransportSessionBuilder().setAcceptor(NetSkillCalculator.class).addData(baos.toByteArray());
    }
}

