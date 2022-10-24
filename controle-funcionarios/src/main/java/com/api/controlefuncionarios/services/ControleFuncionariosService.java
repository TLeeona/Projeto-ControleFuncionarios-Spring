package com.api.controlefuncionarios.services;

import com.api.controlefuncionarios.models.ControleFuncionariosModel;
import com.api.controlefuncionarios.repositories.ControleFuncionariosRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
//Service é o Beans especifico para serviço. É o Bean que faz intermédio entre o Beans Control e o Beans Repository
@AllArgsConstructor
//AllArgsConstructor cria, automaticamente, o construtor de injeção de dependencia. A injeção de dependencia dá ao
//Beans de Service autorização para mandar e receber informações do Banco de Dados.
public class ControleFuncionariosService {

    final ControleFuncionariosRepository controleFuncionariosRepository;
    //Final inpede que os dados mandados ao Repository pelo Service, seja alterado.

@Transactional
//Como estamos salvando (ou deletando) em cascata (Salvando em um canto, que salva no outro, que salva no outro), o
//@Transactional serve para garantir que o salvamento ou o "deletamente" seja feito certinho, não gerando erros, dados
//quebrados no código.
    public ControleFuncionariosModel save(ControleFuncionariosModel novoFuncionario) {
    //Public pq é uma informação pública, que qualquer um pode ver.
    //O método foi alterado de Object para ControleFuncionariosModel, pois queremos que retorno um model.
        return controleFuncionariosRepository.save(novoFuncionario);
        //No retorno vamos usar dos métodos do JPA. Lembrando que JPA é uma extenção do BD.
        //controleFuncionariosRepository no retorno não significa que vai retornar um Repository, mas sim que vai retor-
        //nar PARA o Resitory. Lembra que o métono é um model, então ele vai retornar um Model para o Repository.
    }

    public boolean existByMatriculaDoFuncionario(String matriculaDoFuncionario) {
        //Esse método é "personalisado", pois varia com a necessidade do atributo (com a necessidade que não pode repetir),
        //então nai vai ter pronto dentro do JPA. Vai ter que ser criado "manualmente" no Repository, antes de ser usado
        //pelo Servece.
        //Lembrando: O método é boolean pq queremos um retorno "sim ou não", falso ou verdadeiro.
        //Lembrando: O parâmetro é a String matriculaDoFuncionario.
        return controleFuncionariosRepository.existsByMatriculaDoFuncionario(matriculaDoFuncionario);
        //Lembrando: return controleFuncionariosRepository não significa que vai retornar Repository.
        //controleFuncionariosRepository está roxo, o que significa que é o retorno é para o Repository.
    }

 //   public List<ControleFuncionariosModel> findAll() {
 //       return controleFuncionariosRepository.findAll();
 //       //Esse método vai retornar uma lista com todos os dados presentes, referente a isso, e vai retornar para o
 //       //Repository.
 //       //O método List não é personalisado, já tem dentro do JPA.
 //       //Lembrando: O Service é o meio de campo entre o Controller e o Repository. Então tudo o que o Controller
 //       //precisar do Repositopry, tem que ser solicitado através do Service.
 //   }
    public Page<ControleFuncionariosModel> findAll(Pageable pageable) {
        return controleFuncionariosRepository.findAll(pageable);
      }


    public Optional<ControleFuncionariosModel> findByID(UUID id) {
        return controleFuncionariosRepository.findById(id);
        //Igual ao método acima, o JPA já tem um modelo pronto dentro.
        //O Parâmetro é o ID.

    }
@Transactional
//@Transactional colocado para evitar erro no cógido, por se tratar de um apagamento de dados em cascata ou relacionado.
//Para mais detalhes olhe o save.
    public void delete(ControleFuncionariosModel controleFuncionariosModel) {
        controleFuncionariosRepository.delete(controleFuncionariosModel);
        //Esse método vai precisar retornar nada, só executar, então é Void e vai usa o método do JPA.
    }
}