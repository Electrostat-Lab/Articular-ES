# Articular-ES
An entity component system (ECS) framework providing good articulations between component objects and game entities in a user-friendly way featuring both the legacy template pattern with the added power of the DI pattern.

## Provisional Class Paradigm:
- [ ] `articular.core.Entity`: represents a game entity articulating some game components.
> Provisional Code:
> ```java
> /**
>  * @param <C> the type of game components.
>  * 
>  * @author pavl_g
>  */ 
> public interface Entity<C, I> {
>    Map<Component.Id, C> getComponents();
>    default C getComponent(Component.Id id) {
>      return getComponents().get(id);
>    }
>    default void update(I input) {
>       getComponents().foreach(component -> component.update(input));
>    }
> }
> ```
- [ ] `articular.core.component.Component`: represents a game component to be utilized by a game entity.
> Provisional Code:
> ```java
> /**
>  * @param <E> the type of game entity encapsulating this component.
>  * @param <I> the type of the game loop input.
>  *
>  * @author pavl_g
>  */ 
> public abstract class Component<E, I> {
>    protected final Component.Id componentId;
>    protected final E entity;
> 
>    public Component(Component.Id componentId, E entity) {
>       this.componentId = componentId;
>       this.entity = entity; 
>    }
>    public E getEntity() {
>        return this.entity;
>    }
>
>    public abstract void update(I input);
> 
>    public Component.Id getId() {
>        return this.componentId;
>    }
>
>    public static final record Id(Object id) {
>    }
> }
> ```
- [ ] `articular.core.ArticulationManager`: manages the articulation between Game Entities and entities' components.
