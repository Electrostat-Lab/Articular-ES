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
import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimFactory;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.events.AnimEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * A system for executing jump-kick-wave cinematic
 * on jaime characters.
 *
 * @author pavl_g
 */
public class JumpKickCinematicBuilder implements SystemEntitiesUpdater<SimpleApplication> {

    @Override
    public ArticularSystem getId() {
        return Systems.CINEMATIC_SYSTEM;
    }

    @Override
    public void update(MemoryMap.EntityComponentMap entityMap, EntityComponentManager<SimpleApplication> entityComponentManager, SimpleApplication input) {
        final Module cinematics =
                entityComponentManager.getComponent(GameComponents.CINEMATIC_COMPONENTS.getEntity(), this);
        try {
            setup(input, entityComponentManager, cinematics);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setup(SimpleApplication app, EntityComponentManager<SimpleApplication> ecsManager, Module module)
            throws NoSuchFieldException, IllegalAccessException {

        // attach cinematic objects to their jaime
        final MemoryMap.EntityComponentMap map = ecsManager.getMemoryMap().get(Systems.ENV_SYSTEM.getId());
        final Module jaimes = (Module) map.get(GameComponents.JAIME.getEntity().getId().longValue());
        jaimes.getComponents().forEach((id, jaimeComponent) -> {
            try {

                if (!module.hasComponent(jaimeComponent.getId())) {
                    return;
                }

                final Component component = module.getComponents().get(id);

                final Cinematic cinematic = component.getData("cinematic");
                final AnimFactory jumpForward = component.getData("jumpForward");
                final AnimFactory startingPosition = component.getData("startingPosition");

                final Node jaime = jaimeComponent.getData("jaime");
                app.getStateManager().attach(cinematic);

                final AnimClip forwardClip = jumpForward.buildAnimation(jaime);
                final AnimComposer composer = jaime.getControl(AnimComposer.class);

                composer.addAnimClip(forwardClip);
                /*
                 * Add a clip that warps the model to its starting position.
                 */
                startingPosition.addTimeTranslation(0f, new Vector3f(0f, 0f, -3f));

                final AnimClip startClip = startingPosition.buildAnimation(jaime);
                composer.addAnimClip(startClip);

                composer.makeLayer("SpatialLayer", null);
                String boneLayer = AnimComposer.DEFAULT_LAYER;

                cinematic.addCinematicEvent(0f,
                        new AnimEvent(composer, "StartingPosition", "SpatialLayer"));
                cinematic.enqueueCinematicEvent(
                        new AnimEvent(composer, "Idle", boneLayer));

                final float jumpStart = cinematic.enqueueCinematicEvent(
                        new AnimEvent(composer, "JumpStart", boneLayer));
                cinematic.addCinematicEvent(jumpStart + 0.2f,
                        new AnimEvent(composer, "JumpForward", "SpatialLayer"));
                cinematic.enqueueCinematicEvent(
                        new AnimEvent(composer, "JumpEnd", boneLayer));
                cinematic.enqueueCinematicEvent(
                        new AnimEvent(composer, "Punches", boneLayer));
                cinematic.enqueueCinematicEvent(
                        new AnimEvent(composer, "SideKick", boneLayer));

                final AnimEvent idleOneSecond = new AnimEvent(composer, "Idle", boneLayer);
                idleOneSecond.setInitialDuration(1f);

                cinematic.enqueueCinematicEvent(idleOneSecond);
                cinematic.enqueueCinematicEvent(
                        new AnimEvent(composer, "Wave", boneLayer));
                cinematic.enqueueCinematicEvent(
                        new AnimEvent(composer, "Idle", boneLayer));

                cinematic.fitDuration();
                cinematic.play();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
