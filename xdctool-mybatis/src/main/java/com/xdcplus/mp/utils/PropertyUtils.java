package com.xdcplus.mp.utils;

import com.github.pagehelper.PageInfo;
import com.xdcplus.pager.vo.PageVO;

import java.util.List;

/**
 * 属性工具类
 * @author Rong.Jia
 * @date 2020/01/14 09:22
 */
public class PropertyUtils {

    /**
     *  page 转vo 对象
     * @param page   分页查询结果对象
     * @param pageVO 分页查询结果vo对象
     * @param <T> VO对象类型
     * @param records VO对象集合
     * @date 2019/07/02 11:13:22
     * @author Rong.Jia
     */
    public static <T> void copyProperties(PageInfo<?> page, PageVO<T> pageVO, List<T> records) {

        pageVO.setTotalPages(page.getPages());
        pageVO.setHasNext(page.isHasNextPage());
        pageVO.setHasPrevious(page.isHasPreviousPage());
        pageVO.setIsFirst(page.isIsFirstPage());
        pageVO.setIsLast(page.isIsLastPage());
        pageVO.setTotal((int)page.getTotal());
        pageVO.setCurrentPage(page.getPageNum());
        pageVO.setPageSize(page.getSize());
        pageVO.setRecords(records);

    }

    /**
     *  page 转vo 对象
     * @param page   分页查询结果对象
     * @param pageVO 分页查询结果vo对象
     * @date 2019/07/02 11:13:22
     * @author Rong.Jia
     */
    public static <T> void copyProperties(PageInfo<?> page, PageVO<T> pageVO) {
        copyProperties(page, pageVO, pageVO.getRecords());
    }

}
