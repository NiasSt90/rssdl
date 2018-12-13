package de.a0zero.rssdl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import de.a0zero.rssdl.dto.JsonLastFmArtistInfo;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class ManitobaGsonBuilder {

	private static GsonBuilder instance;

	private static Gson gson;

	private synchronized static GsonBuilder instance() {
		if (instance == null) {
			instance = new GsonBuilder()
				.registerTypeAdapter(Date.class, ser)
				.registerTypeAdapter(Calendar.class, calDeser)
				.registerTypeAdapter(Date.class, deser)
				.registerTypeAdapter(JsonLastFmArtistInfo.JsonSimilarDjs.class, similarDjs)
				.setDateFormat("yyyy-MM-dd HH:mm:ss");
			gson = instance.create();
		}
		return instance;
	}

	public static Gson create() {
		if (gson == null) {
			gson = instance().create();
		}
		return gson;
	}

	static JsonSerializer<Date> ser = (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.getTime());

	static JsonDeserializer<Date> deser =
			(json, typeOfT, context) -> json == null ? null : new Date(1000 * json.getAsLong());

	static JsonSerializer<Calendar> calSer = (src, typeOfSrc, context) -> {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return src == null ? null : new JsonPrimitive(sdf.format(src.getTime()));
	};

	static JsonDeserializer<Calendar> calDeser = (json, typeOfT, context) -> {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Calendar result = Calendar.getInstance();
			if (json == null) return null;
			result.setTime(sdf.parse(json.getAsString()));
			return result;
		}
		catch (ParseException e) {
			throw new JsonParseException(e.getMessage());
		}
	};

	static JsonDeserializer<JsonLastFmArtistInfo.JsonSimilarDjs> similarDjs = new JsonDeserializer<JsonLastFmArtistInfo.JsonSimilarDjs>() {
		@Override
		public JsonLastFmArtistInfo.JsonSimilarDjs deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
			if (json instanceof JsonObject) {
				JsonElement element = ((JsonObject) json).get("artist");
				JsonLastFmArtistInfo.JsonSimilarDjs res = new JsonLastFmArtistInfo.JsonSimilarDjs();
				final Gson gson = instance.create();
				if (element instanceof JsonObject) {
					//no list...
					res.artist.add(gson.fromJson(json, JsonLastFmArtistInfo.class));
				}
				else
				if (element instanceof JsonArray) {
					for (JsonElement artist : ((JsonArray) element)) {
						res.artist.add(gson.fromJson(artist, JsonLastFmArtistInfo.class));
					}
				}
				return res;
			}
			return null;
		}
	};


}
