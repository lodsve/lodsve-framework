package de.sty.io.mimetype;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.sty.io.mimetype.helper.ByteHelper;
import de.sty.io.mimetype.helper.Error;
import de.sty.io.mimetype.helper.Logger;

/**
 * Parses the fd.o-XML File with mimetype definitions.
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: FreeDesktopMimeTypeXmlParser.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
class FreeDesktopMimeTypeXmlParser {
	/** Use Log4j to printout messages. */
	protected static Logger logger = Logger.getLogger(FreeDesktopMimeTypeXmlParser.class);
	/** Shows if we print out debug messages. */
	protected static boolean isDebug = logger.isDebugEnabled();

	// ----------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------

	/** The namespace uri of xml definitions of mimetypes. */
	public static final String FDODEF_NS =
		"http://www.freedesktop.org/standards/shared-mime-info";
	/** The root node name for xml definitions of mimetypes. */
	public static final String FDODEF_ROOTNODE = "mime-info";

	/** The local resource name of the xml definition from freedesktop.org. */
	public static final String RES_FDODEF = "freedesktop.org.xml";
	
	/** The endianess of this machine. */
	public static final boolean BIGENDIAN = "big".equalsIgnoreCase(System.getProperty("sun.cpu.endian"));

	// ----------------------------------------------------------------------
	// static fields
	// ----------------------------------------------------------------------

	protected static boolean isInitialized = false;

	// ----------------------------------------------------------------------
	// static methods
	// ----------------------------------------------------------------------

	/** Read mimetype definitions from a XML file in the freedesktop.org format
	 * @param defUrl the url to open and read the xml file from. 
	 * @return A collection of {@link MimeTypeImpl} */
	public static Collection readMimeTypes(URL defUrl)
		throws ParserConfigurationException, SAXException, IOException {
		// The collection of all mimetypes
		Collection mimetypes = new ArrayList();

		// the user locale
		String locale = Locale.getDefault().toString();
		String[] locales = { locale, Locale.getDefault().getLanguage()};

		// read xml-file with definitions
		DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = b.parse(defUrl.toString());

		// check for mimetype definition file
		Element root = doc.getDocumentElement();
		String rootname = root.getNodeName();
		if (!FDODEF_ROOTNODE.equals(rootname)) {
			logger.warn("Mimetype-definitions from url " + defUrl + " have wrong root node:<"
					+ rootname + ">. Expected name:<" + FDODEF_ROOTNODE + ">. Aborting.");
			return null;
		}
		String rootns = root.getNamespaceURI();
		if (rootns == null || "".equals(rootns))
			logger.warn("Mimetype-definitions from url " + defUrl + " have no namespace! Trying to read anyway.");
		else if (!FDODEF_NS.equals(rootns)) {
			logger.error("Mimetype-definitions from url " + defUrl + " have wrong namespace:"
					+ rootns + ". Expected namespace:" + FDODEF_NS + ". Aborting.");
			return null;
		}

		// iterate over all nodes, collect definitions, create MimeType objs
		NodeList entries = root.getChildNodes();
		for (int iH = 0; iH < entries.getLength(); iH++) {
			Node entry = entries.item(iH);
			String nodename = entry.getNodeName();
			// ignore other nodes
			if ("mime-type".equals(nodename)) {
				// found mimetype entry
				NamedNodeMap attrs = entry.getAttributes();

				// get mimetype
				Node nMType = attrs.getNamedItem("type");
				Error.assertTrue(nMType != null, "mime-type has no type!" + entry);
				String type = nMType.getNodeValue();
				if(type == null || "".equals(type)) {
					logger.warn("Mimetype-definitions from url " + defUrl 
						+ " defines <mime-type> without <type>. Aborting.");
				}

				// Get comment for current locale 
				Node nComment = getChildNodeByLanguage(entry, "comment", locales);
				String comment ;
				if(nComment == null)
					comment = "Mimetype " + type;
				else
					comment = nComment.getFirstChild().getNodeValue();

				// get simple types
				MimeTypeImpl mt = MimeTypeResolver.getMimeTypeImpl(type.trim());
				mimetypes.add(mt);
				mt.description = comment.trim();
									
				// get filename globs
				ArrayList globs = collectElementAttributeValues(entry, "glob", "pattern");
				for (Iterator i = globs.iterator(); i.hasNext();) {
					String g = (String) i.next();
					mt.addGlobImpl(g);
				}
				// get aliases
				ArrayList aliases = collectElementAttributeValues(entry, "alias", "type");
				for (Iterator i = aliases.iterator(); i.hasNext();) {
					String a = (String) i.next();
					mt.addAlias(a);
				}
				// get superclasses
				ArrayList superclasses = collectElementAttributeValues(entry, "sub-class-of", "type");
				for (Iterator i = superclasses.iterator(); i.hasNext();) {
					String s = (String) i.next();
					MimeTypeImpl smt = MimeTypeResolver.getMimeTypeImpl(s);
					mt.addSuperTypes(smt);
				}
				
				Node[] magics = getChildNodes(entry, "magic");
				for (int i = 0; i < magics.length; i++) {
					Node nMagic = magics[i];
					NamedNodeMap mattrs = nMagic.getAttributes();
					Node nPrio = mattrs.getNamedItem("priority");
					int prio = 50;
					if(nPrio != null) {
						String sPrio = nPrio.getNodeValue();
						prio = Integer.parseInt(sPrio);
					}
					Error.assertTrue((prio >= 0 && prio <= 100), 
							"Invalid prio for magic: " + nMagic);
					MagicImpl magic = mt.addMagicImpl(prio);
					readMatches(nMagic, magic);
				}
				
			}
		}

		return mimetypes;
	}

	// ----------------------------------------------------------------------
	// Helper 
	// ----------------------------------------------------------------------

	/** Read values from match nodes. */
	static Collection readMatches(Node nMagic, MagicImpl magic) {
		// get match nodes
		Node[] matches = getChildNodes(nMagic, "match");
		ArrayList ret = new ArrayList(matches.length);
		for (int i = 0; i < matches.length; i++) {
			// get attributes from match
			Node nMatch = matches[i];
			NamedNodeMap attrs = nMatch.getAttributes();
			Node nAttr = attrs.getNamedItem("type");
			Error.assertTrue(nAttr != null, "Match does not contain type!" + nMagic);
			String sType = nAttr.getNodeValue();
			int type = magic.typeToType(sType);

			nAttr = attrs.getNamedItem("offset");
			Error.assertTrue(nAttr != null, "Match does not contain offset!" + nMagic);
			String offset = nAttr.getNodeValue();
			int pos = offset.indexOf(':');
			int offsetStart = 0, offsetEnd = 0;
			if(pos < 0) {
				offsetStart = Integer.parseInt(offset);
				offsetEnd = offsetStart;
			} else {
				offsetStart = Integer.parseInt(offset.substring(0, pos));				
				offsetEnd = Integer.parseInt(offset.substring(pos + 1));
			}
				 
			nAttr = attrs.getNamedItem("value");
			Error.assertTrue(nAttr != null, "Match does not contain value!" + nMagic);
			String sValue = nAttr.getNodeValue();
			byte[] value = ByteHelper.decode(-1, sValue, BIGENDIAN); 
			
			nAttr = attrs.getNamedItem("mask");
			String sMask = null;
			byte[] mask = null;
			if(nAttr != null) {// mask is optional
				sMask = nAttr.getNodeValue();
				mask = ByteHelper.decode(-1, sMask, BIGENDIAN);
			}
			
			MatchImpl match = magic.addMatchImpl(type, offsetStart, offsetEnd, value, mask);
			// read sub matches
			readMatches(nMatch, match);
		}
		
		return ret;
	}

	// ----------------------------------------------------------------------
	// Generic XML helpers 
	// ----------------------------------------------------------------------

	/** Iterate over all elements. Take values for attributes with the given name from elements
	 *  with the given name.
	 * @return A collection of strings with all values from all matching elements/attributes. */ 
	static ArrayList collectElementAttributeValues(Node entry, String element, String attr) { 
		// get filename globs
		ArrayList ret = new ArrayList();
	
		Node[] nGlobs = getChildNodes(entry, element);
		for (int iH2 = 0; nGlobs != null && iH2 < nGlobs.length; iH2++) {
			Node g = nGlobs[iH2];
			if (g.hasAttributes()) {
				Node v = g.getAttributes().getNamedItem(attr);
				if (v != null) {
					String value = v.getNodeValue();
					if(value != null && ! "".equals(value)) {
						ret.add(value);
					}
				}
			}
		}
		return ret;
	}

	/** @return the first node with a xml:lang value contained in locales
	 * 		or (if no node was found) the first node without any xml:lang attribute. */
	public static Node getChildNodeByLanguage(Node n, String tagname, String[] locales) {
		Node ret = null;
		ret = getChildNodeByAttrValues(n, tagname, "xml:lang", locales);
		if (ret == null)
			ret = getChildNode(n, tagname);
		return ret;
	}

	/** @return The first tag with some name matching some attribute value. */
	public static Node getChildNodeByAttrValues(
		Node n,
		String tagname,
		String attrname,
		String[] attrvalues) {
		Node ret = null;
		for (int iH = 0; ret == null && iH < attrvalues.length; iH++) {
			ret = getChildNodeByAttrValue(n, tagname, attrname, attrvalues[iH]);
		}
		return ret;
	}
	/** @return the first tag with some name matching a special attribute value. */
	public static Node getChildNodeByAttrValue(
		Node n,
		String tagname,
		String attrname,
		String attrvalue) {
		Node ret = null;
		NodeList children = n.getChildNodes();
		for (int iH = 0; iH < children.getLength(); iH++) {
			Node child = children.item(iH);
			String cName = child.getNodeName();
			if (tagname.equals(cName)) {
				// tagname matches, now check attribute
				NamedNodeMap cAttrs = child.getAttributes();
				Node cAttr = cAttrs.getNamedItem(attrname);
				if (cAttr != null) {
					// attribute found, check value
					String cAttrVal = cAttr.getNodeValue();
					if (attrvalue.equals(cAttrVal)) {
						ret = child;
						break;
					}
				}
			}
		}
		return ret;
	}
	/** @return the first tag with the given name. */
	public static Node getChildNode(Node n, String tagname) {
		Node ret = null;
		NodeList children = n.getChildNodes();
		for (int iH = 0; iH < children.getLength(); iH++) {
			Node child = children.item(iH);
			String cName = child.getNodeName();
			if (tagname.equals(cName)) {
				ret = child;
				break;
			}
		}
		return ret;
	}

	/** @return all tags with the given name. */
	public static Node[] getChildNodes(Node n, String tagname) {
		Node[] ret = null;
		ArrayList list = new ArrayList();
		NodeList children = n.getChildNodes();
		for (int iH = 0; iH < children.getLength(); iH++) {
			Node child = children.item(iH);
			String cName = child.getNodeName();
			if (tagname.equals(cName)) {
				list.add(child);
			}
		}
		ret = (Node[]) list.toArray(new Node[list.size()]);
		return ret;
	}

}
