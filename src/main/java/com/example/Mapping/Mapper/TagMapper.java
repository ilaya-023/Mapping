package com.example.Mapping.Mapper;

import com.example.Mapping.Entity.Tag;
import com.example.Mapping.RequestBean.TagRequestDTO;
import com.example.Mapping.ResponceBean.TagResponseDTO;

public class TagMapper {

    public static Tag toEntity(TagRequestDTO dto) {
        if (dto == null) return null;

        return Tag.builder()
                .tagCode(dto.getTagCode())
                .tag_name(dto.getTagName())
                .build();
    }

    public static TagResponseDTO toDTO(Tag tag) {
        if (tag == null) return null;

        return TagResponseDTO.builder()
                .tagCode(tag.getTagCode())
                .tagName(tag.getTag_name())
                .build();
    }
}
