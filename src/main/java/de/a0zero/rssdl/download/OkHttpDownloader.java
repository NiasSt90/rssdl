package de.a0zero.rssdl.download;

import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import de.a0zero.rssdl.FileDownloader;
import de.a0zero.rssdl.MainArguments;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class OkHttpDownloader implements FileDownloader {

	private static final Logger log = Logger.getLogger(OkHttpDownloader.class.getName());

	private final MainArguments arguments;


	public OkHttpDownloader(MainArguments arguments) {
		this.arguments = arguments;
	}


	public File download(URL url, String type) throws IOException {
		final File target = File.createTempFile("rssdl_", type);
		target.deleteOnExit();
		FileUtils.copyURLToFile(url, target);
		return target;
	}


	@Override
	public void downloadSet(int nodeID, SyndEntry entry, SyndEnclosure rssFile) {
		String targetFileName = calculateTargetFilenameWithPath(nodeID, entry, rssFile);
		final File targetFile = new File(targetFileName);
		if (arguments.dryRun) {
			log.info("Dry-Run: " + (targetFile.exists() ? "already exists " : "Downloading " + rssFile.getUrl() + " to ") +  targetFileName);
			return;
		}
		try {
			if (!targetFile.exists() || (targetFile.length() == 0)) {
				log.log(Level.INFO, () -> String.format("Start downloading(%b/%d) %s to %s", targetFile.exists(), rssFile.getLength(), rssFile.getUrl(), targetFile));
				ProgressBar progressBar = null;
				if (!MainArguments.quiet && arguments.downloadParallel == 1) {
					progressBar = new ProgressBar("" + nodeID, rssFile.getLength(), ProgressBarStyle.UNICODE_BLOCK).start();
				}
				try (Response response = new Progress().run(new URL(rssFile.getUrl()), progressBar)) {
					if (response.isSuccessful() && response.body() != null) {
						final InputStream source = response.body().byteStream();
						FileUtils.copyInputStreamToFile(source, targetFile);
					}
				}
				if (progressBar != null) {
					progressBar.stop();
				}
			}
			else {
				log.log(Level.FINE, () -> "Skipping " + nodeID + " file already exists " + targetFile);
			}
		}
		catch (IOException e) {
			log.log(Level.SEVERE, e, () -> String.format("IOException(%s) from download %s to file %s", e.getMessage(), rssFile.getUrl(), targetFileName));
			targetFile.delete();//hopefully delete an incomplete download
		}
	}


	private String calculateTargetFilenameWithPath(int node, SyndEntry entry, SyndEnclosure file) {
		String targetPath = arguments.djJunkiesDownloadPath + "/" + node + "/";
		final String url = file.getUrl();
		final String filename = new File(url).getName();
		if (filename.endsWith(".mp3") || filename.endsWith(".m4a")) {
			return targetPath + filename;
		}
		String targetFile = targetPath + entry.getTitle();
		if ("audio/mpeg".equals(file.getType())) {
			targetFile += ".mp3";
		}
		else {
			targetFile += ".m4a";
		}
		return targetFile;
	}

}
