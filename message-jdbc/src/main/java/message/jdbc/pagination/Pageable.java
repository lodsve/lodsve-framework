package message.jdbc.pagination;

/**
 * 封装分页的一些参数.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/6/29 下午2:16
 */
public class Pageable {
    private int start;
    private int num;

    public Pageable(int start, int num) {
        this.start = start;
        this.num = num;
    }

    public int getStart() {
        return start;
    }

    public int getNum() {
        return num;
    }
}
