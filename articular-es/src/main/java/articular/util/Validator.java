package articular.util;

import articular.core.Entity;
import articular.core.Type;
import articular.core.system.AssociatedSystemNotFoundException;
import articular.core.system.EntityNotFoundException;
import articular.core.system.SystemController;

public final class Validator {
    public static void validate(Type.EntityMap entityMap, SystemController systemController) {
        if (entityMap == null) {
            throw new AssociatedSystemNotFoundException(systemController);
        }
    }

    public static void validate(Type.ComponentMap componentMap, Entity entity) {
        if (componentMap == null) {
            throw new EntityNotFoundException(entity);
        }
    }
}
