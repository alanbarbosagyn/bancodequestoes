package br.com.bancodequestoes.model;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Alan
 */
@Entity
public class Professor extends Usuario {
    
    @OneToMany(mappedBy = "professor")
    private Collection<Prova> provas;

    public Professor() {
    }

    /**
     * @return the provas
     */
    public Collection<Prova> getProvas() {
        return provas;
    }

    /**
     * @param provas the provas to set
     */
    public void setProvas(Collection<Prova> provas) {
        this.provas = provas;
    }
}
