package com.thoughtmechanix.licenses.repository;

import com.thoughtmechanix.licenses.model.License;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LicenseRepository extends CrudRepository<License, String> {

    List<License> findByOrganizationId(String organizationId);

    License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);

}
