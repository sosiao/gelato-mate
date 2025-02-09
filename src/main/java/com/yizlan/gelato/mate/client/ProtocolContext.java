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

package com.yizlan.gelato.mate.client;

import com.yizlan.gelato.core.protocol.TerResult;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents the context of a data protocol, providing methods to access and assert the protocol's code, message,
 * and data.
 *
 * @param <P> the type of data protocol itself that implements {@link TerResult}
 * @param <T> the type of the code field, should implement {@link Comparable} and {@link Serializable}
 * @param <U> the type of the message field, should implement {@link Comparable} and {@link Serializable}
 * @param <S> the type of the data filed
 * @author Zen Gershon
 * @since 1.0
 */
public class ProtocolContext<P extends TerResult<T, U, S>, T extends Comparable<T> & Serializable,
        U extends Comparable<U> & Serializable, S> {

    protected final P original;

    protected ProtocolContext(P original) {
        this.original = original;
    }

    /**
     * Factory method for creating a new ProtocolContext instance.
     *
     * @param original the original protocol object
     * @param <P>      the type of data protocol
     * @param <T>      the type of the code field
     * @param <U>      the type of the message field
     * @param <S>      the type of the data field
     * @return a new instance of ProtocolContext
     */
    public static <P extends TerResult<T, U, S>, T extends Comparable<T> & Serializable,
            U extends Comparable<U> & Serializable, S> ProtocolContext<P, T, U, S> of(P original) {
        return new ProtocolContext<>(Objects.requireNonNull(original));
    }

    public T getCode() {
        return original.getCode();
    }

    public Optional<U> getMessage() {
        return Optional.ofNullable(original.getMessage());
    }

    public Optional<S> getData() {
        return Optional.ofNullable(original.getData());
    }

    public Optional<S> getData(Predicate<? super P> predicate) {
        assertNonNull(predicate);
        return predicate.test(original) ? getData() : Optional.empty();
    }

    /**
     * Compares the code of the protocol with the specified value for equality.
     *
     * @param value the value to compare with the code of the protocol
     * @return {@code true} if the code of the protocol is equal to the specified value, otherwise {@code false}
     */
    public boolean codeEquals(T value) {
        return Objects.equals(original.getCode(), value);
    }

    /**
     * Compares the code of the protocol with the specified value for inequality.
     *
     * @param value the value to compare with the code of the protocol
     * @return {@code true} if the code of the protocol is not equal to the specified value, otherwise {@code false}
     */
    public boolean codeNotEquals(T value) {
        return !codeEquals(value);
    }

    /**
     * Returns the current protocol object.
     *
     * @return the current protocol object
     */
    public P peek() {
        return original;
    }

    /**
     * Asserts that the specified arguments are not null.
     *
     * @param args the arguments to check
     */
    private void assertNonNull(Object... args) {
        for (Object arg : args) {
            Objects.requireNonNull(arg);
        }
    }

    public <Q extends TerResult<T, U, R>, R> ProtocolContext<Q, T, U, R> map(Function<? super P, ? extends Q> mapper) {
        assertNonNull(mapper);
        Q protocol = mapper.apply(original);
        return of(protocol);
    }

    /**
     * Performs an operation on the data of the current protocol context object.
     *
     * @param consumer a non-interfering, stateless operation to perform on the data
     */
    public void accept(Consumer<? super S> consumer) {
        assertNonNull(consumer);
        consumer.accept(original.getData());
    }

    /**
     * Performs an operation on the data of the current protocol context object if it meets the specified condition.
     *
     * @param predicate a non-interfering, stateless predicate to determine whether to perform the operation
     * @param consumer  a non-interfering, stateless operation to perform on the data
     */
    public void accept(Predicate<? super P> predicate, Consumer<? super S> consumer) {
        assertNonNull(predicate, consumer);
        if (predicate.test(original)) {
            consumer.accept(original.getData());
        }
    }
}
