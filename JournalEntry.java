import java.math.BigDecimal;
import java.time.LocalDate;

/** One line of a journal entry. */
public class JournalEntry {
    public final int id;
    public final LocalDate date;
    public final String accountName;
    public final String description;
    public final BigDecimal debit;
    public final BigDecimal credit;

    public JournalEntry(int id, LocalDate date, String accountName,
                        String description, BigDecimal debit, BigDecimal credit) {
        this.id = id;
        this.date = date;
        this.accountName = accountName;
        this.description = description;
        this.debit = debit;
        this.credit = credit;
    }
}
