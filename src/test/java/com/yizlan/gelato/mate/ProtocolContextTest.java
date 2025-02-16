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

package com.yizlan.gelato.mate;

import com.yizlan.gelato.mate.client.ProtocolContext;
import com.yizlan.gelato.mate.dto.Gender;
import com.yizlan.gelato.mate.exception.I18nException;
import com.yizlan.gelato.mate.protocol.ApiResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class ProtocolContextTest {

    private static final ApiResult<Gender> API_RESULT = new ApiResult<>();
    private static final ApiResult<Gender> RESULT = new ApiResult<>();

    @BeforeEach
    void init() {
        Gender gender = new Gender();
        gender.setCode(1);
        gender.setName("男");

        API_RESULT.setCode(200);
        API_RESULT.setMessage("success");
        API_RESULT.setData(gender);

        RESULT.setCode(200);
        RESULT.setData(gender);
    }

    @Test
    public void test() {
        ProtocolContext<ApiResult<Gender>, Integer, String, Gender> protocolContext = ProtocolContext.of(API_RESULT);

        // 200
        Integer code = protocolContext.getCode();
        assert code.equals(200);

        // true
        boolean codeEquals = protocolContext.codeEquals(200);
        assert codeEquals;

        // false
        boolean codeNotEquals = protocolContext.codeNotEquals(200);
        assert !codeNotEquals;

        // message
        protocolContext.getMessage().ifPresent(m -> System.out.printf("message: %s.%n", m));

        // data
        protocolContext
                .assertData(Objects::nonNull, r -> new I18nException("error：" + r.getCode()))
                .getData()
                .ifPresent(System.out::println);
        protocolContext.getData(m -> {
                    System.out.printf("The current code is %s. When code is 200, printing data => ", m.getCode());
                    return m.getCode() == 200;
                })
                .ifPresent(System.out::println);

        // protocol data
        ApiResult<Gender> peek = protocolContext
                .assertCode(m -> m == 200, r -> new I18nException("error：" + r.getCode()))
                .peek();
        System.out.println(peek);

        // convert protocol data
        ProtocolContext<ApiResult<String>, Integer, String, String> map = protocolContext
                .map(m -> {
                    Gender gender = m.getData();
                    ApiResult<String> targetApiResult = ApiResult.of();
                    targetApiResult.setCode(m.getCode());
                    targetApiResult.setMessage(m.getMessage());
                    targetApiResult.setData(gender.getName());
                    return targetApiResult;
                });
        ApiResult<String> peekTarget = map.peek();
        System.out.println(peekTarget);

        // consumer
        ProtocolContext.of(RESULT)
                .accept(m -> System.out.printf("RESULT：%s%n", m));
        ProtocolContext.of(RESULT)
                .accept(m -> {
                    String info = String.format("The current code is %s. When code is 500, printing data.%n", m.getCode());
                    if (m.getCode() == 500) {
                        info += "Result data is：";
                    }
                    System.out.print(info);
                    return m.getCode() == 500;
                }, System.out::println);

    }
}
