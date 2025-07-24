package de.muenchen.lizenzliste.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListRequest {
    public String application;
    public String user;
}
