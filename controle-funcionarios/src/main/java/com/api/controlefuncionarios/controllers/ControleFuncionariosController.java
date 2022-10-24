package com.api.controlefuncionarios.controllers;

import com.api.controlefuncionarios.dtos.ControleFuncionariosDtos;
import com.api.controlefuncionarios.models.ControleFuncionariosModel;
import com.api.controlefuncionarios.services.ControleFuncionariosService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
//É o Beans que recebe as solicitações e avisa ao Service (e o Service avisa ao Repository)
@RequestMapping("/controle-funcionarios")
//RequestMapping faz o mapeamento da URL do projeto
@AllArgsConstructor
public class ControleFuncionariosController {

    final ControleFuncionariosService controleFuncionariosService;

    @PostMapping
    //É necessário colocar @PostMapping para ser um método post. Post é criar, cadastrar um objeto.
    public ResponseEntity<Object> saveFuncionario(@RequestBody @Valid ControleFuncionariosDtos controleFuncionariosDtos){
        //O método utilizado é o de Resposta, já que o retorno é uma resposta.
        //Colocar <Object> permite que tenha diferetes tipos de retorno, dependo do que queremos.
        //@Valid deve ser colocada para poder ativar as validações que foram colocadas na classe Dto.
        if (controleFuncionariosService.existByMatriculaDoFuncionario(controleFuncionariosDtos.getMatriculaDoFuncionario())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: Já existe funcionário cadastrado com esta Matrícula.");
        } //If está sendo usado como um forma de verificar se uma determinada informção já existe. Ex.: Não existe duas
        //pessoas com o mesmo CPF, então colocando o If para verificar se tem um cadastro com aquele CFP.
        //Então o If se comunica com o método existByMatriculaDoFuncionario (Caso esteja vermelho é pq não foi criando
        //ainda, feito o sava abaixo, basta apenas criar), que está dentro do Service controleFuncionariosService, e
        //pede para verificar a existencia do getMatriculaDoFuncionario, que esta dentro do controleFuncionariosDtos.
        //Caso seja verdade, retorna a resposta do status no método, que é "CONFLICT", e exibe uma mensagem.

        var saveNewEmployee = new ControleFuncionariosModel();
        //var foi inseriso no lugar de ControleFuncionariosModel (Como a gente faz para criar um novo objeto do teste),
        //para simplificar. "Regra de etiquta para deixar mais bonito.
        BeanUtils.copyProperties(controleFuncionariosDtos, saveNewEmployee);
        //O BeanUtils está convertendo as infomações do controleFuncionariosDtos para controleFuncionarioModel, para
        //poder ser salvo no banco de dados. Já que é o controleFuncionarioModel que tem os atributos
        saveNewEmployee.setDataDeRegistro(LocalDateTime.now(ZoneId.of("UTC")));
        //Essa linha serve para adicionar um atributo que são gerados automaticamento no model e que não é inserido no
        //Dto. (O ID não precisa disso, pois é gerado atuomaticamente pelo Banco e a Data de Registo é gerado
        //automaticamente pelo código).
        //LocalDataTime.now é salva a data e hora do momento exato do registro. ZoneId.of("UTC") é o formato da hora.
        return ResponseEntity.status(HttpStatus.CREATED).body(controleFuncionariosService.save(saveNewEmployee));
        //Retorna a resposta do status no método, que é "criado" (status(HttpStatus.CREATED) e salvo (body(controle
        //FuncionariosService.save(saveNewEmployee).
    }

   //@GetMapping
   //É necessário colocar @GetMapping para ser um método Get é para retornar dados do Banco de Dados.
   //public ResponseEntity<List<ControleFuncionariosModel>> getAllControleFuncionarios(){
   //    //Diferente do Post acima, a resposta do Get é uma lista List de objetos salvos ControleFuncionariosModel.
   //    //getAllControleFuncionarios é um compando pra deternar tudo.
   //    return ResponseEntity.status(HttpStatus.OK).body(controleFuncionariosService.findAll());
   //    //Retorna a resposta do status no método, que é "Ok" e vai buscar todos, através do Service.
   //}

    @GetMapping
    public ResponseEntity<Page<ControleFuncionariosModel>> getAllControleFuncionarios(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(controleFuncionariosService.findAll(pageable));
        //Fizemos uma melhoria na forma de buscar os funcionários. No comentário acima, está a forma de buscar em lista
        //que é a forma mais básica (comentada para não dar problema para o código). Melhoramos ela para o formato de
        //busca ser por página Page. Essa forma pode não ser significativa quando são poucos funcionários, mas tem
        //uma grande importancia para um BD com muito funcionário. Por tanto é melhor deixar assim pois aproveita para
        //os dois tipos de BD.
    }

