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
import articular.core.system.ComponentUpdater;
import articular.core.system.SystemController;
import articular.core.system.SystemEntitiesUpdater;
import articular.core.system.SystemsUpdater;
import articular.core.system.data.DataPipe;
import articular.util.Validator;
import java.util.Objects;

/**
 * @param <I> the type of the input for the update loop
 * @author pavl_g
 * @see CacheManager for entity-system-component layouting
 * @see SystemController for instantiating systems
 */
@SuppressWarnings("unchecked")
public class EntityComponentManager<I> implements SystemManager<MemoryMap.SystemMap, MemoryMap.EntityComponentMap, SystemController> {

    /**
     * Provides a memory-map for registering systems; systems
     * are the first sets in the ECS architectures.
     */
    protected MemoryMap.SystemMap systems = new MemoryMap.SystemMap();

    /**
     * Provides a memory-map for data-pipes as an extension to the
     * data-flow pattern between systems (user-side).
     */
    protected MemoryMap.DataPipeMap dataPipeMap = new MemoryMap.DataPipeMap();

    /**
     * Instantiates a new basic system-first ecs-manager.
     */
    public EntityComponentManager() {
    }

    @Override
    public <T extends Component> T allocateComponent(Entity entity, SystemController systemController, Component.Id id) {
        final Component component = () -> id;
        register(entity, component, systemController);
        return (T) component;
    }

    /**
     * Allocates and creates a new entity under the system
     * associated with this system-controller.
     *
     * @param systemController the system controller to retrieve its system (not null).
     * @param name the name of the entity (used to hash an identifier).
     * @return the newly allocated entity registered to that system.
     */
    public Entity createEntity(SystemController systemController, String name) {
        return createEntity(new SystemController[]{systemController}, name);
    }

    /**
     * Allocates and creates a new entity under the systems
     * associated with those system-controllers
     *
     * @param systemControllers the system controllers to retrieve their systems (not null).
     * @param name the name of the entity (used to hash an identifier) (not null).
     * @return the newly allocated entity registered to those systems.
     */
    public Entity createEntity(SystemController[] systemControllers, String name) {
        final Entity entity = new Entity(name);
        for (SystemController systemController : Objects.requireNonNull(systemControllers)) {
            Validator.validate(systemController, Validator.Message.INVALID_ASSOCIATED_SYSTEM);
            allocateComponent(entity, systemController, entity.getId());
        }
        return entity;
    }

    @Override
    public <T extends Component> T getComponent(Entity entity, SystemController systemController) {
        Validator.validate(entity, Validator.Message.INVALID_ENTITY);
        MemoryMap.EntityComponentMap components = getMemoryMap(systemController);
        Validator.validate(components, Validator.Message.INVALID_ASSOCIATED_ENTITY_COMPONENT_MAP);
        return (T) components.get(entity.getId().longValue());
    }

    @Override
    public MemoryMap.SystemMap getMemoryMap() {
        return systems;
    }

    @Override
    public MemoryMap.EntityComponentMap getMemoryMap(SystemController systemController) {
        Validator.validate(systemController, Validator.Message.INVALID_ASSOCIATED_SYSTEM);
        return systems.get(systemController.getId().getId());
    }

    @Override
    public void register(SystemController systemController, MemoryMap.EntityComponentMap memoryMap) {
        Validator.validate(systemController, Validator.Message.INVALID_ASSOCIATED_SYSTEM);
        Validator.validate(memoryMap, Validator.Message.INVALID_ASSOCIATED_ENTITY_COMPONENT_MAP);
        systems.put(systemController.getId().getId(), memoryMap);
    }

    @Override
    public void unregister(SystemController systemController) {
        Validator.validate(systemController, Validator.Message.INVALID_ASSOCIATED_SYSTEM);
        systems.remove(systemController.getId().getId());
    }

    @Override
    public void register(Entity entity, Component component, SystemController systemController) {
        Validator.validate(component, Validator.Message.INVALID_COMPONENT);
        Validator.validate(entity, Validator.Message.INVALID_ENTITY);
        Validator.validate(systemController, Validator.Message.INVALID_ASSOCIATED_SYSTEM);
        getMemoryMap(systemController).put(entity.getId().longValue(), component);
    }

