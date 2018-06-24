package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.domain.Order;

@Mapper
public interface OrderDao {

	 List<Order> get();
	 
	 void  add(Order o);
}
