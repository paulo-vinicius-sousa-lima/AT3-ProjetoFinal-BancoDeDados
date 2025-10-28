/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.sistema.control;

import ucb.sistema.model.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ucb.sistema.dao.ConexaoBD;
import ucb.sistema.dao.ConexaoMySQL;

public class GerenciadorPaciente implements RepositorioDePacientes {
    private final ConexaoBD conexaoBD;
    
    private static final String NOME_TABELA_PESSOA = "pessoa";
    private static final String NOME_TABELA_PACIENTE = "paciente";
    private static final String NOME_TABELA_ENDERECO = "endereco";
    private static final String NOME_TABELA_TELEFONE = "telefone";
    private static final String NOME_TABELA_EMAIL = "email";
    
    private static final String ID_PACIENTE = "ID_paciente"; 
    private static final String CPF = "CPF"; 
    private static final String PESSOA_CPF = "pessoa_CPF"; 
    
    private static final String NOME = "nome";
    private static final String DATA_NASCIMENTO = "data_nascimento";

    private static final String CONVENIO = "convenio"; 
    private static final String PRIORIDADE = "prioridade";
    
    public GerenciadorPaciente() {
        this.conexaoBD = new ConexaoMySQL(); 
    }

    @Override
    public void cadastrar(Paciente paciente) throws Exception {
       if (paciente.getCpf() == null || paciente.getCpf().length() != 11) {
            throw new IllegalArgumentException("O CPF deve ter 11 dígitos.");
        }
        
        Connection conexao = null;
        PreparedStatement psPessoa = null;
        PreparedStatement psPaciente = null;
        PreparedStatement psTelefone = null;
        PreparedStatement psEmail = null;
        PreparedStatement psEndereco = null;
        ResultSet rsPessoa = null; 
        
        try {
            if (buscarPorCpf(paciente.getCpf()) != null) {
                throw new Exception("Já existe um paciente cadastrado com este CPF.");
            }
            
            conexao = conexaoBD.obterConexao();
            conexao.setAutoCommit(false); 
            
            String sqlPessoa = "INSERT INTO " + NOME_TABELA_PESSOA + " (" + NOME + ", " + CPF + ", " + DATA_NASCIMENTO + ") VALUES (?, ?, ?)";
            psPessoa = conexao.prepareStatement(sqlPessoa);
            
            psPessoa.setString(1, paciente.getNome());
            psPessoa.setString(2, paciente.getCpf());
            psPessoa.setObject(3, paciente.getDataNascimento());
            
            psPessoa.executeUpdate();

            String sqlPaciente = "INSERT INTO " + NOME_TABELA_PACIENTE + " (" + CONVENIO + ", " + PRIORIDADE + ", " + PESSOA_CPF + ") VALUES (?, ?, ?)";
            psPaciente = conexao.prepareStatement(sqlPaciente, Statement.RETURN_GENERATED_KEYS);
            
            psPaciente.setString(1, paciente.getConvenio());
            psPaciente.setString(2, paciente.getPrioridade());
            psPaciente.setString(3, paciente.getCpf()); 
            psPaciente.executeUpdate();

            ResultSet rsPaciente = psPaciente.getGeneratedKeys();
            if (rsPaciente.next()) {
                int idPacienteGerado = rsPaciente.getInt(1);
                paciente.setIdPaciente(idPacienteGerado);
            }
            rsPaciente.close();
            
            String sqlTelefone = "INSERT INTO " + NOME_TABELA_TELEFONE + " (" + "numero" + ", " + PESSOA_CPF + ") VALUES (?, ?)";
            psTelefone = conexao.prepareStatement(sqlTelefone);
            psTelefone.setString(1, paciente.getTelefone());
            psTelefone.setString(2, paciente.getCpf());
            psTelefone.executeUpdate();

            String sqlEmail = "INSERT INTO " + NOME_TABELA_EMAIL + " (" + "email" + ", " + PESSOA_CPF + ") VALUES (?, ?)";
            psEmail = conexao.prepareStatement(sqlEmail);
            psEmail.setString(1, paciente.getEmail());
            psEmail.setString(2, paciente.getCpf());
            psEmail.executeUpdate();
            
            String sqlEndereco = "INSERT INTO " + NOME_TABELA_ENDERECO + " (" + "rua" + ", " + "cidade" + ", " + "numero" + ", " + PESSOA_CPF + ") VALUES (?, ?, ?, ?)";
            psEndereco = conexao.prepareStatement(sqlEndereco);
            psEndereco.setString(1, paciente.getRua());
            psEndereco.setString(2, paciente.getCidade());
            psEndereco.setInt(3, paciente.getNumero());
            psEndereco.setString(4, paciente.getCpf());
            psEndereco.executeUpdate();


            conexao.commit(); 
            
        } catch (SQLException e) {
            if (conexao != null) conexao.rollback(); 
            throw new Exception("Erro de acesso a dados ao cadastrar paciente: " + e.getMessage(), e);
        } finally {
            if (rsPessoa != null) rsPessoa.close();
            if (psPessoa != null) psPessoa.close();
            if (psPaciente != null) psPaciente.close();
            if (psTelefone != null) psTelefone.close();
            if (psEmail != null) psEmail.close();
            if (psEndereco != null) psEndereco.close();
            conexaoBD.fecharConexao(conexao);
        }
    }

