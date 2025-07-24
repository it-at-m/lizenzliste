package de.muenchen.lizenzliste.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoftwareGroup {
    private String name;
    private String displayName;
    @JsonIgnore
    private String group;
}
