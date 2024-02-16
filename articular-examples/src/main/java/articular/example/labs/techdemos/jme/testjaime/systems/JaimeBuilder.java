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

package articular.example.labs.techdemos.jme.testjaime.systems;

import articular.core.MemoryMap;
import articular.core.component.Component;
import articular.core.component.Module;
import articular.core.system.ArticularSystem;
import articular.core.system.SystemEntitiesUpdater;
import articular.core.system.manager.EntityComponentManager;
import articular.example.labs.techdemos.jme.testjaime.components.GameComponents;
import com.jme3.anim.util.AnimMigrationUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;

/**
 * A system used for building Jaime characters at runtime.
 *
 * @author pavl_g
 */
public class JaimeBuilder implements SystemEntitiesUpdater<SimpleApplication> {

    @Override
    public ArticularSystem getId() {
        return Systems.ENV_SYSTEM;
    }

    @Override
    public void update(MemoryMap.EntityComponentMap entityMap, EntityComponentManager<SimpleApplication> entityComponentManager, SimpleApplication input) {
        final Module env =
                entityComponentManager.getComponent(GameComponents.JAIME.getEntity(), this);
        env.getComponents().forEach((number, component) -> {
            try {
                setup(input, component);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setup(SimpleApplication app, Component env) throws NoSuchFieldException, IllegalAccessException {
        // Get involved components
        final Vector3f worldPosition = env.getData("worldPosition");
        final Node jaime = env.getData("jaime");
        final AmbientLight al = env.getData("ambientLight");
        final PointLight pl = env.getData("pointLight");

        final Vector3f max3f = new Vector3f(Math.max(1, worldPosition.getX()),
                Math.max(1, worldPosition.getY()), Math.max(1, worldPosition.getZ()));

        // perform operational code on them
        final Node environment = new Node();
        environment.setLocalTranslation(worldPosition);

        AnimMigrationUtils.migrate(jaime);
        jaime.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        environment.attachChild(jaime);

        al.setColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1));
        environment.addLight(al);

        //pointlight to fake indirect light coming from the ground
        pl.setColor(ColorRGBA.White.mult(1.5f));
        pl.setPosition(new Vector3f(0, 0, 1).add(max3f));
        pl.setRadius(5);
        environment.addLight(pl);

        app.getRootNode().attachChild(environment);
    }
}