    @GetMapping("/{id}")
    //Como essa busca é por alguém, algo em expecifico. Nesse caso, vamos buscar alguém através do id gerado pelo BD,
    //mas poderia ser por outro atributo.
    public ResponseEntity<Object> getOneControleFuncionarios(@PathVariable(value = "id")UUID id){
        //<Object> pois vamos precisar ter mais de um retorno. Uma mensagem caso não tenha encontrado o funcionário e os
        //dados do funcionário caso ele exista no BD.
        //getOneControleFuncionario é para mostrar o funcionário em específico.
        //@PathVariable é para poder fazer a busca pela variável.
        //value = "id" informa que a variável escolhida foi id. Tem que ser colocada da mesma forma que está entre {} no
        //@GetMapping.
        //UUID id é o formarto da variável. Lembra que foi colocado algo parecido no LocalDateTime, que era para colocar
        //a variável no formato de tempo?
        Optional<ControleFuncionariosModel> controleFuncionariosModelOptional = controleFuncionariosService.findByID(id);
        //Para realizar essa busca, vai precisar ir no BD, logo, vai precisar criar um método no Service que vai
        //retornar uma opção de Model.
        if (!controleFuncionariosModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
            //Ai já é para fazer uma verificação se o id inserido está no BD.
            //Lembrando: A esclamação "!" do controleFuncionariosModelOptional indica que ele vai reagir ao falso e não
            // ao verdadeiro, como de costume.
        }
        return ResponseEntity.status(HttpStatus.OK).body(controleFuncionariosModelOptional.get());
        //Caso existe esse id no BD ele vai para o status Ok e trás a infromação solicitada.
        //.get() serve para obter Model, já que o retorno vai obter um Optional.
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteControleFuncionarios(@PathVariable(value = "id")UUID id){
        Optional<ControleFuncionariosModel> controleFuncionariosModelOptional = controleFuncionariosService.findByID(id);
        if (!controleFuncionariosModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
        }
        controleFuncionariosService.delete(controleFuncionariosModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Funcionário deletado com sucesso!");
            //O Delete é igual ao GetOne. Ele vai precisar procurar para saber se o funcionário está presentre no BD. A
            //única diferença é que, caso encontre o registro, ele vai acionar o Service para acionar o Repository e
            //deleta do BD.
            //Após deletado, vai informar ao usuário final.
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateControleFuncionarios(@PathVariable(value = "id")UUID id,
                                                             @RequestBody @Valid ControleFuncionariosDtos controleFuncionariosDtos){
        Optional<ControleFuncionariosModel> controleFuncionariosModelOptional = controleFuncionariosService.findByID(id);
        if (!controleFuncionariosModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
        }
        //PutMapping é como se fosse a mistura do GetOn e o Post. Ele vai precisar procurar o funcionário e salvar o
        //funcionário. Não é atoa que os dois estão foram colocados no parâmentro, o Id da procura e Dtos coma validação.
        var updateEmployee = controleFuncionariosModelOptional.get();
        //Essa forma de "salvar" é diferente. Como o id e o Data de registro vão permanecer o mesmo, então deve ser
        //aproveitar o que já tem e foi buscado pelo Opitional. Para isso, em vez de colocar New controleFuncionariosModel,
        //vamos colocar controleFuncionariosModelOptional e converter para Model com o get().
        updateEmployee.setNomeCompletoDoFuncionario(controleFuncionariosDtos.getNomeCompletoDoFuncionario());
        updateEmployee.setMatriculaDoFuncionario(controleFuncionariosDtos.getMatriculaDoFuncionario());
        updateEmployee.setDepartamentoDofuncionario(controleFuncionariosDtos.getDepartamentoDofuncionario());
        updateEmployee.setSetorDoFuncionario(controleFuncionariosDtos.getSetorDoFuncionario());
        updateEmployee.setCargoDoFuncionario(controleFuncionariosDtos.getCargoDoFuncionario());
        updateEmployee.setStatusDoFuncionario(controleFuncionariosDtos.getStatusDoFuncionario());
        updateEmployee.setTurnoDeTrabalho(controleFuncionariosDtos.getTurnoDeTrabalho());
        //Esse set precisou ser feito, pois, não se sabe qual o atributo foi alterado. Então tem que puxar do dto

            return ResponseEntity.status(HttpStatus.OK).body(controleFuncionariosService.save(updateEmployee));


        //Outra forma para settar é fazer igual ao salvamento. Assim, não precisa ficar setando 1 por 1.
        //var updateEmployee = controleFuncionariosModelOptional.get();
        //BeanUtils.copyProperties(controleFuncionariosDtos, updateEmployee);
                //O BeanUtils está convertendo as infomações do controleFuncionariosDtos para controleFuncionarioModel,
                //para poder ser salvo no banco de dados. Já que é o controleFuncionarioModel que tem os atributos.
        //updateEmployee.setID(controleFuncionariosModelOptional.get().getId());
        //updateEmployee.setDataDeRegistro(controleFuncionariosModelOptional.get().getDataDeRegistro());
                //As únicas coisas que você vai setar são os ids e as datas de registros, que foram adquirinas pelo
                //controleFuncionariosModelOptional, converter para model com o get() e converter para a variável com o
                //getId() e o getDataDeRegistro()
        //return ResponseEntity.status(HttpStatus.OK).body(controleFuncionariosService.save(updateEmployee));

    }
}