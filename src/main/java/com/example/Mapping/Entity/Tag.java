package com.example.Mapping.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(indexes = @Index(name = "idx_tag_code", columnList = "tag_code"))
public class Tag {

    @Id
    @GeneratedValue
    private UUID tag_id;

    @Column(name = "tag_code", unique = true)
    private String tagCode;

    private String tag_name;

    @JsonBackReference
    @ManyToMany(mappedBy = "tags" )
    private Set<Product> products = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o) || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return tag_id != null && tag_id.equals(tag.tag_id);
    }

    @Override
    public int hashCode() {
        return tag_id != null ? tag_id.hashCode() : 0;
    }

}
