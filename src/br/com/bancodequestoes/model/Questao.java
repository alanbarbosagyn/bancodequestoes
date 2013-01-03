package br.com.bancodequestoes.model;

import java.util.Calendar;
import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author Alan
 */
@Entity
public class Questao {
    
    @Id
    @GeneratedValue
    private int id;
    private Calendar dataCriacao;
    private Calendar dataModificacao;
    private String usuarioCriador;
    private Calendar usuarioModificador;
    private String enunciado;
    private Collection<Assunto> assunto;
    private Alternativa resposta;
    private Collection<Alternativa> alternativas;
}
