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

package articular.example;

import articular.core.Entity;
import articular.core.MemoryMap;
import articular.core.component.Component;
import articular.core.system.ArticularSystem;
import articular.core.system.ComponentUpdater;
import articular.core.system.SystemEntitiesUpdater;
import articular.core.system.SystemsUpdater;
import articular.core.system.manager.EntityComponentManager;
import articular.core.component.Module;
import articular.util.ArticularManager;

/**
 * Tests the ultimate ecs manager with caching {@link ArticularManager}.
 *
 * @author pavl_g
 */
public class TestArticularManager {
    // ecs manager
    private static final ArticularManager<String> ecsManager = new ArticularManager<>();
    // system controller
    private static final InputDataProcessor dataProcessor = new InputDataProcessor();
    // initialize components
    private static final Module electronicModule = new ElectronicModuleChip();
    private static final ElectronicModuleChip.Button button0 = new ElectronicModuleChip.Button(0, 0);
    private static final ElectronicModuleChip.Button button1 = new ElectronicModuleChip.Button(1, 0);
    private static final ElectronicModuleChip.Scroll scroll = new ElectronicModuleChip.Scroll(2, 0);
    private static final ElectronicModuleChip.LaserSensor laserSensor = new ElectronicModuleChip.LaserSensor(3, 0,0);
    private static Entity serialMouse;

    public static void main(String[] args) {
        // 1) allocate a system map by the associated system
        ecsManager.allocateMemoryMap(dataProcessor);
        // 2) create an entity representing the mouse
        serialMouse = ecsManager.createEntity(dataProcessor, "Serial-Mouse");
        ecsManager.getCacheManager().allocateMemoryMap(serialMouse);
        // 3) add components to the electronic module
        electronicModule.register(button0);
        electronicModule.register(button1);
        electronicModule.register(scroll);
        electronicModule.register(laserSensor);
        ecsManager.register(serialMouse, electronicModule, dataProcessor);
        // 4) register components to the entity
        ecsManager.register(serialMouse, electronicModule, dataProcessor);
        System.out.println("Get = " + ecsManager.getComponent(serialMouse, dataProcessor));
        // 5) dispatch update
        ecsManager.updateSystemComponents(dataProcessor, "---- Update Systems-Components Controller Dispatched ----");
        ecsManager.updateEntityComponents(dataProcessor, serialMouse, "---- Update Entity-Components Controller Dispatched ----");
        ecsManager.updateSystems(dataProcessor, "---- Update Systems Controller Dispatched ----");
    }

    public static final class InputDataProcessor implements SystemEntitiesUpdater<String>, ComponentUpdater<String>, SystemsUpdater<String> {

        @Override
        public ArticularSystem getId() {
            return HID.INPUT_PROCESSOR;
        }

        @Override
        public void update(MemoryMap.EntityComponentMap entityMap, EntityComponentManager<String> entityComponentManager, String input) {
            // print data
            System.out.println();
            System.out.println(input);
            // retrieve the serial mouse entity
            final Component components = entityMap.get(serialMouse.getId().longValue());
            final Module electronicModule = (Module) components;
            // print the electronic module of the serial mouse
            System.out.println(electronicModule.getComponents());
            // delegate to serial4j internals
            //...........................
        }

        @Override
        public void update(MemoryMap.SystemComponentMap components, Entity entity, EntityComponentManager<String> entityComponentManager, String input) {
            System.out.println();
            System.out.println(input);
            System.out.println(entity.getId().longValue());
            try {
                final MemoryMap.EntityComponentMap ecsMap = entityComponentManager.getComponent(entity, this)
                                                            .getData("entityComponentMap");
                System.out.println(ecsMap);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void update(MemoryMap.SystemMap systemMap, EntityComponentManager<String> entityComponentManager, String input) {
            System.out.println();
            System.out.println(input);
            final Module module = entityComponentManager.getComponent(serialMouse, this);
            System.out.println(module.getComponents());
        }
    }

    public static final class ElectronicModuleChip implements Module {
        private final MemoryMap.EntityComponentMap entityComponentMap = new MemoryMap.EntityComponentMap();

        @Override
        public Id getId() {
            return new Component.Id((hashCode() >>> 16) ^ hashCode());
        }

        @Override
        public MemoryMap.EntityComponentMap getComponents() {
            return entityComponentMap;
        }

        public record Button(int id, int state) implements Component {
            @Override
            public Id getId() {
                return new Component.Id((id >>> 16) ^ hashCode());
            }
        }

        public record Scroll(int id, int resolution) implements Component {
            @Override
            public Id getId() {
                return new Component.Id((id >>> 16) ^ hashCode());
            }
        }

        public record LaserSensor(int id, int x, int y) implements Component {
            @Override
            public Id getId() {
                return new Component.Id((id >>> 16) ^ hashCode());
            }
        }
    }
}
