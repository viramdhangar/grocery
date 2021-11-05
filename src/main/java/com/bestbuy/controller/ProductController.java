package com.bestbuy.controller;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.swing.ImageIcon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bestbuy.model.CategoryDTO;
import com.bestbuy.model.DAOUser;
import com.bestbuy.model.ImageModel;
import com.bestbuy.model.ProductDTO;
import com.bestbuy.repository.CategoryRepository;
import com.bestbuy.repository.ImageRepository;
import com.bestbuy.repository.ProductRepository;
import com.bestbuy.repository.UserRepository;
import com.bestbuy.service.IImageModelService;

@CrossOrigin (origins = {"*"}, maxAge = 3600)
@RestController
public class ProductController {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	IImageModelService imageModelService;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/searchProductsByName/{name}")
	public List<ProductDTO> searchProductsByName (@PathVariable String name) {
		if(name.length()>=2) {
			List<ProductDTO> byName = productRepository.findByNameIsContaining(name);
			List<ProductDTO> byType = productRepository.findByTypeIsContaining(name);
			Set<ProductDTO> products = new LinkedHashSet<>();
			products.addAll(new LinkedHashSet<ProductDTO>(byName));
			products.addAll(new LinkedHashSet<ProductDTO>(byType));
			products.forEach(product->{
				List<ImageModel> list = imageModelService.getImages(product.getId());
				list.forEach(img->{
					try {
						/*String data = DatatypeConverter.printBase64Binary(decompressBytes(img.getPicByte()));
				        String imageString = "data:"+img.getType()+";base64," + data;
				        img.setImage(imageString);
				        if(null == product.getImage()) {
							product.setImage(imageString);
						}*/
						String decodedString = new String(decompressBytes(img.getPicByte()));
						img.setDecodedBase64(decodedString);
						img.setPicByte(decompressBytes(img.getPicByte()));
					}catch(Exception e) {
						System.out.println("image currupted");
					}
				});
				
				product.setImages(list);
			});
			return new ArrayList<ProductDTO>(products);	
		}
		else {
			return new ArrayList<ProductDTO>();
		}
	}
	
	@GetMapping("/products")
	public List<ProductDTO> products() {
		List<ProductDTO> products = productRepository.findAllValid(); //findAll();
		products.forEach(product->{
			List<ImageModel> list = imageModelService.getImages(product.getId());
			list.forEach(img->{
				try {
					/*String data = DatatypeConverter.printBase64Binary(decompressBytes(img.getPicByte()));
			        String imageString = "data:"+img.getType()+";base64," + data;
			        img.setImage(imageString);
			        if(null == product.getImage()) {
						product.setImage(imageString);
					}*/
					String decodedString = new String(decompressBytes(img.getPicByte()));
					img.setDecodedBase64(decodedString);
					img.setPicByte(null);
				}catch(Exception e) {
					System.out.println("image currupted");
				}
			});
			
			product.setImages(list);
		});
		return products;
	}
	
	@GetMapping("/productByUser/{userId}/{offset}/{limit}")
	public List<ProductDTO> productByUser(@PathVariable Long userId, @PathVariable int offset,@PathVariable int limit) {
		List<ProductDTO> products = productRepository.findMyProductsLimited(userId, offset, limit);
		products.forEach(product->{
			List<ImageModel> list = imageModelService.getImages(product.getId());
			list.forEach(img->{
				try {
					String decodedString = new String(decompressBytes(img.getPicByte()));
					img.setDecodedBase64(decodedString);
					img.setPicByte(null);
				}catch(Exception e) {
					System.out.println("image currupted");
				}
			});
			
			product.setImages(list);
		});
		return products;
	}
	
	@GetMapping("/activateProduct/{offset}/{limit}")
	public List<ProductDTO> activateProduct(@PathVariable int offset,@PathVariable int limit) {
		List<ProductDTO> products = productRepository.findMyProductsLimited(offset, limit);
		products.forEach(product->{
			List<ImageModel> list = imageModelService.getImages(product.getId());
			list.forEach(img->{
				try {
					String decodedString = new String(decompressBytes(img.getPicByte()));
					img.setDecodedBase64(decodedString);
					img.setPicByte(null);
				}catch(Exception e) {
					System.out.println("image currupted");
				}
			});
			
			product.setImages(list);
		});
		return products;
	}
	
