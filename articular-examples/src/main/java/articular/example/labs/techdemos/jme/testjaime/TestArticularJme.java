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

package articular.example.labs.techdemos.jme.testjaime;

import articular.core.component.Component;
import articular.example.labs.techdemos.jme.testjaime.systems.JumpKickCinematicBuilder;
import articular.example.labs.techdemos.jme.testjaime.components.*;
import articular.example.labs.techdemos.jme.testjaime.systems.JaimeBuilder;
import articular.example.labs.techdemos.jme.testjaime.systems.KeyInputSystem;
import articular.util.ArticularManager;
import com.jme3.app.*;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

/**
 * Refactored TestJaime to ECS architecture.
 *
 * @author Nehon
 * @author pavl_g
 */
public class TestArticularJme extends SimpleApplication {

    private final ArticularManager<SimpleApplication> ecsManager = new ArticularManager<>();
    private final JaimeBuilder jaimeBuilder = new JaimeBuilder();
    private final JumpKickCinematicBuilder jumpKickCinematicBuilder = new JumpKickCinematicBuilder();
    private final KeyInputSystem keyInputSystem = new KeyInputSystem();
    private final ComponentCollection characters =
            new ComponentCollection(GameComponents.JAIME.getEntity().getId());
    private final ComponentCollection cinematics =
            new ComponentCollection(GameComponents.CINEMATIC_COMPONENTS.getEntity().getId());

    private final ComponentCollection inputs =
            new ComponentCollection(GameComponents.INPUT_COMPONENTS.getEntity().getId());

    public static void main(String... argv){
        TestArticularJme app = new TestArticularJme();
        final AppSettings settings = new AppSettings(true);
        settings.setResizable(true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {

        // environment setup and update
        final Component jaime0 = new Jaime(getAssetManager(), new Vector3f(0, 0f, 0)); // initialize components
        final Component jaime1 = new Jaime(getAssetManager(), new Vector3f(2f, 0f, 0f)); // initialize components
        final Component jaime2 = new Jaime(getAssetManager(), new Vector3f(0f, 0f, 2f)); // initialize components
        final Component jaime3 = new Jaime(getAssetManager(), new Vector3f(2f, 0f, 2f)); // initialize components
        final Component jaime4 = new Jaime(getAssetManager(), new Vector3f(4f, 0f, 0f)); // initialize components

        ecsManager.allocateMemoryMap(jaimeBuilder); // allocate a system and register it
        characters.register(jaime0);
        characters.register(jaime1);
        characters.register(jaime2);
        characters.register(jaime3);
        characters.register(jaime4);
        ecsManager.register(GameComponents.JAIME.getEntity(), characters, jaimeBuilder);
        ecsManager.updateSystemComponents(jaimeBuilder, this); // update environment components

        final Component cinematicComponent0 = new JumpKickCinematic(rootNode, 30);
        final Component cinematicComponent1 = new JumpKickCinematic(rootNode, 30);
        final Component cinematicComponent2 = new JumpKickCinematic(rootNode, 30);
        final Component cinematicComponent3 = new JumpKickCinematic(rootNode, 30);

        ecsManager.allocateMemoryMap(jumpKickCinematicBuilder);
        cinematics.register(jaime0.getId(), cinematicComponent0);
        cinematics.register(jaime1.getId(), cinematicComponent1);
        cinematics.register(jaime2.getId(), cinematicComponent2);
        cinematics.register(jaime3.getId(), cinematicComponent3);

        ecsManager.register(GameComponents.CINEMATIC_COMPONENTS.getEntity(), cinematics, jumpKickCinematicBuilder);
        ecsManager.updateSystemComponents(jumpKickCinematicBuilder, this);

        final Component playInput = new InputComponent("start", new KeyTrigger(KeyInput.KEY_P));
        final Component exitInput = new InputComponent("exit", new KeyTrigger(KeyInput.KEY_E));

        ecsManager.allocateMemoryMap(keyInputSystem);
        inputs.register(playInput);
        inputs.register(exitInput);
        ecsManager.register(GameComponents.INPUT_COMPONENTS.getEntity(), inputs, keyInputSystem);
        ecsManager.updateSystemComponents(keyInputSystem, this);
    }
}
