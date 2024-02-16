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

package articular.example.labs.techdemos.jme.testjaime.components;

import articular.core.component.Component;
import com.jme3.anim.AnimFactory;
import com.jme3.animation.LoopMode;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.MotionPath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class JumpKickCinematic implements Component {

    private final Cinematic cinematic;
    private final AnimFactory jumpForward = new AnimFactory(1f, "JumpForward", 30f);
    private final AnimFactory startingPosition = new AnimFactory(0.01f, "StartingPosition", 30f);
    private final MotionPath motionPath = new MotionPath();

    public JumpKickCinematic(final Node scene, final float initialDuration) {
        cinematic = new Cinematic(scene, initialDuration);

        jumpForward.addTimeTranslation(0, new Vector3f(0, 0, -3));
        jumpForward.addTimeTranslation(0.35f, new Vector3f(0, 1, -1.5f));
        jumpForward.addTimeTranslation(0.7f, new Vector3f(0, 0, 0));

        motionPath.addWayPoint(new Vector3f(1.1f, 1.2f, 2.9f));
        motionPath.addWayPoint(new Vector3f(0f, 1.2f, 3.0f));
        motionPath.addWayPoint(new Vector3f(-1.1f, 1.2f, 2.9f));
        // motionPath.enableDebugShape(app.getAssetManager(), app.getRootNode());
        motionPath.setCurveTension(0.8f);

        cinematic.setSpeed(1.2f);
        cinematic.setLoopMode(LoopMode.Loop);
    }

    @Override
    public Id getId() {
        return new Component.Id((hashCode() >>> 16) ^
                GameComponents.CINEMATIC_COMPONENTS.getEntity().getId().longValue());
    }
}
