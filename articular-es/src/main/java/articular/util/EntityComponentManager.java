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
import articular.core.Type;
import articular.core.component.Component;
import articular.core.system.*;

/**
 *
 * @param <I>
 * @author pavl_g
 */
public class EntityComponentManager<I> {

    /**
     * A Collection of game entity components to be provided mapped
     * by their types.
     */
    protected Type.SystemMap systems = new Type.SystemMap();

    /**
     * Instantiates an articulation manager object.
     */
    public EntityComponentManager() {
    }

    public Type.EntityMap getSystemComponents(SystemController systemController) {
        return systems.get(systemController.getAssociatedSystem());
    }

    /**
     * Retrieves the entity components in a particular system.
     *
     * @param systemController the system
     * @param entity           the entity
     * @return a map of components in a system-entity
     */
    public Type.ComponentMap getEntityComponents(SystemController systemController, Entity entity) {
        Type.EntityMap systemComponents = getSystemComponents(systemController);
        Validator.validate(systemComponents, systemController);
        return systemComponents.get(entity.getId().intValue());
    }

    public Component getEntityComponent(SystemController systemController, Entity entity,
                                        Component.Id componentId) {
        Type.ComponentMap entityComponents = getEntityComponents(systemController, entity);
        Validator.validate(entityComponents, entity);
        return entityComponents.get(componentId.intValue());
    }

    public Entity createEntity(SystemController systemController, String name) {
        return createEntity(new SystemController[]{ systemController }, name);
    }

    public Entity createEntity(SystemController[] systemControllers, String name) {
        final Entity entity = new Entity(name);
        for (SystemController systemController : systemControllers) {
            Type.EntityMap entityMap = systems.get(systemController.getAssociatedSystem());
            Validator.validate(entityMap, systemController);
            entityMap.put(entity.getId().intValue(), new Type.ComponentMap());
        }
        return entity;
    }

    /**
     * Registers a component under an entity to a system.
     *
     * @param component        the component to register
     * @param entity           the entity under which the component will be registered
     * @param systemController the system to which the entity resides holding the system
     *                         components
     */
    public void register(Component component, Entity entity, SystemController systemController) {
        getEntityComponents(systemController, entity).put(component.getId().intValue(), component);
    }

    /**
     * Attaches an entity to a controller-associated system.
     *
     * @param entity           the entity
     * @param systemController the controller to use its associated system
     */
    public void attachEntity(Entity entity, SystemController systemController) {
        Type.EntityMap entityMap = getSystemComponents(systemController);
        Validator.validate(entityMap, systemController);
        entityMap.put(entity.getId().intValue(), new Type.ComponentMap());
    }

    /**
     * Attaches an entity to multiple controllers-associated systems.
     *
     * @param entity            the entity to be attached to the systems
     * @param systemControllers the systems to attach the entity to
     */
    public void attachEntity(Entity entity, SystemController... systemControllers) {
        for (SystemController systemController : systemControllers) {
            attachEntity(entity, systemController);
        }
    }

    /**
     * Attaches a controller-associated system, successive
     * calls will replace previous objects.
     *
     * @param systemController the system controller to retrieve the associated system
     */
    public void attachAssociatedSystem(SystemController systemController) {
        systems.put(systemController.getAssociatedSystem(), new Type.EntityMap());
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
     * Tests whether an entity is present in a controller-associated system.
     *
     * @param systemController
     * @param entity
     * @return
     */
    public boolean hasEntityComponents(SystemController systemController, Entity entity) {
        return getEntityComponents(systemController, entity) != null;
    }

    public boolean hasEntityComponent(SystemController systemController, Entity entity,
                                      Component component) {
        Component component1 = getEntityComponent(systemController, entity, component.getId());
        return component1 != null && component1 == component;
    }

    /**
     * Forwards all systems to a single dispatch.
     *
     * @param systemsUpdater the system representing the dispatch source
     * @param input          the input of the game loop
     */
    public void updateSystems(SystemsUpdater<I> systemsUpdater, I input) {
        systemsUpdater.update(systems, this, input);
    }

    /**
     * Forwards a system entities to a single dispatch.
     *
     * @param updater the system that holds the components in an entity (aka. ID)
     * @param input   the input of the game loop
     */
    public void updateSystemEntities(SystemEntitiesUpdater<I> updater, I input) {
        Type.EntityMap entityMap = systems.get(updater.getAssociatedSystem());
        updater.update(entityMap, this, input);
    }

    /**
     * Forwards the system components in an entity to a component updater.
     *
     * @param updater the controller that holds the components in an entity (aka. ID)
     * @param entity  the entity or the ID under which the components are stored
     * @param input   the input of the game loop
     */
    public void updateSystemComponents(ComponentsUpdater<I> updater, Entity entity, I input) {
        Type.ComponentMap componentMap = systems.get(updater.getAssociatedSystem())
                .get(entity.getId().intValue());
        updater.update(componentMap, entity, this, input);
    }
}