package fr.devoxx.javaeeasy.services.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

@Entity
@NamedQueries({
        @NamedQuery(name = "Slot.findAll", query = "select s from SlotJpa s")
})
public class SlotJpa {
    @Id
    private String id;

    @Version
    private long version;

    public SlotJpa() {
        // no-op
    }

    public SlotJpa(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }
}
