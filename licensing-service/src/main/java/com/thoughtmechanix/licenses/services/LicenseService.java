package com.thoughtmechanix.licenses.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.thoughtmechanix.licenses.client.OrganizationDiscoveryClient;
import com.thoughtmechanix.licenses.client.OrganizationFeignClient;
import com.thoughtmechanix.licenses.client.OrganizationRestTemplateClient;
import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationFeignClient organizationFeignClient;

    @Autowired
    OrganizationRestTemplateClient organizationRestClient;

    @Autowired
    OrganizationDiscoveryClient organizationDiscoveryClient;


    private Organization retrieveOrgInfo(String organizationId, String clientType){
        Organization organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestClient.getOrganization(organizationId);
        }

        return organization;
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value ="1000" )
    },
            fallbackMethod = "getLicenseCache")
    public License getLicense(String organizationId, String licenseId,String clientType) {

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        Organization org = retrieveOrgInfo(organizationId, clientType);
        return license
                .withOrganizationName( org.getName())
                .withContactName( org.getContactName())
                .withContactEmail( org.getContactEmail() )
                .withContactPhone( org.getContactPhone() )
                .withComment(config.getExampleProperty());
    }

    public void saveLicense(License license) {
        licenseRepository.save(license);
    }

    public void updateLicense(License license) {

    }

    public void deleteLicense(License license) {
        licenseRepository.delete(license);
    }

    public List<License> getLicensesByOrg(String organizationId) {
        return licenseRepository.findByOrganizationId(organizationId);
    }


    public License getLicenseCache(String organizationId, String licenseId,String clientTyp){

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization organization=new Organization();
        organization.setId(organizationId);
        organization.setName("Oracle-Cache");
        organization.setContactEmail("contact@oracle.com");
        organization.setContactName("Oracle contacts Cache");
        organization.setContactPhone("333-44444");
        return license
                .withOrganizationName( organization.getName())
                .withContactName( organization.getContactName())
                .withContactEmail( organization.getContactEmail() )
                .withContactPhone( organization.getContactPhone() )
                .withComment(config.getExampleProperty());
    }

}
