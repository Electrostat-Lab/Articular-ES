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

package articular.example.labs.techdemos.jme.systems;

import articular.core.MemoryMap;
import articular.core.component.Module;
import articular.core.system.ArticularSystem;
import articular.core.system.SystemEntitiesUpdater;
import articular.core.system.manager.EntityComponentManager;
import articular.example.labs.techdemos.jme.components.GameComponents;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.PlayState;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import java.util.ArrayList;

/**
 * A system the procedural code for the input manager.
 *
 * @author pavl_g
 */
public class KeyInputSystem implements SystemEntitiesUpdater<SimpleApplication> {

    @Override
    public ArticularSystem getAssociatedSystem() {
        return Systems.KEY_INPUT_SYSTEM;
    }

    @Override
    public void update(MemoryMap.EntityComponentMap entityMap, EntityComponentManager<SimpleApplication> entityComponentManager, SimpleApplication input) {
        final Module inputs = (Module)
                entityComponentManager.getComponent(GameComponents.INPUT_COMPONENTS.getEntity(), this);
        // collect the data from the components
        final ArrayList<String> mappings = new ArrayList<>();
        inputs.getComponents().forEach((number, component) -> {
            try {
                final String mapping = component.getData("mapping");
                final KeyTrigger key = component.getData("key");
                input.getInputManager().addMapping(mapping, key);

                mappings.add(mapping);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        // perform operational code upon them
        input.getInputManager().addListener((ActionListener) (name, isPressed, tpf) -> {
            if (name.equals("start") && isPressed) {

                final MemoryMap.EntityComponentMap map = entityComponentManager.getPrimaryMemoryMap().get(Systems.CINEMATIC_SYSTEM.getSystemName());
                final Module cinematics = (Module) map.get(GameComponents.CINEMATIC_COMPONENTS.getEntity().getId().intValue());

                cinematics.getComponents().forEach((number, component) -> {
                    final Cinematic cinematic;
                    try {
                        cinematic = component.getData("cinematic");
                        if(cinematic.getPlayState() != PlayState.Playing){
                            cinematic.play();
                        }else{
                            cinematic.pause();
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (name.equals("exit") && isPressed) {
                System.exit(0);
            }
        }, mappings.toArray(new String[]{}));
    }
}
