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

/**
 * A template representing a Game {@link Entity} component.
 *
 * @param <I> the input type to the game loop pattern
 * @author pavl_g
 */
public interface Component<I> {

    /**
     * Retrieves the entity that encapsulates this component.
     *
     * <p>
     * Override this method and use the DI pattern to link the component
     * entity here.
     * </p>
     *
     * @return the entity encapsulating this game component
     */
    Entity<I> getEntity();

    /**
     * Updates this game component with an abstract input.
     *
     * <p>
     * Override this method and use the Game loop pattern to link the
     * original game states to this component via the game {@link Entity}s.
     * </p>
     *
     * <p>
     * This method is dispatched by {@link Entity#update(Object)}, never call
     * it manually!
     * </p>
     *
     * @param input an input to perform the update on
     */
    void update(I input);

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
            throw new UnsupportedOperationException("Deprecated call, use `Component.Id#getId()`");
        }

        @Deprecated
        @Override
        public float floatValue() {
            throw new UnsupportedOperationException("Deprecated call, use `Component.Id#getId()`");
        }

        @Deprecated
        @Override
        public double doubleValue() {
            throw new UnsupportedOperationException("Deprecated call, use `Component.Id#getId()`");
        }
    }
}
