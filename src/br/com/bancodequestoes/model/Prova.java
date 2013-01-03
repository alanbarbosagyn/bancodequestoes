package br.com.bancodequestoes.model;

import java.util.Calendar;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author Alan
 */
@Entity
public class Prova {

    @Id
    @GeneratedValue
    private int id;
    private String nome;
    private Calendar dataElaboracao;
    private Collection<Questao> questao;
}
