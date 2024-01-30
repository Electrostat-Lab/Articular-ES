package articular.throwable;

import articular.util.Validator;

public class AssociatedObjectNotFoundException extends RuntimeException {
    protected final Validator.Message validatorMessage;

    public AssociatedObjectNotFoundException(Validator.Message validatorMessage) {
        super(validatorMessage.getMessage());
        this.validatorMessage = validatorMessage;
    }

    public Validator.Message getValidatorMessage() {
        return validatorMessage;
    }
}