    @Override
    public void unregister(Entity entity, SystemController systemController) {
        Validator.validate(entity, Validator.Message.INVALID_ENTITY);
        Validator.validate(systemController, Validator.Message.INVALID_ASSOCIATED_SYSTEM);
        getMemoryMap(systemController).remove(entity.getId().longValue());
    }

    /**
     * Registers a data-pipe object as an intruder of the
     * data-flow pattern among system controllers.
     *
     * @param dataPipe the data pipe object (not null).
     * @param <T> the type of the algorithm return
     * @param <A> the type of the algorithm parameter
     */
    public <T, A> void registerDataPipe(DataPipe<T, A> dataPipe) {
        registerDataPipe(dataPipe.getId(), dataPipe);
    }

    /**
     * Registers a data-pipe object using an external identifier.
     *
     * @param id the identifier to use (not null).
     * @param dataPipe the data pipe object (not null).
     * @param <T> the type of the algorithm return.
     * @param <A> the type of the algorithm parameter.
     */
    public <T, A> void registerDataPipe(Component.Id id, DataPipe<T, A> dataPipe) {
        Validator.validate(id, Validator.Message.INVALID_ID);
        Validator.validate(dataPipe, Validator.Message.INVALID_DATA_PIPE);
        dataPipeMap.put(id.longValue(), dataPipe);
    }

    /**
     * Registers a data-pipe object using the object identifier.
     *
     * @param dataPipe the data pipe object (not null).
     * @param <T> the type of the algorithm return.
     * @param <A> the type of the algorithm parameter.
     */
    public <T, A> void unregisterDataPipe(DataPipe<T, A> dataPipe) {
        Validator.validate(dataPipe, Validator.Message.INVALID_DATA_PIPE);
        unregisterDataPipe(dataPipe.getId());
    }

    /**
     * Registers a data-pipe object using an external identifier.
     *
     * @param id the identifier to use.
     */
    public void unregisterDataPipe(Component.Id id) {
        dataPipeMap.remove(id.longValue());
    }

    /**
     * Retrieves a data-pipe object using an external identifier.
     *
     * @param id the identifier to use.
     * @param <T> the type of the algorithm return.
     * @param <A> the type of the algorithm parameter.
     * @return the data-pipe object (not null).
     */
    public <T, A> DataPipe<T, A> getDataPipe(Component.Id id) {
        Validator.validate(id, Validator.Message.INVALID_ID);
        return (DataPipe<T, A>) dataPipeMap.get(id.longValue());
    }

    public boolean hasDataPipe(Component.Id id) {
        return getDataPipe(id) != null;
    }

    @Override
    public MemoryMap.EntityComponentMap allocateMemoryMap(SystemController systemController) {
        final MemoryMap.EntityComponentMap components = new MemoryMap.EntityComponentMap();
        register(systemController, components);
        return components;
    }

    public boolean hasSystemComponents(SystemController systemController) {
        return getMemoryMap(systemController) != null;
    }

    public boolean hasEntityComponent(SystemController systemController, Entity entity) {
        return getComponent(entity, systemController) != null;
    }

    public boolean hasSameComponent(SystemController systemController, Entity entity,
                                    Component component) {
        Component component1 = getComponent(entity, systemController);
        return component1 != null && component1 == component;
    }

    public void updateSystems(SystemsUpdater<I> updater, I input) {
        Validator.validate(updater, Validator.Message.INVALID_ASSOCIATED_SYSTEM);
        updater.update(systems, this, input);
    }

    public void updateSystemComponents(SystemEntitiesUpdater<I> updater, I input) {
        Validator.validate(updater, Validator.Message.INVALID_ASSOCIATED_SYSTEM);
        MemoryMap.EntityComponentMap components = systems.get(updater.getId().getId());
        Validator.validate(components, Validator.Message.INVALID_ASSOCIATED_ENTITY_COMPONENT_MAP);
        updater.update(components, this, input);
    }

    public void updateEntityComponents(ComponentUpdater<I> updater, Entity entity, I input) {
        Validator.validate(updater, Validator.Message.INVALID_ASSOCIATED_SYSTEM);
        Validator.validate(entity, Validator.Message.INVALID_ENTITY);
        // do a realtime manipulation every time, Big-O notation of (n), linear CPU clock cycles
        final MemoryMap.SystemComponentMap components = new MemoryMap.SystemComponentMap();
        systems.forEach((associatedSystem, entityComponentMap) ->
                components.put(associatedSystem, getComponent(entity, updater)));
        updater.update(components, entity, this, input);
    }
}