package com.ninjaone.dundie_awards.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class FindHelper {

//    public static Pageable translateSortProperties(
//            Pageable pageable,
//            Sort defaultSort
//    ){
//        var orders = pageable.getSortOr(defaultSort)
//                .stream()
//                .map (order -> order.withProperty(translateProperty(it.property, tableAliasesByPrefix)) ).toList()
//        return PageRequest.of(pageable.pageNumber, pageable.pageSize, Sort.by(orders))
//    }
//}
//
//private String translateProperty(String property, tableAliasesByPrefix: Map<String, String>): String {
//    return if (property.contains('.')) {
//        val prefix = property.takeWhile { it != '.' }
//        val suffix = property.substring(prefix.length + 1)
//        val tableAlias = replacePrefixWithTableAlias(prefix, tableAliasesByPrefix)
//        val snakeCaseSuffix = suffix.toSnakeCase()
//        "$tableAlias.$snakeCaseSuffix"
//    } else {
//        property.toSnakeCase()
//    }
}