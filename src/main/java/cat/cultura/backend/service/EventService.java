package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.repository.EventJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired
    public EventJpaRepository repo;

    public Event saveEvent(Event ev) {
        return repo.save(ev);
    }

    public List<Event> saveEvents(List<Event> evs) {
        return repo.saveAll(evs);
    }

    public List<Event> getEvents() {
        return repo.findAll();
    }

    public Event getEventByID(Long id) {
        return repo.findById(id).orElse(null);
    }

//    public Event getEventByCodi(Long codi) {
//        return repo.findByCodi(codi);
//    }

    public String deleteEvent(Long id) {
        repo.deleteById(id);
        return "event removed";
    }

    public Event updateEvent(Event ev) {
        Event existingEvent = repo.findById(ev.getId()).orElse(null);
        existingEvent.setCodi((Long) ev.getCodi());
        existingEvent.setDataFi(ev.getDataFi());
        existingEvent.setDataInici(ev.getDataInici());
        existingEvent.setDataFiAprox(ev.getDataFiAprox());
        existingEvent.setDenominacio(ev.getDenominacio());
        existingEvent.setDescripcio(ev.getDescripcio());
        existingEvent.setEntrades(ev.getEntrades());
        existingEvent.setHorari(ev.getHorari());
        existingEvent.setSubtitol(ev.getSubtitol());
        existingEvent.setTagsAmbits(ev.getTagsAmbits());
        existingEvent.setTagsCateg(ev.getTagsCateg());
        existingEvent.setTagsAltresCateg(ev.getTagsAltresCateg());
        existingEvent.setLinks(ev.getLinks());
        existingEvent.setDocuments(ev.getDocuments());
        existingEvent.setImatges(ev.getImatges());
        existingEvent.setVideos(ev.getVideos());
        existingEvent.setAdreca(ev.getAdreca());
        existingEvent.setCodiPostal(ev.getCodiPostal());
        existingEvent.setComarcaIMunicipi(ev.getComarcaIMunicipi());
        existingEvent.setEmail(ev.getEmail());
        existingEvent.setEspai(ev.getEspai());
        existingEvent.setLatitud(ev.getLatitud());
        existingEvent.setLocalitat(ev.getLocalitat());
        existingEvent.setLongitud(ev.getLongitud());
        existingEvent.setRegioOPais(ev.getRegioOPais());
        existingEvent.setTelf(ev.getTelf());
        existingEvent.setURL(ev.getURL());
        existingEvent.setImgApp(ev.getImgApp());
        existingEvent.setDescripcioHtml(ev.getDescripcioHtml());
        return repo.save(existingEvent);
    }
}