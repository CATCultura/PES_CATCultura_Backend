package domain;

import java.util.ArrayList;

public class Event {
    private long codi;
    private String data_fi;

    private String data_inici;
    private String horari;

    private String descripcio;
    private ArrayList<String> tags_ambits;
    private ArrayList<String> tags_categories;

    public String getData_fi() {
        return data_fi;
    }

    public void setData_fi(String data_fi) {
        this.data_fi = data_fi;
    }

    public long getCodi() {
        return codi;
    }

    public void setCodi(long codi) {
        this.codi = codi;
    }

    public String getHorari() {
        return horari;
    }

    public void setHorari(String horari) {
        this.horari = horari;
    }

    public String getData_inici() {
        return data_inici;
    }

    public void setData_inici(String data_inici) {
        this.data_inici = data_inici;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public ArrayList<String> getTags_ambits() {
        return tags_ambits;
    }

    public void setTags_ambits(ArrayList<String> tags_ambits) {
        this.tags_ambits = tags_ambits;
    }

    public ArrayList<String> getTags_categories() {
        return tags_categories;
    }

    public void setTags_categories(ArrayList<String> tags_categories) {
        this.tags_categories = tags_categories;
    }
}
