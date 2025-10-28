/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.sistema.model;

import java.time.LocalDate;

public class Pessoa {
    // Atributos privados
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;
    private String cidade;
    private int numero;
    private String rua;
    
    // Construtor padrão (para instâncias vazias)
    public Pessoa() {
    }
    
    // Construtor com inicialização do atributo nome
    public Pessoa(String nome) {
        this(); 
        this.nome = nome;
    }
    
    // Construtor para inicialização de nome e CPF
    public Pessoa(String nome, String cpf) {
        this(nome);
        this.cpf = cpf;
    }
    
    // Construtor que inclui a data de nascimento
    public Pessoa(String nome, String cpf, LocalDate dataNascimento) {
        this(nome, cpf);
        this.dataNascimento = dataNascimento;
    }
    
    // Construtor com dados de contato
    public Pessoa(String nome, String cpf, LocalDate dataNascimento, String telefone, String email) {
        this(nome, cpf, dataNascimento);
        this.telefone = telefone;
        this.email = email;
    }
    
    // Construtor que inicializa todos os atributos de Pessoa, incluindo endereço
    public Pessoa(String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String cidade, int numero, String rua) {
        this(nome, cpf, dataNascimento, telefone, email);
        this.cidade = cidade;
        this.numero = numero;
        this.rua = rua;
    }

    // Gets e Sets (Permitem que classes externas, do view e control, leiam ou modifiquem os atributos privados do objeto de forma controlada
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }
 
}
