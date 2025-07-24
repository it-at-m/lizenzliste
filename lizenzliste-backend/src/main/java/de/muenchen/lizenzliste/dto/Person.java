package de.muenchen.lizenzliste.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String uid;
    private String displayName;
    private String department;

    public Person(final String uid) {
        this.uid = uid;
    }
}
