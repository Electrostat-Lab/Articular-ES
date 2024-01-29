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

package articular.util;

import articular.core.Entity;
import articular.core.Type;
import articular.core.component.Component;
import articular.core.system.ComponentsUpdater;
import articular.core.system.SystemController;
import articular.core.system.SystemEntitiesUpdater;
import articular.core.system.SystemsUpdater;
import java.util.Objects;

/**
 * Represents the entry and the operative point of the articular-es for
 * system-entity-component manipulation as a primary layout, and entity-system-component
 * as a cache layout that is controllable via {@link CacheManager}.
 *
 * @param <I> the type of the input for the update loop
 * @author pavl_g
 * @see CacheManager for entity-system-component layouting
 * @see SystemController for instantiating systems
 */
public class EntityComponentManager<I> implements SystemManager {

    /**
     * A Collection of game entity components to be provided mapped
     * by their types.
     */
    protected Type.SystemMap systems = new Type.SystemMap();

    /**
     *
     */
    protected CacheManager cacheManager = new CacheManager();

    /**
     * Instantiates an articulation manager object.
     */
    public EntityComponentManager() {
    }

    public Type.EntityComponentMap getSystemComponents(SystemController systemController) {
        Validator.validate(systemController, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        return systems.get(systemController.getAssociatedSystem());
    }

    public Component getEntityComponent(SystemController systemController, Entity entity) {
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        Type.EntityComponentMap components = getSystemComponents(systemController);
        Validator.validate(components, Validator.Message.ASSOCIATED_ENTITY_COMPONENT_MAP_NOT_FOUND);
        return components.get(entity.getId().intValue());
    }

    /**
     * Attaches a controller-associated system, successive
     * calls will replace previous objects.
     *
     * @param systemController the system controller to retrieve the associated system
     */
    public void attachAssociatedSystem(SystemController systemController) {
        Validator.validate(systemController, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        systems.put(systemController.getAssociatedSystem(), new Type.EntityComponentMap());

        if (!cacheManager.isEnableCaching()) {
            return;
        }
        // not supported! (limited mental model)
        // cannot cache to the [?][system](component) layout; missing entity
    }

    /**
     * Allocates a new empty component under this entity to a controller-associated system.
     * A dispatch to this method creates a new initialized memory of type {@link Component} under
     * the id {@link Entity#getId()}.
     *
     * @param entity           the entity to use its identifier
     * @param systemController the controller to use its associated system
     */
    public void allocateComponent(Entity entity, SystemController systemController) {
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        final Type.EntityComponentMap components = getSystemComponents(systemController);
        Validator.validate(components, Validator.Message.ASSOCIATED_ENTITY_COMPONENT_MAP_NOT_FOUND);
        final Component component = new Component() {};
        components.put(entity.getId().intValue(), component);

        if (!cacheManager.isEnableCaching()) {
            return;
        }
        // cache to the [entity][system](component) layout
        cacheManager.register(entity, component, systemController);
    }

    /**
     * Allocates new components under different systems.
     *
     * @param entity            the entity to be attached to the systems
     * @param systemControllers the systems to attach the entity to
     * @see EntityComponentManager#allocateComponent(Entity, SystemController)
     */
    public void allocateComponent(Entity entity, SystemController... systemControllers) {
        for (SystemController systemController : systemControllers) {
            allocateComponent(entity, systemController);
        }
    }

    public Entity createEntity(SystemController systemController, String name) {
        return createEntity(new SystemController[]{ systemController }, name);
    }

    public Entity createEntity(SystemController[] systemControllers, String name) {
        final Entity entity = new Entity(name);
        for (SystemController systemController : Objects.requireNonNull(systemControllers)) {
            Validator.validate(systemController, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
            allocateComponent(entity, systemController);
        }
        return entity;
    }

    /**
     * Registers a component under an entity to a system.
     *
     * @param entity           the entity under which the component will be registered
     * @param component        the component to register
     * @param systemController the system to which the entity resides holding the system
     *                         components
     */
    @Override
    public void register(Entity entity, Component component, SystemController systemController) {
        Validator.validate(component, Validator.Message.COMPONENT_NOT_FOUND);
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        getSystemComponents(systemController).put(entity.getId().intValue(), component);

        if (!cacheManager.isEnableCaching()) {
            return;
        }
        // cache to the [entity][system](component) layout
        cacheManager.register(entity, component, systemController);
    }

    /**
     * Tests whether a controller-associated system is attached.
     *
     * @param systemController the controller to get its associated system
     * @return true if the controller-associated system is attached, false otherwise
     */
    public boolean hasSystemComponents(SystemController systemController) {
        return getSystemComponents(systemController) != null;
    }

    /**
     * Asserts whether an entity under a specific system is existing.
     *
     * @param systemController the controller to retrieve the associated system
     * @param entity the entity to retrieve the component
     * @return
     */
    public boolean hasEntityComponent(SystemController systemController, Entity entity) {
        return getEntityComponent(systemController, entity) != null;
    }

    /**
     * Asserts whether a system would have the same component object under
     * a specific entity.
     *
     * @param systemController
     * @param entity
     * @param component
     * @return
     */
    public boolean hasSameComponent(SystemController systemController, Entity entity,
                                      Component component) {
        Component component1 = getEntityComponent(systemController, entity);
        return component1 != null && component1 == component;
    }

    /**
     * Forwards all systems to a single dispatch.
     *
     * @param updater the dispatch source (not null)
     * @param input          the input of the game loop (nullable)
     */
    public void updateSystems(SystemsUpdater<I> updater, I input) {
        Validator.validate(updater, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        updater.update(systems, this, input);
    }

    public void updateSystemComponents(SystemEntitiesUpdater<I> updater, I input) {
        Validator.validate(updater, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        Type.EntityComponentMap components = systems.get(updater.getAssociatedSystem());
        Validator.validate(components, Validator.Message.ASSOCIATED_ENTITY_COMPONENT_MAP_NOT_FOUND);
        updater.update(components, this, input);
    }

    public void updateEntityComponents(ComponentsUpdater<I> updater, Entity entity, I input) {
        Validator.validate(updater, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        if (!cacheManager.isEnableCaching()) {
            // do a realtime manipulation every time, Omega notation of (n), linear CPU clock cycles
            final Type.SystemComponentMap components = new Type.SystemComponentMap();
            systems.forEach((associatedSystem, entityComponentMap) ->
                    components.put(associatedSystem, getEntityComponent(updater, entity)));
            updater.update(components, entity, this, input);
        } else {
            // do a manipulation from the cache, constant omega notation, single CPU clock cycles
            // manipulate cache of the [entity][system](component) layout
            updater.update(cacheManager.getSystemComponentMap(entity), entity, this, input);
        }
    }
}