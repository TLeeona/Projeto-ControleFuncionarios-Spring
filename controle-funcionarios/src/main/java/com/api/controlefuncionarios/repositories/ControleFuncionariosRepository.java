package com.api.controlefuncionarios.repositories;

import com.api.controlefuncionarios.models.ControleFuncionariosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
//Repository é um Beans indicado para classe de banco de dados. Por mais que o JPA já cria o Beans do
//repositório/repository, é bom colocar @Repository para facilitar a identificação.

public interface ControleFuncionariosRepository extends JpaRepository <ControleFuncionariosModel, UUID> {
    //extends é para externer o JPA para esta interface.
    //JPA é uma automatização do Banco de Dados e atua internamente. Deve ser colocado entre <> o model (é onde está os
    //atributos da tabela do banco de dados) e o identificador único da tabela do banco de dados.

    boolean existsByMatriculaDoFuncionario(String matriculaDoFuncionario);
    //É necessario apenas declarar, não precisa implementar
    //No existsByMatriculaDoFuncionario, você pode adicional mais um junto com a Matricula para ser verficiado. Ex.:
    //Existem duas pessoas com o mesmo número de telefone? Sim, mas não no mesmo estado. Então deve ser verificado dessa
    //forma: existsByNumeroDeTeleneAndEstado
}