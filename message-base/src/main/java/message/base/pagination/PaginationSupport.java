package message.base.pagination;

import java.io.Serializable;
import java.util.List;

/**
 * 分页组件的bean
 * @author sunhao(sunhao.java@gmail.com)
 */
public class PaginationSupport<T> implements Serializable {
	private static final long serialVersionUID = -1963816523264131890L;
	
	private List<T> items;				//要显示的内容
	private int num;				//每页显示条数
	private int pageSize;			//总的页数
	private int currentIndex; 		//当前页数
	private int previousIndex;		//上一页
	private int nextIndex;			//下一页
	private int endIndex;			//尾页
	private int startIndex;			//首页
    private int totalRow;           //数据库中共有多少条
	
	/**
	 * 构造器
	 * @param items				要显示的内容
	 * @param num				每页显示条数
	 * @param pageSize			总的页数
	 * @param currentIndex		当前页数
     * @param totalRow          数据库中共有多少条
	 */
	public PaginationSupport(List<T> items, int num, int pageSize, int currentIndex, int totalRow){
		this.items = items;
		this.num = num;
		this.pageSize = pageSize;
		this.currentIndex = currentIndex / num + 1;
		this.previousIndex = this.currentIndex <= 1 ? 1 : this.currentIndex - 1;
		this.nextIndex = this.currentIndex == pageSize ? pageSize : this.currentIndex + 1;
		this.startIndex = 1;
		this.endIndex = pageSize;
        this.totalRow = totalRow;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getPreviousIndex() {
		return previousIndex;
	}

	public void setPreviousIndex(int previousIndex) {
		this.previousIndex = previousIndex;
	}

	public int getNextIndex() {
		return nextIndex;
	}

	public void setNextIndex(int nextIndex) {
		this.nextIndex = nextIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * 计算起始页
	 * @return
	 */
	public int getStartIndexOnShow() {
		if (currentIndex < 10 / 2 + 1)
			return 1;
		if (currentIndex > endIndex - (10 / 2 - 1))
			return endIndex - (10 - 1) <= 0 ? 1 : endIndex
					- (10 - 1);
		else
			return currentIndex - 10 / 2;
	}

	/**
	 * 计算结束页
	 * @return
	 */
	public int getEndIndexOnShow() {
		if (currentIndex < 10 / 2 + 1)
			if (endIndex > 10)
				return 10;
			else
				return endIndex;
		if (currentIndex >= endIndex - (10 / 2 - 1))
			return endIndex;
		else
			return currentIndex + 10 / 2;
	}

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }
}
