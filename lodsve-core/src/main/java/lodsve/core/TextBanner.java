package lodsve.core;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 * 打印classpath下banner.txt.
 *
 * @author sunhao(sunhao.java @ gmail.com)
 * @version V1.0, 2018-1-18-0018 10:06
 */
public class TextBanner implements Banner {
    private Resource resource;

    TextBanner(Resource resource) {
        Assert.notNull(resource, "Text file must not be null");
        Assert.isTrue(resource.exists(), "Text file must exist");

        this.resource = resource;
    }

    @Override
    public void print(PrintStream out) {
        try {
            String banner = StreamUtils.copyToString(this.resource.getInputStream(), Charset.forName("UTF-8"));

            out.println(banner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
