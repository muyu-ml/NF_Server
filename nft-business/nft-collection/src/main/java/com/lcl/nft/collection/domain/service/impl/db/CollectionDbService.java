package com.lcl.nft.collection.domain.service.impl.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcl.nft.base.response.PageResponse;
import com.lcl.nft.collection.domain.entity.Collection;
import com.lcl.nft.collection.domain.service.impl.BaseCollectionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 藏品服务-数据库
 * @Author conglongli
 * @date 2025/2/27 10:11
 */
@Service
@ConditionalOnProperty(name = "spring.elasticsearch.enable", havingValue = "false", matchIfMissing = true)
public class CollectionDbService extends BaseCollectionService {

    @Override
    public PageResponse<Collection> pageQueryByState(String keyWord, String state, int currentPage, int pageSize) {
        Page<Collection> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Collection> wrapper = new QueryWrapper<>();
        wrapper.eq("state", state);

        if (StringUtils.isNotBlank(keyWord)) {
            wrapper.like("name", keyWord);
        }
        wrapper.orderBy(true, true, "gmt_create");

        Page<Collection> collectionPage = this.page(page, wrapper);

        return PageResponse.of(collectionPage.getRecords(), (int) collectionPage.getTotal(), pageSize, currentPage);
    }
}
