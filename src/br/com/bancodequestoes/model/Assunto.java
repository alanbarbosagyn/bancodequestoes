package br.com.bancodequestoes.model;

import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author Alan
 */
@Entity
public class Assunto {

    @Id
    @GeneratedValue
    private int id;
    private String nome;
    private Collection<Questao> questao;
}
