package de.muenchen.lizenzliste.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupProperties {
    private String name;
    private String displayName;
}
