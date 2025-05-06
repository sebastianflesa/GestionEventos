package com.example.demo.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    public void establecerYObtenerNombre() {
        Role role = new Role();
        role.setName("ROLE_USER");
        assertEquals("ROLE_USER", role.getName(), "El nombre del rol debe coincidir con el valor establecido");
    }

    @Test
    public void compararRoles_nombresDiferentes_noSonIguales() {
        Role role1 = new Role();
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setName("ROLE_ADMIN");

        assertNotEquals(role1, role2, "Dos roles con nombres distintos no deben ser iguales");
    }

    @Test
    public void compararRol_conNullYOtroTipo() {
        Role role = new Role();
        role.setName("ROLE_USER");

        assertNotEquals(role, null, "El rol no debe ser igual a null");
        assertNotEquals(role, "ROLE_USER", "El rol no debe ser igual a un objeto de otro tipo");
    }
}
