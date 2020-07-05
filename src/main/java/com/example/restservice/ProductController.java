package com.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    /**
     * 每個值都要給
     * 而且不能改變順序
     * **/
    @GetMapping("/products")
    public ResponseEntity<String> getProducts(@ModelAttribute ProductQueryParameter param) {
        String nameKeyword = param.getKeyword();
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();

        String returnString = "keyword: " + nameKeyword + ", orderBy: " + orderBy + ", sortRule: " + sortRule;

        return ResponseEntity.ok().body(returnString);
    }

    /**
     * 不用給每個值，沒給會是 null
     * 不用照順序
     * **/
//    @RequestMapping(method = RequestMethod.GET, value = "/custom")
    @GetMapping("/custom")
    public ResponseEntity<String> controllerMethod(@RequestParam Map<String, String> customQuery) {

        String s1 = "customQuery = brand " + customQuery.get("brand") + ", ";
        String s2 = "customQuery = limit " + customQuery.get("limit") + ", ";
        String s3 = "customQuery = price " + customQuery.get("price") + ", ";
        String s4 = "customQuery = other " + customQuery.get("other") + ", ";
        String s5 = "customQuery = sort " + customQuery.get("sort");

        String returnString = s1 + s2 + s3 + s4 + s5;

        return ResponseEntity.ok().body(returnString);

//        return customQuery.toString();
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
