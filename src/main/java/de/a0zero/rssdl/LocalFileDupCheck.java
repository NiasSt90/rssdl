package de.a0zero.rssdl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * User: Markus Schulz <msc@0zero.de>
 */
public class LocalFileDupCheck implements SetDuplicateCheck {

	private static final Logger log = Logger.getLogger(LocalFileDupCheck.class.getName());

	private final Properties db;


	public LocalFileDupCheck(String filename) {
		db = new Properties();
		File file = new File(filename);
		if (file.exists()) {
			try {
				db.load(new FileReader(file));
			}
			catch (IOException e) {
				log.log(Level.SEVERE, e, () -> String.format("Can't load Duplicate-DB %s!", filename));
			}
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> save(filename)));
	}


	public void save(String filename) {
		try (Writer writer = new FileWriter(filename, false)) {
			db.store(writer, "Save new DB");
		}
		catch (IOException e) {
			log.log(Level.SEVERE, e, () -> String.format("Can't save Duplicate-DB to %s!", filename));
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
