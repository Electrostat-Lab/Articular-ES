package articular.core;

import articular.core.component.Component;
import java.util.Map;

/**
 * Represents a game entity with some {@link Component}s.
 *
 * <p>
 * A Map of components must be provided via a DI pattern.
 * </p>
 *
 * @author pavl_g
 */
public interface Entity {

    /**
     * A Map of game entity components to be provided.
     *
     * <p>
     * Override and provide a map of components for this entity via the DI pattern.
     * </p>
     *
     * @return a map of components by their identifiers (not null)
     */
    Map<? super Number, Component> getComponents();

    /**
     * Retrieves the name of this entity.
     *
     * <p>
     * Override and provide a name for this entity via the DI pattern.
     * </p>
     *
     * @return the name of this entity (not null)
     */
    String getName();

    /**
     * Retrieves a specific game entity component by its identifier.
     *
     * @param id the game entity component identifier
     * @return a game entity component or null if it doesn't exist (nullable)
     */
    default Component getComponent(Component.Id id) {
        return getComponents().get(id);
    }

    /**
     * Updates this game entity through updating its components.
     *
     * @param input the input to the game loop pattern
     */
    default void update(Object input) {
        getComponents().forEach((id, component) -> component.update(input));
    }
}
