/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.loading.FMLPaths
 */
package org.zeith.improvableskills.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Optional;
import net.minecraftforge.fml.loading.FMLPaths;

public class ClientData {
    public static Optional<String> readData(String sub) {
        File file = ClientData.getModDataPath().resolve(sub).toFile();
        if (file.isFile()) {
            return Optional.ofNullable(ClientData.tryFileRead(file));
        }
        return Optional.empty();
    }

    public static void writeData(String sub, String data) {
        File file = ClientData.getModDataPath().resolve(sub).toFile();
        ClientData.tryFileWrite(file, data);
    }

    public static Path getModDataPath() {
        Path path = FMLPaths.GAMEDIR.get().resolve("hammerlib").resolve("improvableskills");
        try {
            Files.createDirectories(path, new FileAttribute[0]);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return path;
    }

    private static String tryFileRead(File file) {
        try {
            return file.isFile() ? Files.readString(file.toPath()) : null;
        }
        catch (Exception e) {
            return null;
        }
    }

    private static void tryFileWrite(File file, String str) {
        try {
            Files.writeString(file.toPath(), (CharSequence)str, new OpenOption[0]);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

