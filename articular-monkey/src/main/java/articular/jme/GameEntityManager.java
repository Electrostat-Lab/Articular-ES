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

package articular.jme;

import articular.util.ConcurrentEntityComponentManager;
import articular.util.EntityComponentManager;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

/**
 * Binds the {@link EntityComponentManager} to a jMonkeyEngine {@link BaseAppState}
 * in a game environment.
 *
 * @author pavl_g
 */
public class GameEntityManager extends BaseAppState {

    /**
     * The ECS manager controlling the game entities and their components.
     */
    protected EntityComponentManager<Float> entityComponentManager =
                                                new ConcurrentEntityComponentManager<>();

    @Override
    protected void initialize(Application app) {

    }

    @Override
    protected void cleanup(Application app) {
        entityComponentManager = null;
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    /**
     * Updates the game entities, their sub-entities and their game components.
     *
     * @param tpf the time taken to render a frame (time-per-frame) in a game loop
     */
    @Override
    public void update(float tpf) {
        getEntityComponentManager().update(tpf);
    }

    /**
     * Retrieves the game entity-component manager used for
     * loading, unloading and updating {@link articular.core.Entity}s
     * and their {@link articular.core.component.Component}s.
     *
     * @return the game entity-component manager for this state
     */
    public EntityComponentManager<Float> getEntityComponentManager() {
        return entityComponentManager;
    }
}
