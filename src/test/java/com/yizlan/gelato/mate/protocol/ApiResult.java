/*
 * Copyright (C) 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yizlan.gelato.mate.protocol;

import com.yizlan.gelato.canonical.protocol.TerResult;

import java.io.Serializable;
import java.util.Objects;

public class ApiResult<T> implements TerResult<ApiResult<T>, Integer, String, T>, Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private T data;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public ApiResult<T> self() {
        return this;
    }

    public ApiResult() {
        // to do nothing
    }

    private ApiResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResult<T> of() {
        return new ApiResult<>();
    }

    private static <T> ApiResult<T> build(Integer code, String message, T data) {
        return new ApiResult<>(code, message, data);
    }

    public ApiResult<T> failure(Integer code, String message) {
        return build(code, message, null);
    }

    @Override
    public ApiResult<T> failure() {
        return build(500, "failure", null);
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + (Objects.nonNull(data) ? data.toString() : null) +
                '}';
    }

}
