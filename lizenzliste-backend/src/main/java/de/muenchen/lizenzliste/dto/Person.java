package de.muenchen.lizenzliste.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    public String uid;
    public String displayName;
    public String department;

    public Person(String uid) {
        this.uid = uid;
    }
}
