package com.xdcplus.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 业务层 （父级）接口
 *
 * @param <T>  目标对象
 * @param <S>  源对象
 * @param <E>  实体类
 * @author Rong.Jia
 * @date 2020/05/21 18:58
 */
public interface BaseService<S, T, E> extends IService<E> {

    /**
     *  对象转换
     * @param s  源对象
     * @return 目标对象
     */
    T objectConversion(S s);

    /**
     *  对象转换
     * @param sList  源对象集合
     * @return 目标对象目标集合
     */
    List<T> objectConversion(List<S> sList);

    /**
     *  对象转换
     * @param s  源对象
     * @param ignoreProperties 不拷贝的的属性列表
     * @return 目标对象
     */
    T objectConversion(S s, String... ignoreProperties);

    /**
     *  对象转换
     * @param sList  源对象集合
     * @param ignoreProperties 不拷贝的的属性列表
     * @return 目标对象目标集合
     */
    List<T> objectConversion(List<S> sList, String... ignoreProperties);








}
