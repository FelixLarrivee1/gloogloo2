

package org.equipe39.dto;


import org.equipe39.domain.Outil;

import java.util.UUID;

public class OutilDTO {
    public UUID id;
    public String name;
    public double epaisseur;

    public OutilDTO(String name, double epaisseur) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.epaisseur = epaisseur;
    }
    
    public OutilDTO( UUID id,  String name,  double epaisseur) {
        this.id = id;
        this.name = name;
        this.epaisseur = epaisseur;
    }

    //to outil
    public Outil toOutil() {
        return new Outil(this.id, this.name, this.epaisseur);
    }

    //to string
    @Override
    public String toString() {
        return "OutilDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", epaisseur=" + epaisseur +
                '}';
    }

}
