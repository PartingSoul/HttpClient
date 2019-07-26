package com.parting_soul.http.net.request;

import com.parting_soul.http.bean.FilePair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author parting_soul
 * @date 2019-07-23
 */
public class MultipartRequest extends BaseRequest<MultipartRequest> {
    private Map<String, FilePair> mFilePairs;

    public MultipartRequest(String url) {
        super(url);
    }

    @Override
    public Map<String, FilePair> getFilePairs() {
        return mFilePairs;
    }

    /**
     * 添加文件
     *
     * @param key
     * @param pair
     * @return
     */
    public BaseRequest addFilePair(String key, FilePair pair) {
        if (mFilePairs == null) {
            mFilePairs = new HashMap<>();
        }
        mFilePairs.put(key, pair);
        return this;
    }


}
