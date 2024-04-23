package com.rometools.modules.itunes.io;

import org.jdom2.Namespace;


/**
 *	HACK: Es gibt Feeds die tauschen in der ITunes-xmlns URI "http" gegen "https" (was falsch ist....)
 *	Das hier ist ein einfacher Hack/Workaround, damit wir trotzdem an die Daten (Duration) kommen.
 */
public class ITunesParserHttps extends ITunesParser {



	public ITunesParserHttps() {
		this.ns = Namespace.getNamespace("https://www.itunes.com/dtds/podcast-1.0.dtd");
	}


	@Override
	public String getNamespaceUri() {
		return "https://www.itunes.com/dtds/podcast-1.0.dtd";
	}
}
