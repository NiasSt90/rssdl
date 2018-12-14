package de.a0zero.rssdl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.a0zero.rssdl.dto.JsonDJ;
import de.a0zero.rssdl.dto.JsonLoginResult;
import de.a0zero.rssdl.dto.JsonSetNode;
import de.a0zero.rssdl.dto.OldJsonDJ;
import de.a0zero.rssdl.junkies.JunkiesClient;
import de.a0zero.rssdl.junkies.create.CreateSetNode;
import de.a0zero.rssdl.junkies.create.CreateSetNodeResult;
import io.reactivex.Observable;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
@EnabledIfSystemProperty(named = "username", matches = ".+")
class JunkiesClientTest {

	static {
		MainArguments.verbose = HttpLoggingInterceptor.Level.BODY;
	}
	private JunkiesAPI api = new JunkiesClient().client(new MainArguments().djJunkiesURL).create(JunkiesAPI.class);

	private String username;

	private String password;


	@BeforeEach
	void setUp() {
		username = System.getProperty("username");
		password = System.getProperty("password");
	}


	@Test
	void testLogin() throws IOException {
		JsonLoginResult login = api.login(username, password).blockingFirst();
		Assertions.assertNotNull(login.getSessionName());
	}


	@Test
	void getNode() throws Exception {
		final Response<JsonSetNode> response = api.getNode(171221).execute();
		Assertions.assertEquals(171221, response.body().getNid());
	}


	@Test
	void findDJ() throws IOException {
		final String authToken = api.login(username, password).blockingFirst().getUser().getAuthToken();
		final Observable<List<OldJsonDJ>> eelke = api.findDJ("Eelke Kleijn");
		final List<OldJsonDJ> jsonDJS = eelke.blockingFirst();
		Assertions.assertTrue(jsonDJS.size() > 0);

		final List<Integer> djList = api.modifiedartists(0).blockingFirst();
		Assertions.assertTrue(djList.size() > 0);

		final JsonDJ jsonDJ = api.artistinfo(djList.get(0)).blockingFirst();
		Assertions.assertNotNull(jsonDJ);
	}


	@Test
	void createNode() throws Exception {
		api.login(username, password).blockingFirst().getUser().getAuthToken();

		final CreateSetNode node = new CreateSetNode();
		node.status = 0;
		node.title = "Eelke Kleijn - Mein Test Create";
		node.fulltitle = "Eelke Kleijn - Mein Test Create";
		node.artistnames = "Eelke Kleijn";
		node.artistdetectionDisabled = 0;
		node.created = new Date();
		node.setSetCreated(new SimpleDateFormat("yyyy-MM-dd").parse("2011-04-20"));

		Response<CreateSetNodeResult> response = api.createSet(node).execute();
		Assertions.assertEquals(response.code(), 200);
		int nid = response.body().nid;
		Assertions.assertTrue(nid > 0);

		final Response<Void> execute = api.forcemp3info(nid).execute();
		Assertions.assertTrue(execute.isSuccessful());
	}
}