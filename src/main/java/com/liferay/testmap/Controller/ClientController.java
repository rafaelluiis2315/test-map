    package com.liferay.testmap.Controller;

    import com.liferay.testmap.DTO.ClientDao;
    import com.liferay.testmap.Model.Client;

    import org.apache.poi.hssf.usermodel.HSSFWorkbook;
    import org.apache.poi.ss.usermodel.*;

    import org.apache.poi.xssf.usermodel.XSSFWorkbook;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;


    import java.io.IOException;
    import java.io.InputStream;
    import java.util.Iterator;

    @Controller
    public class ClientController {

        @Autowired
        private ClientDao clientDao;

        @GetMapping("/")
        public String home(Model model) {
            model.addAttribute("clients", clientDao.getAll());
            return "home";
        }

        @PostMapping("/client/save")
        public String addClient( @RequestParam("name") String name, @RequestParam("testMapFile") MultipartFile multipartFile,  RedirectAttributes redirectedAttributes) {
            String fileName = multipartFile.getOriginalFilename();

            if (!multipartFile.getContentType().equals("application/vnd.ms-excel") && !multipartFile.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                redirectedAttributes.addFlashAttribute("error", "O arquivo deve ser do tipo Excel (.xls ou .xlsx).");
                return "redirect:/";
            }

            InputStream inputStream = null;
            try {
                inputStream = multipartFile.getInputStream();
            } catch (IOException e) {
                redirectedAttributes.addFlashAttribute("error", "error");
                return "redirect:/";
            }

            try {


                Workbook workbook;

                if (fileName.contains(".xls")) {
                    workbook = new XSSFWorkbook(inputStream);
                }else {
                    workbook = new HSSFWorkbook(inputStream);
                }

                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

                Sheet sheetSummary = workbook.getSheetAt(50);

                Iterator<Row> rowIterator = sheetSummary.iterator();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        switch (cell.getCellType()) {
                            case BOOLEAN:
                                System.out.println(cell.getBooleanCellValue());
                                break;
                            case NUMERIC:
                                System.out.println(cell.getNumericCellValue());
                                break;
                            case STRING:
                                System.out.println(cell.getStringCellValue());
                                break;
                            case FORMULA:

                                try{
                                    CellValue cellValue = evaluator.evaluate(cell);

                                    switch (cellValue.getCellType()) {
                                        case BOOLEAN:
                                            System.out.println(cellValue.getBooleanValue());
                                            break;
                                        case NUMERIC:
                                            System.out.println(cellValue.getNumberValue());
                                            break;
                                        case STRING:
                                            System.out.println(cellValue.getStringValue());
                                            break;
                                        default:
                                            System.out.println("Unsupported cell type");
                                    }
                                }catch (Exception e) {
                                    System.out.println("Error ==>" + e.getMessage());
                                }

                            default:
                                System.out.println("Unsupported cell type");
                        }



                    }
                }


            } catch (Exception e) {
                System.out.println("Error while processing action: " + e.getMessage());

            };


            Client client = new Client();
            client.setName(name);
            client.setTestMapFile(inputStream);

            clientDao.save(client);

            redirectedAttributes.addFlashAttribute("success", "Cliente adicionado com sucesso!");
            return "redirect:/";
        }


        @PostMapping("/client/delete/{id}")
        public String deleteClient(@PathVariable("id") Long id) {
            clientDao.deleteById(id);
            return "redirect:/";
        }
    }
