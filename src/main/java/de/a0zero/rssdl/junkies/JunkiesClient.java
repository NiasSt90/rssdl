package de.a0zero.rssdl.junkies;

import de.a0zero.rssdl.JunkiesAPI;
import de.a0zero.rssdl.MainArguments;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JunkiesClient {

	private static final Logger log = Logger.getLogger(JunkiesClient.class.getName());

	public Retrofit client(String endpointBaseURL) {
		return new Retrofit.Builder()
				.baseUrl(endpointBaseURL)
				.client(new OkHttpClient().newBuilder()
						.addInterceptor(new HttpLoggingInterceptor().setLevel(MainArguments.verbose))
						.readTimeout(2, TimeUnit.MINUTES)
						.callTimeout(2, TimeUnit.MINUTES)
						.cookieJar(new SessionCookieJar())
						.build())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create(ManitobaGsonBuilder.create()))
				.build();
	}

	public JunkiesAPI api(String endpointURL) {
		return client(endpointURL).create(JunkiesAPI.class);
	}

	private static class SessionCookieJar implements CookieJar {

		private List<Cookie> cookies;

		@Override
		public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
			if (url.encodedPath().endsWith("login")) {
				log.log(Level.INFO, "Saving login cookie:" + cookies);
				this.cookies = new ArrayList<>(cookies);
			}
		}


		@Override
		public List<Cookie> loadForRequest(HttpUrl url) {
			if (!url.encodedPath().endsWith("login") && cookies != null) {
				log.log(Level.INFO, "Using login cookie:" + cookies + " for Request " + url);
				return cookies;
			}
			return Collections.emptyList();
		}
	}

}
