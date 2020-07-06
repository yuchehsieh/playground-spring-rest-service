package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product request) {
        Product product = productService.createProduct(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> replaceProduct(
            @PathVariable("id") String id, @RequestBody Product request) {
        Product product = productService.replaceProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 每個值都要給
     * 而且不能改變順序
     * 與下方 @RequestParam 接收一個 Map<String, String> 做比較
     * **/
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@ModelAttribute ProductQueryParameter param) {
        List<Product> products = productService.getProducts(param);
        return ResponseEntity.ok(products);
    }


    /**
     * 不用給每個值，沒給會是 null
     * 不用照順序
     * 與上方利用 @ModelAttribute 做比較
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
