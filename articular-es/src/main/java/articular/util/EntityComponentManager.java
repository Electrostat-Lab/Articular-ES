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
 * @param <I> the type of the input to the game loop
 * @author pavl_g
 */
public class EntityComponentManager<I> {

    /**
     * The proposed Game entities
     */
    protected Map<? super Number, Entity> entities;

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
    public void register(StandardGameEntity entity) {
        entities.put(entity.getId().longValue(), entity);
    }

    /**
     * Unregisters a game entity to this game articulation manager.
     *
     * @param entity a game entity instance to unregister
     */
    public void unregister(StandardGameEntity entity) {
        entities.remove(entity.getId().longValue(), entity);
    }

    /**
     * Registers a game entity component to a game entity.
     *
     * @param entity the game entity instance
     * @param component the component instance to register to this game entity
     */
    public void register(StandardGameEntity entity, Component component) {
        entity.getComponentsMap().put(component.getId().longValue(), component);
    }

    /**
     * Unregisters a game entity component from a game entity.
     *
     * @param entity the game entity instance
     * @param component the component instance to be unregistered from this game entity
     */
    public void unregister(StandardGameEntity entity, Component component) {
        entity.getComponentsMap().remove(component.getId().longValue());
    }

    /**
     * Updates the registered game entities and their components.
     *
     * @param input the game loop input value
     */
    @SuppressWarnings("unchecked")
    public void update(I input) {
        entities.forEach((key, entity) -> {
            if (entity instanceof UpdatableEntity) {
                ((UpdatableEntity<I>) entity).update(input);
            }
        });
    }

    /**
     * Retrieves the registered game entities.
     *
     * @return a collection of the registered game entities
     */
    public Collection<Entity> getEntities() {
        return entities.values();
    }
}