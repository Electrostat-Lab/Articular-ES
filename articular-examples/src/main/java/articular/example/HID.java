package articular.example;

import articular.core.system.ArticularSystem;

public enum HID implements ArticularSystem {
    INPUT_PROCESSOR("InputData-processing"),
    OUTPUT_PROCESSOR("OutputData-processing"),
    FEATURE_PROCESSOR("FeatureData-processing");
    private final String system;
    HID(final String system) {
        this.system = system;
    }

    @Override
    public String getId() {
        return system;
    }
}
