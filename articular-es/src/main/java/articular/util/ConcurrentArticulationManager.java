package articular.util;

import articular.core.Entity;
import articular.core.component.Component;

/**
 * A thread-safe version of the {@link ArticulationManager} to properly
 * synchronize structural changes (addition/removal) of the map {@link ArticulationManager#getEntities()}
 * with the game loop update {@link ArticulationManager#update(Object)}.
 *
 * @author pavl_g
 */
public class ConcurrentArticulationManager extends ArticulationManager {

    /**
     * Instantiates a thread-safe ECS manager.
     */
    public ConcurrentArticulationManager() {
        super();
    }

    @Override
    public synchronized void register(Entity entity) {
        super.register(entity);
    }

    @Override
    public synchronized void register(Entity entity, Component component) {
        super.register(entity, component);
    }

    @Override
    public synchronized void unregister(Entity entity) {
        super.unregister(entity);
    }

    @Override
    public synchronized void unregister(Entity entity, Component component) {
        super.unregister(entity, component);
    }

    @Override
    public synchronized void update(Object input) {
        super.update(input);
    }
}
