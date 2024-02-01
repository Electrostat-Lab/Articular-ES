# <img src="https://github.com/Software-Hardware-Codesign/Articular-ES/assets/60224159/6d7d9ff3-60f9-476e-85aa-686ca4330b7f" width=120 height=120/> Articular-ES
An entity component system (ECS) framework that provides good articulations between components
among different systems. The API is powered by a caching system that caches data in different
configurations. Operational interactions take place within the system manager through the controller
objects.

## Software specification:

| Problem                                                                                                                                                                                                                                                                                        | General Approach                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | Articular-ES Approach                                                                                                                                                                                                                                                                                                                                                                                                                                                            | Date Caching                                                                                                                                                                                                                                                                                                                    | Articular-monkey                                                                                                                                                              | 
|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| In software detailed design phase, generalization or inheritance, a crucial part of OOP paradigm, has already created a lot of mess in regard to data association between objects, and re-usability or extensibility of these associations which becomes a burden in the implementation phase. | This is a structural problem (requires a structural architecture), thus a generalized proposed solution is to build solution repositories that take hold of exclusive independent data units (composition applied). A solution repository will also take care of the instructional code by providing action-holder interfaces (strategy pattern applied), then organizational problems can be decomposed into systems and solved by incrementally providing output processed packets using design-patterns. Two commonly used patterns are the recursive pattern, and the finite-state automaton pattern. The architecture, in-use here, can be well categorized under the data-centered architectural patterns. | Articular-ES approaches this problem similarly by providing a `system manager`(solution repository) that takes hold of `components`(data units) in a system-first memory format (system-[entity]-component). A system manager is an instantiable object that is once instantiated, it can composite components in their respective systems. A system manager also provides the ability to perform user-defined actions on this memory map format through the strategic patterns. | Caching data in other formats can help the system manager to provide operational actions on even bizarre memory configurations, for example: (entity-[system]-component); where all the components under an entity can be accessed with a single CPU clock phase without the need to execute a searching algorithm of any sort. | Articular-ES is a generalized architectural API. Specializations can be implemented easily bearing in mind the pros and weaknesses. Articular-monkey is a WIP specialization. |

## Provisional architectural component diagram:
<img src="https://github.com/Software-Hardware-Codesign/Articular-ES/blob/master/.assets/component-interface-design.svg"/>

## Development phases: 
- [x] Architecture.
- [x] Detailed design.
- [x] Constructional design.
- [ ] Articular-monkey design.
- [ ] Deployment design.
- [x] Testing and tech-demos.
    * [TestArticularManager](https://github.com/Software-Hardware-Codesign/Articular-ES/blob/master/articular-examples/src/main/java/articular/example/TestArticularManager.java): tests the articular manager utility composing both the standard ecs manager, and the cache manager in a embedded data-flow environment.  
- [ ] Documentation.
- [ ] Deployment scripting.
- [ ] Deployment.
- [ ] Real life examples (Serial4j).

## In-use architectural patterns: 
- [x] Data-centered architectural pattern.
- [x] Hierarchical architectural pattern.
- [ ] Data-flow pipe architectural pattern.

## In-use detailed-design patterns:
- [x] Strategy pattern.
- [x] Template pattern.
- [x] Composite pattern.
- [ ] Finite-state-automata (FSA).

## In-use DSA libraries: 
- [x] Java Collection framework.
- [ ] Arithmos.
