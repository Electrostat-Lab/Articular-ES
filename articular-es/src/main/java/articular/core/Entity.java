/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2024, Articular-ES, The AvrSandbox Project
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

package articular.core;

import articular.core.system.manager.EntityComponentManager;
import articular.util.Validatable;

/**
 * Offers a static ID provider for the {@link MemoryMap.EntityComponentMap}
 * through the {@link EntityComponentManager}.
 *
 * @author pavl_g
 * @see MemoryMap.EntityComponentMap
 * @see EntityComponentManager
 */
public final class Entity implements Validatable {

    private final String name;

    public Entity(String name) {
        this.name = name;
    }

    public Id getId() {
        return new Entity.Id((name.hashCode() >>> 16) ^ name.hashCode());
    }

    /**
     * Represents the component identifier of type number, this encapsulates
     * a constant long value to accommodate larger number of game entity components.
     */
    public static final class Id extends Number {
        private final int id;

        /**
         * Instantiates a game entity component identifier object.
         *
         * @param id the identifier in long format
         */
        public Id(final int id) {
            this.id = id;
        }

        /**
         * Retrieves the identifier in a long format.
         *
         * @return the component identifier in longs
         */
        @Override
        public int intValue() {
            return id;
        }

        @Deprecated
        @Override
        public long longValue() {
            throw new UnsupportedOperationException("Deprecated call, use \"Component.Id#intValue()\"");
        }

        @Deprecated
        @Override
        public float floatValue() {
            throw new UnsupportedOperationException("Deprecated call, use \"Component.Id#intValue()\"");
        }

        @Deprecated
        @Override
        public double doubleValue() {
            throw new UnsupportedOperationException("Deprecated call, use \"Component.Id#intValue()\"");
        }
    }
}