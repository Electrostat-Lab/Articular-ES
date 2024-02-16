/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2023-2024, Articular-ES, The AvrSandbox Project
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

package articular.util;

import articular.throwable.AssociatedObjectNotFoundException;

/**
 * Provides a validator utility to validate parameters and return
 * values, and throw a generic exception {@link AssociatedObjectNotFoundException}
 * that wraps a {@link Validator.Message} object identifying the faulty operation.
 *
 * @author pavl_g
 * @see Validatable
 * @see AssociatedObjectNotFoundException
 */
public final class Validator {

    /**
     * Provides a validator message object to track the faulty operation.
     */
    public enum Message {

        /**
         * Denotes that the associated {@link articular.core.system.ArticularSystem} is invalid.
         */
        INVALID_ASSOCIATED_SYSTEM("Associated System is invalid!"),

        /**
         * Denotes that the associated {@link articular.core.MemoryMap.EntityComponentMap}
         * is invalid.
         */
        INVALID_ASSOCIATED_ENTITY_COMPONENT_MAP("Entity-Component map is invalid!"),

        /**
         * Denotes that the system component map {@link articular.core.MemoryMap.SystemComponentMap} is invalid.
         */
        INVALID_ASSOCIATED_SYSTEM_COMPONENT_MAP("Cache is not found or disabled!"),

        /**
         * Denotes an invalid {@link articular.core.Entity}.
         */
        INVALID_ENTITY("Entity is invalid!"),

        /**
         * Denotes an invalid {@link articular.core.component.Component}.
         */
        INVALID_COMPONENT("Component is invalid!"),

        /**
         * Denotes an invalid {@link articular.core.system.data.DataPipe}.
         */
        INVALID_DATA_PIPE("Data-pipe is invalid!"),

        /**
         * Denotes an invalid identifier {@link Identifiable#getId()}.
         */
        INVALID_ID("Identifier is invalid!");

        private final String message;

        Message(String message) {
            this.message = message;
        }

        /**
         * Retrieves the message content in string format.
         *
         * @return the message content in string format.
         */
        public String getMessage() {
            return message;
        }
    }

    /**
     * Validates an object against [null] value, and
     * throws an exception with a message on failure.
     *
     * @param validatable the object to validate.
     * @param msg         a message to display on validation failure.
     * @throws AssociatedObjectNotFoundException if the validation fails and the
     *                                           object is [null]
     */
    public static void validate(Validatable validatable, Message msg)
            throws AssociatedObjectNotFoundException {
        validate(validatable, null, msg);
    }

    /**
     * Validates the equivalency of two objects, and
     * throws an exception with a message on failure.
     *
     * @param substrate0 the first object.
     * @param substrate1 the second object to validate against.
     * @param msg        a message to display on failure.
     * @throws AssociatedObjectNotFoundException if the validation fails and
     *                                           the two objects are equivalent.
     */
    public static void validate(Validatable substrate0, Validatable substrate1, Message msg)
            throws AssociatedObjectNotFoundException {
        if (substrate0 != substrate1) {
            return;
        }
        throw new AssociatedObjectNotFoundException(msg);
    }
}
