/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.sistema.control;

import ucb.sistema.model.Paciente;
import java.util.List;

public interface RepositorioDePacientes {
    void cadastrar(Paciente paciente) throws Exception;
    
    Paciente buscarPorCpf(String cpf) throws Exception;
    
    List<Paciente> listarTodos() throws Exception;
    
    void atualizar(Paciente paciente) throws Exception;
    
    void excluir(int idPaciente) throws Exception;
}
