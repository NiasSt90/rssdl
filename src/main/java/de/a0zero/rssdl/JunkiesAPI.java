package de.a0zero.rssdl;

import com.google.gson.JsonObject;
import de.a0zero.rssdl.dto.JsonDJ;
import de.a0zero.rssdl.dto.JsonLoginResult;
import de.a0zero.rssdl.dto.JsonSetNode;
import de.a0zero.rssdl.dto.OldJsonDJ;
import de.a0zero.rssdl.junkies.create.CreateSetNode;
import de.a0zero.rssdl.junkies.create.CreateSetNodeResult;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public interface JunkiesAPI {

	@FormUrlEncoded
	@POST("js-api/user/login")
	Observable<JsonLoginResult> login(@Field("username") String username, @Field("password") String password);

	@GET("js-api/mischungxl/{nid}")
	Call<JsonSetNode> getNode(@Path("nid") int nid);

	@POST("js-api/node")
	Call<CreateSetNodeResult> createSet(@Body CreateSetNode set);


	//ACHTUNG:
	@PUT("js-api/node/{nid}")
	Call<JsonObject> rawUpdate(@Path("nid") int nid, @Body JsonObject set);
	@GET("js-api/node/{nid}")
	Call<JsonObject> rawLoad(@Path("nid") int nid);



	//ACHTUNG: kein REST-API Call...
	@GET("node/{nid}?forcemp3info")
	Call<Void> forcemp3info(@Path("nid") int nid /*, @Query("token") String authToken*/);



	@POST("js-api/mischungxl/djs")
	Observable<List<OldJsonDJ>> findDJ(/*@Query("token") String authToken, */ @Query(value = "s", encoded = true) String searchString);

	@POST("js-api/mischungxl/modifiedartists")
	Observable<List<Integer>> modifiedartists(@Query("since") int epoch);

	@POST("js-api/mischungxl/artistinfo")
	Observable<JsonDJ> artistinfo(/*@Query("token") String authToken, */ @Query(value = "id") int djID);


}
