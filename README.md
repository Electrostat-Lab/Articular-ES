# Articular-ES
An entity component system (ECS) framework providing good articulations between component objects and game entities in a user-friendly way featuring both the legacy template pattern with the added power of the DI pattern.

## Provisional Class Paradigm:
- [x] `articular.core.Entity`: represents a game entity articulating some game components.
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
- [x] `articular.core.component.Component`: represents a game component to be utilized by a game entity.
> Provisional Code:
> ```java
> /**
>  * @param <E> the type of game entity encapsulating this component.
>  * @param <I> the type of the game loop input.
>  *
>  * @author pavl_g
>  */ 
> public interface Component<E, I> {
>    E getEntity();
>
>    void update(I input);
> 
>    Component.Id getId();
>
>    public static final record Id(Object id) {
>    }
> }
> ```
- [x] `articular.util.ArticulationManager`: manages the articulations between Game Entities and entities' components.
> Provisional Code:
> ```java
> public class ArticulationManager<E, C> {
>   protected final Map<String, E> entities;
>
>   public ArticulationManager() {
>       this.entities = new HashMap<>();
>   }
>
>   public void register(E entity) {
>       entities.put(entity.getName(), entity);
>   }
>
>   public void unregister(E entity) {
>       entities.remove(entity.getName(), entity);
>   }
>
>   public void register(E entity, C component) {
>     entity.getComponents().put(component.getComponentId().id(), component);
>   }
>
>   public void unregister(E entity, C component) {
>     entity.getComponents().remove(component.getComponentId().id());
>   }
> }
