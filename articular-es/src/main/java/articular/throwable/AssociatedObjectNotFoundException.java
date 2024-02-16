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

package articular.throwable;

import articular.util.Validator;

/**
 * Represents a recoverable breakpoint marking a failure in the
 * validation of objects' process as an output of the {@link Validator}
 * API.
 *
 * <p>
 * This exception associates a tracking object with a message; to know the reason
 * of the crash; you have to catch this exception and recover from it by
 * retrieving the causative message object, then act accordingly.
 * </p>
 *
 * @author pavl_g
 * @see Validator
 */
public class AssociatedObjectNotFoundException extends RuntimeException {

    /**
     * An exception tracking object to differentiate
     * between different breaking points while debugging.
     */
    protected final Validator.Message validatorMessage;

    /**
     * Instantiates a new recoverable application breakpoint
     * denoting an invalid {@link articular.util.Validatable}.
     *
     * @param validatorMessage a tracking object to associate.
     */
    public AssociatedObjectNotFoundException(Validator.Message validatorMessage) {
        super(validatorMessage.getMessage());
        this.validatorMessage = validatorMessage;
    }

    /**
     * Retrieves the tracking object of the responsible
     * event.
     *
     * @return the tracking object of this exception.
     */
    public Validator.Message getValidatorMessage() {
        return validatorMessage;
    }
}
