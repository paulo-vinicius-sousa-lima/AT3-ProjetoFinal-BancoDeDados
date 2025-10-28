/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.sistema.model;

import java.time.LocalDate;

// Estende Pessoa, herdando todos os atributos definidos na superclasse e os métodos de acesso
public class Paciente extends Pessoa {
    private int idPaciente;
    private String convenio;
    private String prioridade; 

    // Construtor padrão. Chama o construtor padrão da superclasse Pessoa 
    public Paciente() {
        super(); 
    }
      
    // Construtor que inicializa somente os atributos herdados de Pessoa
    public Paciente(String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String cidade, int numero, String rua) {
        super(nome, cpf, dataNascimento, telefone, email, cidade, numero, rua);
    }
    
    // Construtor que inicializa Pessoa e os atributos próprios de Paciente
    public Paciente(String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String cidade, int numero, String rua, String convenio, String prioridade) {
        this(nome, cpf, dataNascimento, telefone, email, cidade, numero, rua);
        this.convenio = convenio;
        this.prioridade = prioridade;
    }
    
    // Construtor completo, incluindo o ID. Usado tipicamente ao carregar dados do banco
    public Paciente(int idPaciente, String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String cidade, int numero, String rua, String convenio, String prioridade) {
        this(nome, cpf, dataNascimento, telefone, email, cidade, numero, rua, convenio, prioridade);
        this.idPaciente = idPaciente;
    }

    // Gets e Sets
    public int getIdPaciente() {
        return idPaciente;
    }

    // O Control vai usar este setter após o cadastro para registrar o ID gerado pelo BD
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
