import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

public class TsvFileReader {
    public static void main(String args[]) {

        try {
            readWithCsvBeanReader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static CellProcessor[] getProcessors() {

        final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+"; // just an example, not very robust!
        StrRegEx.registerMessage(emailRegex, "must be a valid email address");

        final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // customerNo (must be unique)
                new NotNull(), // firstName
                new NotNull(), // lastName
                new ParseDate("dd/MM/yyyy"), // birthDate
                new NotNull(), // mailingAddress
                new NotNull(), // favouriteQuote
                new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
        };

        return processors;
    }


    private static void readWithCsvBeanReader() throws Exception {

        ICsvBeanReader beanReader = null;
        try {
            String certificateExportToWebsite = "F:\\WeTech Projects\\certificatetest\\file\\CertificateExportToWebsite.tsv";
            beanReader = new CsvBeanReader(new FileReader(certificateExportToWebsite), CsvPreference.TAB_PREFERENCE);

            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();

            TsvPojo pojo;
            while ((pojo = beanReader.read(TsvPojo.class, header)) != null) {
                System.out.println(String.format("lineNo=%s, rowNo=%s, customer=%s", beanReader.getLineNumber(),
                        beanReader.getRowNumber(), pojo));
            }

        } finally {
            if (beanReader != null) {
                beanReader.close();
            }
        }
    }

}