	@GetMapping("/images/{productId}")
	public List<ImageModel> getProductImages (@PathVariable Long productId) {
		List<ImageModel> list = imageModelService.getImages(productId);
		list.forEach(img->{
			try {
				/*String data = DatatypeConverter.printBase64Binary(decompressBytes(img.getPicByte()));
		        String imageString = "data:"+img.getType()+";base64," + data;
		        img.setImage(imageString);
		        if(null == product.getImage()) {
					product.setImage(imageString);
				}*/
				String decodedString = new String(decompressBytes(img.getPicByte()));
				img.setDecodedBase64(decodedString);
				img.setPicByte(null);
				//img.setPicByte(decompressBytes(img.getPicByte()));
			}catch(Exception e) {
				System.out.println("image currupted");
			}
		});
		return list;
	}
	
	@GetMapping("/productsByCat/{categoryId}/{offset}/{limit}")
	public List<ProductDTO> productsByCat (@PathVariable Long categoryId,@PathVariable int offset,@PathVariable int limit) {
		//List<ProductDTO> products = productRepository.findByCategoryId(categoryId);
		List<ProductDTO> products = productRepository.findByCategoryIdLimited(categoryId, offset, limit);
		for (ProductDTO p : products) {
			if (p.getSpecification() != null && !p.getSpecification().isEmpty()) {
				Map<String, String> map = new HashMap<>();
				String[] str = p.getSpecification().split("###");
				for (String s : str) {
					String[] strInner = s.split("##");
					map.put(strInner[0], strInner[1]);
				}
				p.setSpecificationMap(map);
			}
		}
		return products;//productRepository.findByCategoryId(categoryId);
		
	}
	
	public Image createImage(byte[] bytearray){
		  
		   return new ImageIcon(bytearray).getImage();
		}
	
	@GetMapping("/product/{id}")
	public ProductDTO product (@PathVariable Long id) {
		
		Optional<ProductDTO> products  =  productRepository.findById(id);
		ProductDTO product = new ProductDTO();
		if(products.isPresent()) {
			product = products.get();
		}
		product.setCatId(product.getCategory().getId());
		// get images 
		//product.setImages(imageRepository.findByProductId(product.getId()));
		if(product.getQuantity() < 1) {
			product.setAvailability("No");
		}
		return product;
	}
	
	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[10000];
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
	

	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[10000];
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

	@PostMapping("/product/{userId}")
	public List<ImageModel> addProduct(@RequestBody ProductDTO productDTO, @PathVariable Long userId) {
		Optional<CategoryDTO> category =  categoryRepository.findById(productDTO.getCatId());
		productDTO.setUserId(userId);
		productDTO.setCategory(category.get());
		productDTO.setAvailability("Yes");
		productDTO.setDelivery("true");
		productDTO.setPopularity(3);
		productDTO.setStatus("Available");
		productDTO.setType(category.get().getName());
				
		ProductDTO product = productRepository.save(productDTO);
		List<ImageModel> imageData = productDTO.getImagesList();
		List<ImageModel> allImages = new ArrayList<>();;
		for(ImageModel str: imageData) {
			String[] str1 = str.getDecodedBase64().split(",");
			String contentType = str1[0].replace(";base64", "").replace("data:", "");
			
			//byte[] decodedByte = Base64.decode(str);
			String encodedString = Base64.getEncoder().encodeToString(str.getDecodedBase64().getBytes());
			
			byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
			
			
			ImageModel img = new ImageModel("base64", contentType,
					compressBytes(decodedBytes));
			img.setProduct(product);
			if(str.getId() != null) {
				img.setId(str.getId());
			}
			allImages.add(img);			
		}
		if(productDTO.getId() != null) {
			imageRepository.deleteByProductId(product.getId());
		}
		return imageRepository.saveAll(allImages);
	}
}
