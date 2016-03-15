package de.sty.io.mimetype;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;

/**
 * <p> 
 * Used to access the data of a mimetype.
 * </p>
 * <p>
 * Use 
 * {@link MimeTypeResolver#resolve(java.io.File)}
 * (and friends) to get the mimetypes from a file, stream, url or uri.
 * To get a new instance of a {@link MimeType} use 
 * {@link MimeTypeResolver#getMimeType(String)}.
 * </p>
 * 
 * @see MimeType
 *
 * @author <a href="http://www.stuelten.de">Timo Stuelten</a>
 * @version $Id: MimeType.java,v 1.1 2005/06/29 20:38:31 stuelten Exp $
 */
public interface MimeType extends Comparable, Serializable {

	/** Default value for unknown types: the super type of all mimetypes. */
	public static final String ROOTTYPE_NAME = "application/octet-stream";
	/** Default value for text types: the super type of all text types. */
	public static final String TEXT_PLAIN_NAME = "text/plain";

	// ----------------------------------------------------------------------
	// getter 
	// ----------------------------------------------------------------------

	/** @return The name of this mimetype (e.g. "text/plain"). */
	public String getName();

	/** @return The description of this mimetype (e.g. "text file"). */
	public String getDescription();

	/** @return The language (e.g. "English") or 
	 * <code>null</code> if not applicable. */
	public String getLang();

	/** @return The encoding (e.g. "iso-8859-1") or 
	 * <code>null</code> if not applicable. 
	 * The encoding is mostly case insensitive. */
	public String getEncoding();

	/** Get the more generic mimetypes of this mimetype. 
	 * The most generic mimetype is {@link MimeType#ROOTTYPE_NAME}. 
	 * For "text/x-java", "text/plain" is a supertype. 
	 * For "text/plain", "application/unknown" is a supertype. 
	 * @return A collection of {@link MimeType}s. */
	public Collection getSuperTypes();

	/** This mimetype may be a subclass off a less specific mimetypes. 
	 * For "text/plain", "text/x-java" is a subtype. 
	 * @return A collection of {@link MimeType}s. */
	public Collection getSubTypes();

	/** A mimetype may have aliases.
	 * @return A collection of {@link MimeType}s. */
	public Collection getAliases();
	
	/** Get the globs for this mimetype. 
	 * @return A collection of {@link Glob}s. */
	public Collection getGlobs();
	
	/** Get the magics of this mimetype. 
	 * @return A collection of {@link Magic}s. */
	public Collection getMagics();
	
	/** If the mimetype of a file is <code>text/xml</code>, more specific
	 * mimetypes can be obtained from the namespaces or name of the root node
	 * used inside the xml file.
	 * @return A collection of {@link XMLRootNode}s. */
	public Collection getXMLRootNodes();
	
	// ----------------------------------------------------------------------
	// Convenience methods 
	// ----------------------------------------------------------------------
	
	/** Get a list of all mimetypes: this mimetype and all it's supertypes.
	 * @return A {@link List} of {@link MimeType}s. */
	public List listAllTypes();

	// ----------------------------------------------------------------------

	/** A glob (or pattern) which matches a filename is used to determine a mimetype. */
	public interface Glob extends Serializable {

		/** The glob for the unknown mimetype. */
		public static final String UNKNOWN_GLOB = "*";

		/** @return The glob (or pattern). E.g. <code>"*.txt"</code>. */
		public String getGlob();

		/** @return <code>true</code> if <code>name</code> matches
		 * this glob. */
		public boolean matches(String name);

	}

	// ----------------------------------------------------------------------

	/** 
	 * Contains a magic match. The file/stream must be read to  
	 * compare it with the matches for some mimetype. 
	 */
	public interface Magic extends Serializable {
		
		public static final int MAX_PRIO = 100;
		public static final int MIN_PRIO = 0;

		/** @return The priority of this magic. */
		public int getPrio();
		
		/** Get the matches for this magic. 
		 * @return A collection of <code>Magic.Match[]</code>es. */
		public Collection getMatches();

		/** @return the maximal length of the combination of all matches 
		 * of this magic. Used to determine the size of the read buffer. */
		public int matchLength();

		/** @return <code>true</code> if <code>buffer</code> matches
		 * this magic. */
		public boolean matches(byte[] buffer);

		// ----------------------------------------------------------------------
		
		/** A single match for a magic. A magic can contain multiple matches. */
		public interface Match extends Serializable {
			
			public static int TYPE_STRING = 1;
			public static int TYPE_HOST16 = 2;
			public static int TYPE_HOST32 = 3;
			public static int TYPE_BIG16 = 4;
			public static int TYPE_BIG32 = 5;
			public static int TYPE_LITTLE16 = 6;
			public static int TYPE_LITTLE32 = 7;
			public static int TYPE_BYTE = 8;
			
			public static final String[] TYPES = {
					"", "string", "host16", "host32", "big16", "big32", "little16", "little32", "byte"
			};
			
			/** Get the type of this match. 
			 * @return one of the <code>TYPE_*</code> constants from above. */
			public int getType();
			
			/** @return The start offset for this match */
			public int getOffsetStart();
			/** @return The end offset for this match */
			public int getOffsetEnd();
			/** @return The value of the match. */
			
			public byte[] getValue();
			/** @return the mask. */
			public byte[] getMask();
			
			/** Get the sub-matches for this match. 
			 * @return A collection of <code>Magic.Match[]</code>es. 
			 * @see Magic#getMatches() */
			public Collection getMatches();

			/** @return the maximal length of the combination of all matches 
			 * of this magic. Used to determine the size of the read buffer. */
			public int matchLength();
			
			/** @return <code>true</code> if <code>buffer</code> matches
			 * this match. */
			public boolean matches(byte[] buffer);
		}
	}
	
	// ----------------------------------------------------------------------

	/** 
	 * Contains matches for XML files. The namespaces and localnames are 
	 * used to determine more specific mimetypes. 
	 */
	public interface XMLRootNode extends Serializable {
		
		/** @return A {@link String} used as localname for the XML root node
		 * 		(see {@link Document#getDocumentElement()}).
		 * 		If it is <code>null</code> or <code>""</code> the 
		 * 		name of the root node is not compared, only
		 * 		the URI of one of the namespaces must match. */
		public String getLocalName();

		/** @return A {@link String} of the namespace, 
		 * 		which identifies the mimetype.
		 * 		If it is <code>null</code> or <code>""</code>, only
		 * 		the name of the root node must match. */
		public String getNamespace();
		
		/** @return <code>true</code> if <code>doc</code> matches
		 * this entry. */
		public boolean matches(Document doc);

		/** @return <code>true</code> if <code>namespace</code> 
		 * and <code>localname</code> matches this entry. */
		public boolean matches(String namespace, String localname);
	}
}
