
package it.com.torneo;

import java.util.Objects;

public class Squadra {
    private String nome;
    private String sede;

    public Squadra(String nome, String sede) {
        this.nome = nome;
        this.sede = sede;
    }
    
    public int getPunteggio(){
        //scorrere tutte le partita della squadra
        
        return 0;
    }

    public String getNome() {
        return nome;
    }

    public String getSede() {
        return sede;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.nome);
        hash = 17 * hash + Objects.hashCode(this.sede);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Squadra other = (Squadra) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.sede, other.sede)) {
            return false;
        }
        return true;
    }

    String getCSV() {
        String ris = "";

        ris += nome + ";" + sede + "\n";

        return ris;

    }    
}
