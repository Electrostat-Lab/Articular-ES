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
import articular.throwable.AssociatedObjectNotFoundException;
import articular.util.Validator;

/**
 * A compensatory ecs-manager that caches data in an entity-first
 * format as Entity-[System]-Component for ease of access.
 *
 * <p>
 * // {TODO-Usages - TODO-Algorithm-Analysis-Model}
 * </p>
 *
 * <p>
 * Articular-es provides an API {@link articular.util.ArticularManager} to access ecs-caches;
 * for low-memory systems; use {@link articular.util.ArticularManager#setEnableCaching(boolean)}
 * to disable the caching API.
 * </p>
 *
 * @author pavl_g
 */
@SuppressWarnings("unchecked")
public class CacheManager implements SystemManager<MemoryMap.CacheMap, MemoryMap.SystemComponentMap, Entity> {

    /**
     * Provides a cache-memory map to enable
     * Entity-[System]-Component accessibility.
     */
    protected MemoryMap.CacheMap cacheMap = new MemoryMap.CacheMap();

    /**
     * Instantiates a new cache-manager to provide
     * Entity-[System]-Component accessibility.
     */
    public CacheManager() {
    }

    @Override
    public void register(Entity entity, MemoryMap.SystemComponentMap components) {
        Validator.validate(entity, Validator.Message.INVALID_ENTITY);
        Validator.validate(components, Validator.Message.INVALID_ASSOCIATED_SYSTEM_COMPONENT_MAP);
        cacheMap.put(entity.getId().longValue(), components);
    }

    @Override
    public void unregister(Entity entity) {
        Validator.validate(entity, Validator.Message.INVALID_ENTITY);
        cacheMap.remove(entity.getId().longValue());
    }

    @Override
    public void register(Entity entity, Component component, SystemController systemController) {
        Validator.validate(entity, Validator.Message.INVALID_ENTITY);
        Validator.validate(systemController, Validator.Message.INVALID_ASSOCIATED_SYSTEM);
        Validator.validate(component, Validator.Message.INVALID_COMPONENT);
        MemoryMap.SystemComponentMap components = getMemoryMap(entity);
        try {
            Validator.validate(components, Validator.Message.INVALID_ASSOCIATED_SYSTEM_COMPONENT_MAP);
        } catch (AssociatedObjectNotFoundException ex) {
            // allocate a new memory-map and register it
            register(entity, (components = new MemoryMap.SystemComponentMap()));
        } finally {
            components.put(systemController.getId().getId(), component);
        }
    }

    @Override
    public void unregister(Entity entity, SystemController systemController) {
        final MemoryMap.SystemComponentMap map = getMemoryMap(entity);
        Validator.validate(map, Validator.Message.INVALID_ASSOCIATED_ENTITY_COMPONENT_MAP);
        map.remove(systemController.getId().getId());
    }

    @Override
    public MemoryMap.SystemComponentMap allocateMemoryMap(Entity entity) {
        final MemoryMap.SystemComponentMap components = new MemoryMap.SystemComponentMap();
        register(entity, components);
        return components;
    }

    @Override
    public <T extends Component> T allocateComponent(Entity entity, SystemController systemController, Component.Id id) {
        final Component component = () -> id;
        register(entity, component, systemController);
        return (T) component;
    }

    @Override
    public <T extends Component> T getComponent(Entity entity, SystemController systemController) {
        Validator.validate(systemController, Validator.Message.INVALID_ASSOCIATED_SYSTEM);
        final MemoryMap.SystemComponentMap components = getMemoryMap(entity);
        Validator.validate(components, Validator.Message.INVALID_ASSOCIATED_SYSTEM_COMPONENT_MAP);
        return (T) components.get(systemController.getId().getId());
    }

    @Override
    public MemoryMap.CacheMap getMemoryMap() {
        return cacheMap;
    }

    @Override
    public MemoryMap.SystemComponentMap getMemoryMap(Entity entity) {
        Validator.validate(entity, Validator.Message.INVALID_ENTITY);
        return cacheMap.get(entity.getId().longValue());
    }
}