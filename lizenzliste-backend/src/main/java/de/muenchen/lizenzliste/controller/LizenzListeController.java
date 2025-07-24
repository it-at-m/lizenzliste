package de.muenchen.lizenzliste.controller;

import de.muenchen.lizenzliste.dto.ListRequest;
import de.muenchen.lizenzliste.dto.Person;
import de.muenchen.lizenzliste.dto.SoftwareGroup;
import de.muenchen.lizenzliste.dto.SoftwareGroups;
import de.muenchen.lizenzliste.props.LizenzListeProperties;
import de.muenchen.lizenzliste.service.LdapService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class LizenzListeController {

    private LdapService ldapService;
    private final SoftwareGroups groups;
    private final LizenzListeProperties config;

    @PostMapping("/list")
    public List<Person> getGroup(@RequestBody ListRequest request) throws IOException
    {
        if (!groups.containsKey(request.getApplication())) {
            throw new RuntimeException("Group does not exist");
        }
        return ldapService.getADGroupMembers(groups.get(request.getApplication()), new Person(request.getUser()));
    }

    @GetMapping("/software")
    public List<SoftwareGroup> getGroups() {
        return groups.entrySet().stream().map((e) -> new SoftwareGroup(e.getKey(), e.getValue().getDisplayName(), e.getValue().getGroup())).toList();
    }

    @GetMapping("/config")
    public LizenzListeProperties getConfig() {
        return new LizenzListeProperties(config.getAd(), config.getGroups());
    }
}
