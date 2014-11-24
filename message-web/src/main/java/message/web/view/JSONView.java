package message.web.view;

import message.web.commons.WebOutput;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * view for json.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 13-4-13 上午12:29
 */
public class JSONView implements View {
    public static View view = new JSONView();

    public String getContentType() {
        return "application/x-javascript";
    }

    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        WebOutput out = new WebOutput(request, response);
        out.toFlexJson(model);
    }
}
