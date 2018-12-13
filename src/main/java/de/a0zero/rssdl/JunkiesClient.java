package de.a0zero.rssdl;

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


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class JunkiesClient {

	public static final String BASE_URL = "https://test.dj-junkies.de/";


	Retrofit client() {
		return new Retrofit.Builder()
				.baseUrl(BASE_URL)
				.client(new OkHttpClient().newBuilder()
						.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
						.readTimeout(2, TimeUnit.MINUTES)
						.callTimeout(2, TimeUnit.MINUTES)
						.cookieJar(new SessionCookieJar())
						.build())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create(ManitobaGsonBuilder.create()))
				.build();
	}

	public JunkiesAPI api() {
		return client().create(JunkiesAPI.class);
	}

	private static class SessionCookieJar implements CookieJar {

		private List<Cookie> cookies;

		@Override
		public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
			if (url.encodedPath().endsWith("login")) {
				this.cookies = new ArrayList<>(cookies);
			}
		}


		@Override
		public List<Cookie> loadForRequest(HttpUrl url) {
			if (!url.encodedPath().endsWith("login") && cookies != null) {
				return cookies;
			}
			return Collections.emptyList();
		}
	}

}
