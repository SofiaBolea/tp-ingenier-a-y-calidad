package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.implementations;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.Usuario;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.UsuarioDAO;

@Repository
public class UsuarioDAOImpl implements UsuarioDAO {

    @PersistenceContext // Esta anotación indica a JPA que el contenedor debe inyectar automáticamente un objeto EntityManager en este campo.
    private EntityManager entityManager;

    @Override
    @Transactional // Asegura que todas las operaciones de base de datos en el metodo se realicen como una unidad de trabajo (transaccion, es decir, todas tienen éxito o todas se deshacen en caso de que ocurra un error).
    public Usuario findByNombreUsuario(String nombreUsuario) {

        // Primero buscamos en la tabla Bedel asegurándonos de que el estado del bedel a auteneticar sea HABILITADO (true):
        TypedQuery<Usuario> query = entityManager.createQuery("SELECT u FROM Bedel u WHERE u.nombreUsuario = :nombreUsuario AND u.estado = true",Usuario.class);
        query.setParameter("nombreUsuario", nombreUsuario);

        Usuario result = query.getResultStream().findFirst().orElse(null);

        if (result != null) {
            return result; // Retornamos el resultado si se encuentra en Bedel con estado true.
        }

        // Si no se encontró, buscamos en la tabla Administrador, esta vez sin verificar estado ya que no consideramos la eliminación lógica de administradores.
        query = entityManager.createQuery("SELECT u FROM Administrador u WHERE u.nombreUsuario = :nombreUsuario",Usuario.class);
        query.setParameter("nombreUsuario", nombreUsuario);

        return query.getResultStream().findFirst().orElse(null); // Retornamos el resultado o null si no se encuentra.
    }

}