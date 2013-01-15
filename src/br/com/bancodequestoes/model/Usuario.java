package br.com.bancodequestoes.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

/**
 *
 * @author Alan
 */
@NamedQuery(name="Usuario.buscaPorNome", query="SELECT p FROM Usuario p WHERE p.nome like :nome")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@Entity
public class Usuario implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique=true)
    private String nome;
    
    @Column(length=8)
    private String senha;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar dataCadastro;

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
        Long oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
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
        String oldNome = this.nome;
        this.nome = nome;
        changeSupport.firePropertyChange("nome", oldNome, nome);
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        String oldSenha = this.senha;
        this.senha = senha;
        changeSupport.firePropertyChange("senha", oldSenha, senha);
    }

    /**
     * @return the dataCadastro
     */
    public Calendar getDataCadastro() {
        return dataCadastro;
    }

    /**
     * @param dataCadastro the dataCadastro to set
     */
    public void setDataCadastro(Calendar dataCadastro) {
        Calendar oldDataCadastro = this.dataCadastro;
        this.dataCadastro = dataCadastro;
        changeSupport.firePropertyChange("dataCadastro", oldDataCadastro, dataCadastro);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
