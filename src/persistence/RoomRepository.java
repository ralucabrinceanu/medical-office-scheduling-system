package persistence;

import domain.Room;

import java.util.Vector;

public class RoomRepository implements GenericRepository<Room> {
    private static final Vector<Room> storage = new Vector<>();

    @Override
    public void add(Room entity) {
        storage.add(entity);
    }

    @Override
    public Room get(int index) {
        return storage.get(index);
    }

    @Override
    public void update(int index, Room entity) {
        storage.set(index, entity);
    }

    @Override
    public void delete(int index) {
        storage.remove(index);
    }

    @Override
    public int getSize() {
        return storage.size();
    }
}
