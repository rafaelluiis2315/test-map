    package com.liferay.testmap.Controller;

    import com.liferay.testmap.DTO.ClientDao;
    import com.liferay.testmap.Model.Client;

    import com.liferay.testmap.Util.UtilExcel;
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
    import java.io.Serializable;
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

            UtilExcel utilExcel = new UtilExcel();
            Serializable resulte;
            try {
                resulte = utilExcel.readFileExcel(inputStream, fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(resulte);

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
