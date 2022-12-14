package cat.cultura.backend.dtos;

import java.util.ArrayList;
import java.util.List;

public class EventDto {

    private Long id;
    private Long codi;
    private String dataFi;
    private String dataInici;
    private String dataFiAprox;
    private String denominacio;
    private String descripcio;
    private String entrades;
    private String horari;
    private String subtitol;
    private List<String> tagsAmbits = new ArrayList<>();
    private List<String> tagsCateg = new ArrayList<>();
    private List<String> tagsAltresCateg = new ArrayList<>();
    private String links;
    private String documents;
    private List<String> imatges;
    private String videos;
    private String adreca = "";
    private int codiPostal;
    private String email;
    private String espai = "";
    private double latitud;
    private double longitud;
    private String telf;
    private String URL;
    private String ubicacio;
    private String imgApp;
    private boolean cancelado;
    private boolean outdated;

    private String nomOrganitzador;

    private String urlOrganitzador;

    private String telefonOrganitzador;

    private String emailOrganitzador;

    public Long getIdOrganitzador() {
        return idOrganitzador;
    }

    public void setIdOrganitzador(Long idOrganitzador) {
        this.idOrganitzador = idOrganitzador;
    }

    private Long idOrganitzador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodi() {
        return codi;
    }

    public void setCodi(Long codi) {
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

    public List<String> getTagsAmbits() {
        return tagsAmbits;
    }

    public void setTagsAmbits(List<String> tagsAmbits) {
        this.tagsAmbits = tagsAmbits;
    }

    public List<String> getTagsCateg() {
        return tagsCateg;
    }

    public void setTagsCateg(List<String> tagsCateg) {
        this.tagsCateg = tagsCateg;
    }

    public List<String> getTagsAltresCateg() {
        return tagsAltresCateg;
    }

    public void setTagsAltresCateg(List<String> tagsAltresCateg) {
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

    public List<String> getImatges() {
        return imatges;
    }

    public void setImatges(List<String> imatges) {
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

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
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

    public String getUbicacio() {
        return ubicacio;
    }

    public void setUbicacio(String ubicacio) {
        this.ubicacio = ubicacio;
    }

    public String getNomOrganitzador() {
        return nomOrganitzador;
    }

    public void setNomOrganitzador(String nomOrganitzador) {
        this.nomOrganitzador = nomOrganitzador;
    }

    public String getUrlOrganitzador() {
        return urlOrganitzador;
    }

    public void setUrlOrganitzador(String urlOrganitzador) {
        this.urlOrganitzador = urlOrganitzador;
    }

    public String getTelefonOrganitzador() {
        return telefonOrganitzador;
    }

    public void setTelefonOrganitzador(String telefonOrganitzador) {
        this.telefonOrganitzador = telefonOrganitzador;
    }

    public String getEmailOrganitzador() {
        return emailOrganitzador;
    }

    public void setEmailOrganitzador(String emailOrganitzador) {
        this.emailOrganitzador = emailOrganitzador;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public boolean isOutdated() {
        return outdated;
    }

    public void setOutdated(boolean outdated) {
        this.outdated = outdated;
    }
}
