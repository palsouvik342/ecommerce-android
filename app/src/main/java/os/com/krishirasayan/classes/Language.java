package os.com.krishirasayan.classes;

public class Language {

    private int id;
    private String name;
    private String code;

    public Language(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Language) {
            Language d = (Language) obj;
            if (d.getName().equals(name) && d.getId() == id) return true;
        }

        return false;
    }
}