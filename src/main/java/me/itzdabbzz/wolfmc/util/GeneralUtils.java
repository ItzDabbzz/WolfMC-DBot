package me.itzdabbzz.wolfmc.util;

import me.vem.jdab.utils.Logger;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class GeneralUtils {

    /**
     * This will download and cache the image if not found already!
     *
     * @param fileUrl  Url to download the image from.
     * @param fileName Name of the image file.
     * @param user     User to send the image to.
     */
    public static void sendImage(String fileUrl, String fileName, User user) {
        try {
            File dir = new File("imgs");
            if (!dir.exists() && !dir.mkdir())
                throw new IllegalStateException("Cannot create 'imgs' folder!");
            File image = new File("imgs" + File.separator + fileName);
            if (!image.exists() && image.createNewFile()) {
                URL url = new URL(fileUrl);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 FlareBot");
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(image);
                byte[] b = new byte[2048];
                int length;
                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
                is.close();
                os.close();
            }
            user.openPrivateChannel().complete().sendFile(image, fileName, null)
                    .queue();
        } catch (IOException | ErrorResponseException e) {
            Logger.errf("Unable to send image '" + fileName + "'", e);
        }
    }

}
