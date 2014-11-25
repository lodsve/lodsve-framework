package message.json;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Internal class used by Flexjson to represent a path to a field within a serialized stream.
 */
public class Path {
    LinkedList path = new LinkedList();

    public Path() {
    }

    public Path( String[] fields ) {
        for (int i=0; i<fields.length; i++) {
            path.add(fields[i]);
        }
    }

    public Path enqueue( String field ) {
        path.add( field );
        return this;
    }

    public String pop() {
        return (String) path.removeLast();
    }

    public List getPath() {
        return path;
    }

    public int length() {
        return path.size();
    }

    public String toString() {
        StringBuffer builder = new StringBuffer ( "[ " );
        boolean afterFirst = false;
        Iterator it = path.iterator();
        while(it.hasNext())
        {
        	String current = (String)it.next();
            if( afterFirst ) {
                builder.append( "." );
            }
            builder.append( current );
            afterFirst = true;
        }
        builder.append( " ]" );
        return builder.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Path path1 = (Path) o;

        if (!path.equals(path1.path)) return false;

        return true;
    }

    public int hashCode() {
        return path.hashCode();
    }
}
