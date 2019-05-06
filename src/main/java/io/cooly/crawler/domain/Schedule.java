package io.cooly.crawler.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Schedule.
 */
@Document(collection = "schedule")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "schedule")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("name")
    private String name;

    // coolebot-needle-entity-add-field - Coolebot will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Schedule name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    // coolebot-needle-entity-add-getters-setters - Coolebot will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Schedule schedule = (Schedule) o;
        if (schedule.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), schedule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Schedule{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
