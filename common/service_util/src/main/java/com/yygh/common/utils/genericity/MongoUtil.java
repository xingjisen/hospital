package com.yygh.common.utils.genericity;

import cn.hutool.core.convert.Convert;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.common.utils.genericity
 * @Description
 * @date 2024-09-23 21:57
 */
public final class MongoUtil {
    /**
     * 构建查询条件
     *
     * @param queryVo
     * @param entityClass
     * @param <T>
     * @param <Q>
     * @return
     */
    public static <T, Q> Example<T> buildExample(Q queryVo, Class<T> entityClass) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        T entity = Convert.convert(entityClass, queryVo);
        return Example.of(entity, matcher);
    }

    /**
     * 构建分页参数
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static Pageable buildPageable(int pageNum, int pageSize) {
        return PageRequest.of(pageNum - 1, pageSize);
    }


}
