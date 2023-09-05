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
import java.util.HashMap;
import java.util.Map;

/**
 * The Entity Component System (ECS) Manager manages the articulation (linkage) between game entities
 * and game entities' components.
 *
 * <p>
 * This utility is optional to use, it helps in writing good applications.
 * </p>
 *
 * <p>
 * This utility is not thread-safe.
 * </p>
 *
 * <p>
 * This utility is best used as a composited object inside a Game Engine State class registered
 * to the game loop.
 * </p>
 *
 * @param <I> the input type to the game loop pattern
 * @author pavl_g
 */
public class EntityComponentManager<I> {

    /**
     * The proposed Game entities
     */
    protected Map<String, Entity<I>> entities;

    /**
     * Instantiates an articulation manager object by
     * initializing a {@link HashMap} of game {@link Entity}s.
     */
    public EntityComponentManager() {
        this.entities = new HashMap<>();
    }

    /**
     * Registers a game entity to this game articulation manager.
     *
     * @param entity a game entity instance to register
     */
    public void register(Entity<I> entity) {
        entities.put(entity.getName(), entity);
    }

    /**
     * Unregisters a game entity to this game articulation manager.
     *
     * @param entity a game entity instance to unregister
     */
    public void unregister(Entity<I> entity) {
        entities.remove(entity.getName(), entity);
    }

    /**
     * Registers a game entity component to a game entity.
     *
     * @param entity the game entity instance
     * @param component the component instance to register to this game entity
     */
    public void register(Entity<I> entity, Component<I> component) {
        entity.getComponents().put(component.getId().longValue(), component);
    }

    /**
     * Unregisters a game entity component from a game entity.
     *
     * @param entity the game entity instance
     * @param component the component instance to be unregistered from this game entity
     */
    public void unregister(Entity<I> entity, Component<I> component) {
        entity.getComponents().remove(component.getId().longValue());
    }

    /**
     * Updates the registered game entities and their components.
     *
     * @param input the game loop input value
     */
    public void update(I input) {
        entities.forEach((key, entity) -> entity.update(input));
    }

    /**
     * Retrieves the registered game entities.
     *
     * @return a map of the registered game entities
     */
    public Map<String, Entity<I>> getEntities() {
        return entities;
    }
}