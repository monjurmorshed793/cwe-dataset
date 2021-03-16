package ac.bd.buet.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A CweList.
 */
@Entity
@Table(name = "cwe_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cwelist")
public class CweList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cwe_id")
    private String cweId;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "code")
    private String code;

    @Column(name = "tags")
    private String tags;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CweList id(Long id) {
        this.id = id;
        return this;
    }

    public String getCweId() {
        return this.cweId;
    }

    public CweList cweId(String cweId) {
        this.cweId = cweId;
        return this;
    }

    public void setCweId(String cweId) {
        this.cweId = cweId;
    }

    public String getDescription() {
        return this.description;
    }

    public CweList description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public CweList code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTags() {
        return this.tags;
    }

    public CweList tags(String tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CweList)) {
            return false;
        }
        return id != null && id.equals(((CweList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CweList{" +
            "id=" + getId() +
            ", cweId='" + getCweId() + "'" +
            ", description='" + getDescription() + "'" +
            ", code='" + getCode() + "'" +
            ", tags='" + getTags() + "'" +
            "}";
    }
}
