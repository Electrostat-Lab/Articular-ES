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

import articular.core.component.Component;

/**
 * A thread-safe version of the {@link EntityComponentManager} to properly
 * synchronize structural changes (addition/removal) of the map {@link EntityComponentManager#getEntities()}
 * with the game loop update {@link EntityComponentManager#update(Object)}.
 *
 * @param <T> the type of the game component data
 * @param <I> the input type to the game loop pattern
 * @author pavl_g
 */
public class ConcurrentEntityComponentManager<T, I> extends EntityComponentManager<T, I> {

    /**
     * Instantiates a thread-safe ECS manager.
     */
    public ConcurrentEntityComponentManager() {
        super();
    }

    @Override
    public synchronized void register(StandardGameEntity<T> entity) {
        super.register(entity);
    }

    @Override
    public synchronized void register(StandardGameEntity<T> entity, Component<T> component) {
        super.register(entity, component);
    }

    @Override
    public synchronized void unregister(StandardGameEntity<T> entity) {
        super.unregister(entity);
    }

    @Override
    public synchronized void unregister(StandardGameEntity<T> entity, Component<T> component) {
        super.unregister(entity, component);
    }

    @Override
    public synchronized void update(I input) {
        super.update(input);
    }
}
