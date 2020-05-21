package com.gmail.sergick6690;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/")
public class MyController {

    private Map<Long, byte[]> photos = new HashMap<>();
   private List<Long> list = new ArrayList<>();

    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping(value = "/add_photo", method = RequestMethod.POST)
    public String onAddPhoto(Model model, @RequestParam MultipartFile photo) {
        if (photo.isEmpty())
            throw new PhotoErrorException();

        try {
            long id = System.currentTimeMillis();
            photos.put(id, photo.getBytes());

            model.addAttribute("photo_id", id);
            return "result";
        } catch (IOException e) {
            throw new PhotoErrorException();
        }
    }

    @RequestMapping("/photo/{photo_id}")
    public ResponseEntity<byte[]> onPhoto(@PathVariable("photo_id") long id) {
        return photoById(id);
    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public ResponseEntity<byte[]> onView(@RequestParam("photo_id") long id) {
        if(photos.containsKey(id)){
            return photoById(id);
        }else {
            throw new PhotoNotFoundException();
        }

    }

    @RequestMapping("/delete/{photo_id}")
    public String onDelete(@PathVariable("photo_id") long id) {
        if (photos.remove(id) == null)
            throw new PhotoNotFoundException();
        else
            return "index";
    }
    @RequestMapping("/deletes")
    public RedirectView onDeletes(@RequestParam("photo") long [] photoId) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8081/see_all");
        for(long id:photoId){
            photos.remove(id);
        }
        return redirectView;
    }
    @GetMapping("see_all")
    public String seeAllPhotos(Model model){
        model.addAttribute("photos" ,photos);
        return "allPhotos";
    }
    @PostMapping("do_zip")
    public void  doZip (HttpServletResponse response ,@RequestParam("file") List <MultipartFile> filels) throws IOException{
        MediaType mediaType = MediaType.MULTIPART_FORM_DATA;
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            for (MultipartFile file : filels) {
                if(file.isEmpty()){
                    response.sendRedirect("http://localhost:8081/");
                }
                 byte[] fileBytes = file.getBytes();
                response.setContentType(mediaType.getType());
                ZipEntry zipEntry = new ZipEntry(file.getOriginalFilename());
                zipOut.putNextEntry(zipEntry);
                zipOut.write(fileBytes);
            }
        }
    }

    private ResponseEntity<byte[]> photoById(long id) {
        byte[] bytes = photos.get(id);
        if (bytes == null)
            throw new PhotoNotFoundException();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);

    }
}
