package de.a0zero.rssdl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class LocalFileDupCheck implements SetDuplicateCheck {

	private final Properties db;


	public LocalFileDupCheck(String filename) {
		db = new Properties();
		File file = new File(filename);
		if (file.exists()) {
			try {
				db.load(new FileReader(file));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> save(filename)));
	}


	public void save(String filename) {
		try (Writer writer = new FileWriter(filename, false)) {
			db.store(writer, "Save new DB");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public boolean alreadyExists(String guid, String title) {
		return db.get(guid) != null;
	}


	@Override
	public Integer getNodeId(String guid) {
		final Object o = db.get(guid);
		if (o == null) return null;
		return Integer.parseInt((String) o);
	}


	@Override
	public void addEntry(String uri, String title, int nid) {
		db.put(uri, ""+nid);
	}
}
