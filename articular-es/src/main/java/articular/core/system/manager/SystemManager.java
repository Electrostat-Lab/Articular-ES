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
import articular.util.Validatable;

/**
 * Represents a generic blueprint for ecs manager.
 * The generalized behavior and structure of this
 * class provides better encapsulation, re-usability, and
 * extensibility.
 *
 * @param <P> a generic type for the primary memory map
 * @param <S> a generic type for the secondary (second dimensional map) memory map
 * @param <K> a generic type for the key substrate for the data values
 * @author pavl_g
 */
public interface SystemManager<P extends MemoryMap, S extends MemoryMap, K extends Validatable> extends Validatable {
    void register(K substrate, S memoryMap);

    void unregister(K substrate);

    void register(Entity entity, Component component, SystemController systemController);

    void unregister(Entity entity, SystemController systemController);

    S allocateMemoryMap(K substrate);

    <T extends Component> T allocateComponent(Entity entity, SystemController systemController, Component.Id id);

    <T extends Component> T getComponent(Entity entity, SystemController systemController);

    P getPrimaryMemoryMap();

    S getSecondaryMemoryMap(K substrate);

    boolean hasComponent(Entity entity, SystemController systemController);

    boolean hasMemoryMap(K substrate);
}
