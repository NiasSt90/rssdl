package de.a0zero.rssdl;

import de.a0zero.rssdl.dto.JsonDJ;
import de.a0zero.rssdl.dto.JsonLoginResult;
import de.a0zero.rssdl.dto.JsonSetNode;
import de.a0zero.rssdl.dto.OldJsonDJ;
import de.a0zero.rssdl.junkies.JunkiesClient;
import io.reactivex.Observable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
@EnabledIfSystemProperty(named = "username", matches = ".+")
class JunkiesClientTest {

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
		final String authToken = api.login(username, password).blockingFirst().getUser().getAuthToken();

		final JsonSetNode jsonSetNode = new JsonSetNode(0);
		jsonSetNode.setStatus(0);
		jsonSetNode.setTitle("Mein Test Create");
		JsonDJ dj = new JsonDJ(56151, "michael canitrot");
		dj.setNid(130568);
		jsonSetNode.setDj(dj);
		jsonSetNode.setDuration(100);
		jsonSetNode.setCreated(new Date());
		jsonSetNode.setSetcreated(new Date());
		jsonSetNode.setArtistnids(Collections.singletonList(130568));

		final Response<JsonSetNode> response = api.createSet(jsonSetNode).execute();
		Assertions.assertEquals(response.code(), 200);

		final Response<Void> execute = api.forcemp3info(response.body().getNid()).execute();
		Assertions.assertTrue(execute.isSuccessful());

	}
}