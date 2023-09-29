package com.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.entity.Contact;
import com.entity.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>{

	//pagination
	
	//current page 1
	//contact per page
	@Query("from Contact as c where c.user.id = :userId")
	public Page<Contact> findContactsByUser(@Param("userId") int id,Pageable pageable);
	
	//search result query name containng
	public List<Contact> findByNameContainingAndUser(String name,User user);
}
