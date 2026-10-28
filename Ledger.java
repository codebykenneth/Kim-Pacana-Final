import java.math.BigDecimal;
import java.util.*;

/** Posts journal entries to accounts and computes running/ending balances. */
public class Ledger {

    /** A posted line with its running balance (debit - credit). */
    public static class Posting {
        public final JournalEntry entry;
        public final BigDecimal runningBalance;
        Posting(JournalEntry entry, BigDecimal runningBalance) {
            this.entry = entry;
            this.runningBalance = runningBalance;
        }
    }

    public static class Account {
        public final String name;
        public final List<Posting> postings = new ArrayList<>();
        public BigDecimal totalDebit = BigDecimal.ZERO;
        public BigDecimal totalCredit = BigDecimal.ZERO;
        Account(String name) { this.name = name; }
        public BigDecimal endingBalance() { return totalDebit.subtract(totalCredit); }
    }

    private final Map<String, Account> accounts = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /** Group entries under their accounts (posting) and compute balances. */
    public Ledger(List<JournalEntry> entries) {
        for (JournalEntry e : entries) {
            Account a = accounts.computeIfAbsent(e.accountName, Account::new);
            a.totalDebit = a.totalDebit.add(e.debit);
            a.totalCredit = a.totalCredit.add(e.credit);
            a.postings.add(new Posting(e, a.endingBalance()));
        }
    }

    public Collection<Account> allAccounts() { return accounts.values(); }

    /** Accounts whose name contains the query (case-insensitive). */
    public List<Account> search(String query) {
        String q = query.trim().toLowerCase();
        List<Account> out = new ArrayList<>();
        for (Account a : accounts.values())
            if (a.name.toLowerCase().contains(q)) out.add(a);
        return out;
    }
}
