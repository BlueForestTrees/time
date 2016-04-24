package time.web.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import time.web.service.ParserService;

@Controller
public class ParserController {

	@Autowired
	private ParserService parserService;

	@RequestMapping(value = "/api/upload", method = {RequestMethod.POST})
	public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			final InputStream uploadStream = file.getInputStream();
			final InputStream response = parserService.parse(uploadStream);
			redirectAttributes.addFlashAttribute("message", "You successfully uploaded !");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "You failed to upload => " + e.getMessage());
		}

		return "redirect:upload";
	}
}
