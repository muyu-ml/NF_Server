package com.lcl.ntf.api.goods.constant;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
public enum GoodsType {

    /** 藏品 */
    COLLECTION("藏品"),

    /**
     * 盲盒
     */
    BLIND_BOX("盲盒");


    private String value;

    GoodsType(String value) {
        this.value = value;
    }
}
