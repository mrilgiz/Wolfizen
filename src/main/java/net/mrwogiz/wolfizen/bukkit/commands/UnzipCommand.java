package net.mrwogiz.wolfizen.bukkit.commands;

import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipCommand extends AbstractCommand {

    public UnzipCommand() {
        setName("unzip");
        setSyntax("unzip [source:<path>] [destination:<path>]");
        autoCompile();
    }

    // <--[command]
    // @Name Unzip
    // @Syntax unzip [source:<path>] [destination:<path>]
    // @Required 2
    // @Short Unarchives a zip file to a specified folder.
    // @Group Wolfizen
    //
    // @Description
    // Unarchives a zip file to a specified folder.
    //
    // @Tags
    // <ElementTag>
    //
    // @Usage
    // - unzip source:/data/test.zip destination:/data/folder
    // -->

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgName("source") @ArgPrefixed ElementTag source,
                                   @ArgName("destination") @ArgPrefixed ElementTag destination) {
        try {
            unzip(source.asString(), destination.asString());
            Debug.log("Successfully unzipped: " + source.asString() + " to " + destination.asString());
        } catch (IOException e) {
            Debug.echoError("Failed to unzip: " + e.getMessage());
        }
    }

    private static void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                File newFile = new File(destDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile))) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            bos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }
}