package cn.niit.shop_online.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 86187
 * @Date 2023/11/11
 */
@Getter
@AllArgsConstructor
public enum CategoryRecommendEnum {

    ALL_RECOMMEND(0,"全推荐"),
    INDEX_RECOMMEND(1,"首页推荐"),
    CATEGORY_HOME_RECOMMEND(2,"分类页面推荐");

    private final Integer value;
    private final String name;
}
