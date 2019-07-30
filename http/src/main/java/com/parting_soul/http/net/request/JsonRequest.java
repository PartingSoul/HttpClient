package com.parting_soul.http.net.request;

import java.util.Map;

/**
 * @author parting_soul
 * @date 2019-07-23
 */
public final class JsonRequest extends BaseRequest<JsonRequest> {
    private String json;

    public JsonRequest(String url, String json) {
        super(url);
        this.json = json;
    }

    @Override
    public String getJson() {
        return json;
    }

    @Override
    public Map<String, Object> getParams() {
        return null;
    }

    @Override
    public JsonRequest addParam(String key, Object value) {
        return this;
    }

}
