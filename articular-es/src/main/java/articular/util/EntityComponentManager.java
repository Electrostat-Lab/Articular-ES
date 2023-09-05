package articular.util;

import articular.core.Entity;
import articular.core.component.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * The Entity Component System (ECS) Manager manages the articulation (linkage) between game entities
 * and game entities' components.
 *
 * <p>
 * This utility is optional to use, it helps in writing good applications.
 * </p>
 *
 * <p>
 * This is utility is not thread-safe.
 * </p>
 *
 * <p>
 * This utility is best used as a composited object inside a Game Engine State class registered
 * to the game loop.
 * </p>
 *
 * @author pavl_g
 */
public class ArticulationManager<I> {

    /**
     * The proposed Game entities
     */
    protected final Map<String, Entity<I>> entities;

    /**
     * Instantiates an articulation manager object by
     * initializing a {@link HashMap} of game {@link Entity}s.
     */
    public ArticulationManager() {
        this.entities = new HashMap<>();
    }

    /**
     * Registers a game entity to this game articulation manager.
     *
     * @param entity a game entity instance to register
     */
    public void register(Entity<I> entity) {
        entities.put(entity.getName(), entity);
    }

    /**
     * Unregisters a game entity to this game articulation manager.
     *
     * @param entity a game entity instance to unregister
     */
    public void unregister(Entity<I> entity) {
        entities.remove(entity.getName(), entity);
    }

    /**
     * Registers a game entity component to a game entity.
     *
     * @param entity the game entity instance
     * @param component the component instance to register to this game entity
     */
    public void register(Entity<I> entity, Component component) {
        entity.getComponents().put(component.getId().getId(), component);
    }

    /**
     * Unregisters a game entity component from a game entity.
     *
     * @param entity the game entity instance
     * @param component the component instance to be unregistered from this game entity
     */
    public void unregister(Entity<I> entity, Component component) {
        entity.getComponents().remove(component.getId().getId());
    }

    /**
     * Updates the registered game entities and their components.
     *
     * @param input the game loop input value
     */
    public void update(I input) {
        entities.forEach((key, entity) -> entity.update(input));
    }

    /**
     * Retrieves the registered game entities.
     *
     * @return a map of the registered game entities
     */
    public Map<String, Entity<I>> getEntities() {
        return entities;
    }
}