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

package articular.core;

import articular.core.component.Component;
import articular.core.system.data.DataPipe;
import articular.core.system.manager.EntityComponentManager;
import articular.util.Validatable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Defines aliases for types. A {@link SystemMap} encloses the
 * systems; each system groups some components in a {@link EntityComponentMap}
 * by their {@link Entity#getId()}; which represents entities.
 *
 * @author pavl_g
 * @see Component
 * @see Entity
 * @see EntityComponentManager
 */
public interface MemoryMap extends Validatable {

    /**
     * Defines a group of systems (repositories) to group some data (components)
     * and manipulate activities through them.
     */
    final class SystemMap extends ConcurrentHashMap<String, EntityComponentMap> implements MemoryMap {
    }

    /**
     * Defines a group of components mapped by their IDs which can be derived
     * from the {@link Entity} class.
     */
    final class EntityComponentMap extends ConcurrentHashMap<Number, Component> implements MemoryMap {
    }

    /**
     * TODO
     */
    final class SystemComponentMap extends ConcurrentHashMap<String, Component> implements MemoryMap {
    }

    /**
     * TODO
     */
    final class CacheMap extends ConcurrentHashMap<Number, SystemComponentMap> implements MemoryMap {
    }

    /**
     * TODO
     */
    final class DataPipeMap extends ConcurrentHashMap<Number, DataPipe<?, ?>> implements MemoryMap {
    }
}


