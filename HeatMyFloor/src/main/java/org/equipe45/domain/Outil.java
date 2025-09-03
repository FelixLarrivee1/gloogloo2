package org.domain;

import java.util.UUID;

public class Outil {
    //id
    //name
    //profondeur (int)

    private UUID id;
    private String name;
    //private int profondeur;
    private float profondeur;

    public Outil(String name, float profondeur) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.profondeur = profondeur;
    }


    public Outil(UUID id, String name, float profondeur) {
        this.id = id;
        this.name = name;
        this.profondeur = profondeur;
    }

    public UUID getId() { return this.id; }
    public String getName() { return this.name; }

    public float getProfondeur() {return profondeur;}
}
