package br.com.bancodequestoes.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author Alan
 */
@Entity
public class Questao implements Serializable {

    public Questao() {
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar dataCriacao;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar dataModificacao;
    
    private String usuarioCriador;
   
    private String usuarioModificador;
    
    @Column(nullable=false, length=250)    
    private String enunciado;
    
    @OneToOne
    private Alternativa resposta;
    
    @OneToMany(cascade= CascadeType.ALL, mappedBy="questao")
    private Collection<Alternativa> alternativas;
    
    @ManyToMany(mappedBy = "questoes")
    private Collection<Assunto> assuntos;
    
    @ManyToMany
    private Collection<Prova> provas;

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
     * @return the dataCriacao
     */
    public Calendar getDataCriacao() {
        return dataCriacao;
    }

    /**
     * @param dataCriacao the dataCriacao to set
     */
    public void setDataCriacao(Calendar dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    /**
     * @return the dataModificacao
     */
    public Calendar getDataModificacao() {
        return dataModificacao;
    }

    /**
     * @param dataModificacao the dataModificacao to set
     */
    public void setDataModificacao(Calendar dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    /**
     * @return the usuarioCriador
     */
    public String getUsuarioCriador() {
        return usuarioCriador;
    }

    /**
     * @param usuarioCriador the usuarioCriador to set
     */
    public void setUsuarioCriador(String usuarioCriador) {
        this.usuarioCriador = usuarioCriador;
    }

    /**
     * @return the usuarioModificador
     */
    public String getUsuarioModificador() {
        return usuarioModificador;
    }

    /**
     * @param usuarioModificador the usuarioModificador to set
     */
    public void setUsuarioModificador(String usuarioModificador) {
        this.usuarioModificador = usuarioModificador;
    }

    /**
     * @return the enunciado
     */
    public String getEnunciado() {
        return enunciado;
    }

    /**
     * @param enunciado the enunciado to set
     */
    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    /**
     * @return the resposta
     */
    public Alternativa getResposta() {
        return resposta;
    }

    /**
     * @param resposta the resposta to set
     */
    public void setResposta(Alternativa resposta) {
        this.resposta = resposta;
    }

    /**
     * @return the alternativas
     */
    public Collection<Alternativa> getAlternativas() {
        return alternativas;
    }

    /**
     * @param alternativas the alternativas to set
     */
    public void setAlternativas(Collection<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    /**
     * @return the assuntos
     */
    public Collection<Assunto> getAssuntos() {
        return assuntos;
    }

    /**
     * @param assuntos the assuntos to set
     */
    public void setAssuntos(Collection<Assunto> assuntos) {
        this.assuntos = assuntos;
    }

    /**
     * @return the prova
     */
    public Collection<Prova> getProvas() {
        return provas;
    }

    /**
     * @param prova the prova to set
     */
    public void setProvas(Collection<Prova> provas) {
        this.provas = provas;
    }
    
}
