package com.caa.organizationservice.controller;


import com.caa.organizationservice.model.Organization;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(value="v1/organizations")
public class OrganizationServiceController {




    @RequestMapping(value="/{organizationId}",method = RequestMethod.GET)
    public Organization getOrganization( @PathVariable("organizationId") String organizationId) {


        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        if (randomNum==3) sleep();



        Organization organization=new Organization();
        organization.setId(organizationId);
        organization.setName("Oracle");
        organization.setContactEmail("contact@oracle.com");
        organization.setContactName("Oracle contacts");
        organization.setContactPhone("333-44444");

        return organization;
    }

    private void sleep(){
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

   /* @RequestMapping(value="/{organizationId}",method = RequestMethod.PUT)
    public void updateOrganization( @PathVariable("organizationId") String orgId, @RequestBody Organization org) {
        orgService.updateOrg( org );
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.POST)
    public void saveOrganization(@RequestBody Organization org) {
        orgService.saveOrg( org );
    }

    @RequestMapping(value="/{organizationId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization( @PathVariable("orgId") String orgId,  @RequestBody Organization org) {
        orgService.deleteOrg( org );
    }

    */
}