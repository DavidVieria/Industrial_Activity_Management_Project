/**
 * Represents an operation in a production or workflow system.
 * An operation has a unique identifier and a name describing its purpose.
 */
package project.domain.model;

public class Operation {

    private String id;
    private String name;

    /**
     * Constructs an Operation with the specified ID and name.
     *
     * @param id   The unique identifier for the operation.
     * @param name The name of the operation.
     */
    public Operation(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the unique identifier of the operation.
     *
     * @return The operation's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the operation.
     *
     * @return The operation's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the operation, including its ID and name.
     *
     * @return A string representation of the operation.
     */
    @Override
    public String toString() {
        return "Operation: {" +
                "id= '" + id + '\'' +
                ", name= '" + name + "'}";
    }
}
