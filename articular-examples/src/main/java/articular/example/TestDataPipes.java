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

package articular.example;

import articular.core.Entity;
import articular.core.MemoryMap;
import articular.core.component.Component;
import articular.core.component.Module;
import articular.core.system.ArticularSystem;
import articular.core.system.SystemEntitiesUpdater;
import articular.core.system.data.DataPipe;
import articular.core.system.manager.EntityComponentManager;
import articular.util.ArticularManager;

/**
 * Examines the data pipe interface {@link articular.core.system.data.DataPipe}.
 *
 * @author pavl_g
 */
public final class TestDataPipes {

    private static final EntityComponentManager<String> ecsManager = new ArticularManager<>();
    private static final Entity sensors = new Entity("Sensors");
    private static final Module sensorModule = new SensorModule();
    private static DataPipe<Float, Float> dataPipe;

    public static void main(String[] args) {

        final SystemEntitiesUpdater<String> dataCollector = new SystemEntitiesUpdater<>() {
            @Override
            public void update(MemoryMap.EntityComponentMap entityMap, EntityComponentManager<String> entityComponentManager, String input) {
                System.out.println(input);

                // retrieve the value of proximitor
                final Module sensorModule1 = (Module) entityMap.get(sensors.getId().longValue());
                // set the new data under this module
                final SensorModule.AccelerometerData data = new SensorModule.AccelerometerData(2, 3, 5);
                sensorModule1.register(SensorModule.Entities.ACCELEROMETER.getEntity().getId(), data);

                // process some data (e.g.: vector maths) and pass it through a data pipe
                // for other systems to act on
                dataPipe = new DataPipe<>() {
                    @Override
                    public Float getData(Float argument) {
                        return (argument * 0.005f) * data.x();
                    }

                    @Override
                    public Component.Id getId() {
                        return sensorModule.getId(); // shared id with the sensor module
                    }
                };

                System.out.println("Raw Data in-processing = " + dataPipe.getData(1f));

                entityComponentManager.registerDataPipe(dataPipe);
            }

            @Override
            public ArticularSystem getId() {
                return () -> "Sensor-module-data-collector";
            }
        };

        final SystemEntitiesUpdater<String> dataPostProcessing = new SystemEntitiesUpdater<>() {
            @Override
            public void update(MemoryMap.EntityComponentMap entityMap, EntityComponentManager<String> entityComponentManager, String input) {
                System.out.println(input);

                // unpack the data pipe and act on data
                final Module sensorModule1 = (Module) entityMap.get(sensors.getId().longValue());
                final SensorModule.ProximitorData data = sensorModule1.getComponent(SensorModule.Entities.PROXIMITY.getEntity().getId());

                final DataPipe<Float, Float> dataPipe1 = entityComponentManager.getDataPipe(sensorModule.getId());
                final float raw = dataPipe1.getData(data.signal);

                System.out.println("Raw Data post processing = " + raw);
                entityComponentManager.unregisterDataPipe(dataPipe1.getId());
            }

            @Override
            public ArticularSystem getId() {
                return () -> "Sensor-module-data-postprocessing";
            }
        };

        ecsManager.allocateMemoryMap(dataCollector);
        ecsManager.allocateMemoryMap(dataPostProcessing);
        
        sensorModule.register(SensorModule.Entities.PROXIMITY.getEntity().getId(), new SensorModule.ProximitorData(0.33f));

        ecsManager.register(sensors, sensorModule, dataCollector);
        ecsManager.register(sensors, sensorModule, dataPostProcessing);

        ecsManager.updateSystemComponents(dataCollector, "DISPATCH_COLLECTOR");
        ecsManager.updateSystemComponents(dataPostProcessing, "DISPATCH_POST_PROCESSOR");
    }

    private static final class SensorModule implements Module {

        private final MemoryMap.EntityComponentMap components = new MemoryMap.EntityComponentMap();

        @Override
        public Id getId() {
            return sensors.getId();
        }

        @Override
        public MemoryMap.EntityComponentMap getComponents() {
            return components;
        }

        private record AccelerometerData(float x, float y, float z) implements Component {
            @Override
            public Id getId() {
                return new Id((hashCode() >>> 16) ^ hashCode());
            }
        }

        private record ProximitorData(float signal) implements Component {

            @Override
            public Id getId() {
                return new Id((hashCode() >>> 16) ^ hashCode());
            }
        }

        private enum Entities {
            PROXIMITY(ProximitorData.class.getName()),
            ACCELEROMETER(AccelerometerData.class.getName());

            private final String name;
            Entities(final String name) {
                this.name = name;
            }

            public Entity getEntity() {
                return new Entity(name);
            }
        }
    }
}
