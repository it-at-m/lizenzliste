package de.muenchen.lizenzliste.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoftwareGroup {
    public String name;
    public String displayName;
    @JsonIgnore
    public String group;
}
