package com.lcl.nft.file;

import java.io.InputStream;

/**
 * @Author conglongli
 * @date 2025/1/13 01:23
 */
public class MockFileServiceImpl implements FileService{
    @Override
    public boolean upload(String path, InputStream fileStream) {
        return true;
    }
}
