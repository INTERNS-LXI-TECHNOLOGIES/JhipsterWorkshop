package com.lxisoft.socialmedia.domain.enumeration;

/**
 * The ReactionMode enumeration.
 */
public enum ReactionMode {
    LIKE("Like"),
    HEART("Heart"),
    DISLIKE("Dislike");

    private final String value;

    ReactionMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
