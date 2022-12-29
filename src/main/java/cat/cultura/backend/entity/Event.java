package cat.cultura.backend.entity;

import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.utils.RandomString;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.lang.reflect.*;
import java.util.Map;

@Entity
@Table(name = "Event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="codi")
    private Long codi;

    @Column(name="dataFi")
    private String dataFi;

    @Column(name="dataInici")
    private String dataInici;

    @Column(name="dataFiAprox")
    private String dataFiAprox;

    @Column(name="denominacio")
    private String denominacio;

    @Lob
    @Column(name="descripcio")
    private String descripcio;

    @Column(name = "entrades", length = 1024)
    private String entrades;

    @Lob
    @Column(name="horari")
    private String horari;

    @Column(name="subtitol")
    private String subtitol;

    @ManyToMany
    private List<Tag> tagsAmbits = new ArrayList<>();

    @ManyToMany
    private List<Tag> tagsCateg = new ArrayList<>();

    @ManyToMany
    private List<Tag> tagsAltresCateg = new ArrayList<>();

    @Lob
    @Column(name="links")
    private String links;

    @Column(name="documents")
    private String documents;

    @ElementCollection
    @CollectionTable(name="imatges", joinColumns=@JoinColumn(name="id"))
    @Column(name="imatges")
    private List<String> imatges;

    @Lob
    @Column(name="videos")
    private String videos;

    @Column(name="adreca")
    private String adreca = "";

    @Column(name="codiPostal")
    private int codiPostal;

    @Column(name="ubicacio")
    private String ubicacio;

    @Column(name="email")
    private String email;

    @Column(name="espai")
    private String espai = "";

    @Column(name="latitud")
    private double latitud;

    @Column(name="longitud")
    private double longitud;

    @Column(name="telf")
    private String telf;

    @Column(name="URL")
    private String URL;

    @Column(name="imgApp")
    private String imgApp;

    @Column(name="cancelado")
    private boolean cancelado;

    @ManyToOne
    private Organizer organizer;

    @Column(name="outdated")
    @ColumnDefault("false")
    private boolean outdated;

    @Column(name="attendanceCode")
    private String attendanceCode;

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

    public List<Tag> getTagsAmbits() {
        return tagsAmbits;
    }

    public void setTagsAmbits(List<Tag> tagsAmbits) {
        this.tagsAmbits = tagsAmbits;
    }

    public List<Tag> getTagsCateg() {
        return tagsCateg;
    }

    public void setTagsCateg(List<Tag> tagsCateg) {
        this.tagsCateg = tagsCateg;
    }

    public List<Tag> getTagsAltresCateg() {
        return tagsAltresCateg;
    }

    public void setTagsAltresCateg(List<Tag> tagsAltresCateg) {
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

    public void setImgApp(String imgApp) {
        this.imgApp = imgApp;
    }


    public boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public String getImgApp() {
        return imgApp;
    }


    public String getUbicacio() {
        return ubicacio;
    }

    public void setUbicacio(String ubicacio) {
        this.ubicacio = ubicacio;
    }

    public boolean isOutdated() {
        return outdated;
    }

    public void setOutdated(boolean outdated) {
        this.outdated = outdated;
    }

    public String getAttendanceCode() {
        return attendanceCode;
    }

    public void setAttendanceCode(String attendanceCode) {
        this.attendanceCode = attendanceCode;
    }

    public String generateAttendanceCode() {
        attendanceCode = RandomString.generate();
        return attendanceCode;
    }

    public boolean isPastEvent() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000'");
        Date date = new Date(System.currentTimeMillis());
        return dataInici.compareTo(formatter.format(date)) < 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Event that = (Event) obj;
        if (!this.denominacio.equalsIgnoreCase(that.denominacio)) return false;
        if (!this.dataInici.equalsIgnoreCase(that.dataInici)) return false;
        if (!this.ubicacio.equalsIgnoreCase(that.ubicacio)) return false;
        if (!this.adreca.equalsIgnoreCase(that.adreca)) return false;
        return this.espai.equalsIgnoreCase(that.espai);
    }

    @Override
    public int hashCode() {
        int hash = ((this.id == null) ? 1 : this.id.hashCode());
        hash += this.denominacio.hashCode() + this.dataInici.hashCode() + ubicacio.hashCode();
        hash += adreca.hashCode() + espai.hashCode();
        return hash;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public void update(Map<String, Object> ev) {
        Class cls = this.getClass();
        Field[] fieldList = cls.getDeclaredFields();
        for (Field f : fieldList) {
            if (ev.containsKey(f.getName())) {
                try {
                    f.set(this, ev.get(f.getName()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
