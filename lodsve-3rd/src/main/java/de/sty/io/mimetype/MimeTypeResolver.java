package de.sty.io.mimetype;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.sty.io.mimetype.helper.Error;
import de.sty.io.mimetype.helper.Helper;
import de.sty.io.mimetype.helper.Logger;

/**
 * Resolves mimetypes for URIs, files and streams.
 * 
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: MimeTypeResolver.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
public class MimeTypeResolver {
	/** Use logger to printout messages. */
	protected static Logger logger = Logger.getLogger(MimeTypeResolver.class);
	/** Shows if we print out debug messages. */
	protected static boolean isDebug = logger.isDebugEnabled();
	
	// ----------------------------------------------------------------------
	// Constants
	// ----------------------------------------------------------------------
	
	/** The name of this class. */
	public static final String CLASSNAME = MimeTypeResolver.class.getName();
	/** Set this system property to recreate the mimetype definitions
	 * (and ignore the propably existing serialized definitions).	 */
	public static final String PROP_RECREATE = "de.sty.io.mimetype.RecreateDefinitions";
	
	/** The directory where to store the serialized mimetype definitions. */
	protected static final File RES_DIR = Helper.getStorageDir();
	/** The file where to store the serialized mimetype definitions. */
	protected static final String RES_MIMEINFO = "MimeInfo.ser";
	/** The version string. */
	protected static final String SER_VERSION = "MimeInfo $Revision: 1.1 $";
	
	protected static final File dataDir = new File(RES_DIR, CLASSNAME.replace('.', '/'));
	protected static final File serFile = new File(dataDir, RES_MIMEINFO);
	
	
	// ----------------------------------------------------------------------
	// static fields
	// ----------------------------------------------------------------------
	
	/** Contains end patterns (<code>*.txt</code>). */
	protected static Map endPatterns = new HashMap();
	
	/** Contains start patterns (<code>makefile*</code>). */
	protected static Map startPatterns = new HashMap();
	
	/** Contains literal patterns (<code>Makefile</code>). */
	protected static Map literalPatterns = new HashMap();
	
	/** Contains shell patterns (<code>*.*gf</code>). */
	protected static Map shellPatterns = new HashMap();
	
	/** The size of the read-buffer to be able to match all 
	 * matches against that buffer without rereading. */
	protected static int longestMatch = -1;
	
	// ----------------------------------------------------------------------
	// Static fields
	// ----------------------------------------------------------------------
	
	/** A mapping from mimetype name to MimeTypeImpl. */
	protected static HashMap mimetypes = new HashMap(); 
	
	// ----------------------------------------------------------------------
	// Initialization
	// ----------------------------------------------------------------------
	
	/** Initialize this resolver. Read mimetype defintions and fill internal tables. */	
	static {
		boolean isInitialized = loadDefinitions();
		
		// if reading did not succeed, rebuild internal data structures,
		// serialize and write them to local cache file
		if(!isInitialized) {
			// reread definition
			rebuildDefinitions();
			// and save them
			saveDefinitions();
		}
		isInitialized = true;
	}
	
	/** Adds a new mapping from <code>sPattern</code> 
	 * to <code>sMimeType</code>. Used in initializer.
	 * @param sPattern The pattern ("*.txt")
	 * @param mt The mimetype as string ("text/plain"). */
	protected synchronized static void addMapping(String sPattern, MimeType mt) {
		Error.checkNotNullMult(sPattern, mt);
		// into which patternset?
		int iQ = sPattern.indexOf('?');
		int iL = sPattern.indexOf('[');
		int iR = sPattern.indexOf(']');
		int iA = sPattern.indexOf('*');
		
		if (iQ < 0 && iL < 0 && iR < 0) {
			if (iA < 0) {
				// found pattern with literal filename
				// if(isDebug) logger.debug("Read filename pattern: " + sPattern + "=" + mt);
				literalPatterns.put(sPattern, mt.getName());
			} else {
				if (iA == 0 && sPattern.lastIndexOf('*') == 0) {
					// found end pattern
					// cut * 
					String sH = sPattern.substring(1);
					endPatterns.put(sH, mt.getName());
				} else if (sPattern.endsWith("*") && iA == sPattern.length() - 1) {
					// found start pattern
					// cut *
					String sH = sPattern.substring(0, sPattern.length() - 1);
					startPatterns.put(sH, mt.getName());
				} else {
					// found shell pattern
					// build proper regex
					String sH = Helper.convertGlobPattern2Regex(sPattern);
					if(isDebug) 
						logger.debug("Found shell pattern:" + sPattern 
								+ " changed to internal regex:" + sH);
					shellPatterns.put(sH, mt.getName());
				}
			}
		} else {
			// found shell pattern
			// build proper regex
			String sH = Helper.convertGlobPattern2Regex(sPattern);
			if(isDebug) 
				logger.debug("Found shell pattern:" + sPattern 
						+ " changed to internal regex:" + sH);
			shellPatterns.put(sH, mt.getName());
		}
	}
	
	// ----------------------------------------------------------------------
	// Generation methods 
	// ----------------------------------------------------------------------
	
	/** Lookup a mimetype. */	
	public static MimeType getMimeType(String name) {
		Error.checkNotNull(name);
		return getMimeTypeImpl(name, null, null, null, null, null, null, null, null);
	}
	/** Create a new mimetype with the given details. */
	public static MimeType getMimeType(String name, String lang, String encoding) {
		Error.checkNotNull(name);
		return getMimeTypeImpl(name, encoding, lang, null, null, null, null, null, null);
	}
	/** Create a new mimetype with the given details. */
	public static MimeType getMimeType(String name, String lang, String encoding, 
			String description,
			Collection supertypes, Collection aliases,
			Collection globs, Collection magics, Collection xmlrootnodes) {
		Error.checkNotNull(name);
		return getMimeTypeImpl(name, encoding, lang, description, supertypes, aliases, globs, magics, xmlrootnodes);
	}
	
	/** Lookup a mimetype. */	
	static MimeTypeImpl getMimeTypeImpl(String name) {
		Error.checkNotNull(name);
		return getMimeTypeImpl(name, null, null, null, null, null, null, null, null);
	}
	/** Create a new mimetype with the given details. */
	static MimeTypeImpl getMimeTypeImpl(String name, String lang, String encoding, 
			String description,
			Collection supertypes, Collection aliases,
			Collection globs, Collection magics, Collection xmlrootnodes) {
		Error.checkNotNull(name);
		
		// create mimetype only once
		MimeTypeImpl mimet;
		if(mimetypes.containsKey(name))
			mimet = (MimeTypeImpl) mimetypes.get(name);
		else {
			mimet = new MimeTypeImpl(name, lang, encoding);
			mimetypes.put(name, mimet);
		}
		// use given attributes, even for already created mimetypes
		if(mimet.description == null)
			mimet.description = description;
		// Use elements from given collections, 
		// ignore if any collection is null 
		Helper.addAll(supertypes, mimet.supertypes, MimeTypeImpl.class);
		Helper.addAll(aliases, mimet.aliases, MimeTypeImpl.class);
		Helper.addAll(globs, mimet.globs, GlobImpl.class);
		Helper.addAll(magics, mimet.magics, MagicImpl.class);
		Helper.addAll(xmlrootnodes, mimet.xmlrootnodes, XMLRootNodeImpl.class);
		if(false && isDebug)
			logger.debug("getMimeTypeImpl(" + name + ", ...): return " 
					+ mimet.hashCode());
		return mimet;
	}
	
	// ----------------------------------------------------------------------
	// Getter and setter
	// ----------------------------------------------------------------------
	
	/** Try to read serialized definitions from local file. 
	 * @return <code>true</code> on success. */ 
	public static boolean loadDefinitions() {
		if (isDebug) logger.debug("loadDefinitions(): enter");
		boolean ret = false;
		if((System.getProperty(PROP_RECREATE)!=null)) {
			logger.info("Not loading old definitions: system property '" 
					+ PROP_RECREATE + "' is set.");
		} else {
			// try to read serialized mime defs from cache file in local data dir
			if(!serFile.exists()) {
				logger.info("Not loading old definitions: file '" + serFile 
						+ "' does not exist.");
			} else {
				ObjectInputStream ois = null;
				try {
					if(isDebug) logger.debug("Read serialized mimetype definitons.");
					ois = new ObjectInputStream(
							Helper.openIn(serFile));
					if(SER_VERSION.equals(ois.readObject())) {
						mimetypes = (HashMap) ois.readObject();
						longestMatch = ois.readInt(); 
						
						literalPatterns = (Map) ois.readObject();
						endPatterns = (Map) ois.readObject();
						startPatterns = (Map) ois.readObject();
						shellPatterns = (Map) ois.readObject();
						
						if(logger.isInfoEnabled()) 
							logger.info("Read serialized mimetype definitions: "
									+ mimetypes.size() + " known mimetypes, "
									+ longestMatch + " bytes to read for the longest match, "
									+ literalPatterns.size() + " literal globs, "
									+ endPatterns.size() + " end globs, "
									+ startPatterns.size() + " start globs, "
									+ shellPatterns.size() + " shell globs.");
						ret = true;
					} else {
						logger.info("Cannot use serialized mimetype definitions, version mismatch.");
						ret = false;
					}
				} catch (Exception e) {
					logger.info("Cannot use serialized mimetype definitions, exception caught: " + e);
					ret = false;
				} finally {
					Helper.close(ois);
				}
				if(!ret && serFile.exists()) {
					logger.info("Delete unreadable file " + serFile);
					logger.info("Can delete file: " + serFile.delete());
				}
			}
		}
		if (isDebug) logger.debug("loadDefinitions(): return " + ret);
		return ret;
	}
	
	/** Reread mimetype definitions. */
	public static void rebuildDefinitions() {
		if (isDebug) logger.debug("rebuildDefinitions(): enter");
		logger.info("Recreate mimetype definitons...");
		// read definitons from xml file and build internal tables
		URL src = FreeDesktopMimeTypeXmlParser.class.getResource(
				FreeDesktopMimeTypeXmlParser.RES_FDODEF);
		Error.assertNotNull(src, "Resource not found: " 
				+ FreeDesktopMimeTypeXmlParser.RES_FDODEF);
		if (isDebug) logger.debug("Read mimetype definitions from " + src);
		Collection defs = null;
		try {
			defs = FreeDesktopMimeTypeXmlParser.readMimeTypes(src);
		} catch (Exception e) {
			logger.error("Unexpected Exception caught while reading configuration file " 
					+ src, e);
			Error.errorExit("Error while reading resource " 
					+ FreeDesktopMimeTypeXmlParser.RES_FDODEF
					+ " from URL " + src 
					+ ":" + e.getLocalizedMessage(), 
					e);
		}
		if (isDebug) logger.debug("Build internal tables.");
		
		// collect globs and add pattern mappings 
		for (Iterator im = defs.iterator(); im.hasNext();) {
			MimeTypeImpl mt = (MimeTypeImpl) im.next();
			for (Iterator ig = mt.getGlobs().iterator(); ig.hasNext();) {
				MimeType.Glob glob = (MimeType.Glob) ig.next();
				addMapping(glob.getGlob(), mt);
			}
		}
		// Get longest match
		for (Iterator iM = mimetypes.values().iterator(); iM.hasNext();) {
			MimeTypeImpl mt = (MimeTypeImpl) iM.next();
			for (Iterator iMa = mt.getMagics().iterator(); iMa.hasNext();) {
				MagicImpl magic = (MagicImpl) iMa.next();
				int ml = magic.matchLength() ;
				longestMatch = (longestMatch > ml ? longestMatch : ml);
			}
		}
		if (isDebug) logger.debug("rebuildDefinitions(): exit ");
	}
	
	protected static void saveDefinitions() {
		if (isDebug) logger.debug("saveDefinitions(): enter");
		// try to store serialized state of mimetype definitions
		logger.info("Write out generated serialized mimetype definitons.");
		ObjectOutputStream oos = null;
		try {
			if(!dataDir.exists()) dataDir.mkdirs();
			oos = new ObjectOutputStream(Helper.openOut(serFile));
			oos.writeObject(SER_VERSION);
			oos.writeObject(mimetypes);
			oos.writeInt(longestMatch);
			
			oos.writeObject(literalPatterns);
			oos.writeObject(endPatterns);
			oos.writeObject(startPatterns);
			oos.writeObject(shellPatterns);
			
			oos.close();
			
			if(logger.isInfoEnabled()) logger.info("Wrote serialized mimetype definitions: "
					+ mimetypes.size() + " known mimetypes, "
					+ longestMatch + " bytes to read for the longest match, "
					+ literalPatterns.size() + " literal globs, "
					+ endPatterns.size() + " end globs, "
					+ startPatterns.size() + " start globs, "
					+ shellPatterns.size() + " complex globs.");
			
		} catch (Exception e) {
			logger.error("Unexpected Exception caught while writing serialized mimetype definitons", e);
			logger.warn("Maybe deletion of " + serFile.getAbsolutePath() + " helps.");
		} finally {
			Helper.close(oos);
		}
		if (isDebug) logger.debug("saveDefinitions(): exit");
	}
	
	/** Subclasses may want to access the map directly. */
	protected static Map getMimetypes() {
		return mimetypes;
	}
	/** Subclasses may want to access the map directly. */
	protected static void setMimetypes(HashMap mimetypes) {
		if (isDebug) logger.debug("setMimetypes(" + mimetypes + "): enter");
		Error.checkNotNull(mimetypes);
		MimeTypeResolver.mimetypes = mimetypes;
		if (isDebug) logger.debug("setMimetypes(" + mimetypes + "): exit");
	}
	
	// ----------------------------------------------------------------------
	// Resolver methods
	// ----------------------------------------------------------------------
	
	/** Try to resove all mimetypes for <code>file</code>
	 * while using globbings only. 
	 * @see MimeType.Glob
	 * @param filename The filename to match the globbings against
	 * @return A {@link List} of {@link MimeType}s. */
	public static List resolveFromNameOnly(String filename) {
		Error.checkNotNull(filename);
		List ret = new ArrayList();
		ret = determineFromName(filename);
		return ret;
	}
	
	/** Try to resove all mimetypes for <code>file</code>.
	 * @param file The file to determine mimetypes for.
	 * @return A {@link List} of {@link MimeType}s. */
	public static List resolve(File file) {
		Error.checkNotNull(file);
		List ret = new ArrayList();
		InputStream in = null;
		ret = determineFromName(file.getName());
		try {
			if(file.isFile() && file.canRead())
				in = Helper.openIn(file);
			else
				if(isDebug)
					logger.debug("Resolved mimetype of " + file + " by name only. "
							+ "Cannot read.");
		} catch (Exception e) {
			logger.info("Resolved mimetype of " + file + " by name only. "
					+ "Caught exception while trying to read: " + e.getLocalizedMessage());
		}
		if(in != null)
			// determine additional types by reading
			determineFromMagics(ret, in, 
					MimeType.Magic.MAX_PRIO, MimeType.Magic.MIN_PRIO);
		Helper.close(in);
		return ret;
	}
	/** Try to resove all mimetypes for the given stream.
	 * @param in The stream to determine mimetypes for.
	 * @return A {@link List} of {@link MimeType}s. */
	public static List resolve(InputStream in) {
		Error.checkNotNull(in);
		List ret = new ArrayList();
		determineFromMagics(ret, in, 
				MimeType.Magic.MAX_PRIO, MimeType.Magic.MIN_PRIO);
		Helper.close(in);
		return ret;
	}
	/** Try to resove all mimetypes for the given uri.
	 * @param uri The uri to determine mimetypes for.
	 * @return A {@link List} of {@link MimeType}s. */
	public static List resolve(URI uri) {
		Error.checkNotNull(uri);
		List ret = null;
		ret = determineFromName(uri.getPath());
		return ret;
	}
	/** Try to resove all mimetypes for the given url.
	 * @param url The url to determine mimetypes for.
	 * @return A {@link List} of {@link MimeType}s. */
	public static List resolve(URL url) {
		Error.checkNotNull(url);
		List ret = new ArrayList();
		InputStream in = null;
		ret = determineFromName(url.toString());
		try {
			in = url.openStream();
			determineFromMagics(ret, in, 
					MimeType.Magic.MAX_PRIO, MimeType.Magic.MIN_PRIO);
		} catch (IOException e) {
			if (isDebug)
				logger.debug("Ignoring exception while resolveing mimetype for url " 
						+ url, e);
		}
		Helper.close(in);
		return ret;
	}
	
	// ----------------------------------------------------------------------
	// methods
	// ----------------------------------------------------------------------
	
	static Collection determineFromMagics(List ret, InputStream in, 
			int maxPrio, int minPrio) {
		
		Error.checkNotNullMult(ret, in);
		if(maxPrio <= minPrio 
				|| maxPrio > MimeType.Magic.MAX_PRIO
				|| minPrio < MimeType.Magic.MIN_PRIO)
			throw new IllegalArgumentException("Wrong prio: " + maxPrio + ", " + minPrio);
		
		Collection[] sort = new ArrayList[maxPrio - minPrio];
		
		// fill buffer
		byte[] buffer = null;
		int read = -1 ;
		try {
			read = in.available();
			if(read > longestMatch)
				read = longestMatch;
			buffer = new byte[read];
			read = in.read(buffer, 0, read);
		} catch (Exception e) {
			read = -1;
			if (isDebug)
				logger.debug("Ignore exception while trying to resolve mimetype: " 
						+ e.getLocalizedMessage());
		}
		// try matching only if stream was readable
		if(read <= 0) {
			if (isDebug) logger.debug("Cannot read stream -- no magic matching possible.");
		} else {
			// iterate all matches for given prios
			int pos = 0;
			for (Iterator iM = mimetypes.values().iterator(); iM.hasNext(); pos++) {
				MimeTypeImpl mt = (MimeTypeImpl) iM.next();
				boolean matches = (mt.getMagics().size() > 0);
				int prio = -1;
				// compare until one match does not match 
				for (Iterator iMa = mt.getMagics().iterator(); matches && iMa.hasNext();) {
					MagicImpl magic = (MagicImpl) iMa.next();
					if (magic.getPrio() >= minPrio && magic.getPrio() <= maxPrio) {
						matches = magic.matches(buffer); 
						if(matches) 
							// prio runs from 0 to (maxPrio - minPrio)
							prio = magic.getPrio() - minPrio;
					}
				}
				if(matches) {
					// add match to prio-lists
					if(prio >= 0) {
						// create only used prio lists
						if(sort[prio] == null)
							sort[prio] = new ArrayList();
						if (isDebug)
							logger.debug("Found magic match: " + mt.getName());
						sort[prio].add(mt);
					}
				}
			}
		}
		// collect all entries from prio-lists
		// ignore multiple occurences, use only 
		// mimetype with highest prio
		for(int i = (maxPrio - minPrio - 1); i >= 0; i--) {
			if(sort[i] != null) {
				for (Iterator iMT = sort[i].iterator(); iMT.hasNext();) {
					MimeTypeImpl mt = (MimeTypeImpl) iMT.next();
					if(!ret.contains(mt))
						ret.add(mt);
					else
						if (isDebug)
							logger.debug("Multiple matches for " + mt.getName());
				}
			}
		}
		// return mimetypes as list
		return ret;
	}
	
	/** Check <code>name</code> against known filename patterns. */
	protected static List determineFromName(String uri) {
		Error.checkNotNull(uri);
		
		ArrayList ret = new ArrayList();
		ArrayList matches = new ArrayList();
		// search all patterns
		// from most unimportant match to most important match to get 
		// correct priority
		// As all patterns have to be checked, order should
		// not change speed
		determineOtherPattern(matches, uri);
		determineLiteralPattern(matches, uri);
		
		// return ordered collection of mimetypes
		Collections.sort(matches);
		for (Iterator i = matches.iterator(); i.hasNext();) {
			ResolverMatch rm = (ResolverMatch) i.next();
			if(!ret.contains(rm.getMimetype()))
				ret.add(rm.getMimetype());
		}
		return ret;
	}
	
	/** Check if name is a literal pattern. */
	protected static void determineLiteralPattern(ArrayList ret, String name) {
		Error.checkNotNullMult(ret, name);
		
		// can find only one literal
		String pat = (String) literalPatterns.get(name);
		if(pat != null) {
			MimeTypeImpl mt = getMimeTypeImpl(pat);
			if(mt == null)
				mt = getMimeTypeImpl((String) literalPatterns.get(name.toLowerCase()));
			if(mt != null) {
				// match with maximal prio
				ResolverMatch m = new ResolverMatch(-1, mt);
				ret.add(m);
			}
		}
	}
	
	/** Test all start-/endpatterns against name. 
	 * The longer the pattern, the higher the priority, 
	 * with end patterns higher priorized if same length 
	 * as start patterns. */
	protected static void determineOtherPattern(ArrayList ret, String filename) {
		Error.checkNotNullMult(ret, filename);
		
		final int COMPLEX = 0;
		final int START = 1;
		final int END = 2;
		final int MAX_TYPE = 3;
		
		/** Used to store a match */ 
		class M implements Comparable {
			/** The type of pattern, start or end. */
			int type; 
			String key;
			MimeTypeImpl mt;
			M(int type, String key, MimeTypeImpl mt) {
				this.type = type;
				this.key = key;
				this.mt = mt;
			}
			/** Sort descending, first order by key length, second by type */
			public int compareTo(Object o) {
				M m = (M) o;
				int ret = MAX_TYPE * (m.key.length() - key.length());
				ret += m.type - type;
				return ret;
			}
			public String toString() {
				return type + ":" + key + ":" + (mt == null ? "null" : mt.getName());
			}
		}
		
		int pos = filename.lastIndexOf('/');
		pos = (pos < filename.lastIndexOf('\\') ? filename.lastIndexOf('\\') : pos);
		String name = ( pos >= 0 ? filename.substring(pos + 1) : filename);
		String nameLower = name.toLowerCase();
		// collect matching end pattern keys wrapped in M
		ArrayList matches = new ArrayList();
		for(Iterator i = endPatterns.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			if (name.endsWith(key)) {
				MimeTypeImpl mt = getMimeTypeImpl((String) endPatterns.get(key));
				if(mt != null) {
					if (isDebug) logger.debug("Found end pattern: " 
							+ key + "=" + mt.getName());
					M m = new M(END, key, mt);
					matches.add(m);
				}
			}
		}
		
		// compare start patterns 
		for(Iterator i = startPatterns.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			if (name.startsWith(key)) {
				MimeTypeImpl mt = getMimeTypeImpl((String) startPatterns.get(key));
				if(mt != null) {
					if (isDebug) logger.debug("Found start pattern: " 
							+ key + "=" + mt.getName());
					M m = new M(START, key, mt);
					matches.add(m);
				}
			}
		}
		
		// compare complex patterns 
		for(Iterator i = shellPatterns.keySet().iterator(); i.hasNext(); ) {
			String key = (String) i.next();
			if (name.matches(key)) {
				MimeTypeImpl mt = getMimeTypeImpl((String) shellPatterns.get(key));
				if(mt != null) {
					if (isDebug) logger.debug("Found complex pattern: " 
							+ key + "=" + mt.getName());
					M m = new M(COMPLEX, key, mt);
					matches.add(m);
				}
			}
		}
		
		if(!name.equals(nameLower)) {
			// check filename with all lowercase with less prio 
			for(Iterator i = endPatterns.keySet().iterator(); i.hasNext();) {
				String key = (String) i.next();
				if (nameLower.endsWith(key.toLowerCase())) {
					MimeTypeImpl mt = getMimeTypeImpl((String) endPatterns.get(key));
					if(mt != null) {
						if (isDebug) 
							logger.debug("Found end pattern for lower case filename: " 
									+ key + "=" + mt.getName());
						M m = new M(END, key, mt);
						matches.add(m);
					}
				}
			}
			// compare start patterns 
			for(Iterator i = startPatterns.keySet().iterator(); i.hasNext();) {
				String key = (String) i.next();
				if (nameLower.startsWith(key.toLowerCase())) {
					MimeTypeImpl mt = getMimeTypeImpl((String) startPatterns.get(key));
					if(mt != null) {
						if (isDebug) 
							logger.debug("Found start pattern for lower case filename: " 
									+ key + "=" + mt.getName());
						M m = new M(START, key, mt);
						matches.add(m);
					}
				}
			}
			// compare complex patterns 
			for(Iterator i = shellPatterns.keySet().iterator(); i.hasNext(); ) {
				String key = (String) i.next();
				if (nameLower.matches(key.toLowerCase())) {
					MimeTypeImpl mt = getMimeTypeImpl((String) shellPatterns.get(key));
					if(mt != null) {
						if (isDebug) 
							logger.debug("Found complex pattern for lower case filename: " 
									+ key + "=" + mt.getName());
						M m = new M(COMPLEX, key, mt);
						matches.add(m);
					}
				}
			}
		}
		
		// sort and merge matches
		Collections.sort(matches);
		pos = 0;
		for (Iterator i = matches.iterator(); i.hasNext();) {
			M m = (M) i.next();
			ResolverMatch match = new ResolverMatch(pos++, m.mt);
			ret.add(match);
		}
	}
	
	// ----------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------
	
	/** Creates a new parser. */
	protected MimeTypeResolver() {
		super();
	}
	
}
