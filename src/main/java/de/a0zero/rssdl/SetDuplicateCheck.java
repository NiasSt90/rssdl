package de.a0zero.rssdl;

/**
 * User: Markus Schulz <msc@0zero.de>
 */
public interface SetDuplicateCheck {

	boolean alreadyExists(String guid, String title);

	Integer getNodeId(String guid);

	void addEntry(String uri, String title, int nid);
}
