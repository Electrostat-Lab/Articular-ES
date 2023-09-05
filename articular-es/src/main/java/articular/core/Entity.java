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

package articular.core;

import articular.core.component.Component;
import java.util.Map;

/**
 * Represents a game entity with some {@link Component}s.
 *
 * <p>
 * A game entity is considered also a game component object,
 * this fact provides a pattern to implement sub-entities.
 * </p>
 *
 * <p>
 * A Map of components must be provided via a DI pattern.
 * </p>
 *
 * @param <I> the input type of the game loop
 * @author pavl_g
 */
public interface Entity<I> extends Component<I> {

    /**
     * A Map of game entity components to be provided.
     *
     * <p>
     * Override and provide a map of components for this entity via the DI pattern.
     * </p>
     *
     * @return a map of components by their identifiers (not null)
     */
    Map<? super Number, Component<I>> getComponents();

    /**
     * Retrieves the name of this entity.
     *
     * <p>
     * Override and provide a name for this entity via the DI pattern.
     * </p>
     *
     * @return the name of this entity (not null)
     */
    String getName();

    /**
     * Retrieves a specific game entity component by its identifier.
     *
     * @param id the game entity component identifier
     * @return a game entity component or null if it doesn't exist (nullable)
     */
    default Component<I> getComponent(Component.Id id) {
        return getComponents().get(id);
    }

    /**
     * Updates this game entity through updating its components.
     *
     * @param input the input to the game loop pattern
     */
    default void update(I input) {
        getComponents().forEach((id, component) -> component.update(input));
    }
}