    @Override
    public Paciente buscarPorCpf(String cpf) throws Exception {
        Connection conexao = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexao = conexaoBD.obterConexao();
        
            String sql = "SELECT p." + NOME + ", p." + CPF + ", p." + DATA_NASCIMENTO + ", " +
                     "pac." + ID_PACIENTE + ", pac." + CONVENIO + ", pac." + PRIORIDADE + ", " +
                     "t.numero AS telefone_num, " +
                     "e.email, " +
                     "ed.cidade, ed.numero AS endereco_num, ed.rua " + 
                     "FROM " + NOME_TABELA_PESSOA + " p " +
                     "INNER JOIN " + NOME_TABELA_PACIENTE + " pac ON p." + CPF + " = pac." + PESSOA_CPF + " " +
                     "LEFT JOIN " + NOME_TABELA_TELEFONE + " t ON p." + CPF + " = t." + PESSOA_CPF + " " +
                     "LEFT JOIN " + NOME_TABELA_EMAIL + " e ON p." + CPF + " = e." + PESSOA_CPF + " " +
                     "LEFT JOIN " + NOME_TABELA_ENDERECO + " ed ON p." + CPF + " = ed." + PESSOA_CPF + " " +
                     "WHERE p." + CPF + " = ?";
        
            ps = conexao.prepareStatement(sql);
            ps.setString(1, cpf);
            rs = ps.executeQuery();

            if (rs.next()) {
                Paciente p = new Paciente(
                    rs.getString("nome"), 
                    rs.getString("CPF"), 
                    rs.getObject("data_nascimento", LocalDate.class),
                    rs.getString("telefone_num"), 
                    rs.getString("email"),  
                    rs.getString("cidade"),
                    rs.getInt("endereco_num"),    
                    rs.getString("rua"),
                    rs.getString("convenio"),
                    rs.getString("prioridade")
                );
                p.setIdPaciente(rs.getInt(ID_PACIENTE)); 
                return p;
            }
            return null;
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar paciente por CPF: " + e.getMessage(), e);
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            conexaoBD.fecharConexao(conexao);
        }
    }

    @Override
    public List<Paciente> listarTodos() throws Exception {
        List<Paciente> pacientes = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexao = conexaoBD.obterConexao();

             String sql = "SELECT p." + NOME + ", p." + CPF + ", p." + DATA_NASCIMENTO + ", " +
                         "pac." + ID_PACIENTE + ", pac." + CONVENIO + ", pac." + PRIORIDADE + ", " +
                         "t.numero AS telefone_num, " +  
                         "e.email, " +
                         "ed.cidade, ed.numero AS endereco_num, ed.rua " + 
                         "FROM " + NOME_TABELA_PESSOA + " p " +
                         "INNER JOIN " + NOME_TABELA_PACIENTE + " pac ON p." + CPF + " = pac." + PESSOA_CPF + " " +
                         "LEFT JOIN " + NOME_TABELA_TELEFONE + " t ON p." + CPF + " = t." + PESSOA_CPF + " " +
                         "LEFT JOIN " + NOME_TABELA_EMAIL + " e ON p." + CPF + " = e." + PESSOA_CPF + " " +
                         "LEFT JOIN " + NOME_TABELA_ENDERECO + " ed ON p." + CPF + " = ed." + PESSOA_CPF + " " +
                         "ORDER BY p." + NOME;

            ps = conexao.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Paciente p = new Paciente(
                    rs.getString("nome"), 
                    rs.getString("CPF"), 
                    rs.getObject("data_nascimento", LocalDate.class),
                    rs.getString("telefone_num"), 
                    rs.getString("email"),
                    rs.getString("cidade"),
                    rs.getInt("endereco_num"), 
                    rs.getString("rua"),
                    rs.getString("convenio"),
                    rs.getString("prioridade")
                );
                p.setIdPaciente(rs.getInt(ID_PACIENTE));
                pacientes.add(p);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao listar todos os pacientes: " + e.getMessage(), e);
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            conexaoBD.fecharConexao(conexao);
        }
        return pacientes;
    }

    @Override
    public void atualizar(Paciente paciente) throws Exception {
        if (paciente.getIdPaciente() <= 0) {
            throw new IllegalArgumentException("ID do paciente inválido para atualização.");
        }
        
        Connection conexao = null;
        PreparedStatement psPessoa = null;
        PreparedStatement psPaciente = null;
        PreparedStatement psTelefone = null;
        PreparedStatement psEmail = null;
        PreparedStatement psEndereco = null;
        
        try {
            conexao = conexaoBD.obterConexao();
            conexao.setAutoCommit(false); 
            
            String sqlPessoa = "UPDATE " + NOME_TABELA_PESSOA + " SET " + NOME + "=? WHERE " + CPF + "=?";
            psPessoa = conexao.prepareStatement(sqlPessoa);
            
            psPessoa.setString(1, paciente.getNome());
            psPessoa.setString(2, paciente.getCpf());
            psPessoa.executeUpdate();

            String sqlPaciente = "UPDATE " + NOME_TABELA_PACIENTE + " SET " + CONVENIO + "=?, " + PRIORIDADE + "=? WHERE " + PESSOA_CPF + "=?";
            psPaciente = conexao.prepareStatement(sqlPaciente);
            
            psPaciente.setString(1, paciente.getConvenio());
            psPaciente.setString(2, paciente.getPrioridade());
            psPaciente.setString(3, paciente.getCpf());
            psPaciente.executeUpdate();
            
            String sqlTelefone = "UPDATE " + NOME_TABELA_TELEFONE + " SET " + "numero" + "=? WHERE " + PESSOA_CPF + "=?";
            psTelefone = conexao.prepareStatement(sqlTelefone);
            psTelefone.setString(1, paciente.getTelefone());
            psTelefone.setString(2, paciente.getCpf());
            psTelefone.executeUpdate();

            String sqlEmail = "UPDATE " + NOME_TABELA_EMAIL + " SET " + "email" + "=? WHERE " + PESSOA_CPF + "=?";
            psEmail = conexao.prepareStatement(sqlEmail);
            psEmail.setString(1, paciente.getEmail());
            psEmail.setString(2, paciente.getCpf());
            psEmail.executeUpdate();
            
            String sqlEndereco = "UPDATE " + NOME_TABELA_ENDERECO + " SET " + "cidade" + "=?, " + "rua" + "=?, " + "numero" + "=? WHERE " + PESSOA_CPF + "=?";
            psEndereco = conexao.prepareStatement(sqlEndereco);
            psEndereco.setString(1, paciente.getCidade());
            psEndereco.setString(2, paciente.getRua());
            psEndereco.setInt(3, paciente.getNumero());
            psEndereco.setString(4, paciente.getCpf());
            psEndereco.executeUpdate();


            conexao.commit();
            
        } catch (SQLException e) {
            if (conexao != null) conexao.rollback(); 
            throw new Exception("Erro de acesso a dados ao atualizar paciente: " + e.getMessage(), e);
        } finally {
            if (psPessoa != null) psPessoa.close();
            if (psPaciente != null) psPaciente.close();
            if (psTelefone != null) psTelefone.close();
            if (psEmail != null) psEmail.close();
            if (psEndereco != null) psEndereco.close();
            conexaoBD.fecharConexao(conexao);
        }
    }

    @Override
    public void excluir(int idPaciente) throws Exception {
        Connection conexao = null;
        PreparedStatement psBuscaCpf = null;
        PreparedStatement psExclusao = null;
        ResultSet rs = null;
        String cpfPaciente = null;
        
        PreparedStatement psPaciente = null;
        PreparedStatement psPessoa = null;
        PreparedStatement psTelefone = null;
        PreparedStatement psEmail = null;
        PreparedStatement psEndereco = null;

        try {
            conexao = conexaoBD.obterConexao();
            conexao.setAutoCommit(false); 
            
            String sqlBuscaCpf = "SELECT " + PESSOA_CPF + " FROM " + NOME_TABELA_PACIENTE + " WHERE " + ID_PACIENTE + " = ?";
            psBuscaCpf = conexao.prepareStatement(sqlBuscaCpf);
            psBuscaCpf.setInt(1, idPaciente);
            rs = psBuscaCpf.executeQuery();

            if (rs.next()) {
                cpfPaciente = rs.getString(PESSOA_CPF);
            }
            
            if (rs != null) rs.close();
            if (psBuscaCpf != null) psBuscaCpf.close();

            if (cpfPaciente == null) {
                throw new Exception("Paciente com ID " + idPaciente + " não encontrado.");
            }

            String sqlPaciente = "DELETE FROM " + NOME_TABELA_PACIENTE + " WHERE " + PESSOA_CPF + "=?";
            psPaciente = conexao.prepareStatement(sqlPaciente);
            psPaciente.setString(1, cpfPaciente);
            psPaciente.executeUpdate();

            String sqlTelefone = "DELETE FROM " + NOME_TABELA_TELEFONE + " WHERE " + PESSOA_CPF + "=?";
            psTelefone = conexao.prepareStatement(sqlTelefone);
            psTelefone.setString(1, cpfPaciente);
            psTelefone.executeUpdate();
            
            String sqlEmail = "DELETE FROM " + NOME_TABELA_EMAIL + " WHERE " + PESSOA_CPF + "=?";
            psEmail = conexao.prepareStatement(sqlEmail);
            psEmail.setString(1, cpfPaciente);
            psEmail.executeUpdate();
            
            String sqlEndereco = "DELETE FROM " + NOME_TABELA_ENDERECO + " WHERE " + PESSOA_CPF + "=?";
            psEndereco = conexao.prepareStatement(sqlEndereco);
            psEndereco.setString(1, cpfPaciente);
            psEndereco.executeUpdate();

            String sqlPessoa = "DELETE FROM " + NOME_TABELA_PESSOA + " WHERE " + CPF + "=?";
            psPessoa = conexao.prepareStatement(sqlPessoa);
            psPessoa.setString(1, cpfPaciente);
            psPessoa.executeUpdate();

            conexao.commit();
            
        } catch (SQLException e) {
            if (conexao != null) conexao.rollback(); 
            throw new Exception("Erro de acesso a dados ao excluir paciente: " + e.getMessage(), e);
        } finally {
            if (rs != null) rs.close();
            if (psBuscaCpf != null) psBuscaCpf.close();
            if (psPaciente != null) psPaciente.close();
            if (psPessoa != null) psPessoa.close();
            if (psTelefone != null) psTelefone.close();
            if (psEmail != null) psEmail.close();
            if (psEndereco != null) psEndereco.close();
            conexaoBD.fecharConexao(conexao);
        }
    }
  
}
