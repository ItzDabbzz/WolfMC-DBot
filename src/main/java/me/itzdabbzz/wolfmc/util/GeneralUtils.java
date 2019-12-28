package me.itzdabbzz.wolfmc.util;

import me.itzdabbzz.wolfmc.WolfBot;
import me.itzdabbzz.wolfmc.objects.Report;
import me.itzdabbzz.wolfmc.objects.ReportMessage;
import me.vem.jdab.utils.Logger;
import me.vem.jdab.utils.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import sun.misc.MessageUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.time.format.DateTimeFormatter;

public class GeneralUtils {


    /**
     * Gets the {@link Report} embed with all of the info on the report.
     *
     * @param sender The {@link User} who requested the embed
     * @param report The {@link Report} to get the embed of.
     * @return an {@link EmbedBuilder} that contains all the report data
     */
    public static EmbedBuilder getReportEmbed(User sender, Report report) {
        EmbedBuilder eb = Utils.getEmbed(sender);
        User reporter = WolfBot.getClient().getJDA().getUserById(report.getReporterId());
        User reported = WolfBot.getClient().getJDA().getUserById(report.getReportedId());

        eb.addField("Report ID", String.valueOf(report.getId()), true);
        eb.addField("Reporter", reporter != null ? Utils.getTag(reporter) : "Unknown", true);
        eb.addField("Reported", reported != null ? Utils.getTag(reported) : "Unknown", true);

        //eb.addField("Time", report.getTime().toLocalDateTime().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " GMT/BST", true);
        eb.setTimestamp(report.getTime().toLocalDateTime());
        eb.addField("Status", report.getStatus().getMessage(), true);

        eb.addField("Message", "```" + report.getMessage() + "```", false);
        StringBuilder builder = new StringBuilder("The last 5 messages by the reported user: ```\n");
        for (ReportMessage m : report.getMessages()) {
            builder.append("[").append(m.getTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(" GMT/BST] ")
                    .append(FormatUtils.truncate(100, m.getMessage()))
                    .append("\n");
        }
        builder.append("```");
        eb.addField("Messages from reported user", builder.toString(), false);
        return eb;
    }

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
