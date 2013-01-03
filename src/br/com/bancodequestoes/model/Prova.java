package br.com.bancodequestoes.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Alan
 */
@Entity
public class Prova implements Serializable {

    public Prova() {
    }
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String nome;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar dataElaboracao;

    @OneToMany(mappedBy = "prova")
    private Collection<Questao> questao;

    @ManyToOne
    private Professor professor;

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
     * @return the dataElaboracao
     */
    public Calendar getDataElaboracao() {
        return dataElaboracao;
    }

    /**
     * @param dataElaboracao the dataElaboracao to set
     */
    public void setDataElaboracao(Calendar dataElaboracao) {
        this.dataElaboracao = dataElaboracao;
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

    /**
     * @return the professor
     */
    public Professor getProfessor() {
        return professor;
    }

    /**
     * @param professor the professor to set
     */
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
