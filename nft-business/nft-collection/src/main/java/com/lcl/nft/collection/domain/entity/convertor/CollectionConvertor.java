package com.lcl.nft.collection.domain.entity.convertor;

import com.lcl.nft.api.collection.constant.CollectionStateEnum;
import com.lcl.nft.api.collection.constant.CollectionVoState;
import com.lcl.nft.api.collection.model.CollectionVO;
import com.lcl.nft.api.collection.request.CollectionCreateRequest;
import com.lcl.nft.collection.domain.entity.Collection;
import com.lcl.nft.collection.domain.entity.CollectionSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @Author conglongli
 * @date 2025/2/25 11:17
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CollectionConvertor {

    CollectionConvertor INSTANCE = Mappers.getMapper(CollectionConvertor.class);

    public static final int DEFAULT_MIN_SALE_TIME = 60;

    /**
     * 转换为VO
     *
     * @param request
     * @return
     */
    @Mapping(target = "inventory", source = "request.saleableInventory")
    @Mapping(target = "state", ignore = true)
    public CollectionVO mapToVo(Collection request);


    /**
     * 创建快照
     * @param request
     * @return
     */
    @Mapping(target = "collectionId", source = "request.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gmtCreate", ignore = true)
    @Mapping(target = "gmtModified", ignore = true)
    public CollectionSnapshot createSnapshot(Collection request);

    /**
     * 转换为实体
     *
     * @param request
     * @return
     */
    public Collection mapToEntity(CollectionCreateRequest request);

    /**
     * 转换为VO
     *
     * @param request
     * @return
     */
    public List<CollectionVO> mapToVo(List<Collection> request);

    /**
     * 状态映射
     *
     * @param collection
     * @return
     */
    default CollectionVoState transState(Collection collection) {

        if (collection.getState().equals(CollectionStateEnum.INIT) || collection.getState().equals(CollectionStateEnum.REMOVED)) {
            return CollectionVoState.NOT_FOR_SALE;
        }

        Instant now = Instant.now();

        if (now.compareTo(collection.getSaleTime().toInstant()) >= 0) {
            if (collection.getSaleableInventory() > 0) {
                return CollectionVoState.SELLING;
            } else {
                return CollectionVoState.SOLD_OUT;
            }
        } else {
            if (ChronoUnit.MINUTES.between(now, collection.getSaleTime().toInstant()) > DEFAULT_MIN_SALE_TIME) {
                return CollectionVoState.WAIT_FOR_SALE;
            }
            return CollectionVoState.COMING_SOON;
        }
    }
}
