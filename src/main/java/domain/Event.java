package domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.GenericType;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class Event {

    private long codi;


    private String dataFi;

   private String dataInici;

    public long getCodi() {
        return codi;
    }

    public void setCodi(long codi) {
        this.codi = codi;
    }

    private String dataFiAprox;

   private String denominacio;

   private String descripcio;

   private String entrades;

   private String horari;

   private String subtitol;

   private List<String> tagsAmbits;

   private List<String> tagsCateg;

   private List<String> tagsAltresCateg;

   private String links;

   private String documents;

   private String imatges;

   private String videos;

   private String adreca;

   private int codiPostal;

   private String comarcaIMunicipi;

   private String email;

   private String espai;

   private double latitud;

   private String localitat;

   private double longitud;

   private String regioOPais;

   private String telf;

   private String URL;

   private String imgApp;

   private String descripcioHtml;


}
