package br.ufpr.nc.solicitacaoseis.component;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClobUtil {

    @Autowired
    private DataSource dataSource;

    public Clob criarClob(String texto) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection(); // Abre conexão
            Clob clob = conn.createClob();
            clob.setString(1, texto);
            return clob;
        } finally {
            if (conn != null) {
                conn.close(); // Garante que a conexão seja fechada
            }
        }
    }
}