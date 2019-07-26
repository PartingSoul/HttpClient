package com.parting_soul.http.net.params;

import com.parting_soul.http.net.request.BaseRequest;
import com.parting_soul.http.utils.ContentType;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 上传Json
 *
 * @author parting_soul
 * @date 2019-07-23
 */
public class JsonParamsOutputStrategy extends BaseParamsOutputStrategy {

    @Override
    protected String getContentType() {
        return ContentType.APPLICATION_JSON;
    }

    @Override
    protected void onWriteParams(OutputStream writer, BaseRequest request) throws IOException {
        writer.write(request.getJson().getBytes());
    }
}
