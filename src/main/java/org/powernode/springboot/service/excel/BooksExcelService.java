package org.powernode.springboot.service.excel;

import org.powernode.springboot.bean.vo.ImportBooksByExcelRes;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface BooksExcelService {
    ImportBooksByExcelRes importBooksFromExcel(InputStream file);
}
