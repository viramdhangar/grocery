package com.bestbuy.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bestbuy.model.PashuHaat;
import com.bestbuy.model.PashuImages;
import com.bestbuy.repository.PashuHaatRepository;
import com.bestbuy.repository.PashuImagesRepository;

@RestController
public class PashuHaatController {
	
	@Autowired
	PashuHaatRepository pashuHaatRepositiry;
	
	@Autowired
	PashuImagesRepository pIRepository;
	
	@PostMapping("/pashu")
	public PashuHaat savePashu(@RequestBody PashuHaat pashuHaat) {
		return pashuHaatRepositiry.save(pashuHaat);
	}

	@GetMapping("/pashu")
	public List<PashuHaat> pashu () {
		List<PashuHaat> pashuByType = pashuHaatRepositiry.findAll();
		pashuByType.forEach(pashu->{
			List<PashuImages> list = pIRepository.findByPashuHaatId(pashu.getId());
			list.forEach(img->{
				try {
					img.setPicByte(decompressBytes(img.getPicByte()));
				}catch(Exception e) {
					System.out.println("image currupted");
				}
			});
			pashu.setPashuImages(list);
		});
		return pashuByType;
	}
	
	@GetMapping("/pashuById/{id}")
	public PashuHaat pashuById (@PathVariable Long id) {
		Optional<PashuHaat> pashuById = pashuHaatRepositiry.findById(id);
		PashuHaat pashuHaat = new PashuHaat();
		if(pashuById.get() != null) {
			pashuHaat = pashuById.get();
		}
		List<PashuImages> list = pIRepository.findByPashuHaatId(pashuHaat.getId());
		list.forEach(img->{
			try {
				img.setPicByte(decompressBytes(img.getPicByte()));
			}catch(Exception e) {
				System.out.println("image currupted");
			}
		});
		pashuHaat.setPashuImages(list);
		
		return pashuHaat;
		
	}
	
	@GetMapping("/pashuByType/{type}")
	public List<PashuHaat> pashuByType (@PathVariable String type) {
		List<PashuHaat> pashuByType = pashuHaatRepositiry.findByType(type);
		pashuByType.forEach(pashu->{
			List<PashuImages> list = pIRepository.findByPashuHaatId(pashu.getId());
			list.forEach(img->{
				try {
					img.setPicByte(decompressBytes(img.getPicByte()));
				}catch(Exception e) {
					System.out.println("image currupted");
				}
			});
			pashu.setPashuImages(list);
		});
		return pashuByType;
		
	}
	

	@PostMapping("/uploadPashuImage/{pashuId}")
	public Optional<PashuImages> uplaodPashuImage(@PathVariable Long pashuId, @RequestParam("file") MultipartFile file)
			throws IOException {

		System.out.println("Original Image Byte Size - " + file.getBytes().length);
		PashuImages img = new PashuImages(file.getOriginalFilename(), file.getContentType(),
				compressBytes(file.getBytes()));

		return pashuHaatRepositiry.findById(pashuId).map(pashu -> {
			img.setPashuHaat(pashu);
			return pIRepository.save(img);
		});
	}	
	
	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

		return outputStream.toByteArray();
	}
	
	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
	
}
