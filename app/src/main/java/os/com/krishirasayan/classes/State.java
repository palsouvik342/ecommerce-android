package os.com.krishirasayan.classes;

import java.util.ArrayList;

public class State {

    private int id;
    private String name;
    private ArrayList<Language> languages;

    public State(int id, String name, ArrayList<Language> languages) {
        this.id = id;
        this.name = name;
        this.languages = languages;
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


    public ArrayList<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<Language> languages) {
        this.languages = languages;
    }


    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof State) {
            State d = (State) obj;
            if (d.getName().equals(name) && d.getId() == id) return true;
        }

        return false;
    }
}