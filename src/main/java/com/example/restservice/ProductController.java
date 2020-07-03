package com.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    private final List<Product> productDB = new ArrayList<>();

    @PostConstruct
    private void initDB() {
        productDB.add(new Product("B0001", "Android Development (Java)", 380));
        productDB.add(new Product("B0002", "Android Development (Kotlin)", 420));
        productDB.add(new Product("B0003", "Data Structure (Java)", 250));
        productDB.add(new Product("B0004", "Finance Management", 450));
        productDB.add(new Product("B0005", "Human Resource Management", 330));
    }


    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {
        Optional<Product> productOp = productDB.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (!productOp.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Product product = productOp.get();
        return ResponseEntity.ok().body(product);
    }

//    @GetMapping("/products/{id}")
//    public Product getProduct(@PathVariable("id") String id) {
//        return null;
//    }

    @PostMapping("/products")
    public ResponseEntity<Object> createProduct(@RequestBody Product request) {
        boolean isIdDuplicated = productDB.stream()
                .anyMatch(p -> p.getId().equals(request.getId()));
        if (isIdDuplicated) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productDB.add(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(product);
    }

    @PutMapping("/products/{id}")
    public Product replaceProduct(@PathVariable("id") String id,
                                  @RequestBody Product request) {
        return null;
    }










    /** PLAY GROUND **/
//    @RequestMapping("/product")
//    public String getProductName(
//            @RequestParam(name = "productName", defaultValue = "iphone") String name
////            @RequestParam(name = "otherInfo", defaultValue = "price is high") String otherInfo
//    ) {
////      http://localhost:8080/prodcut?productName=123&productPrice=321
//        return "You are passing:  productName: " + name;
//    }
//
//  @RequestMapping(method = RequestMethod.GET, params = {"productName", "productPrice"})
//    public String getInfo(@RequestParam String productName, @RequestParam String productPrice) {
////      http://localhost:8080/?productName=123&productPrice=321
//        return "productName: " + productName + ", productPrice: " + productPrice;
//  }
}
