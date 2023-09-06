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
import java.util.Collection;

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
 * @param <T> the type of the game component objects
 * @author pavl_g
 */
public interface Entity<T> extends Component<T> {

    /**
     * A Collection of game entity components to be provided.
     *
     * @return a collection of game entity's components
     */
    Collection<Component<T>> getComponents();

    /**
     * Retrieves a game entity's component by its identifier.
     *
     * @param id the component identifier
     * @return a game entity component or a game sub-entity
     */
    Component<T> getComponent(Component.Id id);

    /**
     * Retrieves the name of this entity.
     *
     * @return the name of this entity (nullable)
     */
    String getName();
}
