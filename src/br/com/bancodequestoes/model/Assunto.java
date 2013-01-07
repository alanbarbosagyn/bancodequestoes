package br.com.bancodequestoes.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author Alan
 */
@Entity
public class Assunto implements Serializable {

    public Assunto() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;
    
    private String nome;
    
    @ManyToMany
    private Collection<Questao> questao;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the questao
     */
    public Collection<Questao> getQuestao() {
        return questao;
    }

    /**
     * @param questao the questao to set
     */
    public void setQuestao(Collection<Questao> questao) {
        this.questao = questao;
    }
}
