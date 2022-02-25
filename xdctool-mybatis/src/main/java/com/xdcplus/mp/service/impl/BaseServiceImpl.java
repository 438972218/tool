package com.xdcplus.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdcplus.mp.service.BaseService;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 业务层 （父级）接口 实现类
 * @param <E> 实体类
 * @param <T> 目标对象
 * @param <M> 实体类对应Mapper
 * @param <S> 源对象
 * @author Rong.Jia
 * @date 2020/05/21 18:59
 */
@SuppressWarnings("ALL")
public class BaseServiceImpl<S, T, E, M extends BaseMapper<E>> extends ServiceImpl<M, E> implements BaseService<S, T, E> {

    @Override
    public T objectConversion(S s) {

        if (s == null) return null;

        Class<T> tClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];

        return BeanUtil.copyProperties(s, tClass);
    }

    @Override
    public List<T> objectConversion(List<S> sList) {

        if (CollectionUtil.isNotEmpty(sList)) {

            List<T> tList = new ArrayList<>();
            for (S s : sList) {
                T t = this.objectConversion(s);
                Optional.ofNullable(t).ifPresent(tList::add);
            }

            return tList;
        }

        return null;
    }

    @Override
    public T objectConversion(S s, String... ignoreProperties) {

        if (s == null) return null;

        try {

            T t = ((Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[1]).newInstance();
            BeanUtil.copyProperties(s, t, ignoreProperties);
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
        }
        return null;
    }

    @Override
    public List<T> objectConversion(List<S> sList, String... ignoreProperties) {

        if (CollectionUtil.isNotEmpty(sList)) {

            List<T> toList = CollectionUtil.newArrayList();
            for (S s : sList) {
                T t = this.objectConversion(s, ignoreProperties);
                Optional.ofNullable(t).ifPresent(toList::add);
            }

            return toList;
        }

        return null;
    }
}
