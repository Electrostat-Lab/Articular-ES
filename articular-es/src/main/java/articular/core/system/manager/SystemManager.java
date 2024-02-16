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

package articular.core.system.manager;

import articular.core.Entity;
import articular.core.MemoryMap;
import articular.core.component.Component;
import articular.core.system.SystemController;
import articular.util.Identifiable;
import articular.util.Validatable;

/**
 * Represents a generic blueprint for ecs managers.
 * The generalized behavior and structure of this
 * class provides better encapsulation, re-usability, and
 * extensibility.
 *
 * @param <M> a generic type for the primary memory map (substrates).
 * @param <S> a generic type for the secondary (second dimensional map) memory map.
 * @param <K> a generic type for the key substrate for the data values.
 * @author pavl_g
 */
public interface SystemManager<M extends MemoryMap, S extends MemoryMap, K extends Identifiable<?>> extends Validatable {

    /**
     * Registers a memory-map under a substrate (e.g.: key).
     *
     * @param substrate the key address.
     * @param memoryMap the memory-map to register.
     * @see SystemManager#unregister(Identifiable)
     */
    void register(K substrate, S memoryMap);

    /**
     * Unregisters a memory-map using the substrate (e.g.: key).
     *
     * @param substrate the key address.
     * @see SystemManager#register(Identifiable, MemoryMap)
     */
    void unregister(K substrate);

    /**
     * Registers a component to a system using the entity as an identifier
     * provider.
     *
     * @param entity           the entity as an identifier provider.
     * @param component        the component to register.
     * @param systemController a system-controller to retrieve the associated system.
     * @see SystemManager#unregister(Entity, SystemController)
     */
    void register(Entity entity, Component component, SystemController systemController);

    /**
     * Unregisters a component from a system using the entity as an identifier
     * provider.
     *
     * @param entity           the entity as an identifier provider.
     * @param systemController a system-controller to retrieve the associated system.
     * @see SystemManager#register(Entity, Component, SystemController)
     */
    void unregister(Entity entity, SystemController systemController);

    /**
     * Allocates a new memory-map under a substrate key.
     *
     * @param substrate the substrate key.
     * @return the newly allocated memory-map.
     */
    S allocateMemoryMap(K substrate);

    /**
     * Allocates an empty component under an entity to a system.
     *
     * @param <T>              the return type as component.
     * @param entity           the entity as an identifier provider.
     * @param systemController a controller to retrieve its associated system.
     * @param id               a component identifier to assign (use a hashing utility to spread the hash-code).
     * @return the newly allocated component (not null).
     */
    <T extends Component>
    T allocateComponent(Entity entity, SystemController systemController, Component.Id id);

    /**
     * Retrieves a component from a memory-map using the entity as an identifier
     * provider.
     *
     * @param <T>              the return type as component.
     * @param entity           the entity as an identifier provider.
     * @param systemController a controller to retrieve its associated system.
     * @return the required component (not null).
     */
    <T extends Component>
    T getComponent(Entity entity, SystemController systemController);

    /**
     * Retrieves the first dimensional memory-map (the substrates' memory-map).
     *
     * @return the substrates memory-map.
     * @see SystemManager#getMemoryMap(Identifiable)
     */
    M getMemoryMap();

    /**
     * Retrieves a specific substrate memory-map.
     *
     * @param substrate a substrate to use.
     * @return a substrate memory map.
     * @see SystemManager#getMemoryMap()
     */
    S getMemoryMap(K substrate);

    /**
     * Tests whether a component is registered under a system using the
     * entity as an identifier provider.
     *
     * @param entity the entity as an ID provider.
     * @param systemController the system controller to retrieve its associated system.
     * @return true if the component is present, false otherwise.
     */
    default boolean hasComponent(Entity entity, SystemController systemController) {
        return getComponent(entity, systemController) != null;
    }

    /**
     * Tests whether a substrate (base) memory-map is registered.
     *
     * @param substrate the substrate object to test against.
     * @return true if the substrate memory-map is present, false otherwise.
     */
    default boolean hasMemoryMap(K substrate) {
        return getMemoryMap(substrate) != null;
    }
}
