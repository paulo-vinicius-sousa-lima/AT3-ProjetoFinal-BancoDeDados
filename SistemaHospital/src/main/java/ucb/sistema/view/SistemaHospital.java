/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package ucb.sistema.view;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import ucb.sistema.model.Paciente; 
import ucb.sistema.control.GerenciadorPaciente; 

public class SistemaHospital {

    private static GerenciadorPaciente gerenciador = new GerenciadorPaciente(); 
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static Scanner sc = new Scanner(System.in); 


    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        
        menuPrincipal();
        sc.close();
    }
    
    public static void menuPrincipal() {
        while(true) {
            try { 
                System.out.println("\n=============================");
                System.out.println("=== SISTEMA HOSPITALAR ===");
                System.out.println("=============================");
                System.out.println("1 - Acessar como ADMINISTRADOR");
                System.out.println("2 - Acessar como MÉDICO");
                System.out.println("3 - Sair do sistema");
                System.out.print("Escolha o perfil de acesso: ");
                
                int opcao = sc.nextInt();
                sc.nextLine(); 

                System.out.println();
                
                switch (opcao) {
                    case 1 -> menuAdministrador(); 
                    case 2 -> System.out.println("Acesso como MÉDICO selecionado. Funcionalidade ainda não implementada.");
                    case 3 -> { 
                        System.out.println("Finalizando o sistema...");
                        return; 
                    }
                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida. Por favor, digite apenas números para a opção.");
                sc.nextLine(); 
            } catch (Exception e) {
                System.err.println("ERRO INESPERADO: " + e.getMessage());
            }
        }
    }
    
    public static void menuAdministrador() {
        boolean rodando = true;
        while(rodando) {
            try { 
                System.out.println("\n=== PERFIL: ADMINISTRADOR ===");
                System.out.println("1 - Cadastrar paciente");
                System.out.println("2 - Consultar paciente (por CPF)");
                System.out.println("3 - Listar pacientes");
                System.out.println("4 - Atualizar dados de Paciente (por CPF)"); 
                System.out.println("5 - Excluir paciente (por CPF)"); 
                System.out.println("6 - Sair do sistema");
                System.out.print("Escolha a opção: ");
                
                int opcao = sc.nextInt();
                sc.nextLine();

                System.out.println();
                
                switch (opcao) {
                    case 1 -> {
                        System.out.println("--- CADASTRO DE PACIENTE ---");
                        System.out.print("Nome: ");
                        String nome = sc.nextLine();
                        System.out.print("CPF (11 dígitos, apenas números): ");
                        String cpf = sc.nextLine();
                        
                        System.out.print("Data de Nascimento (yyyy-MM-dd): ");
                        String dataNascStr = sc.nextLine();
                        LocalDate dataNascimento;
                        try {
                            dataNascimento = LocalDate.parse(dataNascStr, DATE_FORMAT);
                        } catch (DateTimeParseException e) {
                            throw new IllegalArgumentException("Formato de data inválido. Use yyyy-MM-dd.");
                        }
                        
                        System.out.print("Telefone: ");
                        String telefone = sc.nextLine();
                        System.out.print("Email: ");
                        String email = sc.nextLine();
                        
                        System.out.print("Cidade: ");
                        String cidade = sc.nextLine();
                        System.out.print("Rua: ");
                        String rua = sc.nextLine();
                        
                        int numero;
                        try {
                            System.out.print("Número da Casa: ");
                            numero = sc.nextInt();
                            sc.nextLine(); 
                        } catch (InputMismatchException e) {
                            sc.nextLine();
                            throw new IllegalArgumentException("O número da casa deve ser um valor numérico.");
                        }
                        
                        System.out.print("Convênio (Ex: SUS, Particular, Unimed): ");
                        String convenio = sc.nextLine();
                        System.out.print("Prioridade (Ex: Normal, Urgente, Emergência): ");
                        String prioridade = sc.nextLine();
                        
                        Paciente novoPaciente = new Paciente(
                            nome, cpf, dataNascimento, telefone, email,
                            cidade, numero, rua, convenio, prioridade
                        );
                        
                        gerenciador.cadastrar(novoPaciente);
                        System.out.println("\n✅ Paciente cadastrado com sucesso! ID Gerado: " + novoPaciente.getIdPaciente());
                    }
                    
                    case 2 -> {
                        System.out.println("--- CONSULTAR PACIENTE ---");
                        System.out.print("Buscar paciente por CPF: ");
                        String cpf = sc.nextLine();
                        
                        Paciente paciente = gerenciador.buscarPorCpf(cpf);
                        
                        if (paciente != null) {
                            System.out.println("--- DETALHES DO PACIENTE ---");
                            System.out.println("ID: " + paciente.getIdPaciente());
                            System.out.println("Nome: " + paciente.getNome());
                            System.out.println("CPF: " + paciente.getCpf());
                            System.out.println("Nascimento: " + paciente.getDataNascimento().format(DATE_FORMAT));
                            System.out.println("Telefone: " + paciente.getTelefone());
                            System.out.println("Email: " + paciente.getEmail());
                            System.out.println("Endereço: " + paciente.getRua() + ", " + paciente.getNumero() + " (" + paciente.getCidade() + ")");
                            System.out.println("Convênio: " + paciente.getConvenio());
                            System.out.println("Prioridade: " + paciente.getPrioridade());
                            System.out.println("----------------------------");
                        } else {
                            System.out.println("Paciente com CPF " + cpf + " não encontrado.");
                        }
                    }
                    
                    case 3 -> {
                        System.out.println("--- LISTA DE PACIENTES CADASTRADOS ---");
                        List<Paciente> pacientes = gerenciador.listarTodos();
                        
                        if (pacientes.isEmpty()) {
                            System.out.println("Nenhum paciente cadastrado no sistema.");
                        } else {
                            for (Paciente p : pacientes) {
                                System.out.printf("ID: %d | Nome: %s | CPF: %s | Convênio: %s%n", 
                                    p.getIdPaciente(), 
                                    p.getNome(), 
                                    p.getCpf(),
                                    p.getConvenio()
                                );
                            }
                            System.out.println("----------------------------------------------");
                            System.out.println("Total de pacientes: " + pacientes.size());
                        }
                    }
                    
                    case 4 -> {
                        System.out.println("--- ATUALIZAR DADOS DO PACIENTE ---");
                        System.out.print("Digite o CPF do paciente para atualização: ");
                        String cpfBusca = sc.nextLine();
                        
                        Paciente paciente = gerenciador.buscarPorCpf(cpfBusca);

                        if (paciente == null) {
                            System.out.println("Paciente com CPF " + cpfBusca + " não encontrado.");
                            break; 
                        }

                        System.out.println("Paciente encontrado. Deixe em branco para manter o valor atual.");
                        
                        System.out.println("Nome atual: " + paciente.getNome());
                        System.out.print("Novo Nome: ");
                        String novoNome = sc.nextLine();
                        if (!novoNome.isEmpty()) {
                            paciente.setNome(novoNome);
                        }

                        System.out.println("Telefone atual: " + paciente.getTelefone());
                        System.out.print("Novo Telefone: ");
                        String novoTelefone = sc.nextLine();
                        if (!novoTelefone.isEmpty()) {
                            paciente.setTelefone(novoTelefone);
                        }
                        
                        System.out.println("Convênio atual: " + paciente.getConvenio());
                        System.out.print("Novo Convênio: ");
                        String novoConvenio = sc.nextLine();
                        if (!novoConvenio.isEmpty()) {
                            paciente.setConvenio(novoConvenio);
                        }

                        System.out.println("Prioridade atual: " + paciente.getPrioridade());
                        System.out.print("Nova Prioridade: ");
                        String novaPrioridade = sc.nextLine();
                        if (!novaPrioridade.isEmpty()) {
                            paciente.setPrioridade(novaPrioridade);
                        }
                        
                        gerenciador.atualizar(paciente);
                        System.out.println("Paciente CPF " + paciente.getCpf() + " (ID: " + paciente.getIdPaciente() + ") atualizado com sucesso!");
                    }
                    
                    case 5 -> {
                        System.out.println("--- EXCLUIR PACIENTE ---");
                        System.out.print("Digite o CPF do paciente para EXCLUSÃO: ");
                        String cpfBusca = sc.nextLine();
                        
                        Paciente paciente = gerenciador.buscarPorCpf(cpfBusca);

                        if (paciente == null) {
                            System.out.println("Paciente com CPF " + cpfBusca + " não encontrado.");
                            break; 
                        }
                        
                        System.out.println("\nTem certeza que deseja EXCLUIR o paciente: " + paciente.getNome() + " (CPF: " + paciente.getCpf() + ")? (S/N)");
                        String confirmacao = sc.nextLine().trim().toUpperCase();
                        
                        if (confirmacao.equals("S")) {
                            gerenciador.excluir(paciente.getIdPaciente()); 
                            System.out.println("Paciente CPF " + cpfBusca + " (ID: " + paciente.getIdPaciente() + ") excluído com sucesso.");
                        } else {
                            System.out.println("Exclusão cancelada.");
                        }
                    }
                    case 6 -> { 
                        System.out.println("Finalizando o sistema...");
                        return;
                    }

                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida. Por favor, digite apenas números para a opção.");
                sc.nextLine(); 
            } catch (Exception e) {
                System.err.println("ERRO NA OPERAÇÃO: " + e.getMessage());
            }
        }
    }
}
