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
import articular.core.system.manager.EntityComponentManager;

/**
 * Offers a static ID provider for the {@link MemoryMap.EntityComponentMap}
 * through the {@link EntityComponentManager}.
 *
 * <p>
 * An entity is a component with the data field (name); the data-field
 * name can be exposed by the means of the Component interface, but
 * exposure might produce API leakage; because the hash spreading
 * algorithm is internalized.
 * </p>
 *
 * @author pavl_g
 * @see MemoryMap.EntityComponentMap
 * @see EntityComponentManager
 */
public final class Entity implements Component {

    private final String name;

    /**
     * Instantiates a new identifier provider to map
     * some components in their respective systems.
     *
     * @param name the name of this entity; the name is used
     *             in a hashing compression to produce
     *             the object identifier.
     */
    public Entity(String name) {
        this.name = name;
    }

    @Override
    public Id getId() {
        return new Entity.Id((name.hashCode() >>> 16) /* spreads MSBs to the lower 16-bits */
                ^ name.hashCode()) /* composes the MSBs with the LSBs by XORing them */;
    }
}