# <img src="https://github.com/Software-Hardware-Codesign/Articular-ES/assets/60224159/6d7d9ff3-60f9-476e-85aa-686ca4330b7f" width=120 height=120/> Articular-ES
An entity component system (ECS) framework that provides good articulations between component objects and game entities in a user-friendly way featuring both the legacy template pattern with the added power of the DI pattern.

## Provisional Class Paradigm:
- [x] `articular.core.Entity`: represents a game entity articulating some game components.
> Provisional Code:
> ```java 
> /**
>  * @param <I> the type of the game loop input.
>  * 
>  * @author pavl_g
>  */ 
> public interface Entity<I> extends Component<I> {
>    Map<? super Number, Component<I> getComponents();
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
>  * @param <I> the type of the game loop input.
>  *
>  * @author pavl_g
>  */ 
> public interface Component<I> {
>    Entity getEntity();
>
>    void update(I input);
> 
>    Component.Id getId();
>
>    public static final record Id(long id) {
>    }
> }
> ```
- [x] `articular.util.EntityComponentManager`: manages the articulations between Game Entities and entities' components.
> Provisional Code:
> ```java
> public class EntityComponentManager<I> {
>   protected final Map<String, Entity> entities;
>
>   public ArticulationManager() {
>       this.entities = new HashMap<>();
>   }
>
>   public void register(Entity<I> entity) {
>       entities.put(entity.getName(), entity);
>   }
>
>   public void unregister(Entity<I> entity) {
>       entities.remove(entity.getName(), entity);
>   }
>
>   public void register(Entity<I> entity, Component<I> component) {
>     entity.getComponents().put(component.getComponentId().id(), component);
>   }
>
>   public void unregister(Entity<I> entity, Component<I> component) {
>     entity.getComponents().remove(component.getComponentId().id());
>   }
> }
