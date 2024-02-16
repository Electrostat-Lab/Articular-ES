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

package articular.core.component;

import articular.core.Entity;
import articular.core.MemoryMap;
import articular.util.Validator;

/**
 * A template grouping some components together by their identifiers for
 * an extended system-design.
 *
 * <p>
 * The use of system-modules in software architecture is known as modularized programming; where
 * the systems can be decomposed into components that can be grouped under a single data module,
 * and then the modules can be registered to a particular system (e.g. electronic components
 * and electronic modules in FPGA circuit design - compound collision objects in complex
 * physics - grouping chemical compounds in a chemical reaction model - etc).
 * </p>
 *
 * <p>
 * * ECS Modules extends the data-centered architecture and allows to even group
 * components by their type or specificity or devotedly by their collective functionality;
 * this expands the re-usability virtue, independently from the system design, under different
 * entities.
 * </p>
 *
 * <p>
 * * To improve the API integrity, system's modules should only group relevant components (components
 * should better have common homogeneity factors (e.g. same in type -or- integral in functionality).
 * </p>
 *
 * @author pavl_g
 */
@SuppressWarnings("unchecked")
public interface Module extends Component {

    /**
     * Registers a component to this module using
     * the component identifier tag associated with the
     * component object.
     *
     * @param component the component to register (not null).
     * @see Module#unregister(Component)
     */
    default void register(Component component) {
        register(component.getId(), component);
    }

    /**
     * A helper-method to register a component to this module
     * using a concrete identifier provider (Entity).
     *
     * @param entity    the entity as a concrete id provider (not null).
     * @param component the component to register (not null).
     * @see Module#unregister(Component)
     */
    default void register(Entity entity, Component component) {
        register(entity.getId(), component);
    }

    /**
     * Registers a component to this module using
     * an external component identifier tag.
     *
     * <p>
     * Note: This is not an API leakage as many could think;
     * it's rather a robust way to keep the integrity of the
     * ECS architecture; you are free to use any identifier
     * provider in this API; the most you should tend to use
     * is the {@link articular.core.Entity}.
     * </p>
     *
     * @param id        the identifier to use (not null).
     * @param component the component to register (not null).
     * @see Module#unregister(Id)
     */
    default void register(Component.Id id, Component component) {
        Validator.validate(id, Validator.Message.INVALID_ID);
        Validator.validate(component, Validator.Message.INVALID_COMPONENT);
        getComponents().put(id.longValue(), component);
    }

    /**
     * Unregisters a component from this module using
     * the associated component identifier tag.
     *
     * @param component the component reference to use its id tag (not null).
     * @see Module#register(Component)
     * @see Module#register(Entity, Component)
     */
    default void unregister(Component component) {
        unregister(component.getId());
    }

    /**
     * Unregisters a component from this module using an
     * identifier tag.
     *
     * @param id the id of the component to unregister (not null).
     * @see Module#register(Id, Component)
     */
    default void unregister(Component.Id id) {
        Validator.validate(id, Validator.Message.INVALID_ID);
        getComponents().remove(id.longValue());
    }

    /**
     * Tests whether a component is present under this
     * module using an external identifier provider.
     *
     * @param id the identifier provider object (not null).
     * @return true if a component is found by this identifier,
     * false otherwise.
     */
    default boolean hasComponent(Component.Id id) {
        return getComponent(id) != null;
    }

    /**
     * Tests whether a component is present under this
     * module using the component object identifier.
     *
     * @param component the component object to test against (not null).
     * @return true if the component is present, false otherwise.
     */
    default boolean hasComponent(Component component) {
        return hasComponent(component.getId());
    }

    /**
     * Retrieves a component by a concrete identifier provider (e.g.: Entity).
     *
     * @param entity the identifier provider object (not null).
     * @param <T>    local type of component.
     * @return the component registered by the provided identifier (non-nullable).
     */
    default <T extends Component>
    T getComponent(Entity entity) {
        return getComponent(entity.getId());
    }

    /**
     * Retrieves a component by an external identifier.
     *
     * @param id  the identifier object (not null).
     * @param <T> local type of component.
     * @return the component registered by the provided identifier (non-nullable).
     */
    default <T extends Component>
    T getComponent(Component.Id id) {
        return (T) getComponents().get(id.longValue());
    }

    /**
     * Provides a memory-map to cache game components
     * under this module object.
     *
     * @return a memory-map of modular components (not null).
     */
    MemoryMap.EntityComponentMap getComponents();
}