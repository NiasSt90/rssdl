package de.a0zero.rssdl.download;

import java.io.IOException;
import java.net.URL;

import me.tongfei.progressbar.ProgressBar;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class Progress {


	public Response run(URL url, ProgressBar progressBar) throws IOException {
		Request request = new Request.Builder().url(url).build();
		OkHttpClient client = new OkHttpClient.Builder()
				.followRedirects(true)
				.addNetworkInterceptor(chain -> {
					if (progressBar != null) {
						Response originalResponse = chain.proceed(chain.request());
						return originalResponse.newBuilder()
								.body(new ProgressResponseBody(originalResponse.body(), progressBar))
								.build();
					}
					return chain.proceed(chain.request());
				})
				.build();
		return client.newCall(request).execute();
	}

	private static class ProgressResponseBody extends ResponseBody {

		private final ResponseBody responseBody;
		private final ProgressBar progressListener;
		private BufferedSource bufferedSource;

		ProgressResponseBody(ResponseBody responseBody, ProgressBar progressListener) {
			this.responseBody = responseBody;
			this.progressListener = progressListener;
		}

		@Override public MediaType contentType() {
			return responseBody.contentType();
		}

		@Override public long contentLength() {
			return responseBody.contentLength();
		}

		@Override public BufferedSource source() {
			if (bufferedSource == null) {
				bufferedSource = Okio.buffer(source(responseBody.source()));
			}
			return bufferedSource;
		}

		private Source source(Source source) {
			return new ForwardingSource(source) {
				long totalBytesRead = 0L;

				@Override public long read(Buffer sink, long byteCount) throws IOException {
					long bytesRead = super.read(sink, byteCount);
					// read() returns the number of bytes read, or -1 if this source is exhausted.
					totalBytesRead += bytesRead != -1 ? bytesRead : 0;
					progressListener.stepTo(totalBytesRead);
					//progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
					return bytesRead;
				}
			};
		}
	}
}
