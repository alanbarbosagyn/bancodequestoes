package br.com.bancodequestoes.model;

import java.util.Calendar;
import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author Alan
 */
@Entity
public class Usuario {
    @Id
    @GeneratedValue
    private int id;
    private String nome;
    private String senha;
    private Calendar dataCriacao;
    private Collection<Prova> prova;
}
