/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.sistema.model;

import java.time.LocalDate;

public class Paciente extends Pessoa {
    private int idPaciente;
    private String convenio;
    private String prioridade; 

    public Paciente() {
        super(); 
    }
        
    public Paciente(String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String cidade, int numero, String rua) {
        super(nome, cpf, dataNascimento, telefone, email, cidade, numero, rua);
    }
    
    public Paciente(String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String cidade, int numero, String rua, String convenio, String prioridade) {
        this(nome, cpf, dataNascimento, telefone, email, cidade, numero, rua);
        this.convenio = convenio;
        this.prioridade = prioridade;
    }
    
    public Paciente(int idPaciente, String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String cidade, int numero, String rua, String convenio, String prioridade) {
        this(nome, cpf, dataNascimento, telefone, email, cidade, numero, rua, convenio, prioridade);
        this.idPaciente = idPaciente;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }
 
}
