package message.jdbc.pagination;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页组件的处理类
 * @author sunhao(sunhao.java@gmail.com)
 */
public class PaginationUtils {
	
	/**
	 * 处理生成分页bean
	 * @param items				当前要显示的list集合
	 * @param totalSize			数据库里总共有多少条
	 * @param num				每页显示的条数
	 * @param currentIndex		当前页数
	 * @return
	 */
	public static <T> PaginationSupport<T> makePagination(List<T> items,int totalSize, int num, int currentIndex){
		int pageSize = getPageSize(num, totalSize);
		PaginationSupport<T> paginationSupport = new PaginationSupport(items, num, pageSize, currentIndex, totalSize);
		return paginationSupport;
	}
	
	/**
	 * 验证的时候参数不够或者参数不合法的时候，返回null
	 * 
	 * @return
	 */
	public static <T> PaginationSupport<T> getNullPagination(){
		return new PaginationSupport<T>(new ArrayList<T>(), -1, 0, 0, 0);
	}
	
	/**
	 * 计算共有多少页
	 * @param num				每页显示的条数
	 * @param totalSize			数据库中共有多少条
	 * @return
	 */
	private static int getPageSize(int num, int totalSize){
		if((totalSize%num) == 0) {
			return (totalSize/num);
		} else {
			return (totalSize/num + 1);
		}
	}
	
}
