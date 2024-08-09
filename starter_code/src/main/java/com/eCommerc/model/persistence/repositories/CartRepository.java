package com.eCommerc.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eCommerc.model.persistence.Cart;
import com.eCommerc.model.persistence.User;

import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
