package org.equipe39.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.equipe39.domain.Cut.Cut;
import org.equipe39.domain.InteractiveEntity;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "dtoType" // ce champ doit exister dans le JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SquareDTO.class, name = "square"),
        @JsonSubTypes.Type(value = LDTO.class, name = "l"),
        @JsonSubTypes.Type(value = PanelDTO.class, name = "panel"),
        @JsonSubTypes.Type(value = RestrictedAreaDTO.class, name = "restrictedArea"),
        @JsonSubTypes.Type(value = VerticalLineDTO.class, name = "verticalLine"),
        @JsonSubTypes.Type(value = HorizontalLineDTO.class, name = "horizontalLine"),
        @JsonSubTypes.Type(value = BordureDTO.class, name = "bordure"),
        @JsonSubTypes.Type(value = PointReferenceDTO.class, name = "pointReference")
})

public abstract class InteractiveEntityDTO {
    public UUID id;
    public boolean isSelected;

    @JsonCreator
    public InteractiveEntityDTO(@JsonProperty("id") UUID id, @JsonProperty("isSelected") boolean isSelected) {
        this.id = id;
        this.isSelected = isSelected;
    }

    public abstract InteractiveEntity toInteractiveEntity();

}