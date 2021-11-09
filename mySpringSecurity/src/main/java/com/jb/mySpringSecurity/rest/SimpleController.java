package com.jb.mySpringSecurity.rest;

import com.jb.mySpringSecurity.beans.Company;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("company")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SimpleController {
    private static final List<Company> COMPANIES = Arrays.asList(
            new Company(1, "Comp1"),
            new Company(2, "Comp2"),
            new Company(3, "Comp3"),
            new Company(4, "Comp4")
    );

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPPORT')")
    public List<Company> getAll(){
        return COMPANIES;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:write')")
    public void addCompany(@RequestBody Company company){
        System.out.println(company);
    }

    @DeleteMapping(path = {"companyId"})
    @PreAuthorize("hasAuthority('admin:write')")
    public void deleteCompany(@PathVariable("companyId") int companyId){
        System.out.printf("the company %d will be deleted",companyId);
    }

    @PutMapping(path = {"companyId"})
    @PreAuthorize("hasAnyAuthority('admin:write','company:write')")
    public void updateCompany(@PathVariable("companyId") int companyId,
                              @RequestBody Company company){
        System.out.println("Company "+companyId+" will be updated");
    }
}
