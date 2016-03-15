package de.sty.io.mimetype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.sty.io.mimetype.helper.Error;
import de.sty.io.mimetype.helper.Helper;
import de.sty.io.mimetype.helper.Logger;

/**
 * Implements the {@link MimeType} interface.
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: MimeTypeImpl.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
class MimeTypeImpl implements MimeType {
	/** Use Log4j to printout messages. */
	protected static Logger logger = Logger.getLogger(MimeTypeImpl.class);
	/** Shows if we print out debug messages. */
	protected static boolean isDebug = logger.isDebugEnabled();
	
	// ----------------------------------------------------------------------
	// Fields 
	// ----------------------------------------------------------------------

	String name;
	String description;
	String lang;
	String encoding;
	ArrayList supertypes = new ArrayList();
	ArrayList subtypes = new ArrayList();
	ArrayList aliases = new ArrayList();
	
	ArrayList globs = new ArrayList(); 
	ArrayList magics = new ArrayList(); 
	ArrayList xmlrootnodes = new ArrayList();

	// ----------------------------------------------------------------------
	// Constructor 
	// ----------------------------------------------------------------------
	
	/** Create a new empty mimetype. */
	MimeTypeImpl() {
		super();
	}
	/** Create a new mimetype with the given values. */
	MimeTypeImpl(String name, String lang, String encoding) {
		this();
		Error.checkNotNull(name);

		this.name = name;
		this.lang = lang;
		this.encoding = encoding;
		if(name.startsWith("text/") && !name.equals(TEXT_PLAIN_NAME)) {
			addSuperTypes(MimeTypeResolver.getMimeTypeImpl(TEXT_PLAIN_NAME));
		}
		if(!ROOTTYPE_NAME.equals(name)) {
			addSuperTypes(MimeTypeResolver.getMimeTypeImpl(ROOTTYPE_NAME));
		}
	}
	
	// ----------------------------------------------------------------------
	// Create subclasses 
	// ----------------------------------------------------------------------
	
	GlobImpl addGlobImpl(String glob) {
		for (Iterator i = globs.iterator(); i.hasNext();) {
			GlobImpl g = (GlobImpl) i.next();
			if(g.getGlob().equals(glob))
				return g;
		}
		GlobImpl ret = new GlobImpl(glob);
		this.globs.add(ret);
		return ret;
	}

	MagicImpl addMagicImpl(int prio) {
		MagicImpl ret = new MagicImpl(prio);
		this.magics.add(ret);
		return ret;
	}

	XMLRootNodeImpl addXMLRootNodeImpl(String nodename, String namespace) {
		for (Iterator i = xmlrootnodes.iterator(); i.hasNext();) {
			XMLRootNodeImpl x = (XMLRootNodeImpl) i.next();
			Error.checkNotNull(x, "XMLRootNode must not be null.");
			String s = x.getLocalName();
			if((s == null && nodename == null) || (s != null && s.equals(nodename))) {
				s = x.getNamespace();
				if((s==null && namespace == null) || (s != null && s.equals(namespace)))
					return x;
			}
		}
		XMLRootNodeImpl ret = new  XMLRootNodeImpl(nodename, namespace);
		this.xmlrootnodes.add(ret);
		return ret;
	}
	
	String addAlias(String alias) {
		if(!aliases.contains(alias))
			aliases.add(alias);
		return alias;
	}
	
	MimeTypeImpl addSuperTypes(MimeTypeImpl sT) {
		if(!supertypes.contains(sT))
			supertypes.add(sT);
		if(!sT.subtypes.contains(this))
			sT.subtypes.add(this);
		return sT;
	}

	// ----------------------------------------------------------------------
	// Getter and setter 
	// ----------------------------------------------------------------------
	
	/** @see MimeType#getDescription() */
	public String getDescription() {
		return this.description;
	}

	/** @see MimeType#getEncoding() */
	public String getEncoding() {
		return encoding;
	}

	/** @see MimeType#getLang() */
	public String getLang() {
		return this.lang;
	}

	/** @see MimeType#getName() */
	public String getName() {
		return this.name;
	}

	/** @see MimeType#getAliases() */
	public Collection getAliases() {
		return this.aliases;
	}

	/** @see MimeType#getSubTypes() */
	public Collection getSubTypes() {
		return this.subtypes;
	}

	/** @see MimeType#getSuperTypes() */
	public Collection getSuperTypes() {
		return this.supertypes;
	}
	
	// ----------------------------------------------------------------------
	// Glob 
	// ----------------------------------------------------------------------

	/** @see MimeType#getGlobs() */
	public Collection getGlobs() {
		return this.globs;
	}

	// ----------------------------------------------------------------------
	// Magic
	// ----------------------------------------------------------------------

	/** @see MimeType#getMagics() */
	public Collection getMagics() {
		return this.magics;
	}
	
	// ----------------------------------------------------------------------
	// XMLRoot 
	// ----------------------------------------------------------------------

	/** @see MimeType#getXMLRootNodes() */
	public Collection getXMLRootNodes() {
		return this.xmlrootnodes;
	}
	
	// ----------------------------------------------------------------------
	// Convenience methods 
	// ----------------------------------------------------------------------
	
	/** @see MimeType#listAllTypes() */
	public List listAllTypes() {
		ArrayList types = new ArrayList();
		this.collectSuperTypes(types);
		return types;
	}

	/** @see MimeType#listAllTypes() */
	protected List collectSuperTypes(List types) {
		if(!types.contains(this))
			types.add(this);
		for (Iterator iST = supertypes.iterator(); iST.hasNext();) {
			MimeTypeImpl mt = (MimeTypeImpl) iST.next();
			if(!types.contains(mt))
				mt.collectSuperTypes(types);
		}
		for (Iterator iA = aliases.iterator(); iA.hasNext();) {
			String alias = (String) iA.next();
			MimeTypeImpl mt = MimeTypeResolver.getMimeTypeImpl(alias);
			if(!types.contains(mt))
				mt.collectSuperTypes(types);
		}
		return types;
	}

	// ----------------------------------------------------------------------
	// Comparable
	// ----------------------------------------------------------------------

	/** @see Comparable#compareTo(Object) */
	public int compareTo(Object o) {
		int ret = -1;
		if(this == o)
			ret = 0;
		else if (o instanceof MimeType) {
			MimeType m = (MimeType) o;
			if(m.getSuperTypes() != null && m.getSuperTypes().contains(this))
				ret = 1;
			else {
				ret = Helper.compare(getName(), m.getName());
				if(ret == 0) {
					ret = Helper.compare(getDescription(), m.getDescription());
					if(ret == 0) {
						ret = Helper.compare(getLang(), m.getLang());
						if(ret == 0) {
							ret = Helper.compare(getEncoding(), m.getEncoding());
						}
					}
				}
			}
		}
		return ret;
	}

	// ----------------------------------------------------------------------
	// Object
	// ----------------------------------------------------------------------

	public String toString() {
		StringBuffer ret = new StringBuffer("mime-type:").append(name);
		ret.append(';').append(lang).append(';').append(encoding);
		ret.append(':').append(description);
//		ret.append(':');
//
//		for (Iterator i = globs.iterator(); i.hasNext();) {
//			Glob g = (Glob) i.next();
//			ret.append(g);
//		}
//		for (Iterator i = magics.iterator(); i.hasNext();) {
//			Magic m = (Magic) i.next();
//			ret.append(m);
//		}
//		for (Iterator i = aliases.iterator(); i.hasNext();) {
//			String a = (String) i.next();
//			ret.append("<alias type=\"").append(a).append("\"/>");
//		}
//		for (Iterator i = subtypes.iterator(); i.hasNext();) {
//			MimeType a = (MimeType) i.next();
//			ret.append("<sub-class-of type=\"").append(a.getName()).append("\"/>");
//		}
//		for (Iterator i = xmlrootnodes.iterator(); i.hasNext();) {
//			XMLRootNode x = (XMLRootNode) i.next();
//			ret.append(x);
//		}
//		ret.append('\n');
		return ret.toString();
	}

	/** @see Object#equals(Object) */
	public boolean equals(Object o) {
		boolean ret = (this == o);
		if (!ret && o instanceof MimeTypeImpl) {
			MimeTypeImpl m = (MimeTypeImpl) o;
			ret = Helper.eq(name, m.name)
				&& Helper.eq(description, m.description)
				&& Helper.eq(lang, m.lang) 
				&& Helper.eq(encoding, m.encoding)
				&& Helper.equals(supertypes, m.supertypes)
				&& Helper.equals(subtypes, m.subtypes)
				&& Helper.equals(aliases, m.aliases)
				&& Helper.equals(globs, m.globs)
				&& Helper.equals(magics, m.magics)
				&& Helper.equals(xmlrootnodes, m.xmlrootnodes);
		}
		return ret;
	}

}
