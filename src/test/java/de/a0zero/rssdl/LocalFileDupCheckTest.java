package de.a0zero.rssdl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
class LocalFileDupCheckTest {

	@Test
	void insert() {

		LocalFileDupCheck localFileDupCheck = new LocalFileDupCheck("target.properties");
		localFileDupCheck.addEntry("tag:soundcloud,2010:tracks/542456055", "Bla", 172796);
		localFileDupCheck.save("target.properties");

		localFileDupCheck = new LocalFileDupCheck("target.properties");
		Assertions.assertTrue(localFileDupCheck.alreadyExists("tag:soundcloud,2010:tracks/542456055", "bla"));
	}
}