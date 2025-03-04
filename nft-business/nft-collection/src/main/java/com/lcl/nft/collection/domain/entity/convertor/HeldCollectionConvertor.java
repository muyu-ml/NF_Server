package com.lcl.nft.collection.domain.entity.convertor;

import com.lcl.nft.api.collection.model.HeldCollectionDTO;
import com.lcl.nft.api.collection.model.HeldCollectionVO;
import com.lcl.nft.collection.domain.entity.HeldCollection;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author conglongli
 * @date 2025/2/25 11:18
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface HeldCollectionConvertor {

    HeldCollectionConvertor INSTANCE = Mappers.getMapper(HeldCollectionConvertor.class);

    /**
     * 转换为vo
     *
     * @param request
     * @return
     */
    public HeldCollectionVO mapToVo(HeldCollection request);

    /**
     * 转换为 DTO
     * @param request
     * @return
     */
    public HeldCollectionDTO mapToDto(HeldCollection request);

    /**
     * 转换为vo
     *
     * @param request
     * @return
     */
    public List<HeldCollectionVO> mapToVo(List<HeldCollection> request);

}
