package lodsve.core;

import lodsve.core.ansi.AnsiColor;
import lodsve.core.ansi.AnsiOutput;
import lodsve.core.ansi.AnsiStyle;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 * 默认的banner.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version 1.0 2018/1/12 下午6:31
 */
public class LodsveBanner implements Banner, Ordered {
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

    private static final String DEFAULT_BANNER_TEXT_NAME = "banner.txt";

    @Override
    public void print(PrintStream printStream) {
        Resource resource = textBanner();
        if (resource != null) {
            printTextBanner(resource, printStream);
            return;
        }

        String version = LodsveVersion.getVersion();
        StringBuilder blank1 = new StringBuilder("");
        fillBlank(LINE_WIDTH, LODSVE_TITLE.length() + version.length(), blank1);

        String builter = "Author: " + LodsveVersion.getBuilter();
        StringBuilder blank2 = new StringBuilder("");
        fillBlank(LINE_WIDTH, LODSVE_DESCRIPTION.length() + builter.length(), blank2);

        System.out.println("\n\n\n");
        for (String line : BANNER) {
            System.out.println(line);
        }

        System.out.println("\n" + AnsiOutput.toString(AnsiColor.BLUE, LODSVE_DESCRIPTION, AnsiColor.DEFAULT, blank2.toString(), AnsiStyle.FAINT, builter));
        System.out.println(AnsiOutput.toString(AnsiColor.GREEN, LODSVE_TITLE, AnsiColor.DEFAULT, blank1.toString(), AnsiStyle.FAINT, version));
        System.out.println("\n\n\n");
    }

    private void fillBlank(int maxLength, int nowLength, StringBuilder blank) {
        if (nowLength < maxLength) {
            for (int i = 0; i < maxLength - nowLength; i++) {
                blank.append(" ");
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private Resource textBanner() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Resource resource = new ClassPathResource(DEFAULT_BANNER_TEXT_NAME, classLoader);

        if (!resource.exists()) {
            return null;
        }

        return resource;
    }

    private void printTextBanner(Resource resource, PrintStream printStream) {
        try {
            String banner = StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8"));
            printStream.println(banner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
