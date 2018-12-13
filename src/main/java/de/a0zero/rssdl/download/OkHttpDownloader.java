package de.a0zero.rssdl.download;

import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import de.a0zero.rssdl.FileDownloader;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class OkHttpDownloader implements FileDownloader {

	public File download(URL url, String type) throws IOException {
		if (url.getProtocol().startsWith("http")) {
			final Response response = new Progress().run(url, null);
			if (response.isSuccessful() && response.body() != null) {
				final InputStream source = response.body().byteStream();
				final File file = File.createTempFile("rssdl_", type);
				FileUtils.copyInputStreamToFile(source, file);
				response.close();
				return file;
			}
		}
		return new File(url.getFile());
	}


	@Override
	public void downloadSet(int nodeID, SyndEntry entry, SyndEnclosure file) {
		try {
			String targetFileName = calculateTargetFilenameWithPath(nodeID, entry, file);
			final File targetFile = new File(targetFileName);
			if (!targetFile.exists() || targetFile.length() != file.getLength()) {
				System.out.println("Try downloading " + file.getUrl());
				final ProgressBar progressBar =
						new ProgressBar("" + nodeID, file.getLength(), ProgressBarStyle.UNICODE_BLOCK);
				progressBar.start();
				final Response response = new Progress().run(new URL(file.getUrl()), progressBar);
				if (response.isSuccessful() && response.body() != null) {
					final InputStream source = response.body().byteStream();
					FileUtils.copyInputStreamToFile(source, targetFile);
					response.close();
				}
				progressBar.stop();
			}
			else {
				System.out.println("Skipping " + nodeID + " file exists with same size " + targetFile);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	private String calculateTargetFilenameWithPath(int node, SyndEntry entry, SyndEnclosure file) {
		String targetPath = node + "/";
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
