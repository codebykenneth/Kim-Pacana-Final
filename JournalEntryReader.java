import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Fetches journal entries from MySQL. */
public class JournalEntryReader {
    private final String url, user, password;

    public JournalEntryReader(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public List<JournalEntry> fetchAll() throws SQLException {
        String sql = "SELECT id, entry_date, account_name, description, debit, credit "
                   + "FROM journal_entries ORDER BY entry_date, id";
        List<JournalEntry> list = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(url, user, password);
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new JournalEntry(
                    rs.getInt("id"),
                    rs.getDate("entry_date").toLocalDate(),
                    rs.getString("account_name"),
                    rs.getString("description"),
                    rs.getBigDecimal("debit"),
                    rs.getBigDecimal("credit")));
            }
        }
        return list;
    }
}
