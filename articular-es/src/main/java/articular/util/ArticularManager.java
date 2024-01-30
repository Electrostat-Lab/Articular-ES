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
import articular.core.MemoryMap;
import articular.core.component.Component;
import articular.core.system.ComponentsUpdater;
import articular.core.system.SystemController;
import articular.core.system.manager.CacheManager;
import articular.core.system.manager.EntityComponentManager;

public class ArticularManager<I> extends EntityComponentManager<I> {

    protected CacheManager cacheManager = new CacheManager();

    @Override
    public Component allocateComponent(Entity entity, SystemController systemController) {
        final Component component = new Component() {
        };
        register(entity, new Component() {
        }, systemController);

        if (!cacheManager.isEnableCaching()) {
            return component;
        }
        // cache to the [entity][system](component) layout
        cacheManager.register(entity, component, systemController);
        return component;
    }

    @Override
    public void register(Entity entity, Component component, SystemController systemController) {
        super.register(entity, component, systemController);

        if (!cacheManager.isEnableCaching()) {
            return;
        }
        // cache to the [entity][system](component) layout
        cacheManager.register(entity, component, systemController);
    }

    @Override
    public void register(SystemController systemController, MemoryMap.EntityComponentMap memoryMap) {
        super.register(systemController, memoryMap);

        if (!cacheManager.isEnableCaching()) {
            return;
        }
        // cache to the [entity][system](component) layout is not supported by this method!
        throw new UnsupportedOperationException("Operation not supported by the Articular-System CacheManager!");
    }

    @Override
    public void updateEntityComponents(ComponentsUpdater<I> updater, Entity entity, I input) {
        if (!cacheManager.isEnableCaching()) {
            super.updateEntityComponents(updater, entity, input);
            return;
        }
        Validator.validate(updater, Validator.Message.ASSOCIATED_SYSTEM_NOT_FOUND);
        Validator.validate(entity, Validator.Message.ENTITY_NOT_FOUND);
        // do a manipulation from the cache, constant omega notation, single CPU clock cycles
        // manipulate cache of the [entity][system](component) layout
        updater.update(cacheManager.getSecondaryMemoryMap(entity), entity, this, input);
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }
}
