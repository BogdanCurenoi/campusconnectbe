package com.components.campusconnectbackend.repository;

import com.components.campusconnectbackend.domain.PersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRoleRepository extends JpaRepository<PersonRole, PersonRole.PersonRoleId> {

    List<PersonRole> findByIdPersonUid(String personUid);

    List<PersonRole> findByIdRoleId(Integer roleId);

    Optional<PersonRole> findByIdPersonUidAndIdRoleId(String personUid, Integer roleId);

    void deleteByIdPersonUid(String personUid);

    void deleteByIdRoleId(Integer roleId);

    void deleteByIdPersonUidAndIdRoleId(String personUid, Integer roleId);

    boolean existsByIdPersonUidAndIdRoleId(String personUid, Integer roleId);
}