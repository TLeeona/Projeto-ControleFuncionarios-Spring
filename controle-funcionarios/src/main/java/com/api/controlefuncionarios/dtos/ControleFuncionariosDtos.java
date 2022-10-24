package com.api.controlefuncionarios.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ControleFuncionariosDtos {

    @NotBlank
    //Verifica se o campo preenchido não é nulo, se tem alguma String vázio.
    private String nomeCompletoDoFuncionario;
    @NotBlank
    @Size(max = 7)
    //Limita a quantidade de caracteres.
    private String matriculaDoFuncionario;
    @NotBlank
    private String departamentoDofuncionario;
    @NotBlank
    private String setorDoFuncionario;
    @NotBlank
    private String cargoDoFuncionario;
    private Boolean statusDoFuncionario;
    //Boolean não precisa de @NotBlank, pois ele nunca é vazio por ele mesmo.
    @NotBlank
    private String turnoDeTrabalho;
}