/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2023, Articular-ES, The AvrSandbox Project
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package articular.core.component;

import articular.core.Entity;
import articular.util.EntityComponentManager;
import java.lang.reflect.Field;

/**
 * A template representing a Game {@link Entity} housing data objects.
 *
 * @param <T> the type of the component object data in memory
 * @author pavl_g
 */
@SuppressWarnings("unchecked")
public interface Component<T> {

    /**
     * Retrieves all the declared fields in an array of {@link Field}.
     *
     * @return the data fields of this component wrapped in an array of Field pointers
     */
    default Field[] getData() {
        return getClass().getDeclaredFields();
    }

    /**
     * Retrieves a data field by its name in a field pointer.
     *
     * @param fieldName the field name in the component class
     * @return a reference object to the required field
     * @throws NoSuchFieldException if the submitted field is not found in this component
     * @throws IllegalAccessException if the field is inaccessible by means of private access modifiers
     */
    default T getData(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return (T) getClass().getDeclaredField(fieldName).get(this);
    }

    /**
     * Retrieves the identifier (id) of this game component, the identifier is
     * used by the {@link EntityComponentManager} to link
     * game components to their entities (aka. articulation).
     *
     * @return the component identifier object
     */
    Component.Id getId();

    /**
     * Represents the component identifier of type number, this encapsulates
     * a constant long value to accommodate larger number of game entity components.
     */
    public static final class Id extends Number {
        private final long id;

        /**
         * Instantiates a game entity component identifier object.
         *
         * @param id the identifier in long format
         */
        public Id(final long id) {
            this.id = id;
        }

        /**
         * Retrieves the identifier in a long format.
         *
         * @return the component identifier in longs
         */
        @Override
        public long longValue() {
            return id;
        }

        @Deprecated
        @Override
        public int intValue() {
            throw new UnsupportedOperationException("Deprecated call, use \"Component.Id#longValue()\"");
        }

        @Deprecated
        @Override
        public float floatValue() {
            throw new UnsupportedOperationException("Deprecated call, use \"Component.Id#longValue()\"");
        }

        @Deprecated
        @Override
        public double doubleValue() {
            throw new UnsupportedOperationException("Deprecated call, use \"Component.Id#longValue()\"");
        }
    }
}
