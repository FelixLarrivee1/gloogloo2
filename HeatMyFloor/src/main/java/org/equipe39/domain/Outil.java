package org.equipe39.domain;

import org.equipe39.dto.OutilDTO;

import java.util.UUID;

public class Outil {
    private UUID id;
    private String name;
    private double epaisseur;

    public Outil(String name, double epaisseur) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.epaisseur = epaisseur;
    }


    public Outil(UUID id, String name, double epaisseur) {
        this.id = id;
        this.name = name;
        this.epaisseur = epaisseur;
    }

    public UUID getId() { return this.id; }
    public String getName() { return this.name; }
    public double getEpaisseur() {return epaisseur;}
    public void setEpaisseur(double epaisseur) {this.epaisseur = epaisseur;}
    public void setName(String name) {this.name = name;}

    public OutilDTO toDTO() {
        return new OutilDTO(this.id, this.name, this.epaisseur);
    }

    @Override
    public boolean equals(Object o) {
        //if(this.id == ((Outil) o).id)
        if(this.id.equals(((Outil) o).id))
        {
            return true;
        }
        return false;
    }

    //to string
    @Override
    public String toString() {
        return "Outil{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", epaisseur=" + epaisseur +
                '}';
    }
}
