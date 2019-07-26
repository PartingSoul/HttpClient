package com.parting_soul.http.annotation;

import android.support.annotation.StringDef;

import com.parting_soul.http.net.HttpMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 请求方式
 *
 * @author parting_soul
 * @date 2019-07-25
 */
@StringDef({
        HttpMethod.GET,
        HttpMethod.POST
})
@Retention(RetentionPolicy.SOURCE)
public @interface HttpMethodRule {
}
