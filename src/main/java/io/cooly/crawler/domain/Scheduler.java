package io.cooly.crawler.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Scheduler.
 */
@Document(collection = "scheduler")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "scheduler")
public class Scheduler implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("timezone")
    private String timezone;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Scheduler name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimezone() {
        return timezone;
    }

    public Scheduler timezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Scheduler scheduler = (Scheduler) o;
        if (scheduler.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scheduler.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Scheduler{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", timezone='" + getTimezone() + "'" +
            "}";
    }
}
