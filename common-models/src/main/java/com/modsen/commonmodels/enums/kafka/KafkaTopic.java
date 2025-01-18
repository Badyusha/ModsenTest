package com.modsen.commonmodels.enums.kafka;

public enum KafkaTopic {
    CREATION_TOPIC(Constants.CREATION_TOPIC_VALUE),
    DELETION_TOPIC(Constants.DELETION_TOPIC_VALUE);

    KafkaTopic(String name) {
        if (!name.equals(this.name())) {
            throw new IllegalArgumentException();
        }
    }

    public static class Constants {
        public static final String CREATION_TOPIC_VALUE = "book-creation-topic";
        public static final String DELETION_TOPIC_VALUE = "book-deletion-topic";
    }
}
