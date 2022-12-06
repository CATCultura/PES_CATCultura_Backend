package cat.cultura.backend.entity.tag;

import cat.cultura.backend.entity.Event;
import org.hibernate.annotations.DiscriminatorFormula;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "tags")
//@Table(name = "Tags")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("CASE when type=0 then 'Ambits' when type=1 then 'Categories' when type=2 then 'AltresCategories' end")
public abstract class Tag {

    protected Tag(String name, Type type) {
        this.type = type;
        this.value = name;
    }

    protected Tag() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name ="value")
    private String value;

    @Column(name ="type")
    private Type type;

    public abstract Set<Event> getEventList();

    public abstract void setEventList(Set<Event> eventList);

}
