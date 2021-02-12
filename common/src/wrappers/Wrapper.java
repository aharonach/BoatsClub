package wrappers;

import java.util.Map;

public interface Wrapper {
    int getId();

    /**
     * Return a map with "field key" => "field value"
     * for every field that has changed
     *
     * @return map
     */
    Map<String, Object> updatedFields();

    default boolean changed() {
        return this.updatedFields().size() > 0;
    }
}
