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

package articular.core.system.manager;

import articular.core.Entity;
import articular.core.MemoryMap;
import articular.core.component.Component;
import articular.core.system.ComponentUpdater;
import articular.core.system.SystemController;
import articular.core.system.SystemEntitiesUpdater;
import articular.core.system.SystemsUpdater;
import articular.util.Validator;
import java.util.Objects;

/**
 *
 * @param <I> the type of the input for the update loop
 * @author pavl_g
 * @see CacheManager for entity-system-component layouting
 * @see SystemController for instantiating systems
 */
@SuppressWarnings("unchecked")
public class EntityComponentManager<I> implements SystemManager<MemoryMap.SystemMap, MemoryMap.EntityComponentMap, SystemController> {

    protected MemoryMap.SystemMap systems = new MemoryMap.SystemMap();

    public EntityComponentManager() {
    }

    @Override
    public <T extends Component> T allocateComponent(Entity entity, SystemController systemController, Component.Id id) {
        final Component component = () -> id;
        register(entity, component, systemController);
        return (T) component;
    }

    public Entity createEntity(SystemController systemController, String name) {
        return createEntity(new SystemController[]{systemController}, name);
    }

    public Entity createEntity(SystemController[] systemControllers, String name) {
        final Entity entity = new Entity(name);
        for (SystemController systemController : Objects.requireNonNull(systemControllers)) {
            Validator.validate(systemController, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
            allocateComponent(entity, systemController, entity.getId());
        }
        return entity;
    }

    @Override
    public <T extends Component> T getComponent(Entity entity, SystemController systemController) {
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        MemoryMap.EntityComponentMap components = getSecondaryMemoryMap(systemController);
        Validator.validate(components, Validator.Message.ASSOCIATED_ENTITY_COMPONENT_MAP_NOT_FOUND);
        return (T) components.get(entity.getId().intValue());
    }

    @Override
    public MemoryMap.SystemMap getPrimaryMemoryMap() {
        return systems;
    }

    @Override
    public MemoryMap.EntityComponentMap getSecondaryMemoryMap(SystemController systemController) {
        Validator.validate(systemController, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        return systems.get(systemController.getAssociatedSystem().getSystemName());
    }

    @Override
    public boolean hasComponent(Entity entity, SystemController systemController) {
        return getComponent(entity, systemController) != null;
    }

    @Override
    public boolean hasMemoryMap(SystemController systemController) {
        return getSecondaryMemoryMap(systemController) != null;
    }

    @Override
    public void register(SystemController systemController, MemoryMap.EntityComponentMap memoryMap) {
        Validator.validate(systemController, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        Validator.validate(memoryMap, Validator.Message.ASSOCIATED_ENTITY_COMPONENT_MAP_NOT_FOUND);
        systems.put(systemController.getAssociatedSystem().getSystemName(), memoryMap);
    }

    @Override
    public void unregister(SystemController systemController) {
        Validator.validate(systemController, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        systems.remove(systemController.getAssociatedSystem().getSystemName());
    }

    @Override
    public void register(Entity entity, Component component, SystemController systemController) {
        Validator.validate(component, Validator.Message.COMPONENT_NOT_FOUND);
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        Validator.validate(systemController, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        getSecondaryMemoryMap(systemController).put(entity.getId().intValue(), component);
    }

    @Override
    public void unregister(Entity entity, SystemController systemController) {
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        Validator.validate(systemController, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        getSecondaryMemoryMap(systemController).remove(entity.getId().intValue());
    }

    @Override
    public MemoryMap.EntityComponentMap allocateMemoryMap(SystemController systemController) {
        final MemoryMap.EntityComponentMap components = new MemoryMap.EntityComponentMap();
        register(systemController, components);
        return components;
    }

    public boolean hasSystemComponents(SystemController systemController) {
        return getSecondaryMemoryMap(systemController) != null;
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
        Validator.validate(updater, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        updater.update(systems, this, input);
    }

    public void updateSystemComponents(SystemEntitiesUpdater<I> updater, I input) {
        Validator.validate(updater, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        MemoryMap.EntityComponentMap components = systems.get(updater.getAssociatedSystem().getSystemName());
        Validator.validate(components, Validator.Message.ASSOCIATED_ENTITY_COMPONENT_MAP_NOT_FOUND);
        updater.update(components, this, input);
    }

    public void updateEntityComponents(ComponentUpdater<I> updater, Entity entity, I input) {
        Validator.validate(updater, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        // do a realtime manipulation every time, Big-O notation of (n), linear CPU clock cycles
        final MemoryMap.SystemComponentMap components = new MemoryMap.SystemComponentMap();
        systems.forEach((associatedSystem, entityComponentMap) ->
                components.put(associatedSystem, getComponent(entity, updater)));
        updater.update(components, entity, this, input);
    }
}