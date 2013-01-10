/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bancodequestoes.view;

import br.com.bancodequestoes.dao.UsuarioJpaController;
import br.com.bancodequestoes.model.Professor;
import br.com.bancodequestoes.model.Usuario;
import java.util.Calendar;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Alan
 */
public class Principal {

    public static void main(String args[]) {

        new Login().setVisible(true);

    }
}
