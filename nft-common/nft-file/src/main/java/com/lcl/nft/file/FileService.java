package com.lcl.nft.file;

import java.io.InputStream;

/**
 * @Author conglongli
 * @date 2025/1/13 01:23
 */
public interface FileService {

    /**
     * 文件上传
     * @param path
     * @param fileStream
     * @return
     */
    boolean upload(String path, InputStream fileStream);
}
