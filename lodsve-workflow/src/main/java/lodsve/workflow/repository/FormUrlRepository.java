package lodsve.workflow.repository;

import java.util.List;
import lodsve.workflow.domain.FormUrl;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-18 14:19
 */
@Repository
public interface FormUrlRepository {
    /**
     * 批量保存表单URL
     *
     * @param formUrls 表单URL
     */
    void saveFormUrls(List<FormUrl> formUrls);
}
