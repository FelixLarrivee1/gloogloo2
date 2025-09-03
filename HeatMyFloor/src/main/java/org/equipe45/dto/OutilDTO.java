

package org.dto;

import org.equipe45.domain.Outil;

import java.util.UUID;

public class OutilDTO {
    public UUID id;
    public String name;
    public float profondeur;

    public OutilDTO(String name, float profondeur) {
        this.name = name;
        this.profondeur = profondeur;
    }
    public OutilDTO(UUID id, String name, float profondeur) {
        this.id = id;
        this.name = name;
        this.profondeur = profondeur;
    }

    //to outil
    public Outil toOutil() {
        return new Outil(this.id, this.name, this.profondeur);
    }

}
