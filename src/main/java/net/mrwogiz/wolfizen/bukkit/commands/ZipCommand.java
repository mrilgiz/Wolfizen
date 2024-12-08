package net.mrwogiz.wolfizen.bukkit.commands;

import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCommand extends AbstractCommand {

    public ZipCommand() {
        setName("zip");
        setSyntax("zip [source:<path>] [destination:<path>]");
        autoCompile();
    }

    // <--[command]
    // @Name Zip
    // @Syntax zip [source:<path>] [destination:<path>]
    // @Required 2
    // @Short Archives a folder or file into a zip file.
    // @Group Wolfizen
    //
    // @Description
    // Archives a folder or file into a zip file at the specified destination.
    //
    // @Tags
    // <ElementTag>
    //
    // @Usage
    // - zip source:/data/folder destination:/data/test.zip
    // -->

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgName("source") @ArgPrefixed ElementTag source,
                                   @ArgName("destination") @ArgPrefixed ElementTag destination) {
        try {
            zipFolder(source.asString(), destination.asString());
            Debug.log("Successfully zipped: " + source.asString() + " to " + destination.asString());
        } catch (IOException e) {
            Debug.echoError("Failed to zip: " + e.getMessage());
        }
    }

    private static void zipFolder(String sourceFolder, String zipFilePath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Path sourcePath = Paths.get(sourceFolder);
            Files.walk(sourcePath).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                try {
                    zos.putNextEntry(zipEntry);
                    Files.copy(path, zos);
                    zos.closeEntry();
                } catch (IOException e) {
                    Debug.echoError("Error zipping file: " + e.getMessage());
                }
            });
        }
    }
}