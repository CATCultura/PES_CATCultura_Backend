package cat.cultura.backend;

import javax.persistence.*;

@Entity
@Table(name = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="codi")
    private int codi;
    @Column(name="dataFi")
    private String dataFi;
    @Column(name="dataInici")
    private String dataInici;
    @Column(name="dataFiAprox")
    private String dataFiAprox;
    @Column(name="denominacio")
    private String denominacio;
    @Column(name="descripcio")
    private String descripcio;
    @Column(name="entrades")
    private String entrades;
    @Column(name="horari")
    private String horari;
    @Column(name="subtitol")
    private String subtitol;
    @Column(name="tagsAmbits")
    private String tagsAmbits;
    @Column(name="tagsCateg")
    private String tagsCateg;
    @Column(name="tagsAltresCateg")
    private String tagsAltresCateg;
    @Column(name="links")
    private String links;
    @Column(name="documents")
    private String documents;
    @Column(name="imatges")
    private String imatges;
    @Column(name="videos")
    private String videos;
    @Column(name="adreca")
    private String adreca;
    @Column(name="codiPostal")
    private int codiPostal;
    @Column(name="comarcaIMunicipi")
    private String comarcaIMunicipi;
    @Column(name="email")
    private String email;
    @Column(name="espai")
    private String espai;
    @Column(name="latitud")
    private double latitud;
    @Column(name="localitat")
    private String localitat;
    @Column(name="longitud")
    private double longitud;
    @Column(name="regioOPais")
    private String regioOPais;
    @Column(name="telf")
    private String telf;
    @Column(name="URL")
    private String URL;
    @Column(name="imgApp")
    private String imgApp;
    @Column(name="descripcioHtml")
    private String descripcioHtml;

    public int getCodi() {
        return codi;
    }

    public void setCodi(int codi) {
        this.codi = codi;
    }

    public String getDataFi() {
        return dataFi;
    }

    public void setDataFi(String dataFi) {
        this.dataFi = dataFi;
    }

    public String getDataInici() {
        return dataInici;
    }

    public void setDataInici(String dataInici) {
        this.dataInici = dataInici;
    }

    public String getDataFiAprox() {
        return dataFiAprox;
    }

    public void setDataFiAprox(String dataFiAprox) {
        this.dataFiAprox = dataFiAprox;
    }

    public String getDenominacio() {
        return denominacio;
    }

    public void setDenominacio(String denominacio) {
        this.denominacio = denominacio;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getEntrades() {
        return entrades;
    }

    public void setEntrades(String entrades) {
        this.entrades = entrades;
    }

    public String getHorari() {
        return horari;
    }

    public void setHorari(String horari) {
        this.horari = horari;
    }

    public String getSubtitol() {
        return subtitol;
    }

    public void setSubtitol(String subtitol) {
        this.subtitol = subtitol;
    }

    public String getTagsAmbits() {
        return tagsAmbits;
    }

    public void setTagsAmbits(String tagsAmbits) {
        this.tagsAmbits = tagsAmbits;
    }

    public String getTagsCateg() {
        return tagsCateg;
    }

    public void setTagsCateg(String tagsCateg) {
        this.tagsCateg = tagsCateg;
    }

    public String getTagsAltresCateg() {
        return tagsAltresCateg;
    }

    public void setTagsAltresCateg(String tagsAltresCateg) {
        this.tagsAltresCateg = tagsAltresCateg;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getImatges() {
        return imatges;
    }

    public void setImatges(String imatges) {
        this.imatges = imatges;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public String getAdreca() {
        return adreca;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

    public int getCodiPostal() {
        return codiPostal;
    }

    public void setCodiPostal(int codiPostal) {
        this.codiPostal = codiPostal;
    }

    public String getComarcaIMunicipi() {
        return comarcaIMunicipi;
    }

    public void setComarcaIMunicipi(String comarcaIMunicipi) {
        this.comarcaIMunicipi = comarcaIMunicipi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEspai() {
        return espai;
    }

    public void setEspai(String espai) {
        this.espai = espai;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getLocalitat() {
        return localitat;
    }

    public void setLocalitat(String localitat) {
        this.localitat = localitat;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getRegioOPais() {
        return regioOPais;
    }

    public void setRegioOPais(String regioOPais) {
        this.regioOPais = regioOPais;
    }

    public String getTelf() {
        return telf;
    }

    public void setTelf(String telf) {
        this.telf = telf;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getImgApp() {
        return imgApp;
    }

    public void setImgApp(String imgApp) {
        this.imgApp = imgApp;
    }

    public String getDescripcioHtml() {
        return descripcioHtml;
    }

    public void setDescripcioHtml(String descripcioHtml) {
        this.descripcioHtml = descripcioHtml;
    }
}
