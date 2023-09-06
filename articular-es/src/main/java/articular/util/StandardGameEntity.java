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

package articular.util;

import articular.core.Entity;
import articular.core.component.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines a standard base implementation for the {@link Entity}, the container
 * object of the articular-es API.
 *
 * @param <T> the type of the game components' data
 * @author pavl_g
 */
public abstract class StandardGameEntity<T> implements Entity<T> {

    /**
     * The name for this entity (nullable).
     */
    protected String name;

    /**
     * A map for the game components registered to this entity.
     */
    protected final Map<? super Number, Component<T>> components;

    /**
     * Instantiates a default game entity with a {@link HashMap}.
     *
     * @param name the name of this game entity (nullable)
     */
    public StandardGameEntity(String name) {
        this(name, new HashMap<>());
    }

    /**
     * Instantiates a game entity with a name and game entity components.
     *
     * <p>
     * Subclasses overriding the default constructor should
     * provide a call to this constructor or an acceptable alternative.
     * </p>
     *
     * @param name the name of this game entity (nullable)
     * @param components a map of game entity components (nullable)
     */
    public StandardGameEntity(String name, Map<? super Number, Component<T>> components) {
        this.name = name;
        this.components = components;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<Component<T>> getComponents() {
        return components.values();
    }

    /**
     * Retrieves a specific an entity component by its identifier.
     *
     * @param id the game entity component identifier
     * @return a game entity component or null if it doesn't exist (nullable)
     */
    @Override
    public Component<T> getComponent(Component.Id id) {
        return components.get(id);
    }

    /**
     * Retrieves components of this entity mapped by their identifiers.
     *
     * @see StandardGameEntity#getComponent(Id)
     * @see StandardGameEntity#getComponents()
     * @return a map of game components registered to this entity
     */
    protected Map<? super Number, Component<T>> getComponentsMap() {
        return components;
    }
}