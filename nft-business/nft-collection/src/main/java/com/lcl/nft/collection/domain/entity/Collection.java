package com.lcl.nft.collection.domain.entity;

import com.lcl.nft.api.collection.constant.CollectionStateEnum;
import com.lcl.nft.api.collection.request.CollectionCreateRequest;
import com.lcl.nft.collection.domain.entity.convertor.CollectionConvertor;
import com.lcl.nft.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.dromara.easyes.annotation.IndexName;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author conglongli
 * @date 2025/2/25 11:12
 */

@Getter
@Setter
@Document(indexName = "nfturbo_collection")
@IndexName(value = "nfturbo_collection")
public class Collection extends BaseEntity {

    /**
     * '藏品名称'
     */
    private String name;

    /**
     * '藏品封面'
     */
    private String cover;

    /**
     * '藏品类目id'
     */
    private String classId;

    /**
     * '价格'
     */
    private BigDecimal price;

    /**
     * '藏品数量'
     */
    private Long quantity;

    /**
     * '藏品详情'
     */
    private String detail;

    /**
     * '可售库存'
     */
    private Long saleableInventory;

    /**
     * '已占库存'
     */
    private Long occupiedInventory;

    /**
     * '状态'
     */
    private CollectionStateEnum state;

    /**
     * '藏品创建时间'
     */
    private Date createTime;

    /**
     * '藏品发售时间'
     */
//    @Field(name = "sale_time", type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss || strict_date_optional_time || epoch_millis")
    @Field(name = "sale_time", type = FieldType.Date, format = {DateFormat.date_hour_minute_second, DateFormat.date_time_no_millis})
    private Date saleTime;

    /**
     * '藏品上链时间'
     */
    private Date syncChainTime;

    /**
     * '藏品创建者id'
     */
    private String creatorId;

    /**
     * 版本
     */
    private Integer version;

    public static Collection create(CollectionCreateRequest request) {
        Collection collection = CollectionConvertor.INSTANCE.mapToEntity(request);
        collection.setOccupiedInventory(0L);
        collection.setSaleableInventory(request.getQuantity());
        collection.setState(CollectionStateEnum.INIT);
        collection.setVersion(1);
        return collection;
    }

    public Collection remove() {
        this.state = CollectionStateEnum.REMOVED;
        return this;
    }

}

