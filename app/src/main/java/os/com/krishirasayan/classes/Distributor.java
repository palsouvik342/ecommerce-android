package os.com.krishirasayan.classes;

public class Distributor {

    private int id;
    private String name;

    public Distributor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Distributor) {
            Distributor d = (Distributor) obj;
            if (d.getName().equals(name) && d.getId() == id) return true;
        }

        return false;
    }
}