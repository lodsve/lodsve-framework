/*
 * Copyright (C) 2019 Sun.Hao(https://www.crazy-coder.cn/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.p6spy.engine.spy.appender;

import com.p6spy.engine.spy.P6SpyOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Appender which writes log messages to a file.  This is the default appended for P6Spy.
 */
public class FileLogger extends StdoutLogger {

    private String fileName = null;
    private PrintStream printStream = null;

    private void init() {
        if (fileName == null) {
            throw new IllegalStateException("setLogfile() must be called before init()");
        }

        try {
            // 防止目录不存在报错！
            // add by SUNHAO 2017-12-26
            if (!new File(fileName).getParentFile().exists()) {
                new File((fileName)).getParentFile().mkdirs();
            }

            printStream = new PrintStream(new FileOutputStream(fileName, P6SpyOptions.getActiveInstance().getAppend()));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    protected PrintStream getStream() {
        // Lazy init to allow for the appender to be changed at Runtime without creating an empty log file (assuming
        // that no log message has been written yet)
        if (printStream == null) {
            synchronized (this) {
                if (printStream == null) {
                    init();
                }
            }
        }
        return printStream;
    }

    public void setLogfile(String fileName) {
        this.fileName = fileName;
    }
}

