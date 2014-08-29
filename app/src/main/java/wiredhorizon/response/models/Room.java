package wiredhorizon.response.models;

/**
 * Created by chriszhu on 7/9/14.
 */
public class Room {
    private String name;
    private int id;
    private boolean owned;

    public Room(int id, String name, boolean owned) {
        this.id = id;
        this.name = name;
        this.owned = owned;
    }

    public boolean isOwned() {
        return this.owned;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }
}
