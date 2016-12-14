/*
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
*/

package lodsve.fs.common;

import lodsve.core.exception.ApplicationException;

/**
 * FS Exception
 *
 * @author Happy Fish / YuQing
 * @version Version 1.0
 */
public class FsException extends ApplicationException {

    public FsException(String content) {
        super(content);
    }

    public FsException(Integer code, String content) {
        super(code, content);
    }

    public FsException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}
