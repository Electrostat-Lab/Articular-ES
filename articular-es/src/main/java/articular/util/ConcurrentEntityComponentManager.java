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
public class ConcurrentArticulationManager<I> extends ArticulationManager<I> {

    /**
     * Instantiates a thread-safe ECS manager.
     */
    public ConcurrentArticulationManager() {
        super();
    }

    @Override
    public synchronized void register(Entity<I> entity) {
        super.register(entity);
    }

    @Override
    public synchronized void register(Entity<I> entity, Component component) {
        super.register(entity, component);
    }

    @Override
    public synchronized void unregister(Entity<I> entity) {
        super.unregister(entity);
    }

    @Override
    public synchronized void unregister(Entity<I> entity, Component component) {
        super.unregister(entity, component);
    }

    @Override
    public synchronized void update(I input) {
        super.update(input);
    }
}
