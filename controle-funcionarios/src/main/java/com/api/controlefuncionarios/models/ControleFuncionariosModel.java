package com.api.controlefuncionarios.models;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
//Cria Getter, setter e outra funções. Getter e setter são nomes para método. Getter é retorna os dados e setter é para
//alterarção dos dados. Eles encapsulam as variáveis para proteger e só permitindo o acesso em deteminados lugares.
@Entity
//Indica que essa classe é um modelo de Banco de Dados.
@Table(name = "TABELA_DE_CONTROLE_DE_FUNCIONARIOS")
//Indica que é uma tabela do modelo de Banco de Dados.
public class ControleFuncionariosModel implements Serializable {
    //Implements Serializable significa que ele vai conseguir interpretar algumas informações
//    private static final long serialVersionUID = 1L;

    @Id
    //Id indica que o objeto vai ter um número de séria único para ele, feito CPF.
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; //Gera automaticamento o ID
    @Column(nullable = false, unique = true, length = 100)
    private String nomeCompletoDoFuncionario;
    //@column significa que é para adicionar uma coluna com esse atributo na tabela. Nullable é para liberar a não
    //obrigatoriedade de preenchimento, nesse caso, false, ele "diz" aue não pode liberar. É obrigado a preencher.
    //Unique é que a infomação tem que ser única. Não pode ter dois ou mais objeto com a mesma infomação.
    //Length é a quantantidade de caracteris que pode ser preenchido.
    @Column(nullable = false, unique = true, length = 7)
    private String matriculaDoFuncionario;
    @Column(nullable = false, length = 70)
    private String departamentoDofuncionario;
    @Column(nullable = false, length = 70)
    private String setorDoFuncionario;
    @Column(nullable = false, length = 70)
    private String cargoDoFuncionario;
    @Column(nullable = false, length = 70)
    private Boolean statusDoFuncionario;
    @Column(nullable = true, length = 70)
    private String turnoDeTrabalho;
    @Column(nullable = true, length = 70)
    private LocalDateTime dataDeRegistro;
}