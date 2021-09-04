/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lodsve.core;

import lodsve.core.ansi.AnsiColor;
import lodsve.core.ansi.AnsiOutput;
import lodsve.core.ansi.AnsiStyle;
import lodsve.core.configuration.properties.BannerConfig;

import java.io.PrintStream;

/**
 * 默认的banner.
 *
 * @author <a href="mailto:sunhao.java@gmail.com">sunhao(sunhao.java@gmail.com)</a>
 * @date 2018/1/12 下午6:31
 */
public class LodsveBanner implements Banner {
    private static final String[] BANNER = new String[]{
            "$$\\                      $$\\                                ",
            "$$ |                     $$ |                               ",
            "$$ |      $$$$$$\\   $$$$$$$ | $$$$$$$\\ $$\\    $$\\  $$$$$$\\  ",
            "$$ |     $$  __$$\\ $$  __$$ |$$  _____|\\$$\\  $$  |$$  __$$\\ ",
            "$$ |     $$ /  $$ |$$ /  $$ |\\$$$$$$\\   \\$$\\$$  / $$$$$$$$ |",
            "$$ |     $$ |  $$ |$$ |  $$ | \\____$$\\   \\$$$  /  $$   ____|",
            "$$$$$$$$\\\\$$$$$$  |\\$$$$$$$ |$$$$$$$  |   \\$  /   \\$$$$$$$\\ ",
            "\\________|\\______/  \\_______|\\_______/     \\_/     \\_______|"
    };

    private static final int LINE_WIDTH = BANNER[0].length();
    private static final String LODSVE_DESCRIPTION = "Let our development of Spring very easy!";
    private static final String LODSVE_TITLE = " :: Lodsve Frameowrk :: ";

    @Override
    public void print(BannerConfig config, PrintStream out) {
        String version = LodsveVersion.getVersion();
        StringBuilder blank1 = new StringBuilder();
        fillBlank(LODSVE_TITLE.length() + version.length(), blank1);

        String builter = "Author: " + LodsveVersion.getBuilter();
        StringBuilder blank2 = new StringBuilder();
        fillBlank(LODSVE_DESCRIPTION.length() + builter.length(), blank2);

        out.println("\n\n\n");
        for (String line : BANNER) {
            out.println(line);
        }

        out.println("\n" + AnsiOutput.toString(AnsiColor.BLUE, LODSVE_DESCRIPTION, AnsiColor.DEFAULT, blank2.toString(), AnsiStyle.FAINT, builter));
        out.println(AnsiOutput.toString(AnsiColor.GREEN, LODSVE_TITLE, AnsiColor.DEFAULT, blank1.toString(), AnsiStyle.FAINT, version));
        out.println("\n\n\n");
    }

    private void fillBlank(int nowLength, StringBuilder blank) {
        if (nowLength < LINE_WIDTH) {
            for (int i = 0; i < LINE_WIDTH - nowLength; i++) {
                blank.append(" ");
            }
        }
    }
}
