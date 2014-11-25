package message.json;

import java.util.HashMap;
import java.util.Map;

/**
 * A helper class provided out of the box to encode characters that HTML can't support
 * natively like &lt;, &gt;, &amp;, or &quot;.  This will scan the value passed to the transform
 * method and replace any of these special characters with the HTML encoded equivalent.  This
 * method will NOT work for HTML text because it will blindly encode all characters it sees which
 * means it will strip out any HTML tags.
 */
public class HTMLEncoder implements Transformer {

    private static final Map htmlEntities = new HashMap();

    public HTMLEncoder() {
        if( htmlEntities.isEmpty() ) {
            htmlEntities.put( new Integer(34), "&quot;" );       // " - double-quote
            htmlEntities.put( new Integer(38), "&amp;" );        // & - ampersand
//            htmlEntities.put( 39, "&apos;");        // ' - apostrophe
            htmlEntities.put( new Integer(60), "&lt;" );         // < - less-than
            htmlEntities.put( new Integer(62), "&gt;" );         // > - greater-than
            htmlEntities.put( new Integer(160), "&nbsp;" );      // non-breaking space
            htmlEntities.put( new Integer(169), "&copy;" );      // - copyright
            htmlEntities.put( new Integer(174), "&reg;" );       //  - registered trademark
            htmlEntities.put( new Integer(192), "&Agrave;" );    //  - uppercase A, grave accent
            htmlEntities.put( new Integer(193), "&Aacute;" );    //  - uppercase A, acute accent
            htmlEntities.put( new Integer(194), "&Acirc;");      //  - uppercase A, circumflex accent
            htmlEntities.put( new Integer(195), "&Atilde;" );    //  - uppercase A, tilde
            htmlEntities.put( new Integer(196), "&Auml;" );      //  - uppercase A, umlaut
            htmlEntities.put( new Integer(197), "&Aring;" );     //  - uppercase A, ring
            htmlEntities.put( new Integer(198), "&AElig;" );     //  - uppercase AE
            htmlEntities.put( new Integer(199), "&Ccedil;" );    //  - uppercase C, cedilla
            htmlEntities.put( new Integer(200), "&Egrave;");     //  - uppercase E, grave accent
            htmlEntities.put( new Integer(201), "&Eacute;");     //  - uppercase E, acute accent
            htmlEntities.put( new Integer(202), "&Ecirc;" );     //  - uppercase E, circumflex accent
            htmlEntities.put( new Integer(203), "&Euml;" );      //  - uppercase E, umlaut
            htmlEntities.put( new Integer(204), "&Igrave;" );    //  - uppercase I, grave accent
            htmlEntities.put( new Integer(205), "&Iacute;" );    //  - uppercase I, acute accent
            htmlEntities.put( new Integer(206), "&Icirc;" );     //  - uppercase I, circumflex accent
            htmlEntities.put( new Integer(207), "&Iuml;" );      //  - uppercase I, umlaut
            htmlEntities.put( new Integer(208), "&ETH;" );       //  - uppercase Eth, Icelandic
            htmlEntities.put( new Integer(209), "&Ntilde;");     //  - uppercase N, tilde
            htmlEntities.put( new Integer(210), "&Ograve;");     //  - uppercase O, grave accent
            htmlEntities.put( new Integer(211), "&Oacute;");     //  - uppercase O, acute accent
            htmlEntities.put( new Integer(212), "&Ocirc;" );     //  - uppercase O, circumflex accent
            htmlEntities.put( new Integer(213), "&Otilde;");     //  - uppercase O, tilde
            htmlEntities.put( new Integer(214), "&Ouml;");       //  - uppercase O, umlaut
            htmlEntities.put( new Integer(216), "&Oslash;");     //  - uppercase O, slash
            htmlEntities.put( new Integer(217), "&Ugrave;");     //  - uppercase U, grave accent
            htmlEntities.put( new Integer(218), "&Uacute;");     //  - uppercase U, acute accent
            htmlEntities.put( new Integer(219), "&Ucirc;" );     //  - uppercase U, circumflex accent
            htmlEntities.put( new Integer(220), "&Uuml;" );      //  - uppercase U, umlaut
            htmlEntities.put( new Integer(221), "&Yacute;");     //  - uppercase Y, acute accent
            htmlEntities.put( new Integer(222), "&THORN;");      //  - uppercase THORN, Icelandic
            htmlEntities.put( new Integer(223), "&szlig;");      //  - lowercase sharps, German
            htmlEntities.put( new Integer(224), "&agrave;");     //  - lowercase a, grave accent
            htmlEntities.put( new Integer(225), "&aacute;");     //  - lowercase a, acute accent
            htmlEntities.put( new Integer(226), "&acirc;");      //  - lowercase a, circumflex accent
            htmlEntities.put( new Integer(227), "&atilde;");     //  - lowercase a, tilde
            htmlEntities.put( new Integer(228), "&auml;");       //  - lowercase a, umlaut
            htmlEntities.put( new Integer(229), "&aring;");      //  - lowercase a, ring
            htmlEntities.put( new Integer(230), "&aelig;");      //  - lowercase ae
            htmlEntities.put( new Integer(231), "&ccedil;" );    //  - lowercase c, cedilla
            htmlEntities.put( new Integer(232), "&egrave;");     //  - lowercase e, grave accent
            htmlEntities.put( new Integer(233), "&eacute;");     //  - lowercase e, acute accent
            htmlEntities.put( new Integer(234), "&ecirc;");      //  - lowercase e, circumflex accent
            htmlEntities.put( new Integer(235), "&euml;");       //  - lowercase e, umlaut
            htmlEntities.put( new Integer(236), "&igrave;" );    //  - lowercase i, grave accent
            htmlEntities.put( new Integer(237), "&iacute;");     //  - lowercase i, acute accent
            htmlEntities.put( new Integer(238), "&icirc;");      //  - lowercase i, circumflex accent
            htmlEntities.put( new Integer(239), "&iuml;");       //  - lowercase i, umlaut
            htmlEntities.put( new Integer(240), "&eth;");        //  - lowercase eth, Icelandic
            htmlEntities.put( new Integer(241), "&ntilde;");     //  - lowercase n, tilde
            htmlEntities.put( new Integer(242), "&ograve;");     //  - lowercase o, grave accent
            htmlEntities.put( new Integer(243), "&oacute;");     //  - lowercase o, acute accent
            htmlEntities.put( new Integer(244), "&ocirc;");      //  - lowercase o, circumflex accent
            htmlEntities.put( new Integer(245), "&otilde;");     //  - lowercase o, tilde
            htmlEntities.put( new Integer(246), "&ouml;");       //  - lowercase o, umlaut
            htmlEntities.put( new Integer(248), "&oslash;");     //  - lowercase o, slash
            htmlEntities.put( new Integer(249), "&ugrave;");     //  - lowercase u, grave accent
            htmlEntities.put( new Integer(250), "&uacute;");     //  - lowercase u, acute accent
            htmlEntities.put( new Integer(251), "&ucirc;");      //  - lowercase u, circumflex accent
            htmlEntities.put( new Integer(252), "&uuml;");       //  - lowercase u, umlaut
            htmlEntities.put( new Integer(253), "&yacute;");     //  - lowercase y, acute accent
            htmlEntities.put( new Integer(254), "&thorn;");      //  - lowercase thorn, Icelandic
            htmlEntities.put( new Integer(255), "&yuml;");       //  - lowercase y, umlaut
            htmlEntities.put( new Integer(8364), "&euro;");      // Euro symbol
        }
    }

    public String transform(Object value) {
        String val = value.toString();
        StringBuffer builder = new StringBuffer();
        for (int i = 0; i < val.length(); ++i) {
            int intVal = (int)val.charAt(i);
            Integer intValInteger = new Integer(intVal);
           if( htmlEntities.containsKey( intValInteger ) ) {
                builder.append( htmlEntities.get( intValInteger ) );
            } else if ( intVal > 128 ) {
                    builder.append( "&#" );
                    builder.append( intVal );
                    builder.append( ";" );
            } else {
                builder.append( val.charAt(i) );
            }
        }
        return builder.toString();
    }
}
