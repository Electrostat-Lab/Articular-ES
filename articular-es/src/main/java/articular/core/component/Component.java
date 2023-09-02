package articular.core.component;

import articular.core.Entity;
import articular.util.ArticulationManager;

/**
 * A template representing a Game {@link Entity} component.
 *
 * @author pavl_g
 */
public interface Component {

    /**
     * Retrieves the entity that encapsulates this component.
     *
     * <p>
     * Override this method and use the DI pattern to link the component
     * entity here.
     * </p>
     *
     * @return the entity encapsulating this game component
     */
    Entity getEntity();

    /**
     * Updates this game component with an abstract input.
     *
     * <p>
     * Override this method and use the Game loop pattern to link the
     * original game states to this component via the game {@link Entity}s.
     * </p>
     *
     * <p>
     * This method is dispatched by {@link Entity#update(Object)}, never call
     * it manually!
     * </p>
     *
     * @param input an input to perform the update on
     */
    void update(Object input);

    /**
     * Retrieves the identifier (id) of this game component, the identifier is
     * used by the {@link ArticulationManager} to link
     * game components to their entities (aka. articulation).
     *
     * @return the component identifier object
     */
    Component.Id getId();

    /**
     * Represents the component identifier of type number, this encapsulates
     * a constant long value to accommodate larger number of game entity components.
     */
    public static final class Id extends Number {
        private final long id;

        /**
         * Instantiates a game entity component identifier object.
         *
         * @param id the identifier in long format
         */
        public Id(final long id) {
            this.id = id;
        }

        /**
         * Retrieves the identifier in a long format.
         *
         * @return the component identifier in longs
         */
        public long getId() {
            return id;
        }

        @Deprecated
        @Override
        public int intValue() {
            throw new UnsupportedOperationException("Deprecated call, use `Component.Id#getId()`");
        }

        @Deprecated
        @Override
        public long longValue() {
            throw new UnsupportedOperationException("Deprecated call, use `Component.Id#getId()`");
        }

        @Deprecated
        @Override
        public float floatValue() {
            throw new UnsupportedOperationException("Deprecated call, use `Component.Id#getId()`");
        }

        @Deprecated
        @Override
        public double doubleValue() {
            throw new UnsupportedOperationException("Deprecated call, use `Component.Id#getId()`");
        }
    }
}
