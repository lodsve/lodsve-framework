package de.sty.io.mimetype;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.sty.io.mimetype.helper.Helper;

/**
 * An implementation of {@link MimeType.XMLRootNode}.
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: XMLRootNodeImpl.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
class XMLRootNodeImpl implements MimeType.XMLRootNode {

	/** The localname of the root node to match. */
	String localname;
	/** The namespace of the document to be used. */
	String namespace;

	// ----------------------------------------------------------------------
	// Constructors
	// ----------------------------------------------------------------------
	
	/** Create a new empty instance. */
	public XMLRootNodeImpl() {
		super();
	}
	/** Create a new instance with the given values. */
	XMLRootNodeImpl(String localname, String namespace) {
		this.localname = localname;
		this.namespace = namespace;
	}
		
	// ----------------------------------------------------------------------
	// Methods
	// ----------------------------------------------------------------------
		
	/** @see MimeType.XMLRootNode#getLocalName() */
	public String getLocalName() {
		return localname;
	}

	/** @see MimeType.XMLRootNode#getNamespace() */
	public String getNamespace() {
		return namespace;
	}

	/** @see MimeType.XMLRootNode#matches(Document) */
	public boolean matches(Document doc) {
		Node root = doc.getDocumentElement();
		String doclocalname = root.getLocalName();
		String docnamespace = root.getNamespaceURI();
		return this.matches(doclocalname, docnamespace);
	}

	/** @see MimeType.XMLRootNode#matches(String, String) */
	public boolean matches(String localname, String namespace) {
		boolean ret = true;
		ret = (this.localname == null || this.localname.equals(localname));
		if(ret) {
			// search used namespaces
			ret = (this.namespace != null && this.namespace.equals(namespace));
		}
		return ret;
	}

	// ----------------------------------------------------------------------

	public String toString() {
		StringBuffer ret = new StringBuffer("<xml-Root localName=\"");
		ret.append(localname).append("\" namespaceURI=\"");
		ret.append(namespace).append("\"/>");
		return ret.toString();
	}

	/** @see Object#equals(Object) */
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof MimeTypeImpl) {
			XMLRootNodeImpl xml = (XMLRootNodeImpl) o;
			ret = Helper.eq(localname, xml.localname)
				&& Helper.eq(namespace, xml.namespace);
		}
		return ret;
	}
}
