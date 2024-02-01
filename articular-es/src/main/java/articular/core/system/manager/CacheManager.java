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
import articular.core.system.SystemController;
import articular.throwable.AssociatedObjectNotFoundException;
import articular.util.Validator;

/**
 * @author pavl_g
 */
public class CacheManager implements SystemManager<MemoryMap.CacheMap, MemoryMap.SystemComponentMap, Entity> {
    protected MemoryMap.CacheMap cacheMap = new MemoryMap.CacheMap();

    public CacheManager() {
    }

    @Override
    public void register(Entity entity, MemoryMap.SystemComponentMap components) {
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        Validator.validate(components, Validator.Message.ASSOCIATED_SYSTEM_COMPONENT_MAP_NOT_FOUND);
        cacheMap.put(entity.getId().intValue(), components);
    }

    @Override
    public void unregister(Entity entity) {
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        cacheMap.remove(entity.getId().intValue());
    }

    @Override
    public void register(Entity entity, Component component, SystemController systemController) {
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        Validator.validate(systemController, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        Validator.validate(component, Validator.Message.COMPONENT_NOT_FOUND);
        MemoryMap.SystemComponentMap components = getSecondaryMemoryMap(entity);
        try {
            Validator.validate(components, Validator.Message.ASSOCIATED_SYSTEM_COMPONENT_MAP_NOT_FOUND);
        } catch (AssociatedObjectNotFoundException ex) {
            register(entity, (components = new MemoryMap.SystemComponentMap()));
        } finally {
            components.put(systemController.getAssociatedSystem().getSystemName(), component);
        }
    }

    @Override
    public void unregister(Entity entity, SystemController systemController) {
        final MemoryMap.SystemComponentMap map = getSecondaryMemoryMap(entity);
        Validator.validate(map, Validator.Message.ASSOCIATED_ENTITY_COMPONENT_MAP_NOT_FOUND);
        map.remove(systemController.getAssociatedSystem().getSystemName());
    }

    @Override
    public MemoryMap.SystemComponentMap allocateMemoryMap(Entity entity) {
        final MemoryMap.SystemComponentMap components = new MemoryMap.SystemComponentMap();
        register(entity, components);
        return components;
    }

    @Override
    public Component allocateComponent(Entity entity, SystemController systemController, Component.Id id) {
        final Component component = () -> id;
        register(entity, component, systemController);
        return component;
    }

    @Override
    public Component getComponent(Entity entity, SystemController systemController) {
        Validator.validate(systemController, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        final MemoryMap.SystemComponentMap components = getSecondaryMemoryMap(entity);
        Validator.validate(components, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        return components.get(systemController.getAssociatedSystem().getSystemName());
    }

    @Override
    public MemoryMap.CacheMap getPrimaryMemoryMap() {
        return cacheMap;
    }

    @Override
    public MemoryMap.SystemComponentMap getSecondaryMemoryMap(Entity entity) {
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        return cacheMap.get(entity.getId().intValue());
    }

    @Override
    public boolean hasComponent(Entity entity, SystemController systemController) {
        return getComponent(entity, systemController) != null;
    }

    @Override
    public boolean hasMemoryMap(Entity entity) {
        return getSecondaryMemoryMap(entity) != null;
    }
}
