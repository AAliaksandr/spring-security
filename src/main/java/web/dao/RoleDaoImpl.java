package web.dao;

import org.springframework.stereotype.Repository;
import web.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class RoleDaoImpl implements RoleDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public Role findRoleByRoleName(String role) {
        TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.role = :role", Role.class)
                .setParameter("role", role);
        return query.getSingleResult();
    }
}
