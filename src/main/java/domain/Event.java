package domain;

public class Event {
    private long codi;
    private String dataf;

    private String horari;

    public String getDataf() {
        return dataf;
    }

    public void setDataf(String dataf) {
        this.dataf = dataf;
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
}
