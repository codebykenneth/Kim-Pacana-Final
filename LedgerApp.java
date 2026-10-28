import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class LedgerApp extends JFrame {
    private final Ledger ledger;
    private final DefaultTableModel model = new DefaultTableModel(
        new Object[]{"Account", "Date", "Description", "Debit", "Credit", "Balance"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    public LedgerApp(Ledger ledger) {
        super("General Ledger");
        this.ledger = ledger;

        JTextField search = new JTextField();
        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { refresh(search.getText()); }
            public void removeUpdate(DocumentEvent e)  { refresh(search.getText()); }
            public void changedUpdate(DocumentEvent e) { refresh(search.getText()); }
        });

        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.add(new JLabel("Search account:"), BorderLayout.WEST);
        top.add(search, BorderLayout.CENTER);

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        refresh("");
    }
/*This Void to proceed transaction */
    private void refresh(String query) {
        model.setRowCount(0);
        List<Ledger.Account> shown = query.isBlank()
            ? List.copyOf(ledger.allAccounts()) : ledger.search(query);
        for (Ledger.Account a : shown) {
            for (Ledger.Posting p : a.postings) {
                JournalEntry e = p.entry;
                model.addRow(new Object[]{a.name, e.date, e.description,
                    money(e.debit), money(e.credit), money(p.runningBalance)});
            }
            model.addRow(new Object[]{"", "", "Ending balance — " + a.name,
                money(a.totalDebit), money(a.totalCredit), money(a.endingBalance())});
            model.addRow(new Object[]{"", "", "", "", "", ""});
        }
    }

    private static String money(BigDecimal v) {
        return v.signum() == 0 ? "" : String.format("%,.2f", v);
    }

    public static void main(String[] args) {
        // Edit these or pass via env vars
        String url  = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/ledger_db");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String pass = System.getenv().getOrDefault("DB_PASS", "");
        try {
            List<JournalEntry> entries = new JournalEntryReader(url, user, pass).fetchAll();
            Ledger ledger = new Ledger(entries);
            SwingUtilities.invokeLater(() -> new LedgerApp(ledger).setVisible(true));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Could not load entries:\n" + ex.getMessage(),
                "Database error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
