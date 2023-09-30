/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Administrador;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;
import model.Pessoa;

/**
 *
 * @author steli
 */
public class GenericDAO {

    private EntityManagerFactory fabrica;
    private EntityManager gerente;

    public GenericDAO() {
        fabrica = Persistence.createEntityManagerFactory("SystemPU");
        gerente = fabrica.createEntityManager();
    }

    public void add(Object obj) {
        fabrica = Persistence.createEntityManagerFactory("SystemPU");
        gerente = fabrica.createEntityManager();
        try {
            gerente.getTransaction().begin();
            gerente.persist(obj);
            gerente.getTransaction().commit();
            gerente.close();
            JOptionPane.showMessageDialog(null, "Gravado");
        } catch (Exception e) {
            gerente.getTransaction().rollback();
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao gravar");
        }
    }

    /**
     * Metodo para listar todos do banco Pode colocar a classe pois ele filtra
     * por ser generico
     *
     * @param classe
     * @return
     */
    public List<?> listar(Class<?> classe) {

        try {
            fabrica = Persistence.createEntityManagerFactory("SystemPU");
            gerente = fabrica.createEntityManager();

            // Consulta para listar todos os objetos da classe especificada
            String consulta = "SELECT obj FROM " + classe.getSimpleName() + " obj";
            TypedQuery<?> query = gerente.createQuery(consulta, classe);

            List<?> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Ou lançar uma exceção apropriada, dependendo do seu caso.
        } finally {
            if (gerente != null && gerente.isOpen()) {
                gerente.close();
            }
        }
    }
    
    
    public int contar(Class<?> classe) {

    try {
        fabrica = Persistence.createEntityManagerFactory("SystemPU");
        gerente = fabrica.createEntityManager();

        // Consulta para contar todos os objetos da classe especificada
        String consulta = "SELECT COUNT(obj) FROM " + classe.getSimpleName() + " obj";
        TypedQuery<Long> query = gerente.createQuery(consulta, Long.class);

        Long resultado = query.getSingleResult();
        return resultado.intValue(); // Converter para int

    } catch (Exception e) {
        e.printStackTrace();
        return -1; // Ou lançar uma exceção apropriada, dependendo do seu caso.
    } finally {
        if (gerente != null && gerente.isOpen()) {
            gerente.close();
        }
    }
}


    /**
     * Busca pelo Id
     *
     * @param objectoDaClasse
     * @param id
     * @return
     */
    
    public Object buscaId(Class<?> classe, Object id) {
        try {
            return gerente.find(classe, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   

    /**
     * Metodo Para remover Logico
     *
     * @param classe
     * @param id
     * @param novosDados
     */
    public void removerLogico(Class<?> classe, Long id, Object novosDados) {
        try {
            fabrica = Persistence.createEntityManagerFactory("SystemPU");
            gerente = fabrica.createEntityManager();
            gerente.getTransaction().begin();

            Object obj = gerente.find(classe, id);
            if (obj != null) {
                gerente.merge(novosDados);
                gerente.getTransaction().commit();
            } else {
                JOptionPane.showMessageDialog(null, "ID inserido não existe");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (gerente != null && gerente.isOpen()) {
                gerente.close();
            }
        }
    }

    /**
     * Metodo para Atualizar
     *
     * @param classe
     * @param id
     * @param novosDados
     */
    public void Atualizar(Class<?> classe, Long id, Object novosDados) {
        try {
            fabrica = Persistence.createEntityManagerFactory("SystemPU");
            gerente = fabrica.createEntityManager();
            gerente.getTransaction().begin();

           Object obj = buscaId(classe, id);
//            Object obj = buscarPessoaPorId(id);
            if (obj != null) {
                gerente.merge(novosDados);
                gerente.getTransaction().commit();
            } else {
                JOptionPane.showMessageDialog(null, "ID inserido não existe");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (gerente != null && gerente.isOpen()) {
                gerente.close();
            }
        }

    }

    /**
     * Remover De forma Definitiva
     *
     * @param classe
     * @param id
     */
    public void removeFisico(Class<?> classe, Long id) {
        try {
            fabrica = Persistence.createEntityManagerFactory("SystemPU");
            gerente = fabrica.createEntityManager();
            gerente.getTransaction().begin();

            Object obj = gerente.find(classe, id);
            if (obj != null) {
                gerente.remove(obj);
                gerente.getTransaction().commit();
            } else {
                JOptionPane.showMessageDialog(null, "ID insirido nao existe");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            gerente.close();
        }
    }
    
    /*
    caso tenha varias tabelas para ter acesso
    */
    public <T> T buscarPorEmail(Class<T> entityClass, String email) {
    EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("SystemPU");
    EntityManager gerente = fabrica.createEntityManager();
    
    try {
        String queryName = entityClass.getSimpleName() + ".findByEmail";
        TypedQuery<T> query = gerente.createNamedQuery(queryName, entityClass)
            .setParameter("email", email);
        List<T> resultList = query.getResultList();
        
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null;
        }
    } finally {
        gerente.close();
        fabrica.close();
    }
}
    
    /*
    para tabela pessoa
    */
    //Usando para teste apenas Admin
    public Object logarEmail(String email) {
        fabrica = Persistence.createEntityManagerFactory("SystemPU");
        gerente = fabrica.createEntityManager();
        List<Administrador> admin = gerente.createNamedQuery("Administrador.findByEmail", Administrador.class).setParameter("email", email).getResultList();
        if (!admin.isEmpty()) {
            return admin.get(0);
        } else {
            return null;
        }

    }
    
      public List<Pessoa> buscarPessoasPorNome(String nome) {
        TypedQuery<Pessoa> query = gerente.createNamedQuery("Pessoa.findByName", Pessoa.class);
        query.setParameter("nome", "%" + nome + "%"); // O operador % é usado para consultas "LIKE"
        return query.getResultList();
    }
    
    
    /**
     * Metodos mais especificos caso haja algum problema
     */

     /*
    public Object buscaId(Long id) {
        try {
            return gerente.find(Pessoa.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    */

   /* 
    public Pessoa buscarPessoaPorId(Long id) {
    EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("aaaaaPU");
    EntityManager gerente = fabrica.createEntityManager();

    try {
        TypedQuery<Pessoa> query = gerente.createNamedQuery("Pessoa.findById", Pessoa.class);
        query.setParameter("id", id);
        return query.getSingleResult(); // Supondo que haja apenas uma pessoa com o ID fornecido
    } catch (NoResultException e) {
        // Trate o caso em que nenhuma pessoa com o ID fornecido foi encontrada
        return null;
    } finally {
        gerente.close();
        fabrica.close();
    }
    }
    */
}
