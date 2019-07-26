package com.parting_soul.http.net.params;

import com.parting_soul.http.net.request.BaseRequest;
import com.parting_soul.http.utils.ContentType;
import com.parting_soul.http.utils.UrlUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 原生表单数据输出
 *
 * @author parting_soul
 * @date 2019-07-23
 */
public class FormParamsOutputStrategy extends BaseParamsOutputStrategy {

    @Override
    protected String getContentType() {
        return ContentType.APPLICATION_X_WWW_FORM_URLENCODED;
    }

    @Override
    protected void onWriteParams(OutputStream writer, BaseRequest request) throws IOException {
        String paramsStr = UrlUtils.buildParams(request.getParams());
        writer.write(paramsStr.getBytes());
    }

}
