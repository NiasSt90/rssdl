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
		if (url.getProtocol().startsWith("http")) {
			final Response response = new Progress().run(url, null);
			if (response.isSuccessful() && response.body() != null) {
				final InputStream source = response.body().byteStream();
				final File file = File.createTempFile("rssdl_", type);
				file.deleteOnExit();
				FileUtils.copyInputStreamToFile(source, file);
				response.close();
				return file;
			}
		}
		return new File(url.getFile());
	}


	@Override
	public void downloadSet(int nodeID, SyndEntry entry, SyndEnclosure file) {
		String targetFileName = calculateTargetFilenameWithPath(nodeID, entry, file);
		try {
			final File targetFile = new File(targetFileName);
			if (!targetFile.exists() || targetFile.length() != file.getLength()) {
				log.log(Level.INFO, () -> "Start downloading " + file.getUrl());
				ProgressBar progressBar = null;
				if (!MainArguments.quiet && arguments.downloadParallel == 1) {
					progressBar = new ProgressBar("" + nodeID, file.getLength(), ProgressBarStyle.UNICODE_BLOCK).start();
				}
				final Response response = new Progress().run(new URL(file.getUrl()), progressBar);
				if (response.isSuccessful() && response.body() != null) {
					final InputStream source = response.body().byteStream();
					FileUtils.copyInputStreamToFile(source, targetFile);
					response.close();
				}
				if (progressBar != null) {
					progressBar.stop();
				}
			}
			else {
				log.log(Level.FINE, () -> "Skipping " + nodeID + " file exists with same size " + targetFile);
			}
		}
		catch (IOException e) {
			log.log(Level.SEVERE, e, () -> String.format("IOException from download %s to file %s", file.getUrl(), targetFileName));
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
