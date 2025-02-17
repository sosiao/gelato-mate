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

package com.yizlan.gelato.mate.exception;

import com.yizlan.gelato.canonical.exception.UnaryException;
import com.yizlan.gelato.canonical.panic.MetaException;

public class I18nException extends MetaException {
    private static final long serialVersionUID = 1L;

    @Override
    public String getCode() {
        return (String) super.getCode();
    }

    /**
     * Constructs a new i18n exception with the specified code and placeholder.
     *
     * @param code error code
     * @param args placeholder parameters
     */
    public I18nException(final String code, final Object... args) {
        super(code, args);
    }

    /**
     * Constructs a new i18n exception with unary generic interface as parameter and detail message.
     *
     * @param exception unary generic interface
     * @param args      reserved parameters
     */
    public I18nException(final UnaryException<String> exception, final Object... args) {
        super(exception, args);
    }

}
