package net.mrwogiz.wolfizen.bukkit.commands;

import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ImageTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.MultipartBody;
import kong.unirest.core.Unirest;
import net.mrwogiz.wolfizen.bukkit.Wolfizen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class MineskinCommand extends AbstractCommand {

    public MineskinCommand() {
        setName("mineskin");
        setSyntax("mineskin [apikey:<key>] [image:<ImageTag>] [version:<1|2|3(queue 2)>]");
        autoCompile();
    }

    // <--[command]
    // @Name Mineskin
    // @Syntax mineskin [apikey:<key>] [file:<path>] [version:<1|2>]
    // @Required 3
    // @Short Uploads a skin to the Mineskin API and retrieves the response.
    // @Group Wolfizen
    //
    // @Description
    // Uploads a skin using the provided API key and file data.
    //
    // @Tags
    // <ElementTag>
    //
    // @Usage
    // - mineskin apikey:YOUR_API_KEY file:path/to/your/file.png version:1
    // -->

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgName("apikey") @ArgPrefixed ElementTag apikey,
                                   @ArgName("image") @ArgPrefixed ImageTag image,
                                   @ArgName("version") @ArgPrefixed ElementTag version) throws IOException {
        String apiKey = apikey.asString();
        String apiUrl;
        if (version.asString().equals("2")) {
            apiUrl = "https://api.mineskin.org/v2/generate";
        } else if (version.asString().equals("1")) {
            apiUrl = "https://api.mineskin.org/generate/upload";
        } else if (version.asString().equals("3")) {
            apiUrl = "https://api.mineskin.org/v2/queue";
        } else {
            apiUrl = "https://api.mineskin.org/generate/upload";
        }
        uploadSkin(apiKey, image, apiUrl, scriptEntry);
    }

    private static void uploadSkin(String apiKey, ImageTag image, String apiUrl, ScriptEntry scriptEntry) throws IOException {
        BufferedImage bufferedImage = image.getJavaObject();;
        if (!Files.exists(Wolfizen.getPluginI().getDataFolder().toPath())) {
            try {
            Files.createDirectory(Wolfizen.getPluginI().getDataFolder().toPath());
            } catch (IOException e) {
                Wolfizen.getPluginI().getLogger().warning(e.getMessage());
            }

        }
        if (!Files.exists(Path.of(Wolfizen.getPluginI().getDataFolder().getPath() + "/mineskin"))) {
            try {
                Files.createDirectory(Path.of(Wolfizen.getPluginI().getDataFolder().getPath() + "/mineskin"));
            } catch (IOException e) {
                Wolfizen.getPluginI().getLogger().warning(e.getMessage());
            }
        }
        File file = new File(Wolfizen.getPluginI().getDataFolder().getPath() + "/mineskin/image.png");
        ImageIO.write(bufferedImage,"png",file);
        if (!file.exists()) {
            scriptEntry.saveObject("data", new ElementTag("false"));
            scriptEntry.setFinished(true);
        }
        try {
            // Create the multipart request
            MultipartBody request = Unirest.post(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .field("file", file);

            // Execute the request and get the response
            HttpResponse<String> response = request.asString();

            // Check if the response is successful
            if (response.getStatus() == 200) {
                String result = response.getBody();
                scriptEntry.saveObject("data", new ElementTag(result));
            } else {
                scriptEntry.saveObject("data", new ElementTag("false"));
            }

            scriptEntry.setFinished(true);
        } catch (Exception e) {
            scriptEntry.saveObject("data", new ElementTag("false"));
            scriptEntry.setFinished(true);
        }
    }



}