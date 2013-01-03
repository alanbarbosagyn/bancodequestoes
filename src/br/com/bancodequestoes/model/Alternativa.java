package br.com.bancodequestoes.model;

import javax.persistence.*;

/**
 *
 * @author Alan
 */
@Entity
public class Alternativa {

    @Id
    @GeneratedValue
    private int id;
    
    private String descricao;
    private Questao questao;

    public Alternativa() {
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the questao
     */
    public Questao getQuestao() {
        return questao;
    }

    /**
     * @param questao the questao to set
     */
    public void setQuestao(Questao questao) {
        this.questao = questao;
    }
    
    
}
