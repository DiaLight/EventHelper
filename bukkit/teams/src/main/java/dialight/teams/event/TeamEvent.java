package dialight.teams.event;

import java.util.Collection;

public abstract class TeamEvent {

    private final String name;

    private TeamEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Type getType();

    public static class Add extends TeamEvent {
        public Add(String name) { super(name); }
        @Override public Type getType() { return Type.ADD; }
    }
    public static class Remove extends TeamEvent {
        public Remove(String name) { super(name); }
        @Override public Type getType() { return Type.REMOVE; }
    }
    public static class Update extends TeamEvent {
        public Update(String name) { super(name); }
        @Override public Type getType() { return Type.UPDATE; }
    }
    public abstract static class Members extends TeamEvent {
        private final Collection<String> members;
        private Members(String name, Collection<String> members) { super(name); this.members = members; }
        public Collection<String> getMembers() { return members; }
    }
    public static class AddMembers extends Members {
        public AddMembers(String name, Collection<String> members) { super(name, members); }
        @Override public Type getType() { return Type.ADD_MEMBERS; }
    }
    public static class RemoveMembers extends Members {
        public RemoveMembers(String name, Collection<String> members) { super(name, members); }
        @Override public Type getType() { return Type.REMOVE_MEMBERS; }
    }

    public enum Type {
        ADD,
        REMOVE,
        UPDATE,
        ADD_MEMBERS,
        REMOVE_MEMBERS
    }

}
