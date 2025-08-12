package com.example.Mapping.Repository;

import com.example.Mapping.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    Tag findByTagCode(String tagCode);


}
