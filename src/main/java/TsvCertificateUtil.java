import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class TsvCertificateUtil {
    private static final String certificateExportToWebsite = "F:\\WeTech Projects\\certificatetest\\file\\CertificateExportToWebsite.tsv";
    private static final String url = "https://www.bbacerts.co.uk/uploads/files/CertificateFiles/";
    private static List<Certificate> tsvPojoList = new ArrayList<>();

    public static void main(String[] args) {
        try {
            certificateReader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void certificateReader() throws Exception {
        ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(new FileReader(certificateExportToWebsite), CsvPreference.TAB_PREFERENCE);
            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
            Certificate pojo;
            while ((pojo = beanReader.read(Certificate.class, header)) != null) {
                if (pojo.getFilePath() != null && !pojo.getFilePath().equals("")) {
                    // '\' this character is present in TSV so it is replaced with '/'
                    String filePathWithReplacedDash = pojo.getFilePath().replace('\\', '/');
                    if (!UrlValidator.exists(url + filePathWithReplacedDash)) {
                        tsvPojoList.add(pojo);
                    }
                }
            }
            System.out.println("All Files Scanning completed");
            certificateWriter();

        } finally {
            if (beanReader != null) {
                beanReader.close();
            }
        }
    }


    private static void certificateWriter() throws Exception {

        ICsvBeanWriter beanWriter = null;
        try {
            beanWriter = new CsvBeanWriter(new FileWriter("F:\\WeTech Projects\\certificatetest\\file\\CertificateExportToWebsiteInvalidUrl.tsv"),
                    CsvPreference.TAB_PREFERENCE);

            // the header elements are used to map the bean values to each column (names must match)
            final String[] header = new String[]{"SchemeID", "CertificateID", "CertificateNumber", "CertificateHolder",
                    "Title", "CertificateSeries", "Status", "SheetID", "ProductCodeId", "Sheet",
                    "SheetType", "Issue", "SheetTitle", "SheetStatus", "FilePath", "Abstract",
                    "Web", "Material", "Application", "Style", "GenericKeyword1", "GenericKeyword2",
                    "GenericKeyword3", "GenericKeyword4", "WebSite", "Telephone", "AddressName", "Street",
                    "Block", "City", "County", "Country", "CertificateFirstIssueDate", "SheetPublishedDate",
                    "SheetWithdrswDate", "SheetIssueDate", "SheetSuspensionDate"
            };

            // write the header
            beanWriter.writeHeader(header);
            // write the beans
            System.out.println(tsvPojoList.size()
            );
            for (final Certificate tsvPojo : tsvPojoList) {
                beanWriter.write(tsvPojo, header);
            }
            System.out.println("Operation completed");

        } finally {
            if (beanWriter != null) {
                beanWriter.close();
            }
        }
    }


}
