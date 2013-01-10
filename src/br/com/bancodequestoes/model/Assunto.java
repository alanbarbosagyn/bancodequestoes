package br.com.bancodequestoes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Alan
 */
@Entity
public class Assunto implements Serializable {

    public Assunto() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String nome;
    
    @ManyToMany
    private Collection<Questao> questoes;

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
        return questoes;
    }

    /**
     * @param questao the questao to set
     */
    public void setQuestao(Collection<Questao> questao) {
        this.questoes = questao;
    }
}
